package com.artnaseef.amqtool.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Path("/")
public class AmqToolUiRestApi {

    private static final Logger DEFAULT_LOGGER = LoggerFactory.getLogger(AmqToolUiRestApi.class);

    private Logger log = DEFAULT_LOGGER;

//========================================
// Getters and Setters
//----------------------------------------



//========================================
// REST Endpoints
//----------------------------------------

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/hello")
    public String getHello() {
        return "Hello";
    }

//========================================
// Internal Methods
//----------------------------------------

}
