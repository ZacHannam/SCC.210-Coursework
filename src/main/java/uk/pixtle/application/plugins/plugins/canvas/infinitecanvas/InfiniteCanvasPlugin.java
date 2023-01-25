package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.Application;
import uk.pixtle.application.plugins.expansions.PluginDrawableExpansion;
import uk.pixtle.application.plugins.plugins.canvas.CanvasPlugin;
import uk.pixtle.application.plugins.plugins.canvas.drawing.Drawing;
import uk.pixtle.application.plugins.toolsettings.ToolSetting;
import uk.pixtle.application.plugins.toolsettings.ToolSettingEntry;
import uk.pixtle.application.plugins.toolsettings.inputdevices.InputDevice;
import uk.pixtle.application.plugins.toolsettings.inputdevices.SliderInputDevice;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class InfiniteCanvasPlugin extends CanvasPlugin implements PluginDrawableExpansion {

    // Abstract Methods

    public void printImageOnCanvas(int paramScreenX, int paramScreenY, Drawing paramDrawing) {

        for(int i = 0; i < paramDrawing.getHeight(); i++) {
            for (int j = 0; j < paramDrawing.getWidth(); j++) {
                int targetPixelX = currentPixelX + (int) Math.ceil(paramScreenX * (1 / scale)) + j - 1;
                int targetPixelY = currentPixelY + (int) Math.ceil(paramScreenY * (1 / scale)) + i - 1;

                int chunkIDX = (int) Math.floor((targetPixelX) / pixelsPerChunkLength);
                int chunkIDY = (int) Math.floor((targetPixelY) / pixelsPerChunkLength);

                try {

                    if(paramDrawing.getColor(j, i) != null) {
                        availableChunks[chunkIDX][chunkIDY].getActualImage().setRGB(targetPixelX % pixelsPerChunkLength, targetPixelY % pixelsPerChunkLength, paramDrawing.getColor(j, i).getRGB());
                        availableChunks[chunkIDX][chunkIDY].setRenderingChange(true);
                    }

                } catch(ArrayIndexOutOfBoundsException exception) {continue;};
            }
        }
        repaint();
    }

    // Tool Settings
    @ToolSetting
    private ToolSettingEntry<Integer> zoom = new ToolSettingEntry<Integer>(1){

        @Override
        public void notifyVariableChange(Integer paramVar) {
            double mappedScale = Math.pow(2, 0.07 * paramVar);
            scaleAround(mappedScale, 0, 0);
            repaint();
        }

        @Override
        public boolean validateInput(Integer paramInput) {
            return true;
        }

        @Override
        public String getTitle() {
            return "- Zoom +";
        }

        @Override
        public InputDevice getInputDevice() {
            return new SliderInputDevice(this) {

                @Override
                public int getMinValue() {
                    return -30;
                }

                @Override
                public int getMaxValue() {
                    return 60;
                }

                @Override
                public boolean paintCurrentValue() {
                    return false;
                }

                @Override
                public void renderer(JSlider paramSlider) {
                    paramSlider.setPaintLabels(false);
                }
            };
        }
    };

    // Plugin Drawable Expansion

    @Override
    public void mouseCanvasEvent(int paramCalculatedX, int paramCalculatedY, int paramDifferenceX, int paramDifferenceY) {

        this.updateCurrentPixel(-paramDifferenceX, -paramDifferenceY);
    }

    // Infinite Canvas Plugin

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

    @Getter
    @Setter
    double scale = 1;

    public void scaleAround(double paramScale, int paramX, int paramY) {
        setScale(paramScale);
    }

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
}
