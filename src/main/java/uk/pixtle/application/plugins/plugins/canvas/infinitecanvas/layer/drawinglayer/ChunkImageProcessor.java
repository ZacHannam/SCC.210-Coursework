package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.drawinglayer;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ChunkImageProcessor extends Thread {

    @Getter
    @Setter
    Chunk chunk;

    public void run() {

        if(!this.getChunk().isReRender()) {
            return;
        }

        if(this.getChunk().getLastRenderedImage() == null) {
            this.getChunk().setLastRenderedImage(this.getChunk().getActualImage());
        }

        if(this.getChunk().getScale() != 1) {
            Image img = this.getChunk().getActualImage().getScaledInstance((int) Math.ceil(this.getChunk().getActualImage().getWidth() * this.getChunk().getScale()), (int) Math.ceil(this.getChunk().getActualImage().getHeight() * this.getChunk().getScale()), Image.SCALE_FAST);
            BufferedImage outputImage = new BufferedImage((int) Math.ceil(this.getChunk().getActualImage().getWidth() * this.getChunk().getScale()), (int) Math.ceil(this.getChunk().getActualImage().getHeight() * this.getChunk().getScale()), BufferedImage.TYPE_INT_ARGB | BufferedImage.SCALE_FAST);
            outputImage.getGraphics().drawImage(img, 0, 0, null);

            this.getChunk().setLastRenderedImage(outputImage);
        }

        this.getChunk().setReRender(false);
    }

    public ChunkImageProcessor(Chunk paramChunk) {
        this.setChunk(paramChunk);
    }
}
