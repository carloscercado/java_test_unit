/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.tuten.core.utils;

import java.util.UUID;
import ve.zlab.k.KException;
import ve.zlab.k.KExecutor;

/**
 *
 * @author karol
 */
public class KUtils {
    
    public static String getTenantParams(final KExecutor K, final UUID tenantId, final String param) throws KException {
        return K.
            table("mss_login.tuten_tenant_params").
            select("param_value").
            where("param_key", param).
            where("tenant_id", tenantId).
            single().
            getString("param_value");
    }

    public static Boolean getSynchronizationMagento(final KExecutor K, final UUID tenantId) throws KException {
        return Boolean.valueOf(KUtils.getTenantParams(K, tenantId, "Magento"));
    }
    
    public static String getTenantName(final KExecutor K, final UUID tenantId) throws KException {
        return KUtils.getTenantParams(K, tenantId, "Nombre");
    }
    
    public static String getTenantLogo(final KExecutor K, final UUID tenantId) throws KException {
        return KUtils.getTenantParams(K, tenantId, "Logo");
    }
    
    public static String getTenantFavicon(final KExecutor K, final UUID tenantId) throws KException {
        return KUtils.getTenantParams(K, tenantId, "Favicon");
    }
    
    public static String getTenantTitle(final KExecutor K, final UUID tenantId) throws KException {
        return KUtils.getTenantParams(K, tenantId, "Title");
    }
    
    public static String getTenantEtiquetaBUPais(final KExecutor K, final UUID tenantId) throws KException {
        return KUtils.getTenantParams(K, tenantId, "Etiqueta/BU/Pais");
    }
    
    public static UUID getTenantByToken(final KExecutor K, final String token) throws KException{
        
        return K.
            table("mss_login.tuten_administrator a").
            innerJoin("mss_login.tuten_user_business_unit ubu", "ubu.user_id", "a.user_id").
            innerJoin("mss_login.tuten_business_unit bu", "ubu.business_unit_id", "bu.id").
            select("bu.tenant_id").
            where("a.session_token", token).
            limit(1L).
            single().
            getUUID("tenant_id");
    }
}
