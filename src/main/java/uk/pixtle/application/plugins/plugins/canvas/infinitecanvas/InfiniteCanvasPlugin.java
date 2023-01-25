package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.Application;
import uk.pixtle.application.plugins.expansions.PluginDrawableExpansion;
import uk.pixtle.application.plugins.plugins.canvas.CanvasPlugin;
import uk.pixtle.application.ui.window.canvas.CanvasUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class InfiniteCanvasPlugin extends CanvasPlugin implements PluginDrawableExpansion {

    Chunk[][] availableChunks;

    int pixelsPerChunkLength = 256;

    // Current Pixel refers to pixel in top left corner
    int currentPixelX = 100;
    int currentPixelY = 100;

    private void infiniteCanvas() {
        availableChunks = new Chunk[10][10];

        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                availableChunks[i][j] = new Chunk(pixelsPerChunkLength);
            }
        }
    }

    double scale = 1;

    public void updateCurrentPixel(int paramCurrentPixelX, int paramCurrentPixelY) {
        currentPixelX += (int) ((1.0 / scale) * paramCurrentPixelX);
        currentPixelY += (int) ((1.0 / scale) * paramCurrentPixelY);
        repaint();
    }

    public void updateZoom(double paramZoom) {
        scale = paramZoom;
        repaint();
    }

    @Getter
    @Setter
    ReentrantLock lock = new ReentrantLock(true);

    @Override
    public void paint(Graphics paramGraphics) {
        try {
            lock.lock();

            if(lock.getQueueLength() > 0) { // Only updates enough to show the last image available instead of inbetween ones saving time and processor
                return;
            }

            HashMap<ChunkImageProcessor, Point> renderedChunks = new HashMap<>();

            for(int i = 0, chunkIDY = (int) Math.floor((currentPixelY) / pixelsPerChunkLength); i < (int) Math.ceil((((JPanel) super.getApplication().getUIManager().getWindow().getCanvas()).getHeight() + (currentPixelY % pixelsPerChunkLength * scale)) / (scale * pixelsPerChunkLength)); i++, chunkIDY++) {
                for(int j = 0, chunkIDX = (int) Math.floor((currentPixelX) / pixelsPerChunkLength); j < (int) Math.ceil((((JPanel) super.getApplication().getUIManager().getWindow().getCanvas()).getWidth() + (currentPixelX % pixelsPerChunkLength * scale)) / (scale * pixelsPerChunkLength)); j++, chunkIDX++) {

                    try {
                        Chunk chunk = availableChunks[chunkIDX][chunkIDY];
                        chunk.updateValues(scale);

                        ChunkImageProcessor CIP = new ChunkImageProcessor(chunk);
                        CIP.start();
                        renderedChunks.put(CIP, new Point(j, i));
                    } catch(ArrayIndexOutOfBoundsException exception) {continue;}

                }
            }

            // Render the chunks

            for(ChunkImageProcessor CIP : renderedChunks.keySet()) {
                CIP.join();
            }

            // Draw the chunks

            for(Map.Entry<ChunkImageProcessor, Point> entry : renderedChunks.entrySet()) {

                int widthIn = (int) Math.ceil((- (currentPixelX % pixelsPerChunkLength) * scale) + (entry.getValue().getX() * pixelsPerChunkLength * scale));
                int heightIn = (int) Math.ceil((- (currentPixelY % pixelsPerChunkLength) * scale) + (entry.getValue().getY() * pixelsPerChunkLength * scale));
                paramGraphics.drawImage(entry.getKey().getChunk().getLastRenderedImage(), widthIn, heightIn, null);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally{
            lock.unlock();
        }
    }

    public InfiniteCanvasPlugin(Application paramApplication) {
        super(paramApplication);

        infiniteCanvas();
    }

    @Override
    public void mouseCanvasEvent(int paramCalculatedX, int paramCalculatedY, int paramDifferenceX, int paramDifferenceY) {

        this.updateCurrentPixel(-paramDifferenceX, -paramDifferenceY);
    }
}
