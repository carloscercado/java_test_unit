/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.tuten.core.rest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import ve.zlab.k.KException;

@Provider
public class KExceptionMapper implements ExceptionMapper<KException> {
    
    @Override
    public Response toResponse(KException kException) {
        if (kException.getMessage() != null) {
            //return Response.status(kException.getStatus()).type("text/plain").
            //        entity(kException.getMessage()).build();
            
            
            TutenErrorResponse r = new TutenErrorResponse();
            r.setStatusCode(kException.getStatus().getStatusCode());
            r.setStatus(kException.getStatus().toString());
            r.setError(kException.getMessage());
            //r.setErrorCode(null);
            return Response.status(kException.getStatus()).type(MediaType.APPLICATION_JSON).entity(r).build();
        }
        
        return Response.status(kException.getStatus()).build();
    }
}

