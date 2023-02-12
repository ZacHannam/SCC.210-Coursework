package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.imageprocessors;

import com.twelvemonkeys.image.ConvolveWithEdgeOp;
import lombok.Getter;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.Kernel;

public final class Effects {


    public static BufferedImage flipX(BufferedImage paramBufferedImage) {
        AffineTransform flip = AffineTransform.getScaleInstance(-1d, 1d);
        flip.translate(-paramBufferedImage.getWidth(), 0);
        AffineTransformOp op = new AffineTransformOp(flip, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(paramBufferedImage, null);
    }

    public static BufferedImage flipY(BufferedImage paramBufferedImage) {
        AffineTransform flip = AffineTransform.getScaleInstance(1d, -1d);
        flip.translate(0, -paramBufferedImage.getHeight());
        AffineTransformOp op = new AffineTransformOp(flip, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(paramBufferedImage, null);
    }

    public static BufferedImageOp getBlurredEffectBufferOp() {
        int radius = 11;
        int size = radius * 2 + 1;
        float weight = 1.0f / (size * size);
        float[] data = new float[size * size];

        for (int i = 0; i < data.length; i++) {
            data[i] = weight;
        }

        Kernel kernel = new Kernel(size, size, data);
        return new ConvolveWithEdgeOp(kernel, ConvolveWithEdgeOp.EDGE_REFLECT, null);
    }

    public static BufferedImage blur(BufferedImage paramBufferedImage) {
        return getBlurredEffectBufferOp().filter(paramBufferedImage, null);
    }

    public static BufferedImage invertColours(BufferedImage paramBufferedImage) {
        BufferedImage newImage = new BufferedImage(paramBufferedImage.getWidth(), paramBufferedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < paramBufferedImage.getHeight(); i++) {
            for (int j = 0; j < paramBufferedImage.getWidth(); j++) {
                int alpha = (paramBufferedImage.getRGB(j, i) >> 24) & 0xFF;

                if(alpha != 0) {
                    Color color = new Color(paramBufferedImage.getRGB(j, i));
                    Color newColor = new Color(Math.abs(255 - color.getRed()),
                            Math.abs(255 - color.getGreen()),
                            Math.abs(255 - color.getBlue()));

                    int rgba = (alpha << 24) | (newColor.getRGB() & 0x00FFFFFF);

                    newImage.setRGB(j, i, rgba);
                }
            }
        }

        return newImage;
    }

    public static BufferedImage updateOpacity(BufferedImage paramBufferedImage, float paramOpacity) {
        BufferedImage newImage = new BufferedImage(paramBufferedImage.getWidth(), paramBufferedImage.getHeight(), BufferedImage.TYPE_INT_ARGB | BufferedImage.SCALE_FAST);
        for (int i = 0; i < paramBufferedImage.getHeight(); i++) {
            for (int j = 0; j < paramBufferedImage.getWidth(); j++) {
                int oldAlpha = (paramBufferedImage.getRGB(j, i) >> 24) & 0xFF;

                int alpha = (int) Math.floor(oldAlpha * paramOpacity);
                int rgba = (alpha << 24) | (paramBufferedImage.getRGB(j, i) & 0x00FFFFFF);
                newImage.setRGB(j, i, rgba);
            }
        }
        return newImage;
    }

}
