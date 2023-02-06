package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import uk.pixtle.application.plugins.plugins.canvas.drawing.Drawing;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.drawinglayer.Chunk;

import java.awt.image.BufferedImage;

public abstract class Layer {

    public static final boolean DEFAULT_FLIP_X = false;
    public static final boolean DEFAULT_FLIP_Y = false;
    public static final boolean DEFAULT_INVERTCOLOURS = false;
    public static final boolean DEFAULT_BLURRED = false;
    public static final boolean DEFAULT_VISIBILITY = true;
    public static final float DEFAULT_OPACITY = (float) 1;

    /*

                ABSTRACT METHODS

     */

    public abstract LayerType getLayerType();
    public abstract JSONObject saveLayerData() throws Exception;
    public abstract void loadLayerData(JSONObject paramSavedData) throws Exception;
    public abstract LayerImageProcessor getLayerImageProcessor();
    public abstract boolean loadNew();

    /*

                LAYER MANAGER

     */

    @Getter
    @Setter
    private LayerManager layerManager;

    /*

                TITLE

     */

    @Getter
    public String title;

    public void setTitle(String paramTitle) {
        this.title = paramTitle;
        this.getLayerManager().getInfiniteCanvasPlugin().redrawLayers();
    }

    /*

                OPACITY

     */

    @Getter
    public float opacity;

    public void setOpacity(float paramOpacity) {
        if(paramOpacity < 0 || paramOpacity > 1) {
            // TO-DO exception
            return;
        }

        this.opacity = paramOpacity;
        this.getLayerManager().getInfiniteCanvasPlugin().repaint();
    }

    @Getter
    public boolean visible;

    public void setVisible(boolean paramVisible) {
        if(paramVisible == this.isVisible()) {
            return;
        }

        this.visible = paramVisible;

        this.getLayerManager().getInfiniteCanvasPlugin().repaint();
    }

    @Getter
    private boolean blurred;

    public void setBlurred(boolean paramBlurred) {
        if(paramBlurred == this.isBlurred()) {
            return;
        }
        this.blurred = paramBlurred;

        this.getLayerManager().getInfiniteCanvasPlugin().repaint();
    }

    @Getter
    private boolean invertColours;

    public void setInvertColours(boolean paramInvertColours) {
        if(paramInvertColours == this.isInvertColours()) {
            return;
        }
        this.invertColours = paramInvertColours;

        this.getLayerManager().getInfiniteCanvasPlugin().repaint();
    }

    @Getter
    private boolean flipY;

    public void setFlipY(boolean paramFlipY) {
        if(paramFlipY == this.isFlipY()) {
            return;
        }
        this.flipY = paramFlipY;

        this.getLayerManager().getInfiniteCanvasPlugin().repaint();
    }

    @Getter
    private boolean flipX;

    public void setFlipX(boolean paramFlipX) {
        if(paramFlipX == this.isFlipX()) {
            return;
        }
        this.flipX = paramFlipX;

        this.getLayerManager().getInfiniteCanvasPlugin().repaint();
    }




    /*

                LAYER IMAGE PROCESSING

     */

    @Getter
    @Setter
    private boolean reRender;

    @Getter
    @Setter
    private BufferedImage lastRenderedImage;

    /*

                OVERRIDABLE METHODS

     */

    public void onLayerEnable() {

    }

    public void onLayerDisable() {

    }

    /*

                LOADING AND SAVING

     */

    public JSONObject save() throws Exception {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("layerType", this.getLayerType().toString());
        jsonObject.put("layerData", this.saveLayerData());
        jsonObject.put("opacity", this.getOpacity());
        jsonObject.put("visible", this.isVisible());
        jsonObject.put("title", this.getTitle());
        jsonObject.put("blurred", this.isBlurred());
        jsonObject.put("invertColours", this.isInvertColours());
        jsonObject.put("flipY", this.isFlipY());
        jsonObject.put("flipX", this.isFlipX());

        return jsonObject;
    }

    public void load(JSONObject paramSavedData) throws Exception {

        this.setOpacity(paramSavedData.getFloat("opacity"));
        this.setVisible(paramSavedData.getBoolean("visible"));
        this.setTitle(paramSavedData.getString("title"));
        this.setBlurred(paramSavedData.getBoolean("blurred"));
        this.setInvertColours(paramSavedData.getBoolean("invertColours"));
        this.setFlipY(paramSavedData.getBoolean("flipY"));
        this.setFlipX(paramSavedData.getBoolean("flipX"));

        this.loadLayerData(paramSavedData.getJSONObject("layerData"));
    }

    /*

                CONSTRUCTOR

     */

    public Layer(LayerManager paramLayerManager) {
        this.setLayerManager(paramLayerManager);

        this.setReRender(true);
        this.setFlipY(this.DEFAULT_FLIP_Y);
        this.setFlipX(this.DEFAULT_FLIP_X);
        this.setInvertColours(this.DEFAULT_INVERTCOLOURS);
        this.setBlurred(this.DEFAULT_BLURRED);
        this.setVisible(this.DEFAULT_VISIBILITY);
        this.setOpacity(this.DEFAULT_OPACITY);

    }
}
