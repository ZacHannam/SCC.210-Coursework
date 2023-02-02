package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.chunk.Chunk;

public class Layer {

    @Getter
    @Setter
    LayerManager layerManager;

    @Getter
    @Setter
    private int layerID;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private boolean shown;

    @Getter
    private float opacity;

    public void setOpacity(float paramOpacity) {
        this.opacity = paramOpacity;
        for(Chunk chunk : this.getLayerManager().getInfiniteCanvasPlugin().getChunkMap().values()) {
            if(chunk.getActualImages().containsKey(this.getLayerID())) {
                chunk.setRenderingChange(true);
            }
        }
    }

    public void load(JSONObject paramData) throws Exception {
        this.setTitle(paramData.getString("title"));
        this.setShown(paramData.getBoolean("shown"));
        this.setOpacity(paramData.getFloat("opacity"));
    }

    public JSONObject save() throws Exception {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", this.getTitle());
        jsonObject.put("shown", this.isShown());
        jsonObject.put("opacity", this.getOpacity());

        return jsonObject;
    }

    public Layer(LayerManager paramLayerManager, int paramLayerID) {
        this.setLayerManager(paramLayerManager);
        this.setLayerID(paramLayerID);
        this.setShown(true);
        this.setOpacity(1);
    }
}
