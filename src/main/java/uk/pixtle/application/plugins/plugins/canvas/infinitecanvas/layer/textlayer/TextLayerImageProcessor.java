package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.textlayer;

import org.w3c.dom.Text;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.InfiniteCanvasPlugin;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.Layer;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.LayerImageProcessor;
import uk.pixtle.application.ui.window.canvas.CanvasUI;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TextLayerImageProcessor extends LayerImageProcessor {

    public TextLayerImageProcessor(Layer paramLayer) {
        super(paramLayer);
    }

    @Override
    public BufferedImage getLayerAsBufferedImage() {

        TextLayer textLayer = (TextLayer) this.getLayer();

        CanvasUI canvasUI = ((CanvasUI) this.getLayer().getLayerManager().getInfiniteCanvasPlugin().getApplication().getUIManager().getWindow().getCanvas());
        //InfiniteCanvasPlugin infiniteCanvasPlugin = this.getLayer().getLayerManager().getInfiniteCanvasPlugin();


        BufferedImage bufferedImage = new BufferedImage(canvasUI.getWidth(), canvasUI.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();
        graphics2D.setFont(new Font(textLayer.getFontType(), Font.PLAIN, textLayer.getTextSize()));
        graphics2D.setPaint(Color.BLACK);
        graphics2D.drawString(textLayer.getText(), 0, 100);

        graphics2D.dispose();

        return bufferedImage;
    }
}
