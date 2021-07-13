package cl.tuten.core.utils;

import org.json.JSONObject;
import javax.ws.rs.core.Response;
import java.util.HashMap;

/*
* @autor Carlos Cercado
* */
public class ResponseHelper {
    private HashMap<String, Object> data = new HashMap();

    public ResponseHelper() {
    }

    public static ResponseHelper create() {
        return new ResponseHelper();
    }

    public ResponseHelper add(String name, Object value) {
        if (value == null) {
            return this;
        } else {
            data.put(name, value);
            return this;
        }
    }

    public Response toResponse(final Response.Status status) {
        return Response.status(status).entity(this.toJSON()).build();
    }

    public String toJSON() {
        return (new JSONObject(this.data)).toString();
    }

}
