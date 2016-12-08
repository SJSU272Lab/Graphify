package com.graphify.db.resource;

import com.graphify.db.model.ibm.graph.GraphSchema;
import com.graphify.db.model.mysql.Schema;
import com.graphify.db.model.mysql.Validate;
import com.graphify.db.service.DBService;
import com.graphify.db.service.impl.DBServiceImpl;
import com.graphify.db.util.StringUtil;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Sushant on 22-11-2016.
 */
@Path("/db")
public class DBServiceResource {
    DBService dbService = new DBServiceImpl();
    private static final Logger LOGGER = Logger.getLogger(DBServiceResource.class);
    @GET
    @Path("/schema")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSchema(@QueryParam("dburl") String url, @QueryParam("schema") String schemaName) {
        //String url = "jdbc:mysql://localhost:3306/expense?user=root&password=admin&autoReconnect=true&useSSL=false";
        Schema schema = dbService.getDBSchema(url, schemaName);
        return Response.ok()
                .entity(schema)
                .build();
    }

    @GET
    @Path("/validate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateFiles(@QueryParam("dburl") String url, @QueryParam("file") String fileLocation, @QueryParam("schema") String schemaName) {
        //String url = "jdbc:mysql://localhost:3306/expense?user=root&password=admin&autoReconnect=true&useSSL=false";
        //String fileLocation = "D:/DevEnv/Fall16-Team12/DBService/src/main/resources/mysql";
        LOGGER.info(DBServiceResource.class.getCanonicalName() + " " + url + " " + fileLocation);
        Validate validate = dbService.validateSchema(StringUtil.reformatHttp(url), StringUtil.reformatHttp(schemaName), StringUtil.reformatHttp(fileLocation));
        LOGGER.info(DBServiceResource.class.getCanonicalName() + " " + validate);
        return Response.ok()
                .entity(validate)
                .build();
    }

    @GET
    @Path("/convert")
    @Produces(MediaType.APPLICATION_JSON)
    public Response convertDB(@QueryParam("dburl") String url, @QueryParam("schema") String schemaName) {
        //String url = "jdbc:mysql://localhost:3306/expense?user=root&password=admin&autoReconnect=true&useSSL=false";
        //String fileLocation = "D:/DevEnv/Fall16-Team12/DBService/src/main/resources/mysql";
        GraphSchema graphSchema = dbService.convertDB(StringUtil.reformatHttp(url), StringUtil.reformatHttp(schemaName));
        LOGGER.info(DBServiceResource.class.getCanonicalName() + " " + graphSchema);
        return Response.ok()
                .entity(graphSchema)
                .build();
    }

    @POST
    @Path("/conadd")
    @Produces(MediaType.APPLICATION_JSON)
    public Response convertAndAdd(@QueryParam("dburl") String url, @QueryParam("schema") String schemaName, @QueryParam("file") String fileLocation) {
        //String url = "jdbc:mysql://localhost:3306/expense?user=root&password=admin&autoReconnect=true&useSSL=false";
        //String fileLocation = "D:/DevEnv/Fall16-Team12/DBService/src/main/resources/mysql";
        //String schemaName = "expense";
        GraphSchema graphSchema = dbService.convertAndAdd(StringUtil.reformatHttp(url), StringUtil.reformatHttp(schemaName), StringUtil.reformatHttp(fileLocation));
        LOGGER.info(DBServiceResource.class.getCanonicalName() + " " + graphSchema);
        LOGGER.info(DBServiceResource.class.getCanonicalName() + " Graph name ==> " + graphSchema.getGraphName());

        return Response.ok()
                .entity(graphSchema)
                .build();
    }
}