/*
 * Copyright 2015 Tuten.
 */
package cl.tuten.core.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miguel@tuten.cl
 */
public class Token {

    private static final SecureRandom random = new SecureRandom();

    
    @Deprecated
    public static String getRandomString() {
        return new BigInteger(130, random).toString(32);
    }
    
    public static String getBookingIDUUID(long id){
        return UUID.randomUUID().toString()+"-"+id;
    }

    public static String getBetterRandomString(){
        return getStringHash(System.nanoTime()+(new BigInteger(130, random).toString(32)));
    }
    
    public static String getStringHash(String str) {
        if(str == null){
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes());
            return Base64.getEncoder().encodeToString(messageDigest.digest());
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
