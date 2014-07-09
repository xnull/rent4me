package bynull.realty.web.filters;

import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author dionis on 09/07/14.
 */

public class TokenAuthenticationFilter extends GenericFilterBean {

    private final AuthenticationDetailsSource<HttpServletRequest,?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

    AuthenticationEntryPoint authenticationEntryPoint;

    AuthenticationManager authenticationManager;

    public void setAuthenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) resp;

        String token = request.getHeader("Token");
        if(token == null) {
            chain.doFilter(request, response);
            return;
        }

        //try to authenticate
        try {


            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(null, token);
            authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
            Authentication authResult = authenticationManager.authenticate(authRequest);
        } catch (AuthenticationException e) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response, e);
            throw new BadCredentialsException("Bad creds");
//            return;
        }

        chain.doFilter(request, response);
    }
}
