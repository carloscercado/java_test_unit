package cl.tuten.core.rest;

import javax.ws.rs.core.Response;

/**
 *
 * @author miguel@tuten.cl
 */
public class TutenResponse {

    protected String status;
    protected int statusCode;
    
    public static Response getOK() {
        TutenResponse r = new TutenResponse();
        r.setStatusCode(Response.Status.OK.getStatusCode());
        r.setStatus(Response.Status.OK.toString());
        return Response.ok(r).build();
    }
    
    public static Response getOK(int statusCode) {
        TutenResponse r = new TutenResponse();
        r.setStatusCode(statusCode);
        r.setStatus(Response.Status.OK.toString());
        return Response.ok(r).build();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

}
