package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.imageprocessors;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.Layer;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.imagelayer.ImageLayer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageLayerImageProcessor extends LayerImageProcessor {

    @Getter
    @Setter
    ImageLayer imageLayer;

    public ImageLayerImageProcessor(Layer paramLayer) {
        super(paramLayer);
        this.setImageLayer((ImageLayer) paramLayer);
    }

    @Override
    public boolean isDrawn() {
        if(this.getImageLayer().getActualImage() == null){
            return false;
        }

        // FIX:
        //if(imageLayer.getTopLeftPixel().getX() > canvasUI.getWidth() + infiniteCanvasPlugin.getCurrentPixelX()) return false;
        //if(imageLayer.getTopLeftPixel().getY() > canvasUI.getHeight() + infiniteCanvasPlugin.getCurrentPixelY()) return false;
        //if(imageLayer.getBottomRightPixel().getX() < infiniteCanvasPlugin.getCurrentPixelX()) return false;
        //if(imageLayer.getBottomRightPixel().getY() < infiniteCanvasPlugin.getCurrentPixelY()) return false;

        return true;
    }

    @Override
    public BufferedImage getLayerAsBufferedImage() {
        if(this.getImageLayer().getScaledImage() == null) {
            this.getImageLayer().recreateScaledImage();
        }

        return this.getImageLayer().getScaledImage();
    }

    @Override
    public boolean shouldApplyEditFilter() {
        return super.getApplication().getPluginManager().getActivatePlugin() == super.getInfiniteCanvasPlugin() && this.getLayer().getLayerManager().getActiveLayer() == this.getLayer();
    }

    @Override
    public OffsetBufferedImage applyEditFilter(BufferedImage paramBufferedImage) {

        BufferedImage newImage = new BufferedImage(paramBufferedImage.getWidth() + 20, paramBufferedImage.getHeight() + 20, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) newImage.getGraphics();
        g2d.setPaint(Color.blue);
        g2d.fillRect(10, 5, newImage.getWidth() - 15, 5);
        g2d.fillRect(newImage.getWidth() - 10, 10, 5, newImage.getHeight() - 15);
        g2d.fillRect(5, newImage.getHeight() - 10, newImage.getWidth() - 10, 5);
        g2d.fillRect(5, 5, 5, newImage.getHeight() - 10 );
        g2d.drawImage(paramBufferedImage, 10, 10, null);

        g2d.setPaint(Color.black);
        for(int x = 0, i = 0; x < 2; x++, i += newImage.getWidth() - 20) {
            for(int y = 0, j = 0; y < 2; y++, j += newImage.getHeight() - 20) {
                g2d.fillOval(i, j, 20, 20);
            }
        }

        return new OffsetBufferedImage(-10, -10, newImage);
    }

    @Override
    public BufferedImage wrapOnScreenSize(OffsetBufferedImage paramImage, int paramWidth, int paramHeight) {

        BufferedImage bufferedImage = new BufferedImage(paramWidth, paramHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();

        int differenceX = (int) this.getImageLayer().getTopLeftPixel().getX() - super.getCurrentPixelX();
        int differenceY = (int) this.getImageLayer().getTopLeftPixel().getY() - super.getCurrentPixelY();

        g2d.drawImage(paramImage.getBufferedImage(), (int) Math.floor(differenceX * super.getZoom()) + paramImage.getOffsetX(), (int) Math.floor(differenceY * super.getZoom()) + paramImage.getOffsetY(), null);

        return bufferedImage;
    }
}
