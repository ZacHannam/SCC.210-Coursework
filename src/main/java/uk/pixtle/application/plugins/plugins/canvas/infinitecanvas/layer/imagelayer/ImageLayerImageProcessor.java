package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.imagelayer;

import uk.pixtle.application.Application;
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

        if(imageLayer.getActualImage() == null){
            return null;
        }

        if(imageLayer.getScaledImage() == null) {
            imageLayer.recreateScaledImage();
        }

        // check if rendered

        CanvasUI canvasUI = ((CanvasUI) this.getLayer().getLayerManager().getInfiniteCanvasPlugin().getApplication().getUIManager().getWindow().getCanvas());
        InfiniteCanvasPlugin infiniteCanvasPlugin = this.getLayer().getLayerManager().getInfiniteCanvasPlugin();

        // FIX:
        //if(imageLayer.getTopLeftPixel().getX() > canvasUI.getWidth() + infiniteCanvasPlugin.getCurrentPixelX()) return null;
        //if(imageLayer.getTopLeftPixel().getY() > canvasUI.getHeight() + infiniteCanvasPlugin.getCurrentPixelY()) return null;
        //if(imageLayer.getBottomRightPixel().getX() < infiniteCanvasPlugin.getCurrentPixelX()) return null;
        //if(imageLayer.getBottomRightPixel().getY() < infiniteCanvasPlugin.getCurrentPixelY()) return null;

        BufferedImage image = imageLayer.getScaledImage();

        if(infiniteCanvasPlugin.getZoom() != 1) {
            Image img = imageLayer.getScaledImage().getScaledInstance((int) Math.ceil(imageLayer.getScaledImage().getWidth() * infiniteCanvasPlugin.getZoom()), (int) Math.ceil(imageLayer.getScaledImage().getHeight() * infiniteCanvasPlugin.getZoom()), Image.SCALE_SMOOTH);
            BufferedImage outputImage = new BufferedImage((int) Math.ceil(imageLayer.getScaledImage().getWidth() * infiniteCanvasPlugin.getZoom()), (int) Math.ceil(imageLayer.getScaledImage().getHeight() * infiniteCanvasPlugin.getZoom()), BufferedImage.TYPE_INT_ARGB | BufferedImage.SCALE_FAST);
            outputImage.getGraphics().drawImage(img, 0, 0, null);

            image = outputImage;
        }

        // Draw the border

        int indentX = 0, indentY = 0;
        Application application = this.getLayer().getLayerManager().getInfiniteCanvasPlugin().getApplication();
        if(application.getPluginManager().getActivatePlugin() == infiniteCanvasPlugin && this.getLayer().getLayerManager().getActiveLayer() == imageLayer) {
            BufferedImage newImage = new BufferedImage(image.getWidth() + 20, image.getHeight() + 20, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = (Graphics2D) newImage.getGraphics();
            g2d.setPaint(Color.blue);
            g2d.fillRect(10, 5, newImage.getWidth() - 15, 5);
            g2d.fillRect(newImage.getWidth() - 10, 10, 5, newImage.getHeight() - 15);
            g2d.fillRect(5, newImage.getHeight() - 10, newImage.getWidth() - 10, 5);
            g2d.fillRect(5, 5, 5, newImage.getHeight() - 10 );
            g2d.drawImage(image, 10, 10, null);

            g2d.setPaint(Color.black);
            for(int x = 0, i = 0; x < 2; x++, i += newImage.getWidth() - 20) {
                for(int y = 0, j = 0; y < 2; y++, j += newImage.getHeight() - 20) {
                    g2d.fillOval(i, j, 20, 20);
                }
            }

            image = newImage;

            indentX = 10; indentY = 10;
        }

        BufferedImage bufferedImage = new BufferedImage(canvasUI.getWidth(), canvasUI.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();

        int differenceX = (int) imageLayer.getTopLeftPixel().getX() - infiniteCanvasPlugin.getCurrentPixelX();
        int differenceY = (int) imageLayer.getTopLeftPixel().getY() - infiniteCanvasPlugin.getCurrentPixelY();

        g2d.drawImage(image, (int) Math.floor(differenceX * infiniteCanvasPlugin.getZoom()) - indentX, (int) Math.floor(differenceY * infiniteCanvasPlugin.getZoom()) - indentY, null);

        return bufferedImage;
    }

    public ImageLayerImageProcessor(Layer paramLayer) {
        super(paramLayer);
    }
}
