package com.graphify.db.resource;

import com.graphify.db.model.mysql.Schema;
import com.graphify.db.model.mysql.Validate;
import com.graphify.db.service.DBService;
import com.graphify.db.service.impl.DBServiceImpl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    public Response getSchemaExpense() {
        //TODO Currently being hard coded will be sent as a parameter
        String url = "jdbc:mysql://localhost:3306/expense?user=root&password=admin&autoReconnect=true&useSSL=false";
        Schema schema = dbService.getDBSchema(url, "expense");
        return Response.ok()
                .entity(schema)
                .build();
    }

    @GET
    @Path("/validate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateFiles() {
        //TODO Currently being hard coded will be sent as a parameter
        String url = "jdbc:mysql://localhost:3306/expense?user=root&password=admin&autoReconnect=true&useSSL=false";
        String fileLocation = "D:/DevEnv/Fall16-Team12/DBService/src/main/resources/mysql";
        Validate validate = dbService.validateSchema(url, "expense", fileLocation);
        return Response.ok()
                .entity(validate)
                .build();
    }
}
