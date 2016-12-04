package com.graphify.db.resource;

import com.graphify.db.model.mysql.Validate;
import com.graphify.db.service.DBService;
import com.graphify.db.service.impl.DBServiceImpl;
import com.graphify.db.util.StringUtil;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Sushant on 22-11-2016.
 */
@Path("/db")
public class DBServiceResource {
    DBService dbService = new DBServiceImpl();

    @GET
    @Path("/schema")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSchemaExpense(@QueryParam("dburl") String url, @QueryParam("schema") String schema ) {
        //String url = "jdbc:mysql://localhost:3306/expense?user=root&password=admin&autoReconnect=true&useSSL=false";
        //Schema schema = dbService.getDBSchema(url, "expense");
        return Response.ok()
                .entity(schema)
                .build();
    }

    @GET
    @Path("/validate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateFiles(@QueryParam("dburl") String url, @QueryParam("file") String fileLocation, @QueryParam("schema") String schema) {
        //String url = "jdbc:mysql://localhost:3306/expense?user=root&password=admin&autoReconnect=true&useSSL=false";
        //String fileLocation = "D:/DevEnv/Fall16-Team12/DBService/src/main/resources/mysql";
        System.out.println(DBServiceResource.class.getCanonicalName() + " "+ url + " "+ fileLocation);
        Validate validate = dbService.validateSchema(StringUtil.reformatHttp(url), StringUtil.reformatHttp(schema), StringUtil.reformatHttp(fileLocation));
        System.out.println(DBServiceResource.class.getCanonicalName() + " "+  validate);
        return Response.ok()
                .entity(validate)
                .build();
    }
}
