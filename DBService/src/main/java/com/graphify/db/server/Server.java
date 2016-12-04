package com.graphify.db.server;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * Created by Sushant on 22-11-2016.
 */
public class Server {
    private static final String BASE_URI = "http://localhost:8081/";

    public static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("com.graphify.db")
                .property("jersey.config.server.provider.classnames","org.glassfish.jersey.moxy.json.MoxyJsonFeature");
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));

        System.in.read();
        server.stop();
    }
}
