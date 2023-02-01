package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.InfiniteCanvasPlugin;
import uk.pixtle.application.ui.window.notifications.Notification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class LayerManager {

    @Getter
    @Setter
    private ArrayList<Integer> layerOrder;

    @Getter
    @Setter
    private int layerCount;

    @Getter
    @Setter
    private InfiniteCanvasPlugin infiniteCanvasPlugin;

    @Getter
    @Setter
    private HashMap<Integer, Layer> layers;

    @Getter
    private Layer activeLayer;

    public JSONObject save() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("layerOrder", this.getLayerOrder());
        jsonObject.put("layerCount", this.getLayerCount());
        jsonObject.put("activeLayer", this.getActiveLayer().getLayerID());

        JSONObject layers = new JSONObject();
        for(Layer layer : this.getLayers().values()) {
            layers.put(String.valueOf(layer.getLayerID()), layer.save());
        }

        jsonObject.put("layers", layers);

        return jsonObject;
    }

    public void load(JSONObject paramData) throws Exception {

        this.getLayers().clear();
        this.getLayerOrder().clear();

        this.setLayerCount(paramData.getInt("layerCount"));

        for (String key : paramData.getJSONObject("layers").keySet()) {
            JSONObject layerData = paramData.getJSONObject("layers").getJSONObject(key);
            Layer layer = new Layer(this, Integer.valueOf(key));
            layer.load(layerData);

            this.getLayers().put(layer.getLayerID(), layer);

            if (layer.getLayerID() == paramData.getInt("activeLayer")) {
                this.setActiveLayer(layer);
            }
        }

        for(int i = 0; i < paramData.getJSONArray("layerOrder").length(); i++) {
            this.getLayerOrder().add(paramData.getJSONArray("layerOrder").getInt(i));
        }

        this.getInfiniteCanvasPlugin().redrawLayers();
    }

    public void hideLayer(int paramLayerID) {
        if(this.getLayers().containsKey(paramLayerID)){
            this.getLayers().get(paramLayerID).setShown(false);
            this.getInfiniteCanvasPlugin().repaint();
        }
    }

    public void showLayer(int paramLayerID) {
        if(this.getLayers().containsKey(paramLayerID)){
            this.getLayers().get(paramLayerID).setShown(true);
            this.getInfiniteCanvasPlugin().repaint();
        }

    }

    public void toggleLayerShown(int paramLayerID) {
        if(this.getLayers().containsKey(paramLayerID)){
            if(this.getLayers().get(paramLayerID).isShown()) {
                this.getLayers().get(paramLayerID).setShown(false);
            } else {
                this.getLayers().get(paramLayerID).setShown(true);
            }
            this.getInfiniteCanvasPlugin().repaint();
        }
    }

    public void deleteLayer(Layer paramLayer) {
        if(this.getLayerOrder().contains(paramLayer.getLayerID())){
            this.getLayerOrder().remove(this.getLayerOrder().indexOf(paramLayer.getLayerID()));
        }
        if(this.getLayers().containsKey(paramLayer.getLayerID())){
            this.getLayers().remove(paramLayer.getLayerID());
        }
        if(this.getActiveLayer() == paramLayer) {
            if(this.getLayerOrder().size() == 0){
                this.setActiveLayer(null);
            } else {
                this.setActiveLayer(this.getLayers().get(this.getLayerOrder().get(0)));
            }
        }
        this.getInfiniteCanvasPlugin().getApplication().getNotificationManager().displayNotification(Notification.ColourModes.INFO, "Layer Deleted", paramLayer.getTitle() + " has been deleted.", 15 );
        this.getInfiniteCanvasPlugin().redrawLayers();
        this.getInfiniteCanvasPlugin().repaint();
    }

    // Move layers down the arraylist not down the canvas

    public void moveLayerDown(int paramLayerID) {
        if(!this.getLayerOrder().contains(paramLayerID)) {
            return;
        }

        if(this.getLayerOrder().get(this.getLayerOrder().size() - 1) == paramLayerID) {
            return;
        }

        int locationInArray = layerOrder.indexOf(paramLayerID);
        layerOrder.add( locationInArray+ 2, paramLayerID);
        layerOrder.remove(locationInArray);
    }

    public void moveLayerUp(int paramLayerID) {
        if(!this.getLayerOrder().contains(paramLayerID)) {
            return;
        }

        if(this.getLayerOrder().get(0) == paramLayerID) {
            return;
        }

        int locationInArray = layerOrder.indexOf(paramLayerID);
        layerOrder.remove(locationInArray);
        layerOrder.add( locationInArray - 1, paramLayerID);
    }

    public void createNewLayer() {
        this.setLayerCount(this.getLayerCount() + 1);

        int layerID = generateLayerID();
        Layer layer = new Layer(this, layerID);

        layer.setTitle("Layer " + this.getLayerCount());

        this.getLayers().put(layerID, layer);
        this.getLayerOrder().add(0, layerID);

        this.setActiveLayer(layer);

        this.getInfiniteCanvasPlugin().redrawLayers();
        this.getInfiniteCanvasPlugin().repaint();
    }

    @Getter
    @Setter
    private Random random;

    public int generateLayerID() {
        while(true) {
            int id = this.getRandom().nextInt(Integer.MAX_VALUE);
            if(!this.getLayers().containsKey(id)) {
                return id;
            }
        }
    }

    public void setActiveLayer(Layer paramLayer) {
        this.activeLayer = paramLayer;
        this.getInfiniteCanvasPlugin().redrawLayers();
    }

    public ArrayList<Integer> getVisibleLayersInRenderOrder() {
        ArrayList<Integer> visibleLayers = new ArrayList<>();
        for(Integer layerID : this.getLayerOrder()) {
            if(this.getLayers().get(layerID).isShown()) {
                visibleLayers.add(0, layerID);
            }
        }
        return visibleLayers;
    }

    public LayerManager(InfiniteCanvasPlugin paramInfiniteCanvasPlugin) {
        this.setInfiniteCanvasPlugin(paramInfiniteCanvasPlugin);
        this.setRandom(new Random());
        this.setLayers(new HashMap<>());
        this.setLayerOrder(new ArrayList<>());
        this.setLayerCount(0);
    }
}
