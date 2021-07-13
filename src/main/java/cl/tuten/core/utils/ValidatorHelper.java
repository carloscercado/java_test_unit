/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.tuten.core.utils;

/**
 *
 * @author karol
 */
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.core.Response;
import ve.zlab.k.KException;
import ve.zlab.k.KExecutor;
import ve.zlab.k.KRow;
import ve.zlab.k.helper.KExceptionHelper;

public class ValidatorHelper {

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static void assertNotNull(Object value, String response) throws KException {
        assertNotNull(value, Response.Status.BAD_REQUEST, response);
    }
    
    public static void assertNotNull(Object value, final Response.Status status, final String response) throws KException {
        if (value == null) {
            throw new KException(status, response);
        }
        
        if (value instanceof KRow) {
            if (((KRow) value).isNull()) {
                throw new KException(status, response);
            }
        }
    }
    
    public static void assertNotNullNotEmpty(Object value, String response) throws KException {
        assertNotNullNotEmpty(value, Response.Status.BAD_REQUEST, response);
    }
    
    public static void assertNotNullNotEmpty(final Object value, final Response.Status status, final String response) throws KException {
        if (value == null) {
            throw new KException(status, response);
        }
        
        if (value instanceof List) {
            final List<?> list = (List) value;
            
            if (list.isEmpty()) {
                throw new KException(status, response);
            }
            
            return;
        }
        
        if (value.toString().trim().isEmpty()) {
            throw new KException(status, response);
        }
    }
    
    public static void assertContainedIn(Object value, Object[] array, String response) throws KException {
        assertContainedIn(value, array, Response.Status.BAD_REQUEST, response);
    }
    
    public static void assertContainedIn(Object value, Object[] array, final Response.Status status, final String response) throws KException {
        if (value == null || array == null) {
            throw new KException(status, response);
        }
        
        if (!Arrays.asList(array).contains(value)) {
            throw new KException(status, response);
        }
    }
}