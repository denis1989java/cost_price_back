package com.monich.cost_price.common.web.config;

import com.google.common.collect.ImmutableList;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CommonWebConfiguration {


    @Bean
    public JettyServletWebServerFactory jettyServletWebServerFactory(@Value("${server.port}") final String port,
                                                                     @Value("${jetty.threadPool.maxThreads:200}") final String maxThreads,
                                                                     @Value("${jetty.threadPool.minThreads:10}") final String minThreads,
                                                                     @Value("${jetty.threadPool.idleTimeout:60000}") final String idleTimeout) {
        final JettyServletWebServerFactory factory = new JettyServletWebServerFactory(Integer.valueOf(port));
        factory.addServerCustomizers((JettyServerCustomizer) server -> {
            // Tweak the connection pool used by Jetty to handle incoming HTTP connections
            final QueuedThreadPool threadPool = server.getBean(QueuedThreadPool.class);
            threadPool.setMaxThreads(Integer.valueOf(maxThreads));
            threadPool.setMinThreads(Integer.valueOf(minThreads));
            threadPool.setIdleTimeout(Integer.valueOf(idleTimeout));

            ServletContextHandler servletContextHandler = (ServletContextHandler) server.getHandler();
            disableMethodsForHandler(servletContextHandler, new String[]{"TRACE"});
        });
        return factory;
    }


    /*
        https://stackoverflow.com/questions/29348328/java-embedded-jetty-is-accepting-http-trace-method
     */
    private static void disableMethodsForHandler(final ServletContextHandler servletContextHandler, String[] methods) {

        SecurityHandler securityHandler = servletContextHandler.getSecurityHandler();
        if (securityHandler == null) {
            securityHandler = new ConstraintSecurityHandler();
            servletContextHandler.setSecurityHandler(securityHandler);
        }
        if (securityHandler instanceof ConstraintSecurityHandler) {
            ConstraintSecurityHandler constraintSecurityHandler = (ConstraintSecurityHandler) securityHandler;

            for (String method : methods) {
                ConstraintMapping disableTraceMapping = new ConstraintMapping();
                Constraint disableTraceConstraint = new Constraint();
                disableTraceConstraint.setName("Disable " + method);
                disableTraceConstraint.setAuthenticate(true);
                disableTraceMapping.setConstraint(disableTraceConstraint);
                disableTraceMapping.setPathSpec("/");
                disableTraceMapping.setMethod(method);
                constraintSecurityHandler.addConstraintMapping(disableTraceMapping);
            }
        }
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(ImmutableList.of("*"));
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }

}


