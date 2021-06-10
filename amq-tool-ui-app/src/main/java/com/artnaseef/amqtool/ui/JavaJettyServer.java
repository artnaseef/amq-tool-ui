package com.artnaseef.amqtool.ui;

import com.artnaseef.amqtool.ui.init.AmqToolUiJaxrsApplication;
import org.eclipse.jetty.rewrite.handler.RedirectPatternRule;
import org.eclipse.jetty.rewrite.handler.RewriteHandler;
import org.eclipse.jetty.rewrite.handler.Rule;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.CustomRequestLog;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class JavaJettyServer {

    private static final Logger DEFAULT_LOGGER = LoggerFactory.getLogger(JavaJettyServer.class);

    private Logger log = DEFAULT_LOGGER;


    private Server jettyServer;

    private int httpPort = -1;

    // private AmqToolWebsocketConfigurator amqToolWebsocketConfigurator;

//========================================
// Getters and Setters
//----------------------------------------

    public Server getJettyServer() {
        return jettyServer;
    }

    public void setJettyServer(Server jettyServer) {
        this.jettyServer = jettyServer;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }


//========================================
// Lifecycle
//----------------------------------------

    public void init() {
        if (this.jettyServer == null) {
            this.jettyServer = this.createJettyServer();

            try {
                this.jettyServer.start();
            } catch (Exception exc) {
                throw new RuntimeException("failed to startup the embedded jetty server", exc);
            }
        }
    }

//========================================
// Custom Jetty Server Setup
//----------------------------------------

    private Server createJettyServer() {
        Server result = new Server();

        ServletContextHandler rootContextHandler = this.prepareRootContextHandler(result);

        this.configureJettyConnectors(result);
        // this.configureJettyForJsr356Websocket(result);
        this.configureJettyForRest(rootContextHandler);
        this.configureJettyForStaticContent(rootContextHandler);
        this.configureUrlRewriteRules(result, rootContextHandler);
        this.configureLogging(result);

        this.addHandlerToServer(result, rootContextHandler);

        return result;
    }

    private void configureJettyConnectors(Server server) {
        List<Connector> connectors = new LinkedList<>();

        ServerConnector httpConnector = this.prepareHttpConnector(server);
        connectors.add(httpConnector);

        server.setConnectors(connectors.toArray(new Connector[connectors.size()]));
    }

    // private void configureJettyForJsr356Websocket(Server server) {
    //     try {
    //         ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    //         context.setContextPath("/ws");
    //
    //         ServerContainer websocketServerContainer = WebSocketServerContainerInitializer.configureContext(context);
    //
    //         ServerEndpointConfig msgEndpointConfig = ServerEndpointConfig.Builder
    //                 .create(UserInterfaceMessagingWebsocket.class, "/ui-msg")
    //                 .configurator(this.userInterfaceMessagingWebsocketConfigurator)
    //                 .build()
    //                 ;
    //
    //         websocketServerContainer.addEndpoint(msgEndpointConfig);
    //
    //         this.addHandlerToServer(server, context);
    //     } catch (Exception exc) {
    //         throw new RuntimeException("Jetty server websocket setup failed", exc);
    //     }
    // }

    private ServerConnector prepareHttpConnector(Server server) {
        ServerConnector httpConnector = new ServerConnector(server);
        httpConnector.setPort(this.httpPort);

        return httpConnector;
    }

    private ServletContextHandler prepareRootContextHandler(Server server) {
        ServletContextHandler result = new ServletContextHandler();

        return result;
    }

    private void configureJettyForStaticContent(ServletContextHandler rootContextHandler) {
        ServletHolder servletHolder = new ServletHolder("default", DefaultServlet.class);

        URL staticHtmlRootUrl = JavaJettyServer.class.getResource("/static-html");
        servletHolder.setInitParameter("resourceBase", staticHtmlRootUrl.toString());
        servletHolder.setInitParameter("dirAllowed", "false");
        servletHolder.setInitParameter("pathInfoOnly", "true");

        rootContextHandler.addServlet(servletHolder, "/static/*");
    }

    private void configureUrlRewriteRules(Server server, ServletContextHandler rootContextHandler) {
        RewriteHandler rewriteHandler = new RewriteHandler();

        // Use "" for the pattern.  "/" is a special case that matches anything starting with "/".  Not sure why.
        Rule rootMoveRule = new RedirectPatternRule("", "/static/index.html");

        rewriteHandler.addRule(rootMoveRule);

        this.addHandlerToServer(server, rewriteHandler);
    }

    private void configureJettyForRest(ServletContextHandler rootContextHandler) {
        ServletHolder servletHolder = rootContextHandler.addServlet(ServletContainer.class, "/api/*");
        servletHolder.setInitOrder(0);

        servletHolder.setInitParameter("javax.ws.rs.Application", AmqToolUiJaxrsApplication.class.getName());

        this.configureJettyMultipartFeature(servletHolder);
    }

    private void configureJettyMultipartFeature(ServletHolder servletHolder) {
        servletHolder.setInitParameter(
                "jersey.config.server.provider.classnames",
                "org.glassfish.jersey.media.multipart.MultiPartFeature");
    }

    private void configureLogging(Server server) {
        RequestLog requestLog = new CustomRequestLog(new RequestLog.Writer() {
            @Override
            public void write(String requestEntry) throws IOException {
                log.info("{}", requestEntry);
            }
        }, CustomRequestLog.NCSA_FORMAT);

        server.setRequestLog(requestLog);
    }

    private void addHandlerToServer(Server server, Handler handler) {
        Handler existing = server.getHandler();
        if (existing == null) {
            server.setHandler(handler);
        } else {
            HandlerList handlerList;

            if (existing instanceof HandlerList) {
                // Re-use the existing HandlerList.
                handlerList = (HandlerList) existing;
            } else {
                // Wrap the existing handler in a HandlerList.
                handlerList = new HandlerList();
                handlerList.addHandler(existing);

                server.setHandler(handlerList);
            }

            // Add the new handler to the HandlerList.
            handlerList.addHandler(handler);
        }
    }
}
