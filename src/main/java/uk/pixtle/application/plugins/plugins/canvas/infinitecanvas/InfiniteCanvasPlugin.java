package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import uk.pixtle.application.Application;
import uk.pixtle.application.plugins.expansions.PluginDrawableExpansion;
import uk.pixtle.application.plugins.plugins.canvas.CanvasPlugin;
import uk.pixtle.application.plugins.plugins.canvas.drawing.Drawing;
import uk.pixtle.application.plugins.policies.PluginSavePolicy;
import uk.pixtle.application.plugins.toolsettings.ToolSetting;
import uk.pixtle.application.plugins.toolsettings.ToolSettingEntry;
import uk.pixtle.application.plugins.toolsettings.inputdevices.InputDevice;
import uk.pixtle.application.plugins.toolsettings.inputdevices.SliderInputDevice;
import uk.pixtle.application.ui.window.canvas.CanvasUI;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class InfiniteCanvasPlugin extends CanvasPlugin implements PluginDrawableExpansion, PluginSavePolicy {

    /*
                TOOL SETTINGS
     */

    // Tool Settings
    @ToolSetting
    private ToolSettingEntry<Integer> zoomToolSetting = new ToolSettingEntry<Integer>(1) {

        @Override
        public void notifyVariableChange(Integer paramVar) {
            double mappedScale = Math.pow(2, 0.07 * paramVar);
            zoomAround(mappedScale, 0, 0);
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

    /*
                CHUNKS AND CHUNK MAP
     */

    @Getter
    @Setter
    private HashMap<String, Chunk> chunkMap;

    private String convertChunkCoordinateToString(long x, long y) {
        return String.valueOf(x) + ":" + String.valueOf(y);
    }

    public boolean isChunkAt(long x, long y) {
        return this.getChunkMap().containsKey(this.convertChunkCoordinateToString(x, y));
    }

    public Chunk getChunkAt(long x, long y) {
        return this.getChunkMap().get(this.convertChunkCoordinateToString(x, y));
    }

    private Chunk createNewChunkAt(long x, long y) {
        if (this.isChunkAt(x, y)) {
            return this.getChunkAt(x, y);
        }

        //Chunk chunk = new Chunk(this.getPixelsPerChunk(), this.getBackgroundColor());
        Chunk chunk = new Chunk(this.getPixelsPerChunk(), this.getBackgroundColor());
        this.getChunkMap().put(this.convertChunkCoordinateToString(x, y), chunk);

        return chunk;
    }

    /*
                PIXELS AND PAINTING
     */

    @Getter
    @Setter
    private long currentPixelX, currentPixelY;

    @Getter
    @Setter
    private double zoom;

    @Getter
    @Setter
    private int pixelsPerChunk;

    @Getter
    @Setter
    private ReentrantLock lock;

    @Getter
    @Setter
    private Color backgroundColor;

    public void zoomAround(double paramScale, int paramX, int paramY) {
        this.setZoom(paramScale);
    }

    public void updateCurrentPixel(int paramCurrentPixelX, int paramCurrentPixelY) {
        currentPixelX += (int) ((1.0 / this.getZoom()) * paramCurrentPixelX);
        currentPixelY += (int) ((1.0 / this.getZoom()) * paramCurrentPixelY);
        repaint();
    }

    @Override
    public void paint(Graphics paramGraphics) {
        try {
            this.getLock().lock();

            if (this.getLock().getQueueLength() > 0) { // Only updates enough to show the last image available instead of inbetween ones saving time and processor
                return;
            }

            HashMap<ChunkImageProcessor, Point> renderedChunks = new HashMap<>();

            for (long i = 0, chunkIDY = (long) Math.floor((this.getCurrentPixelY()) / this.getPixelsPerChunk()); i < (int) Math.ceil((((JPanel) super.getApplication().getUIManager().getWindow().getCanvas()).getHeight() + (this.getCurrentPixelY() % this.getPixelsPerChunk() * this.getZoom())) / (this.getZoom() * this.getPixelsPerChunk())); i++, chunkIDY++) {
                for (long j = 0, chunkIDX = (long) Math.floor((this.getCurrentPixelX()) / this.getPixelsPerChunk()); j < (int) Math.ceil((((JPanel) super.getApplication().getUIManager().getWindow().getCanvas()).getWidth() + (this.getCurrentPixelX() % this.getPixelsPerChunk() * this.getZoom())) / (this.getZoom() * this.getPixelsPerChunk())); j++, chunkIDX++) {

                    if (chunkIDY < 0 || chunkIDY > Long.MAX_VALUE / this.getPixelsPerChunk() || chunkIDX < 0 || chunkIDX > Long.MAX_VALUE / this.getPixelsPerChunk())
                        continue;

                    if (!this.isChunkAt(chunkIDX, chunkIDY)) {
                        continue;
                    }

                    Chunk chunk = this.getChunkAt(chunkIDX, chunkIDY);
                    chunk.updateValues(this.getZoom());


                    ChunkImageProcessor CIP = new ChunkImageProcessor(chunk);
                    CIP.start();
                    renderedChunks.put(CIP, new Point((int) j, (int) i));

                }
            }

            // Render the chunks

            for (ChunkImageProcessor CIP : renderedChunks.keySet()) {
                CIP.join();
            }

            // Draw the chunks

            for (Map.Entry<ChunkImageProcessor, Point> entry : renderedChunks.entrySet()) {

                int widthIn = (int) Math.ceil((-(this.getCurrentPixelX() % this.getPixelsPerChunk()) * this.getZoom()) + (entry.getValue().getX() * this.getPixelsPerChunk() * this.getZoom()));
                int heightIn = (int) Math.ceil((-(this.getCurrentPixelY() % this.getPixelsPerChunk()) * this.getZoom()) + (entry.getValue().getY() * this.getPixelsPerChunk() * this.getZoom()));

                paramGraphics.drawImage(entry.getKey().getChunk().getLastRenderedImage(), widthIn, heightIn, null);
            }

        } catch (InterruptedException e) {
            // TO-DO error
        } finally {
            this.getLock().unlock();
        }
    }

    @Override
    public void mouseCanvasEvent(int paramCalculatedX, int paramCalculatedY, int paramDifferenceX, int paramDifferenceY) {

        this.updateCurrentPixel(-paramDifferenceX, -paramDifferenceY);
    }

    /**
     * Get the colour of an individual pixel on screen
     * @param paramScreenX
     * @param paramScreenY
     */
    public Color getPixelColour(int paramScreenX, int paramScreenY) {
        long targetPixelX = currentPixelX + (int) Math.ceil(paramScreenX * (1 / this.getZoom())) - 1;
        long targetPixelY = currentPixelY + (int) Math.ceil(paramScreenY * (1 / this.getZoom())) - 1;

        long chunkIDX = (long) Math.floor((targetPixelX) / this.getPixelsPerChunk());
        long chunkIDY = (long) Math.floor((targetPixelY) / this.getPixelsPerChunk());

        if (!this.isChunkAt(chunkIDX, chunkIDY)) {
            this.createNewChunkAt(chunkIDX, chunkIDY);
        }

        Chunk chunk = this.getChunkAt(chunkIDX, chunkIDY);

        return new Color(chunk.getActualImage().getRGB((int) (targetPixelX % this.getPixelsPerChunk()), (int) (targetPixelY % this.getPixelsPerChunk())));
    }

    /**
     * Set the colour of an individual pixel on the screen
     * @param paramScreenX
     * @param paramScreenY
     * @param paramColour - use null for removed colour
     */
    public void setPixelColour(int paramScreenX, int paramScreenY, Color paramColour) {
        long targetPixelX = currentPixelX + (int) Math.ceil(paramScreenX * (1 / this.getZoom())) - 1;
        long targetPixelY = currentPixelY + (int) Math.ceil(paramScreenY * (1 / this.getZoom())) - 1;

        long chunkIDX = (long) Math.floor((targetPixelX) / this.getPixelsPerChunk());
        long chunkIDY = (long) Math.floor((targetPixelY) / this.getPixelsPerChunk());

        if (!this.isChunkAt(chunkIDX, chunkIDY)) {
            this.createNewChunkAt(chunkIDX, chunkIDY);
        }

        Chunk chunk = this.getChunkAt(chunkIDX, chunkIDY);

        if(paramColour == null) {
            chunk.getActualImage().setRGB((int) (targetPixelX % this.getPixelsPerChunk()), (int) (targetPixelY % this.getPixelsPerChunk()), this.getBackgroundColor().getRGB());
        } else {
            chunk.getActualImage().setRGB((int) (targetPixelX % this.getPixelsPerChunk()), (int) (targetPixelY % this.getPixelsPerChunk()), paramColour.getRGB());
        }
        chunk.setRenderingChange(true);

        repaint();
    }


    @Override
    public void printImageOnCanvas(int paramScreenX, int paramScreenY, Drawing paramDrawing, boolean paramCenter) {

        long targetPixelX = currentPixelX + (int) Math.ceil(paramScreenX * (1 / this.getZoom())) - 1;
        long targetPixelY = currentPixelY + (int) Math.ceil(paramScreenY * (1 / this.getZoom())) - 1;

        if(paramCenter) {
            targetPixelX -= (paramDrawing.getWidth() / 2);
            targetPixelY -= (paramDrawing.getHeight() / 2);
        }

        long lastChunkIDX = -1;
        long lastChunkIDY = -1;
        Chunk lastChunk = null;

        for(int i = 0; i < paramDrawing.getHeight(); i++) {
            for (int j = 0; j < paramDrawing.getWidth(); j++) {
                long pixelX = targetPixelX + j ;
                long pixelY = targetPixelY + i;

                long chunkIDX = (long) Math.floor((pixelX) / this.getPixelsPerChunk());
                long chunkIDY = (long) Math.floor((pixelY) / this.getPixelsPerChunk());

                try {

                    if(paramDrawing.getColor(j, i) != null) {

                        Chunk chunk = null;

                        if(lastChunkIDX == chunkIDX && lastChunkIDY == chunkIDY && lastChunk != null) {
                            chunk = lastChunk;
                        } else {
                            if (!this.isChunkAt(chunkIDX, chunkIDY)) {
                                this.createNewChunkAt(chunkIDX, chunkIDY);
                            }

                            chunk = this.getChunkAt(chunkIDX, chunkIDY);

                            lastChunkIDX = chunkIDX;
                            lastChunkIDY = chunkIDY;
                            lastChunk = chunk;
                        }

                        chunk.getActualImage().setRGB((int) (pixelX % this.getPixelsPerChunk()), (int) (pixelY % this.getPixelsPerChunk()), paramDrawing.getColor(j, i).getRGB());
                        chunk.setRenderingChange(true);
                    }

                } catch(ArrayIndexOutOfBoundsException exception) {continue;};
            }
        }
        repaint();
    }

    /*
                FILE LOADING AND SAVING
     */

    @Override
    public JSONObject save() throws Exception {
        try {
            System.out.println(this.getChunkMap().size());
            JSONObject saveData = new JSONObject();

            JSONObject chunkData = new JSONObject();
            for (Map.Entry<String, Chunk> entry : this.getChunkMap().entrySet()) {
                chunkData.put(entry.getKey(), entry.getValue().getSaveData());
            }
            saveData.put("chunkData", chunkData);
            saveData.put("currentX", this.getCurrentPixelX());
            saveData.put("currentY", this.getCurrentPixelY());
            saveData.put("backgroundColour", this.getBackgroundColor());
            saveData.put("scale", this.getZoom());
            saveData.put("pixelsPerChunk", this.getPixelsPerChunk());

            return saveData;
        } catch(Exception e) {
            throw e;
        }
    }

    private Color decodeColour(String paramColour) {

        String cut = paramColour.substring(15, paramColour.length() - 1);
        String[] parts = cut.split(",");

        int r = Integer.valueOf(parts[0].split("=")[1]), g = Integer.valueOf(parts[1].split("=")[1]), b = Integer.valueOf(parts[2].split("=")[1]);
        Color color = new Color(r, g, b);

        return color;
    }

    @Override
    public void load(JSONObject paramSavedJSON) {

        try {
            this.getChunkMap().clear();
            this.setCurrentPixelX(paramSavedJSON.getLong("currentX"));
            this.setCurrentPixelY(paramSavedJSON.getLong("currentY"));
            this.setZoom(paramSavedJSON.getDouble("scale"));
            this.setPixelsPerChunk(paramSavedJSON.getInt("pixelsPerChunk"));
            this.setBackgroundColor(this.decodeColour(paramSavedJSON.getString("backgroundColour")));

            for(String key : paramSavedJSON.getJSONObject("chunkData").keySet()) {
                String data = paramSavedJSON.getJSONObject("chunkData").getString(key);

                Chunk chunk = new Chunk(this.getPixelsPerChunk(), this.getBackgroundColor());
                chunk.load(data);
                this.getChunkMap().put(key, chunk);
            }

            repaint();

        }catch(Exception e) {
            e.printStackTrace();
            throw e;
        }



    }

    /*
                CONSTRUCTOR
     */

    public InfiniteCanvasPlugin(Application paramApplication) {
        super(paramApplication);
        this.setChunkMap(new HashMap<>());
        this.setLock(new ReentrantLock());
        this.setZoom(1);
        this.setPixelsPerChunk(256);
        this.setCurrentPixelX(Long.MAX_VALUE / this.getPixelsPerChunk() / 2);
        this.setCurrentPixelY(Long.MAX_VALUE / this.getPixelsPerChunk() / 2);

        System.out.println(Long.MAX_VALUE / this.getPixelsPerChunk());

        this.setBackgroundColor(Color.white);
        ((CanvasUI) super.getApplication().getUIManager().getWindow().getCanvas()).setBackground(this.getBackgroundColor());

        repaint();
    }
}