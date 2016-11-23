package com.graphify.db.resource;

import com.graphify.db.model.Schema;
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

    /*@GET
    @Path("/{host}/{user}/{password}/{schemaName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSchema(@PathParam("host")String host,@PathParam("user") String user, @PathParam("password") String password, @PathParam("schemaName") String schemaName) {
        System.out.println("In the method");
        String url = String.format("jdbc:mysql://{0}/{3}?user={1}&password={2}", host, user, password, schemaName);
        System.out.println(url);
        Schema schema = dbService.getDBSchema(url, schemaName);
        return Response.ok()
                .entity(schema)
                .build();
    }*/

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSchemaExpense() {
        //TODO Currently being hard coded will be sent as a parameter
        String url = "jdbc:mysql://localhost:3306/expense?user=root&password=admin";
        Schema schema = dbService.getDBSchema(url, "expense");
        return Response.ok()
                .entity(schema)
                .build();
    }
}
