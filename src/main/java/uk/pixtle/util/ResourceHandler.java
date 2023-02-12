package uk.pixtle.util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
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

    public static ImageIcon getResourceAsImageIcon(String paramFileName, String paramTitle, int paramWidth, int paramHeight) {
        Image transformedImage = new ImageIcon(ResourceHandler.getResourceURL(paramFileName)).getImage().getScaledInstance(paramWidth, paramHeight, Image.SCALE_SMOOTH);

        return new ImageIcon(transformedImage);
    }
}
