package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.chunk;

import com.twelvemonkeys.image.ConvolveWithEdgeOp;
import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.Layer;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

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

                if(chunk.getInfiniteCanvasPlugin().getLayerManager().getLayers().get(layerID).getOpacity() != 1) {
                    BufferedImage newImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_ARGB | BufferedImage.SCALE_FAST);
                    for(int i = 0; i < bufferedImage.getHeight(); i++) {
                        for(int j = 0; j < bufferedImage.getWidth(); j++) {
                            int oldAlpha = (bufferedImage.getRGB(j, i) >> 24) & 0xFF;

                            int alpha = (int) Math.floor(oldAlpha * chunk.getInfiniteCanvasPlugin().getLayerManager().getLayers().get(layerID).getOpacity());
                            int rgba = (alpha << 24) | (bufferedImage.getRGB(j, i) & 0x00FFFFFF);
                            newImage.setRGB(j, i, rgba);
                        }
                    }
                    bufferedImage = newImage;
                }


                g2.drawImage(bufferedImage, 0, 0, null);
            }
        }

        chunk.setActualImage(actualImage);

        if(chunk.getScale() != 1) {
            Image img = chunk.getActualImage().getScaledInstance((int) Math.ceil(chunk.getActualImage().getWidth() * chunk.getScale()), (int) Math.ceil(chunk.getActualImage().getHeight() * chunk.getScale()), Image.SCALE_FAST);
            BufferedImage outputImage = new BufferedImage((int) Math.ceil(chunk.getActualImage().getWidth() * chunk.getScale()), (int) Math.ceil(chunk.getActualImage().getHeight() * chunk.getScale()), BufferedImage.TYPE_INT_ARGB | BufferedImage.SCALE_FAST);
            outputImage.getGraphics().drawImage(img, 0, 0, null);

            chunk.setLastRenderedImage(outputImage);
        } else {
            chunk.setLastRenderedImage(actualImage);
        }
        chunk.setRenderingChange(false);
    }

    public ChunkImageProcessor(Chunk paramChunk) {
        this.setChunk(paramChunk);
    }
}
