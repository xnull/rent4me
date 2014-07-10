package bynull.realty.web.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;

/**
 * @author dionis on 10/07/14.
 */
public class CrossDomainFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
//        HttpServletResponse response = (HttpServletResponse) resp;
//        response.addHeader("Access-Control-Allow-Origin", "*");
//        response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
//        response.addHeader("Access-Control-Allow-Credentials", "true");
//        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
//        response.addHeader("Access-Control-Max-Age", "1209600");

        chain.doFilter(request, resp);
    }

    @Override
    public void destroy() {

    }
}
