package uk.pixtle.util;

import java.net.URL;

public final class ResourceHandler {

    public static URL getResourceURL(String paramFileName) {
        URL url = ResourceHandler.class.getClassLoader().getResource(paramFileName);
        if(url == null) {
            // throw error
            return null;
        }
        return url;
    }

}
