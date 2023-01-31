package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.chunk;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ChunkImageProcessor extends Thread {

    @Getter
    @Setter
    Chunk chunk;

    public void run() {

        if(!chunk.isRenderingChange() && !chunk.isChanged()) {
            return;
        }

        BufferedImage actualImage = new BufferedImage(chunk.getSize(), chunk.getSize(), BufferedImage.TYPE_INT_ARGB | BufferedImage.SCALE_FAST);
        Graphics2D g2 = (Graphics2D) actualImage.getGraphics();

        for(int layerID : this.getChunk().getVisibleLayersInOrder()) {
            if(this.getChunk().isActualImageForLayer(layerID)) {
                BufferedImage bufferedImage = this.getChunk().getActualImageByLayer(layerID);
                g2.drawImage(bufferedImage, 0, 0, null);
            }
        }

        chunk.setActualImage(actualImage);

        if(chunk.getScale() != 1) {
            Image img = chunk.getActualImage().getScaledInstance((int) Math.ceil(chunk.getActualImage().getWidth() * chunk.getScale()), (int) Math.ceil(chunk.getActualImage().getHeight() * chunk.getScale()), Image.SCALE_FAST);
            BufferedImage outputImage = new BufferedImage((int) Math.ceil(chunk.getActualImage().getWidth() * chunk.getScale()), (int) Math.ceil(chunk.getActualImage().getHeight() * chunk.getScale()), BufferedImage.TYPE_INT_ARGB | BufferedImage.SCALE_FAST);
            outputImage.getGraphics().drawImage(img, 0, 0, null);

            chunk.setLastRenderedImage(outputImage);
        }
        chunk.setRenderingChange(false);
    }

    public ChunkImageProcessor(Chunk paramChunk) {
        this.setChunk(paramChunk);
    }
}
