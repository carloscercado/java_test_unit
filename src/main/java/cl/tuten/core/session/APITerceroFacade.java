package cl.tuten.core.session;

import cl.tuten.core.utils.TutenUtils;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.json.JSONObject;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.MediaType;

/**
 * @author Carlos Cercado
 */
@Stateless
public class APITerceroFacade implements APITerceroFacadeLocal {

    public String url = "https://jsonplaceholder.typicode.com/posts";

    @EJB
    private TutenAdministratorFacadeLocal administratorFacade;

    public APITerceroFacade() {

    }

    @Override
    public JSONObject createResource(String title, String body){

        if(TutenUtils.isJUnitTest()){
            return new JSONObject().put("title", title).put("body", body).put("id", 100L);
        }

        System.getProperty("TESTING");

        final Client c = Client.create();

        //final String url ="https://jsonplaceholder.typicode.com/posts";

        final WebResource r = c.resource(url);

        final JsonObject obj = Json.createObjectBuilder().add("title", title).add("body", body).build();
        final String response = r.type(MediaType.APPLICATION_JSON_TYPE)
                    .post(String.class, obj.toString());




        final JSONObject json = new JSONObject(response);
        return json;
    }

}
