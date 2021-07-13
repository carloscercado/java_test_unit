package cl.tuten.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HelperEnvironment {

    private Properties prop = new Properties();

    public HelperEnvironment() {

    }

    public String getEnvironmentBackEndWebUrl() {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("config.properties");
        String url = null;
        try {
            prop.load(resourceAsStream);
            url = prop.getProperty("macarena.backend.web.url");
            String protocol = prop.getProperty("macarena.backend.web.protocol");
            url = url == null || url.isEmpty() ? null : protocol.concat("://").concat(url);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return url;
    }


    public String getDefaultEnvironment() {

        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("config.properties");
        String url = null;
        try {
            prop.load(resourceAsStream);
            url = prop.getProperty("macarena.source.url");
            url = url == null || url.isEmpty() ? null : url;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }


}
