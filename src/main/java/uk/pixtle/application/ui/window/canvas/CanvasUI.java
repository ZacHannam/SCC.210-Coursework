package uk.pixtle.application.ui.window.canvas;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class CanvasUI extends JPanel implements Canvas {

    /*
    -------------------- UIComponent Methods --------------------
     */

    @Override
    public AnchoredComponent getAnchors() {
        AnchoredComponent anchoredComponent = new AnchoredComponent();
        anchoredComponent.createAnchor(Anchor.DirectionType.X, 100);
        anchoredComponent.createAnchor(Anchor.DirectionType.X, -200);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, 20);
        anchoredComponent.createAnchor(AnchoredComponent.StandardY.BOTTOM);

        return anchoredComponent;
    }


    // INFINITE CANVAS THINGS

    // 10 tall 10 wide

    Chunk[][] availableChunks; // [x][y]

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
    public void paint(Graphics g) {
        super.paint(g);


        try {
            lock.lock();

            if(lock.getQueueLength() > 0) { // Only updates enough to show the last image available instead of inbetween ones saving time and processor
                return;
            }

            HashMap<ChunkImageProcessor, Point> renderedChunks = new HashMap<>();

            for(int i = 0, chunkIDY = (int) Math.floor((currentPixelY) / pixelsPerChunkLength); i < (int) Math.ceil((super.getHeight() + (currentPixelY % pixelsPerChunkLength)) / (scale * pixelsPerChunkLength)); i++, chunkIDY++) {
                for(int j = 0, chunkIDX = (int) Math.floor((currentPixelX) / pixelsPerChunkLength); j < (int) Math.ceil((super.getWidth() + (currentPixelX % pixelsPerChunkLength)) / (scale * pixelsPerChunkLength)); j++, chunkIDX++) {

                    Chunk chunk = availableChunks[chunkIDX][chunkIDY];
                    chunk.updateValues(scale);

                    ChunkImageProcessor CIP = new ChunkImageProcessor(chunk);
                    CIP.start();
                    renderedChunks.put(CIP, new Point(j, i));

                }
            }

            // Render the chunks

            for(ChunkImageProcessor CIP : renderedChunks.keySet()) {
                CIP.join();
            }

            // Draw the chunks

            for(Map.Entry<ChunkImageProcessor, Point> entry : renderedChunks.entrySet()) {

                int widthIn = (int) Math.floor((- (currentPixelX % pixelsPerChunkLength) * scale) + (entry.getValue().getX() * pixelsPerChunkLength * scale));
                int heightIn = (int) Math.floor((- (currentPixelY % pixelsPerChunkLength) * scale) + (entry.getValue().getY() * pixelsPerChunkLength * scale));
                g.drawImage(entry.getKey().getChunk().getLastRenderedImage(), widthIn, heightIn, null);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally{
            lock.unlock();
        }
    }

    /*
    -------------------- Constructor --------------------
     */

    public CanvasUI() {
        super(true); // double buffered;
        super.addMouseListener(new CanvasListener(this));

        infiniteCanvas();
    }
}
