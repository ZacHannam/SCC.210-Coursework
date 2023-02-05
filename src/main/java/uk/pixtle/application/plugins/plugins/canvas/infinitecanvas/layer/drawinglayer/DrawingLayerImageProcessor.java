package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.drawinglayer;

import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.Layer;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.LayerImageProcessor;
import uk.pixtle.application.ui.window.canvas.CanvasUI;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class DrawingLayerImageProcessor extends LayerImageProcessor {

    public DrawingLayerImageProcessor(Layer paramLayer) {
        super(paramLayer);
    }

    @Override
    public BufferedImage getLayerAsBufferedImage() {

        DrawingLayer drawingLayer = (DrawingLayer) super.getLayer();

        try {
            int currentPositionY = drawingLayer.getLayerManager().getInfiniteCanvasPlugin().getCurrentPixelY();
            int currentPositionX = drawingLayer.getLayerManager().getInfiniteCanvasPlugin().getCurrentPixelX();

            double zoom = drawingLayer.getLayerManager().getInfiniteCanvasPlugin().getZoom();
            CanvasUI canvas = (CanvasUI) drawingLayer.getLayerManager().getInfiniteCanvasPlugin().getApplication().getUIManager().getWindow().getCanvas();

            HashMap<ChunkImageProcessor, Point> renderedChunks = new HashMap<>();

            for (int i = 0, chunkIDY = (int) Math.floor(currentPositionY / drawingLayer.getPixelsPerChunk()); i < (int) Math.ceil((canvas.getHeight() + (currentPositionY % drawingLayer.getPixelsPerChunk() * zoom)) / (zoom * drawingLayer.getPixelsPerChunk())); i++, chunkIDY++) {
                for (int j = 0, chunkIDX = (int) Math.floor(currentPositionX / drawingLayer.getPixelsPerChunk()); j < (int) Math.ceil((canvas.getWidth() + (currentPositionX % drawingLayer.getPixelsPerChunk() * zoom)) / (zoom * drawingLayer.getPixelsPerChunk())); j++, chunkIDX++) {

                    if (chunkIDY < 0 || chunkIDY > Integer.MAX_VALUE / drawingLayer.getPixelsPerChunk() || chunkIDX < 0 || chunkIDX > Integer.MAX_VALUE / drawingLayer.getPixelsPerChunk()) {
                        continue;
                    }


                    if (!drawingLayer.isChunkAt(chunkIDX, chunkIDY)) {

                        continue;
                    }
                    Chunk chunk = drawingLayer.getChunkAt(chunkIDX, chunkIDY);
                    chunk.updateValues(zoom);


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

            BufferedImage bufferedImage = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D bufferedImageGraphics = (Graphics2D) bufferedImage.getGraphics();

            for (Map.Entry<ChunkImageProcessor, Point> entry : renderedChunks.entrySet()) {

                int widthIn = (int) Math.floor((-(currentPositionX % drawingLayer.getPixelsPerChunk()) * zoom) + (entry.getValue().getX() * drawingLayer.getPixelsPerChunk() * zoom));
                int heightIn = (int) Math.floor((-(currentPositionY % drawingLayer.getPixelsPerChunk()) * zoom) + (entry.getValue().getY() * drawingLayer.getPixelsPerChunk() * zoom));

                bufferedImageGraphics.drawImage(entry.getKey().getChunk().getLastRenderedImage(), widthIn, heightIn, null);
            }

            return bufferedImage;
        } catch(InterruptedException exception) {
            return null;
        }
    }

}