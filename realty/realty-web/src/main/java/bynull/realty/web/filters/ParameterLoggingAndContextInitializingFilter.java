package bynull.realty.web.filters;

import bynull.realty.web.utils.ContextHelper;
import bynull.realty.web.utils.LogUtil;
import bynull.realty.web.utils.RequestContext;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;

/**
 * Created by dionis on 3/15/15.
 */
@Slf4j
public class ParameterLoggingAndContextInitializingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }

    @Override
    public void doFilter(ServletRequest _request, ServletResponse _response, FilterChain chain) throws IOException, ServletException {
        RequestContext.getRequestContext().setRequestStartTime();

        LogUtil.initRequestContext();

        HttpServletRequest request = (HttpServletRequest) _request;
        HttpServletResponse response = (HttpServletResponse) _response;

//        log.info(">>> Requested {} {}", request.getMethod(), request.getRequestURI());

        log.info(">>> Request {} {}, query: {}", request.getMethod(),
                request.getRequestURI(),
                request.getQueryString());
        log.info(">>> Headers");

        boolean isTargetTypeForLogging = false;

        final Set<String> loggableContentTypes = ImmutableSet.of("application/json", "application/xml", "text");

        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {

            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                Enumeration<String> headers = request.getHeaders(headerName);
                StringBuilder stringBuilder = new StringBuilder();
                boolean first = true;
                while (headers.hasMoreElements()) {
                    String header = headers.nextElement();

                    if(headerName.equalsIgnoreCase("Content-Type")) {
                        isTargetTypeForLogging |= loggableContentTypes
                                .stream()
                                .anyMatch(s -> s == null || header.toLowerCase().contains(s.toLowerCase()));
                    }

                    if(!first) {
                        stringBuilder.append("\n");
                    }
                    stringBuilder.append(header);
                    first = false;
                }
                log.info(">>> Header [{}]: [{}]", headerName, stringBuilder);
            }
        } else {
            log.info(">>> Headers are null");
        }



        //wrap if it's target content type that could be logged. Otherwise do nothing.
        HttpServletRequest servletRequestWrapper = isTargetTypeForLogging ? new BodyCachingServletRequestWrapper(request) : request;
        CachingServletResponseWrapper cachingServletResponseWrapper = new CachingServletResponseWrapper(response);

        if(servletRequestWrapper instanceof BodyCachingServletRequestWrapper) {
            log.info(">>> BODY: [{}]", ((BodyCachingServletRequestWrapper)servletRequestWrapper).getBody());
        }

        try {
            chain.doFilter(servletRequestWrapper, cachingServletResponseWrapper);
        } finally {
            try {
                log.info("<<< Status code: [{}]", cachingServletResponseWrapper.getStatus());
                log.info("<<< Headers");
                Collection<String> returnHeaderNames = cachingServletResponseWrapper.getHeaderNames();
                if (returnHeaderNames != null) {

                    for (String headerName : returnHeaderNames) {
                        Collection<String> headers = cachingServletResponseWrapper.getHeaders(headerName);
                        StringBuilder stringBuilder = new StringBuilder();
                        boolean first = true;
                        for (String header : headers) {
                            if (!first) {
                                stringBuilder.append("\n");
                            }
                            stringBuilder.append(header);
                            first = false;
                        }
                        log.info("<<< Header [{}]: [{}]", headerName, stringBuilder);
                    }
                } else {
                    log.info("<<< Headers are null");
                }

                log.info("<<< Response: [{}]", cachingServletResponseWrapper.responseString());

                log.info("=== Request took {} ms", RequestContext.getRequestContext().calculateRequestTimeMillis());
                LogUtil.clearRequestContext();

            } finally {
                RequestContext.destroyRequestContext();
                ContextHelper.clearAllContexts();
            }
        }
    }

    public static class CachingServletResponseWrapper extends HttpServletResponseWrapper {


        private final ByteArrayOutputStream byteArrayOutputStream;

        /**
         * Constructs a response adaptor wrapping the given response.
         *
         * @param response
         * @throws IllegalArgumentException if the response is null
         */
        public CachingServletResponseWrapper(HttpServletResponse response) {
            super(response);
            byteArrayOutputStream = new ByteArrayOutputStream();
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            ServletOutputStream parentOutputStream = super.getOutputStream();
            return new ServletOutputStream() {
                @Override
                public void write(int b) throws IOException {
                    byteArrayOutputStream.write(b);
                    parentOutputStream.write(b);
                }

                @Override
                public void write(byte[] b) throws IOException {
                    byteArrayOutputStream.write(b);
                    parentOutputStream.write(b);
                }

                @Override
                public void write(byte[] b, int off, int len) throws IOException {
                    byteArrayOutputStream.write(b, off, len);
                    parentOutputStream.write(b, off, len);
                }

                @Override
                public void flush() throws IOException {
                    byteArrayOutputStream.flush();
                    parentOutputStream.flush();
                }

                @Override
                public void close() throws IOException {
                    byteArrayOutputStream.close();
                    parentOutputStream.close();
                }
            };
        }

        public String responseString() {
            return byteArrayOutputStream.toString();
        }
    }

    public static class BodyCachingServletRequestWrapper extends HttpServletRequestWrapper {
        private final String body;



        /**
         * Creates a ServletRequest adaptor wrapping the given request object.
         *
         * @param request
         * @throws IllegalArgumentException if the request is null
         */
        public BodyCachingServletRequestWrapper(HttpServletRequest request) throws IOException {
            super(request);
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = null;
            try {
                InputStream inputStream = request.getInputStream();
                if (inputStream != null) {
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    char[] charBuffer = new char[128];
                    int bytesRead = -1;
                    while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                        stringBuilder.append(charBuffer, 0, bytesRead);
                    }
                } else {
                    stringBuilder.append("");
                }
            } catch (IOException ex) {
                throw ex;
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException ex) {
                        throw ex;
                    }
                }
            }
            body = stringBuilder.toString();
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
            ServletInputStream servletInputStream = new ServletInputStream() {
                public int read() throws IOException {
                    return byteArrayInputStream.read();
                }

                @Override
                public int read(byte[] b) throws IOException {
                    return byteArrayInputStream.read(b);
                }

                @Override
                public int read(byte[] b, int off, int len) throws IOException {
                    return byteArrayInputStream.read(b, off, len);
                }

                @Override
                public long skip(long n) throws IOException {
                    return byteArrayInputStream.skip(n);
                }

                @Override
                public int available() throws IOException {
                    return byteArrayInputStream.available();
                }

                @Override
                public void close() throws IOException {
                    byteArrayInputStream.close();
                }

                @Override
                public synchronized void mark(int readlimit) {
                    byteArrayInputStream.mark(readlimit);
                }

                @Override
                public synchronized void reset() throws IOException {
                    byteArrayInputStream.reset();
                }

                @Override
                public boolean markSupported() {
                    return byteArrayInputStream.markSupported();
                }
            };
            return servletInputStream;
        }

        public String getBody() {
            return body;
        }
    }

    @Override
    public void destroy() {

    }
}
