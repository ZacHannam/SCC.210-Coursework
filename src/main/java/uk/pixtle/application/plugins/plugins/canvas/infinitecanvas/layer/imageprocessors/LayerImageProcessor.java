package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.imageprocessors;

import com.twelvemonkeys.image.ConvolveWithEdgeOp;
import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.Application;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.InfiniteCanvasPlugin;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.Layer;
import uk.pixtle.application.ui.window.canvas.CanvasUI;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;

public abstract class LayerImageProcessor extends Thread {

    @Getter
    @Setter
    InfiniteCanvasPlugin infiniteCanvasPlugin;

    @Getter
    @Setter
    Application application;

    @Getter
    @Setter
    CanvasUI canvasUI;

    public abstract boolean isDrawn();
    public abstract BufferedImage getLayerAsBufferedImage();
    public abstract boolean shouldApplyEditFilter();
    public abstract OffsetBufferedImage applyEditFilter(BufferedImage paramBufferedImage);
    public abstract BufferedImage wrapOnScreenSize(OffsetBufferedImage paramBufferedImage, int paramWidth, int paramHeight);


    @Getter
    @Setter
    Layer layer;

    public BufferedImage performEffects(BufferedImage renderedImage) {

        // Update opacity
        if (this.getLayer().getOpacity() != 1) renderedImage = Effects.updateOpacity(renderedImage, this.getLayer().getOpacity());

        // Invert Colours
        if(this.getLayer().isInvertColours()) renderedImage = Effects.invertColours(renderedImage);

        // Flip Y
        if(this.getLayer().isFlipY()) renderedImage = Effects.flipY(renderedImage);

        // Flip X
        if(this.getLayer().isFlipX()) renderedImage = Effects.flipX(renderedImage);

        // Blur effect
        if(this.getLayer().isBlurred()) renderedImage = Effects.blur(renderedImage);

        return renderedImage;
    }

    public BufferedImage scaleImage(BufferedImage paramImage) {
        if(this.getInfiniteCanvasPlugin().getZoom() != 1) {
            Image img = paramImage.getScaledInstance((int) Math.ceil(paramImage.getWidth() * this.getInfiniteCanvasPlugin().getZoom()), (int) Math.ceil(paramImage.getHeight() * this.getInfiniteCanvasPlugin().getZoom()), Image.SCALE_SMOOTH);
            BufferedImage outputImage = new BufferedImage((int) Math.ceil(paramImage.getWidth() * this.getInfiniteCanvasPlugin().getZoom()), (int) Math.ceil(paramImage.getHeight() * this.getInfiniteCanvasPlugin().getZoom()), BufferedImage.TYPE_INT_ARGB | BufferedImage.SCALE_FAST);
            outputImage.getGraphics().drawImage(img, 0, 0, null);

            return outputImage;
        }
        return paramImage;
    }

    /**
     * Get a buffered image the size of the screen
     */
    public void run() {

        if (!this.getLayer().isReRender() && this.getLayer().getLastRenderedImage() != null) {
            return;
        }

        if(!this.isDrawn()) {
            BufferedImage bufferedImage = new BufferedImage(this.getCanvasUI().getWidth(), this.getCanvasUI().getHeight(), BufferedImage.TYPE_INT_ARGB);
            this.getLayer().setLastRenderedImage(bufferedImage);
            this.getLayer().setReRender(false);
        }


        BufferedImage renderedImage = this.performEffects(this.scaleImage(this.getLayerAsBufferedImage()));

        OffsetBufferedImage offsetBufferedImage = this.shouldApplyEditFilter() ? this.applyEditFilter(renderedImage) : new OffsetBufferedImage(0, 0, renderedImage);

        BufferedImage finalImage = wrapOnScreenSize(offsetBufferedImage, this.getCanvasUI().getWidth(), this.getCanvasUI().getHeight());

        this.getLayer().setLastRenderedImage(finalImage);
        this.getLayer().setReRender(false);
    }

    public LayerImageProcessor(Layer paramLayer) {
        this.setLayer(paramLayer);

        this.setInfiniteCanvasPlugin(this.getLayer().getLayerManager().getInfiniteCanvasPlugin());
        this.setApplication(this.getInfiniteCanvasPlugin().getApplication());
        this.setCanvasUI((CanvasUI) this.getApplication().getUIManager().getWindow().getCanvas());
    }
}
