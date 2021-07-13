package cl.tuten.core.rest;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class CORSFilter implements Filter {

    public CORSFilter() {
    }

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(
            ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        ((HttpServletResponse) response).addHeader(
                "Access-Control-Allow-Origin", "*"
        );
        ((HttpServletResponse) response).addHeader(
                "Access-Control-Allow-Credentials", "true"
        );
        ((HttpServletResponse) response).addHeader(
                "Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS, HEAD"
        );
        ((HttpServletResponse) response).addHeader(
                "Access-Control-Allow-Headers", "keywords,key,app,token,password,rut,email,userId,emailpro,adminemail,body, X-Requested-With, origin, content-type, accept"
        );
        chain.doFilter(request, response);
    }
}
