package bynull.realty.web.utils;

/**
 * Created by dionis on 3/15/15.
 */
public class RequestContext {
    private static ThreadLocal<RequestContext> requestContextThreadLocal = new ThreadLocal<>();
    private long requestStartTime;

    public static RequestContext getRequestContext() {
        if (requestContextThreadLocal.get() == null) {
            requestContextThreadLocal.set(new RequestContext());
        }
        return requestContextThreadLocal.get();
    }

    public static void destroyRequestContext() {
        requestContextThreadLocal.remove();
    }

    private RequestContext() {
    }

    public void setRequestStartTime() {
        this.requestStartTime = System.currentTimeMillis();
    }


    public long calculateRequestTimeMillis() {
        return System.currentTimeMillis() - requestStartTime;
    }
}
