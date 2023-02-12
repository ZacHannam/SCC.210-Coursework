package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.textlayer;

import org.w3c.dom.Text;
import uk.pixtle.application.Application;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.InfiniteCanvasPlugin;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.Layer;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.LayerImageProcessor;
import uk.pixtle.application.ui.window.canvas.CanvasUI;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class TextLayerImageProcessor extends LayerImageProcessor {

    public TextLayerImageProcessor(Layer paramLayer) {
        super(paramLayer);
    }

    @Override
    public BufferedImage getLayerAsBufferedImage() {

        TextLayer textLayer = (TextLayer) this.getLayer();

        if(textLayer.getText() == null) return null;

        CanvasUI canvasUI = ((CanvasUI) this.getLayer().getLayerManager().getInfiniteCanvasPlugin().getApplication().getUIManager().getWindow().getCanvas());
        InfiniteCanvasPlugin infiniteCanvasPlugin = this.getLayer().getLayerManager().getInfiniteCanvasPlugin();


        Font font = new Font(textLayer.getFontType(), Font.PLAIN, textLayer.getTextSize());
        Dimension size = textLayer.getDimensions();

        if(size.getWidth() == 0 || size.getHeight() == 0) {
            return null;
        }

        BufferedImage image = new BufferedImage((int) size.getWidth(), (int) size.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = (Graphics2D) image.getGraphics();
        graphics2D.setFont(font);
        graphics2D.setPaint(textLayer.getForegroundColor());
        graphics2D.drawString(textLayer.getText(), 0, (int) Math.round(size.getHeight() * 2/3.0));

        graphics2D.dispose();

        if(infiniteCanvasPlugin.getZoom() != 1) {
            Image img = image.getScaledInstance((int) Math.ceil(image.getWidth() * infiniteCanvasPlugin.getZoom()), (int) Math.ceil(image.getHeight() * infiniteCanvasPlugin.getZoom()), Image.SCALE_SMOOTH);
            BufferedImage outputImage = new BufferedImage((int) Math.ceil(image.getWidth() * infiniteCanvasPlugin.getZoom()), (int) Math.ceil(image.getHeight() * infiniteCanvasPlugin.getZoom()), BufferedImage.TYPE_INT_ARGB | BufferedImage.SCALE_FAST);
            outputImage.getGraphics().drawImage(img, 0, 0, null);

            image = outputImage;
        }

        int indentX = 0, indentY = 0;
        Application application = this.getLayer().getLayerManager().getInfiniteCanvasPlugin().getApplication();
        if(application.getPluginManager().getActivatePlugin() == infiniteCanvasPlugin && this.getLayer().getLayerManager().getActiveLayer() == textLayer) {
            BufferedImage newImage = new BufferedImage(image.getWidth() + 20, image.getHeight() + 20, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = (Graphics2D) newImage.getGraphics();
            g2d.setPaint(Color.blue);
            g2d.fillRect(10, 5, newImage.getWidth() - 15, 5);
            g2d.fillRect(newImage.getWidth() - 10, 10, 5, newImage.getHeight() - 15);
            g2d.fillRect(5, newImage.getHeight() - 10, newImage.getWidth() - 10, 5);
            g2d.fillRect(5, 5, 5, newImage.getHeight() - 10 );
            g2d.drawImage(image, 10, 10, null);

            image = newImage;

            indentX = 10; indentY = 10;
        }

        BufferedImage bufferedImage = new BufferedImage(canvasUI.getWidth(), canvasUI.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();

        int differenceX = (int) textLayer.getTopLeftPixel().getX() - infiniteCanvasPlugin.getCurrentPixelX();
        int differenceY = (int) textLayer.getTopLeftPixel().getY() - infiniteCanvasPlugin.getCurrentPixelY();

        g2d.drawImage(image, (int) Math.floor(differenceX * infiniteCanvasPlugin.getZoom()) - indentX, (int) Math.floor(differenceY * infiniteCanvasPlugin.getZoom()) - indentY, null);

        graphics2D.dispose();

        return bufferedImage;
    }
}
