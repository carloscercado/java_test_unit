
package cl.tuten.core.session;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.ejb.Local;


/**
 *
 * @author Carlos Cercado
 */
@Local
public interface APITerceroFacadeLocal {

    JSONObject createResource(final String title, final String body);
    

}
