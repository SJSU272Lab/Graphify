package com.graphify.db.client.ibm.graph;

import com.graphify.db.model.ibm.graph.GraphSchema;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Sushant on 22-11-2016.
 * This class will act as a mediator between IBM Services and
 * the frontend.
 */
public class IBMGraphClient {

    private CloseableHttpClient client = null;
    private String instanceID = null;
    private String apiURL = null;
    private String restifyURL = null;
    private String userId = null;
    private String password = null;
    private String basicAuth = null;
    private String gdsTokenAuth = null;
    private static Logger LOGGER = Logger.getLogger(IBMGraphClient.class);


    private void initialize()
    {
        client = HttpClients.createDefault();
        instanceID = "cbdaceb1-6e60-4378-bfc5-d0cbd1f24b49";
        apiURL = "https://ibmgraph-alpha.ng.bluemix.net/"+instanceID+"/graphify"; // Using sample instead of g
        userId = "4fae6a16-c5e3-4e9c-990b-2d7fcf773369";
        password = "d86312f9-9a7d-49e2-9eed-4e475557b44d";
        restifyURL = "https://graphrestify-alpha.ng.bluemix.net/"+instanceID;
        setBasicAuth();
        setGdsTokenAuth();
    }


    public GraphSchema getGraphSchema(String graphName)
    {
        initialize();
        return getSchema(graphName);
    }

    public void createNewGraph(String graphName)
    {
        initialize();
        createGraph(graphName);
    }

    public void createGraphSchema(GraphSchema graphSchema, String graphName)
    {
        initialize();
        apiURL = "https://ibmgraph-alpha.ng.bluemix.net/"+instanceID+"/"+graphName;
        createSchema(graphSchema);
    }

    private void doWork()
    {
        initialize();
        //createGraph("graphify");
        //createSchema();
        //getSchema("graphify");
    }


    private void setBasicAuth() {
        byte[] userpass = (userId + ":" + password).getBytes();
        //System.out.println("Encoded: "+ new String(Base64.encodeBase64(userpass)));
        this.basicAuth = "Basic " +new String(Base64.encodeBase64(userpass));
    }

    private void setGdsTokenAuth() {
        HttpGet httpGet = new HttpGet(restifyURL+"/_session");
        httpGet.setHeader("Authorization", basicAuth);

        try{
            HttpResponse httpResponse = client.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String content = EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
            JSONObject jsonContent = new JSONObject(content);
            this.gdsTokenAuth = "gds-token " + jsonContent.getString("gds-token");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void createGraph(String name) {
        String postURLGraph = restifyURL + "/_graphs/"+name ;
        HttpPost httpPostGraph = new HttpPost(postURLGraph);
        httpPostGraph.setHeader("Authorization", gdsTokenAuth);
        HttpResponse httpResponseGraph;
        try {
            httpResponseGraph = client.execute(httpPostGraph);
            HttpEntity httpEntityGraph = httpResponseGraph.getEntity();
            String contentGraph = EntityUtils.toString(httpEntityGraph);
            EntityUtils.consume(httpEntityGraph);
            JSONObject jsonContentGraph = new JSONObject(contentGraph);
            System.out.println("response from creating graph" + jsonContentGraph.toString());
            apiURL = jsonContentGraph.getString("dbUrl");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addData(String graphName, String command) {
       String postEntity = "{\"gremlin\":\"".concat(command).concat("\"}").replaceAll("(\\r|\\n)", "");
        LOGGER.info(command);
        initialize();
        try {
            apiURL = restifyURL + "/"+graphName+"/gremlin";
            //System.out.println(IBMGraphClient.class.getCanonicalName()+" Posting at "+apiURL + " data: \n"+ postEntity);
            HttpPost httpPost = new HttpPost(apiURL);
            httpPost.setHeader("Authorization", gdsTokenAuth);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept", "application/json");
            StringEntity strEnt = new StringEntity(postEntity, ContentType.APPLICATION_JSON);
            httpPost.setEntity(strEnt);
            HttpResponse httpResponse = client.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            String content = EntityUtils.toString(httpEntity);
            System.out.println(IBMGraphClient.class.getCanonicalName()+" response content "+content);
            EntityUtils.consume(httpEntity);
            JSONObject jsonContent = new JSONObject(content);
            JSONObject result = jsonContent.getJSONObject("result");
            JSONArray data = result.getJSONArray("data");
            if (data.length() > 0) {
                JSONObject response = data.getJSONObject(0);
                System.out.println("response from adding data" + response);
            }
        }catch (Exception e) {
            e.printStackTrace();
            //System.out.println("Schema creation failed");
        }
    }

    private void createSchema(GraphSchema graphSchema) {
        try {
            JSONObject postData = new JSONObject(graphSchema);
            //System.out.println(IBMGraphClient.class.getCanonicalName()+" Posting at "+apiURL + "/schema\n"+ " data: \n"+ postData.toString());
            HttpPost httpPost = new HttpPost(apiURL + "/schema");
            httpPost.setHeader("Authorization", gdsTokenAuth);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept", "application/json");
            StringEntity strEnt = new StringEntity(postData.toString(), ContentType.APPLICATION_JSON);
            httpPost.setEntity(strEnt);
            HttpResponse httpResponse = client.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            String content = EntityUtils.toString(httpEntity);
            //System.out.println(IBMGraphClient.class.getCanonicalName()+" response content "+content);
            EntityUtils.consume(httpEntity);
            JSONObject jsonContent = new JSONObject(content);
            JSONObject result = jsonContent.getJSONObject("result");
            JSONArray data = result.getJSONArray("data");
            if (data.length() > 0) {
                JSONObject response = data.getJSONObject(0);
                System.out.println("response from creating schema" + response);
            }
        }catch (Exception e) {
            e.printStackTrace();
            //System.out.println("Schema creation failed");
        }
    }

    private GraphSchema getSchema(String graphName) {
        try {
            apiURL = "https://ibmgraph-alpha.ng.bluemix.net/"+instanceID+"/"+graphName;
            HttpGet httpGet = new HttpGet(apiURL + "/schema");
            httpGet.setHeader("Authorization", gdsTokenAuth);
            httpGet.setHeader("Accept", "application/json");

            HttpResponse httpResponse = client.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String content = EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
            JSONObject jsonContent = new JSONObject(content);
            JSONObject result = jsonContent.getJSONObject("result");
            JSONArray data = result.getJSONArray("data");
            if (data.length() > 0) {
                JSONObject response = data.getJSONObject(0);
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        //System.out.println("response from get schema" + response);
                        GraphSchema[] graphSchema =  mapper.readValue(data.toString(), GraphSchema[].class);
                        //System.out.println(graphSchema[0]);
                        return graphSchema[0];
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     *
     */
    private void createAVertex()
    {
        try
        {
            String postURL = apiURL + "/vertices";
            HttpPost httpPost = new HttpPost(postURL);
            httpPost.setHeader("Authorization", gdsTokenAuth);
            HttpResponse httpResponse = client.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            String content = EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
            JSONObject jsonContent = new JSONObject(content);
            System.out.println(content);

            JSONObject result = jsonContent.getJSONObject("result");
            JSONArray data = result.getJSONArray("data");
            if (data.length() > 0)
            {
                JSONObject newVertex = data.getJSONObject(0);
                System.out.println(newVertex);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
