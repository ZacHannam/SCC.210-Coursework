package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.imageprocessors;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.Layer;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.drawinglayer.Chunk;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.drawinglayer.ChunkImageProcessor;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.drawinglayer.DrawingLayer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class DrawingLayerImageProcessor extends LayerImageProcessor {

    @Getter
    @Setter
    DrawingLayer drawingLayer;

    public DrawingLayerImageProcessor(Layer paramLayer) {
        super(paramLayer);
        this.setDrawingLayer((DrawingLayer) paramLayer);
    }

    @Override
    public boolean isDrawn() {
        return true;
    }

    @Override
    public BufferedImage scaleImage(BufferedImage paramImage) {
        return paramImage;
    }

    @Override
    public BufferedImage getLayerAsBufferedImage() {
        try {

            HashMap<ChunkImageProcessor, Point> renderedChunks = new HashMap<>();

            for (int i = 0, chunkIDY = (int) Math.floor(super.getCurrentPixelY() / this.getDrawingLayer().getPixelsPerChunk()); i < (int) Math.ceil((super.getHeight() + (super.getCurrentPixelY() % this.getDrawingLayer().getPixelsPerChunk() * super.getZoom())) / (super.getZoom() * this.getDrawingLayer().getPixelsPerChunk())); i++, chunkIDY++) {
                for (int j = 0, chunkIDX = (int) Math.floor(super.getCurrentPixelX() / this.getDrawingLayer().getPixelsPerChunk()); j < (int) Math.ceil((super.getWidth() + (super.getCurrentPixelX() % this.getDrawingLayer().getPixelsPerChunk() * super.getZoom())) / (super.getZoom() * this.getDrawingLayer().getPixelsPerChunk())); j++, chunkIDX++) {

                    if (chunkIDY < 0 || chunkIDY > Integer.MAX_VALUE / this.getDrawingLayer().getPixelsPerChunk() || chunkIDX < 0 || chunkIDX > Integer.MAX_VALUE / this.getDrawingLayer().getPixelsPerChunk()) {
                        continue;
                    }


                    if (!this.getDrawingLayer().isChunkAt(chunkIDX, chunkIDY)) {

                        continue;
                    }
                    Chunk chunk = this.getDrawingLayer().getChunkAt(chunkIDX, chunkIDY);

                    chunk.updateValues(super.getZoom());


                    ChunkImageProcessor CIP = new ChunkImageProcessor(chunk);
                    CIP.start();
                    renderedChunks.put(CIP, new Point(j, i));

                }
            }

            // Render the chunks

            for (ChunkImageProcessor CIP : renderedChunks.keySet()) {
                CIP.join();
            }

            // Draw the chunks

            BufferedImage bufferedImage = new BufferedImage(super.getWidth(), super.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D bufferedImageGraphics = (Graphics2D) bufferedImage.getGraphics();

            for (Map.Entry<ChunkImageProcessor, Point> entry : renderedChunks.entrySet()) {

                int widthIn = (int) Math.floor((-(super.getCurrentPixelX() % this.getDrawingLayer().getPixelsPerChunk()) * super.getZoom()) + (entry.getValue().getX() * this.getDrawingLayer().getPixelsPerChunk() * super.getZoom()));
                int heightIn = (int) Math.floor((-(super.getCurrentPixelY() % this.getDrawingLayer().getPixelsPerChunk()) * super.getZoom()) + (entry.getValue().getY() * this.getDrawingLayer().getPixelsPerChunk() * super.getZoom()));

                bufferedImageGraphics.drawImage(entry.getKey().getChunk().getLastRenderedImage(), widthIn, heightIn, null);
            }

            return bufferedImage;
        } catch(InterruptedException exception) {
            return null;
        }
    }

    @Override
    public boolean shouldApplyEditFilter() {
        return false;
    }

    @Override
    public OffsetBufferedImage applyEditFilter(BufferedImage paramBufferedImage) {
        return null;
    }

    @Override
    public BufferedImage wrapOnScreenSize(OffsetBufferedImage paramBufferedImage, int paramWidth, int paramHeight) {
        return paramBufferedImage.getBufferedImage();
    }
}