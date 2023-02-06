package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer;

import com.twelvemonkeys.image.ConvolveWithEdgeOp;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;

public abstract class LayerImageProcessor extends Thread {

    public abstract BufferedImage getLayerAsBufferedImage();

    @Getter
    @Setter
    Layer layer;

    @Getter
    BufferedImageOp blurredEffectOp;

    public void createBlurredEffectOp() {
        int radius = 11;
        int size = radius * 2 + 1;
        float weight = 1.0f / (size * size);
        float[] data = new float[size * size];

        for (int i = 0; i < data.length; i++) {
            data[i] = weight;
        }

        Kernel kernel = new Kernel(size, size, data);
        this.blurredEffectOp = new ConvolveWithEdgeOp(kernel, ConvolveWithEdgeOp.EDGE_REFLECT, null);
    }

    public void run() {

        if (!this.getLayer().isReRender() && this.getLayer().getLastRenderedImage() != null) {
            return;
        }

        BufferedImage renderedImage = this.getLayerAsBufferedImage();

        /*
                    OPACITY
         */

        if (this.getLayer().getOpacity() != 1) {
            BufferedImage newImage = new BufferedImage(renderedImage.getWidth(), renderedImage.getHeight(), BufferedImage.TYPE_INT_ARGB | BufferedImage.SCALE_FAST);
            for (int i = 0; i < renderedImage.getHeight(); i++) {
                for (int j = 0; j < renderedImage.getWidth(); j++) {
                    int oldAlpha = (renderedImage.getRGB(j, i) >> 24) & 0xFF;

                    int alpha = (int) Math.floor(oldAlpha * this.getLayer().getOpacity());
                    int rgba = (alpha << 24) | (renderedImage.getRGB(j, i) & 0x00FFFFFF);
                    newImage.setRGB(j, i, rgba);
                }
            }
            renderedImage = newImage;
        }

        /*

                   INVERT COLOURS

         */

        if(this.getLayer().isInvertColours()) {
            BufferedImage newImage = new BufferedImage(renderedImage.getWidth(), renderedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

            for (int i = 0; i < renderedImage.getHeight(); i++) {
                for (int j = 0; j < renderedImage.getWidth(); j++) {
                    int alpha = (renderedImage.getRGB(j, i) >> 24) & 0xFF;

                    if(alpha != 0) {
                        Color color = new Color(renderedImage.getRGB(j, i));
                        Color newColor = new Color(Math.abs(255 - color.getRed()),
                                Math.abs(255 - color.getGreen()),
                                Math.abs(255 - color.getBlue()));

                        int rgba = (alpha << 24) | (newColor.getRGB() & 0x00FFFFFF);

                        newImage.setRGB(j, i, rgba);
                    }
                }
            }
            renderedImage = newImage;
        }

        /*

                    FLIP Y

         */

        if(this.getLayer().isFlipY()) {
            AffineTransform flip = AffineTransform.getScaleInstance(1d, -1d);
            flip.translate(0, -renderedImage.getHeight());
            AffineTransformOp op = new AffineTransformOp(flip, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            renderedImage = op.filter(renderedImage, null);
        }

        /*

                    FLIP X

         */

        if(this.getLayer().isFlipX()) {
            AffineTransform flip = AffineTransform.getScaleInstance(-1d, 1d);
            flip.translate(-renderedImage.getWidth(), 0);
            AffineTransformOp op = new AffineTransformOp(flip, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            renderedImage = op.filter(renderedImage, null);
        }



        /*
                    BLUR EFFECT
         */

        if(this.getLayer().isBlurred()) {
            renderedImage = this.getBlurredEffectOp().filter(renderedImage, null);
        }

        this.getLayer().setLastRenderedImage(renderedImage);
        this.getLayer().setReRender(false);
    }

    public LayerImageProcessor(Layer paramLayer) {
        this.setLayer(paramLayer);

        this.createBlurredEffectOp();
    }
}
