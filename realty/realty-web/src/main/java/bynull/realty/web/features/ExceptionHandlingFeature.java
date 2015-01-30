package bynull.realty.web.features;

import bynull.realty.web.rest.BaseExceptionMapper;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

/**
 * Created by dionis on 30/01/15.
 */
@Provider
public class ExceptionHandlingFeature implements Feature {
    @Override
    public boolean configure(FeatureContext context) {
        context.register(BaseExceptionMapper.class);
        return true;
    }
}
