package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.drawinglayer;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import uk.pixtle.application.plugins.plugins.canvas.drawing.ColorAndAlpha;
import uk.pixtle.application.plugins.plugins.canvas.drawing.Drawing;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.Layer;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.LayerImageProcessor;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.LayerManager;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.LayerType;
import uk.pixtle.application.ui.window.canvas.CanvasUI;

import java.util.HashMap;
import java.util.Map;

public class DrawingLayer extends Layer {

    public static final int DEFAULT_PIXELS_PER_CHUNK = 256;

    /*

                ABSTRACT METHODS

     */

    @Override
    public LayerType getLayerType() {
        return LayerType.DRAWING;
    }

    @Override
    public LayerImageProcessor getLayerImageProcessor() {
        return new DrawingLayerImageProcessor(this);
    }

    public void printImageOnCanvas(int paramScreenX, int paramScreenY, Drawing paramDrawing, boolean paramCenter) {

        int currentPositionY = this.getLayerManager().getInfiniteCanvasPlugin().getCurrentPixelY();
        int currentPositionX = this.getLayerManager().getInfiniteCanvasPlugin().getCurrentPixelX();
        double zoom = this.getLayerManager().getInfiniteCanvasPlugin().getZoom();

        int targetPixelX = currentPositionX + (int) Math.ceil(paramScreenX * (1 / zoom)) - 1;
        int targetPixelY = currentPositionY + (int) Math.ceil(paramScreenY * (1 / zoom)) - 1;

        if(paramCenter) {
            targetPixelX -= (paramDrawing.getWidth() / 2);
            targetPixelY -= (paramDrawing.getHeight() / 2);
        }

        int lastChunkIDX = -1;
        int lastChunkIDY = -1;
        Chunk lastChunk = null;

        for(int i = 0; i < paramDrawing.getHeight(); i++) {
            for (int j = 0; j < paramDrawing.getWidth(); j++) {
                int pixelX = targetPixelX + j;
                int pixelY = targetPixelY + i;

                int chunkIDX = pixelX / 256;
                int chunkIDY = pixelY / 256;

                try {

                    if (paramDrawing.getColor(j, i) != null) {

                        Chunk chunk = null;

                        if (lastChunkIDX == chunkIDX && lastChunkIDY == chunkIDY && lastChunk != null) {
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

                        ColorAndAlpha colorAndAlpha = paramDrawing.getColor(j, i);
                        chunk.setRGB((int) (pixelX % this.getPixelsPerChunk()), (int) (pixelY % this.getPixelsPerChunk()), colorAndAlpha.getColor().getRGB(), colorAndAlpha.getAlpha());
                    }

                } catch (ArrayIndexOutOfBoundsException exception) {
                    continue;
                }
            }
        }
    }

    public boolean loadNew() {
        return true;
    }

    /*

                SAVING AND LOADING

     */

    public JSONObject saveLayerData() throws Exception {
        JSONObject jsonObject = new JSONObject();

        JSONObject chunkData = new JSONObject();
        for (Map.Entry<String, Chunk> entry : this.getChunkMap().entrySet()) {
            chunkData.put(entry.getKey(), entry.getValue().save());
        }

        jsonObject.put("chunkData", chunkData);
        jsonObject.put("pixelsPerChunk", this.getPixelsPerChunk());

        return jsonObject;
    }

    public void loadLayerData(JSONObject paramSavedData) throws Exception {
        this.setPixelsPerChunk(paramSavedData.getInt("pixelsPerChunk"));

        for(String key : paramSavedData.getJSONObject("chunkData").keySet()) {
            JSONObject data = paramSavedData.getJSONObject("chunkData").getJSONObject(key);

            Chunk chunk = new Chunk(this, this.getPixelsPerChunk());
            chunk.load(data);
            this.getChunkMap().put(key, chunk);
        }
    }

    /*

                CHUNKS

     */

    @Override
    public void setReRender(boolean paramReRender) {
        super.setReRender(paramReRender);

        if(this.getChunkMap() != null) {
            for (Chunk chunk : this.getChunkMap().values()) {
                chunk.setReRender(true);
            }
        }
    }

    @Getter
    @Setter
    public HashMap<String, Chunk> chunkMap;

    private String convertChunkCoordinateToString(int x, int y) {
        return String.valueOf(x) + ":" + String.valueOf(y);
    }

    public boolean isChunkAt(int x, int y) {
        return this.getChunkMap().containsKey(this.convertChunkCoordinateToString(x, y));
    }

    public Chunk getChunkAt(int x, int y) {
        return this.getChunkMap().get(this.convertChunkCoordinateToString(x, y));
    }

    private Chunk createNewChunkAt(int x, int y) {
        if (this.isChunkAt(x, y)) {
            return this.getChunkAt(x, y);
        }

        Chunk chunk = new Chunk(this, this.getPixelsPerChunk());
        chunk.createActualImage(chunk.getSize());

        this.getChunkMap().put(this.convertChunkCoordinateToString(x, y), chunk);

        return chunk;
    }

    /*

                PIXELS PER CHUNK

     */

    @Getter
    @Setter
    private int pixelsPerChunk;

    /*

                CONSTRUCTOR

     */

    public DrawingLayer(LayerManager paramLayerManager) {
        super(paramLayerManager);

        this.setChunkMap(new HashMap<>());
        this.setPixelsPerChunk(this.DEFAULT_PIXELS_PER_CHUNK);
    }
}
