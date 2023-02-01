package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

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

    public void load(JSONObject paramData) throws Exception {
        this.setTitle(paramData.getString("title"));
        this.setShown(paramData.getBoolean("shown"));

    }

    public JSONObject save() throws Exception {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", this.getTitle());
        jsonObject.put("shown", this.isShown());

        return jsonObject;
    }

    public Layer(LayerManager paramLayerManager, int paramLayerID) {
        this.setLayerManager(paramLayerManager);
        this.setLayerID(paramLayerID);
        this.setShown(true);
    }
}
