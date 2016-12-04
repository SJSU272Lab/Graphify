package com.graphify.db.client;

import com.graphify.db.model.mysql.Validate;
import com.graphify.db.util.StringUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by Sushant on 01-12-2016.
 */
public class ServiceClient {
    private String host;
    private Integer port;
    private String db;
    private String apiURL = "http://%s:%d/db";

    CloseableHttpClient client = HttpClients.createDefault();

    public ServiceClient(String host, Integer port, String db) {
        this.host = host;
        this.port = port;
        this.db = db;
        apiURL = String.format(apiURL, host, port);
    }

    public Validate validate(String dbUrl, String fileLocation) {
        Validate validate = null;
        Boolean isValid;
        String message;
        try {
            String validateUrl = apiURL + "/validate?dburl=" + StringUtil.formatHttp(dbUrl) + "&file="
                    + StringUtil.formatHttp(fileLocation) + "&schema=" + StringUtil.formatHttp(db);
            HttpGet httpGet = new HttpGet(validateUrl);
            httpGet.setHeader("Accept", "application/json");

            HttpResponse httpResponse = client.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String content = EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
            JSONObject jsonContent = new JSONObject(content);
            isValid = jsonContent.getBoolean("valid");
            message = jsonContent.getString("message");
            validate = new Validate();
            validate.setValid(isValid);
            validate.setMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return validate;
    }
}