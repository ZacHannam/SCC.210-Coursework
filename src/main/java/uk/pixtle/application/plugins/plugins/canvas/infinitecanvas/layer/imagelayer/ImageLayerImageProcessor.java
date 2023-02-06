package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.imagelayer;

import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.InfiniteCanvasPlugin;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.Layer;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.LayerImageProcessor;
import uk.pixtle.application.ui.window.canvas.CanvasUI;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageLayerImageProcessor extends LayerImageProcessor {

    @Override
    public BufferedImage getLayerAsBufferedImage() {
        ImageLayer imageLayer = (ImageLayer) this.getLayer();

        if(imageLayer.getImage() == null){
            return null;
        }

        // check if rendered

        CanvasUI canvasUI = ((CanvasUI) this.getLayer().getLayerManager().getInfiniteCanvasPlugin().getApplication().getUIManager().getWindow().getCanvas());
        InfiniteCanvasPlugin infiniteCanvasPlugin = this.getLayer().getLayerManager().getInfiniteCanvasPlugin();

        // FIX:
        //if(imageLayer.getTopLeftPixel().getX() > canvasUI.getWidth() + infiniteCanvasPlugin.getCurrentPixelX()) return null;
        //if(imageLayer.getTopLeftPixel().getY() > canvasUI.getHeight() + infiniteCanvasPlugin.getCurrentPixelY()) return null;
        //if(imageLayer.getBottomRightPixel().getX() < infiniteCanvasPlugin.getCurrentPixelX()) return null;
        //if(imageLayer.getBottomRightPixel().getY() < infiniteCanvasPlugin.getCurrentPixelY()) return null;

        BufferedImage image = imageLayer.getImage();

        if(infiniteCanvasPlugin.getZoom() != 1) {
            Image img = imageLayer.getImage().getScaledInstance((int) Math.ceil(imageLayer.getImage().getWidth() * infiniteCanvasPlugin.getZoom()), (int) Math.ceil(imageLayer.getImage().getHeight() * infiniteCanvasPlugin.getZoom()), Image.SCALE_FAST);
            BufferedImage outputImage = new BufferedImage((int) Math.ceil(imageLayer.getImage().getWidth() * infiniteCanvasPlugin.getZoom()), (int) Math.ceil(imageLayer.getImage().getWidth() * infiniteCanvasPlugin.getZoom()), BufferedImage.TYPE_INT_ARGB | BufferedImage.SCALE_FAST);
            outputImage.getGraphics().drawImage(img, 0, 0, null);

            image = outputImage;
        }

        BufferedImage bufferedImage = new BufferedImage(canvasUI.getWidth(), canvasUI.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();

        int differenceX = (int) imageLayer.getTopLeftPixel().getX() - infiniteCanvasPlugin.getCurrentPixelX();
        int differenceY = (int) imageLayer.getTopLeftPixel().getY() - infiniteCanvasPlugin.getCurrentPixelY();

        g2d.drawImage(image, (int) Math.floor(differenceX * infiniteCanvasPlugin.getZoom()), (int) Math.floor(differenceY * infiniteCanvasPlugin.getZoom()), null);

        return bufferedImage;
    }

    public ImageLayerImageProcessor(Layer paramLayer) {
        super(paramLayer);
    }
}
