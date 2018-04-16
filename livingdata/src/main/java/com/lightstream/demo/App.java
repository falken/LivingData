package com.lightstream.demo;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.QueryLogger;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import java.util.EnumSet;

import static com.datastax.driver.core.QueryLogger.NORMAL_LOGGER;
import static com.google.common.collect.Lists.newArrayList;

/**
 * Hello world!
 *
 */
public class App extends Application<AppConfiguration>
{
    public static void main( String[] args ) throws Exception {
        if(args.length < 1)
            new App().run("server","config.yml");
        else
            new App().run(args);
    }

    public void run(AppConfiguration configuration, Environment environment) throws Exception {
        FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Add URL mapping
        filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS,PATCH");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_TIMING_ORIGINS_PARAM, "*");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_EXPOSE_HEADERS_HEADER, "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,Location");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,Location");
        filter.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        Cluster cluster = configuration.getCassandraFactory().build(environment);
        environment.jersey().register(new PeopleResource(new PersonService(cluster),new PersonAccountService(cluster)));
    }
}
