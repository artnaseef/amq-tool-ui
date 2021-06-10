package com.artnaseef.amqtool.ui.init;

import com.artnaseef.amqtool.ui.AmqToolUiRestApi;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

/**
 *
 */
public class AmqToolUiJaxrsApplicationStaticInjector {

    public AmqToolUiRestApi getAmqToolUiRestApi() {
        return AmqToolUiJaxrsApplication.getAmqToolUiRestApi();
    }

    public void setAmqToolUiRestApi(AmqToolUiRestApi amqToolUiRestApi) {
        AmqToolUiJaxrsApplication.setAmqToolUiRestApi(amqToolUiRestApi);
    }

    public JacksonJsonProvider getJacksonJsonProvider() {
        return AmqToolUiJaxrsApplication.getJacksonJsonProvider();
    }

    public void setJacksonJsonProvider(JacksonJsonProvider jacksonJsonProvider) {
        AmqToolUiJaxrsApplication.setJacksonJsonProvider(jacksonJsonProvider);
    }
}
