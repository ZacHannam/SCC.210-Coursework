package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer;

import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;

public abstract class LayerImageProcessor extends Thread {

    public abstract BufferedImage getLayerAsBufferedImage();

    @Getter
    @Setter
    Layer layer;

    public void run() {

        if (!this.getLayer().isReRender() && this.getLayer().getLastRenderedImage() != null) {
            return;
        }

        this.getLayer().setLastRenderedImage(this.getLayerAsBufferedImage());
        this.getLayer().setReRender(false);
    }

    public LayerImageProcessor(Layer paramLayer) {
        this.setLayer(paramLayer);
    }
}
