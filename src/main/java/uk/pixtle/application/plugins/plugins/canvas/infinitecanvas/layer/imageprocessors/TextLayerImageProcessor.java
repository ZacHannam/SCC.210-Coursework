package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.imageprocessors;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.Layer;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.textlayer.TextLayer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TextLayerImageProcessor extends LayerImageProcessor {

    @Getter
    @Setter
    TextLayer textLayer;

    public TextLayerImageProcessor(Layer paramLayer) {
        super(paramLayer);
        this.setTextLayer((TextLayer) paramLayer);
    }

    @Override
    public boolean isDrawn() {

        if (textLayer.getText() == null) return false;

        return true;
    }

    @Override
    public BufferedImage getLayerAsBufferedImage() {

        Font font = new Font(this.getTextLayer().getFontType(), Font.PLAIN, this.getTextLayer().getTextSize());
        Dimension size = this.getTextLayer().getDimensions();

        if (size.getWidth() == 0 || size.getHeight() == 0) {
            return null;
        }

        BufferedImage image = new BufferedImage((int) size.getWidth(), (int) size.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = (Graphics2D) image.getGraphics();
        graphics2D.setFont(font);
        graphics2D.setPaint(this.getTextLayer().getForegroundColor());
        graphics2D.drawString(this.getTextLayer().getText(), 0, (int) Math.round(size.getHeight() * 2 / 3.0));

        return image;
    }

    @Override
    public boolean shouldApplyEditFilter() {
        return super.getApplication().getPluginManager().getActivatePlugin() == super.getInfiniteCanvasPlugin() && this.getLayer().getLayerManager().getActiveLayer() == this.getTextLayer();
    }

    @Override
    public OffsetBufferedImage applyEditFilter(BufferedImage paramBufferedImage) {
        BufferedImage newImage = new BufferedImage(paramBufferedImage.getWidth() + 20, paramBufferedImage.getHeight() + 20, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) newImage.getGraphics();
        g2d.setPaint(Color.blue);
        g2d.fillRect(10, 5, newImage.getWidth() - 15, 5);
        g2d.fillRect(newImage.getWidth() - 10, 10, 5, newImage.getHeight() - 15);
        g2d.fillRect(5, newImage.getHeight() - 10, newImage.getWidth() - 10, 5);
        g2d.fillRect(5, 5, 5, newImage.getHeight() - 10);
        g2d.drawImage(paramBufferedImage, 10, 10, null);

        return new OffsetBufferedImage(-10, -10, newImage);
    }

    @Override
    public BufferedImage wrapOnScreenSize(OffsetBufferedImage paramBufferedImage, int paramWidth, int paramHeight) {
        BufferedImage bufferedImage = new BufferedImage(paramWidth, paramHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();

        int differenceX = (int) this.getTextLayer().getTopLeftPixel().getX() - super.getCurrentPixelX();
        int differenceY = (int) this.getTextLayer().getTopLeftPixel().getY() - super.getCurrentPixelY();

        g2d.drawImage(paramBufferedImage.getBufferedImage(), (int) Math.floor(differenceX * super.getZoom()) + paramBufferedImage.getOffsetX(), (int) Math.floor(differenceY * super.getZoom()) + paramBufferedImage.getOffsetY(), null);

        return bufferedImage;
    }
}