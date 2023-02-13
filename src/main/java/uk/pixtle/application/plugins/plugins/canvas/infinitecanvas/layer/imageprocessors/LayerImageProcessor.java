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
    Layer layer;

    @Getter
    @Setter
    int currentPixelX;

    @Getter
    @Setter
    int currentPixelY;

    @Getter
    @Setter
    int width;

    @Getter
    @Setter
    int height;

    @Getter
    @Setter
    double zoom;

    @Getter
    @Setter
    boolean showEditMenu;

    private boolean valuesInserted = false;

    public void setBounds(int paramCurrentPixelX, int paramCurrentPixelY, int paramWidth, int paramHeight, double paramZoom, boolean paramShowEditMenu) {
        this.setCurrentPixelX(paramCurrentPixelX);
        this.setCurrentPixelY(paramCurrentPixelY);
        this.setWidth(paramWidth);
        this.setHeight(paramHeight);
        this.setZoom(paramZoom);
        this.setShowEditMenu(paramShowEditMenu);

        this.valuesInserted = true;

    }

    public abstract boolean isDrawn();
    public abstract BufferedImage getLayerAsBufferedImage();
    public abstract boolean shouldApplyEditFilter();
    public abstract OffsetBufferedImage applyEditFilter(BufferedImage paramBufferedImage);
    public abstract BufferedImage wrapOnScreenSize(OffsetBufferedImage paramBufferedImage, int paramWidth, int paramHeight);

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
        if(this.getZoom() != 1) {
            Image img = paramImage.getScaledInstance((int) Math.ceil(paramImage.getWidth() * this.getZoom()), (int) Math.ceil(paramImage.getHeight() * this.getZoom()), Image.SCALE_SMOOTH);
            BufferedImage outputImage = new BufferedImage((int) Math.ceil(paramImage.getWidth() * this.getZoom()), (int) Math.ceil(paramImage.getHeight() * this.getZoom()), BufferedImage.TYPE_INT_ARGB | BufferedImage.SCALE_FAST);
            outputImage.getGraphics().drawImage(img, 0, 0, null);

            return outputImage;
        }
        return paramImage;
    }

    /**
     * Get a buffered image the size of the screen
     */
    public void run() {

        if(!this.valuesInserted) {
            throw new RuntimeException("Values not inserted");
            // throw error
        }

        if (!this.getLayer().isReRender() && this.getLayer().getLastRenderedImage() != null) {
            return;
        }

        if(!this.isDrawn()) {
            BufferedImage bufferedImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
            this.getLayer().setLastRenderedImage(bufferedImage);
            this.getLayer().setReRender(false);
        }


        BufferedImage renderedImage = this.performEffects(this.scaleImage(this.getLayerAsBufferedImage()));

        OffsetBufferedImage offsetBufferedImage = this.shouldApplyEditFilter() && this.isShowEditMenu()  ? this.applyEditFilter(renderedImage) : new OffsetBufferedImage(0, 0, renderedImage);

        BufferedImage finalImage = wrapOnScreenSize(offsetBufferedImage, this.getWidth(), this.getHeight());

        this.getLayer().setLastRenderedImage(finalImage);
        this.getLayer().setReRender(false);
    }

    public LayerImageProcessor(Layer paramLayer) {
        this.setLayer(paramLayer);

        this.setInfiniteCanvasPlugin(this.getLayer().getLayerManager().getInfiniteCanvasPlugin());
        this.setApplication(this.getInfiniteCanvasPlugin().getApplication());

    }
}
