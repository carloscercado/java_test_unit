/*
 * Copyright TUTEN 2015
 */
package cl.tuten.core.utils;

import cl.tuten.commons.exception.TutenRESTException;
import cl.tuten.core.bo.TutenAdministrator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;

/**
 * @author tuten
 */
public class TutenUtils {

    public static void checkIfTraversalPath(final String relativePath) throws RuntimeException {
        File file = new File(relativePath);
        if (file.isAbsolute()) {
            throw new RuntimeException("Directory traversal attempt - absolute path not allowed");
        }
        String pathUsingCanonical;
        String pathUsingAbsolute;
        try {
            pathUsingCanonical = file.getCanonicalPath();
            pathUsingAbsolute = file.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException("Directory traversal attempt?", e);
        }
        // Require the absolute path and canonicalized path match.
        // This is done to avoid directory traversal 
        // attacks, e.g. "1/../2/" 
        if (!pathUsingCanonical.equals(pathUsingAbsolute)) {
            throw new RuntimeException("Directory traversal attempt?");
        }
    }

    // save uploaded file to new location
    public static boolean saveToFile(InputStream uploadedInputStream,
            String uploadedFileLocation) {
        try (OutputStream out = new FileOutputStream(new File(uploadedFileLocation))) {
            int read;
            byte[] bytes = new byte[1024];
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(TutenUtils.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static void logError(Class clazz, Throwable e, String info) {
        Logger.getLogger(clazz.getName()).log(Level.SEVERE, info, e);
    }

    public static String getFileNameExtension(String filename) {
        String extension = null;

        int i = filename.lastIndexOf('.');
        if (i > 0) {
            extension = filename.substring(i + 1);
        }
        return extension;
    }
    
    public static String getJWT(final TutenAdministrator user, final long expirationTime) {
        final long time = System.currentTimeMillis();
        final Date date = new Date(time);
        final Date expiration = new Date(time + expirationTime);

        final String jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, Constants.JWT_KEY)
                .setSubject(user.getUsername())
                .setId(user.getUserId().toString())
                .setIssuedAt(date)
                .setExpiration(expiration)
                .claim("APP", "APP_BCK")
                .claim("email", user.getEmail())
                .claim("first_name", user.getFirstName())
                .claim("last_name", user.getLastName())
                .compact();
        return jwt;
    }
    
    public static Claims decodeJWT(final String jwt) throws TutenRESTException {
        JsonObject json;
        try {
            if (jwt == null || jwt.isEmpty()) {
                json = Json.createObjectBuilder()
                        .add("code", "JWT_REQUIRED")
                        .add("message", "El token es requerido")
                        .build();
                throw new TutenRESTException(Response.Status.UNAUTHORIZED, json.toString());
            }
            final Claims claims = Jwts.parser()
                    .setSigningKey(Constants.JWT_KEY)
                    .parseClaimsJws(jwt).getBody();
            return claims;

        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            json = Json.createObjectBuilder()
                    .add("code", "JWT_EXPIRED")
                    .add("message", "El token usado ya expiro")
                    .build();
            throw new TutenRESTException(Response.Status.UNAUTHORIZED, json.toString());
        } catch (io.jsonwebtoken.SignatureException ex) {
            json = Json.createObjectBuilder()
                    .add("code", "JWT_INVALID")
                    .add("message", "El token usado es invalido")
                    .build();
            throw new TutenRESTException(Response.Status.UNAUTHORIZED, json.toString());
        } catch (io.jsonwebtoken.JwtException ex) {
            json = Json.createObjectBuilder()
                    .add("code", "JWT_ERROR")
                    .add("message", "Problemas con el token")
                    .build();
            throw new TutenRESTException(Response.Status.UNAUTHORIZED, json.toString());
        }
    }

    public static String createJWT(final TutenAdministrator user, final Long expirationTime) {
        final long time = System.currentTimeMillis();
        final Date date = new Date(time);
        final Date expiration = new Date(time + (expirationTime * 60000));

        final String jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, Constants.JWT_KEY)
                .setSubject(user.getUsername())
                .setId(user.getUserId().toString())
                .setIssuedAt(date)
                .setExpiration(expiration)
                .claim("ROLE", user.getRole().getRoleId()).compact();

        return jwt;
    }

    public static boolean isJUnitTest() {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getClassName().startsWith("org.junit.")) {
                return true;
            }
        }
        return false;
    }
}
