package com.artnaseef.amqtool.ui.init;

import com.artnaseef.amqtool.ui.AmqToolUiRestApi;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * JAX-RS Application class for use wiring together all of the web "resources" and "providers" from the application
 * into the JAX-RS system.
 *
 * Note that this class is instantiated by the JAX-RS implementation (Jersey) as per the JAX-RS specification, so
 * static fields are used to enable injection from the spring context.
 *
 * Sometimes Inversion-of-Control goes too far.
 *
 * Created by art on 8/4/19.
 */
public class AmqToolUiJaxrsApplication extends Application {

    private static AmqToolUiRestApi amqToolUiRestApi;
    private static JacksonJsonProvider jacksonJsonProvider;

//========================================
// STATIC Getters and Setters
//----------------------------------------

    public static AmqToolUiRestApi getAmqToolUiRestApi() {
        return amqToolUiRestApi;
    }

    public static void setAmqToolUiRestApi(AmqToolUiRestApi amqToolUiRestApi) {
        AmqToolUiJaxrsApplication.amqToolUiRestApi = amqToolUiRestApi;
    }

    public static JacksonJsonProvider getJacksonJsonProvider() {
        return jacksonJsonProvider;
    }

    public static void setJacksonJsonProvider(JacksonJsonProvider jacksonJsonProvider) {
        AmqToolUiJaxrsApplication.jacksonJsonProvider = jacksonJsonProvider;
    }


//========================================
// Application API
//----------------------------------------


    @Override
    public Set<Object> getSingletons() {
        Set<Object> result = new HashSet<>();

        result.add(AmqToolUiJaxrsApplication.amqToolUiRestApi);
        result.add(AmqToolUiJaxrsApplication.jacksonJsonProvider);

        return result;
    }
}
