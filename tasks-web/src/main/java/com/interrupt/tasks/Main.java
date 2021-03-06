package com.interrupt.tasks;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.utils.Exceptions;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.net.URI;

public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI =
            "http://0.0.0.0:"+(System.getenv("PORT")!=null?System.getenv("PORT"):"8080")+"/tasker";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in notes package
        final ResourceConfig rc = new ResourceConfig().packages("com.interrupt.tasks.api").
                registerInstances(new MyExceptionMapper());

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Print out an exception as a 500 message, if one bubbles up here.
     */
    @Provider
    public static class MyExceptionMapper implements
            ExceptionMapper<WebApplicationException> {
        @Override
        public Response toResponse(WebApplicationException ex) {
            return Response.status(500).entity("{ \"error\": \"" + ex.getMessage() + "\"}").type("application/json")
                    .build();
        }
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();

        try {
            server.start();

            System.out.println(String.format("Jersey app started with WADL available at "
                    + "%s/application.wadl", BASE_URI));

            Thread.currentThread().join();
        } catch (Exception e) {
            System.out.println("There was an error while starting Grizzly HTTP server.");
            System.out.println(e.getMessage().toString());
        }
    }
}

