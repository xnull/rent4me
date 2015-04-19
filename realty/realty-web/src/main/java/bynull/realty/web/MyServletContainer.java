package bynull.realty.web;

import bynull.realty.web.features.ExceptionHandlingFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * @author dionis on 05/12/14.
 */
public class MyServletContainer extends ServletContainer {
    public MyServletContainer() {
        super(new ResourceConfig()
                .packages(false, "bynull.realty.web.rest")
                .register(ExceptionHandlingFeature.class)
                .register(MultiPartFeature.class)
        )
        ;
    }
}
