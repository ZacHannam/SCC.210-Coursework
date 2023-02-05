package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.InfiniteCanvasPlugin;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.drawinglayer.DrawingLayer;
import uk.pixtle.application.ui.window.notifications.Notification;

import java.util.ArrayList;

public class LayerManager {

    @Getter
    @Setter
    private InfiniteCanvasPlugin infiniteCanvasPlugin;

    @Getter
    @Setter
    private ArrayList<Layer> layers;

    public int getLayerIndex(Layer paramLayer) {
        if(this.getLayers().contains(paramLayer)) {
            return this.getLayers().indexOf(paramLayer);
        }
        // THROW ERROR
        return -1;
    }

    @Getter
    private Layer activeLayer;

    public void setActiveLayer(Layer paramLayer) {
        this.activeLayer = paramLayer;

        // REPAINT UI
    }

    /*

                METHODS

     */

    public void setReRender(boolean paramReRender) {
        for(Layer layer : this.getLayers()) {
            layer.setReRender(paramReRender);
        }
    }

    public void hideLayer(Layer paramLayer) {
        if(this.getLayers().contains(paramLayer)){
            this.getLayers().get(getLayerIndex(paramLayer)).setVisible(false);
            this.getInfiniteCanvasPlugin().repaint();
        }
    }

    public void showLayer(Layer paramLayer) {
        if(this.getLayers().contains(paramLayer)){
            this.getLayers().get(getLayerIndex(paramLayer)).setVisible(true);
            this.getInfiniteCanvasPlugin().repaint();
        }

    }

    public void toggleLayerShown(Layer paramLayer) {
        if(this.getLayers().contains(paramLayer)){
            if(this.getLayers().get(getLayerIndex(paramLayer)).isVisible()) {
                this.getLayers().get(getLayerIndex(paramLayer)).setVisible(false);
            } else {
                this.getLayers().get(getLayerIndex(paramLayer)).setVisible(true);
            }
            this.getInfiniteCanvasPlugin().repaint();
        }
    }

    public void deleteLayer(Layer paramLayer) {
        if(this.getLayers().contains(paramLayer)){
            this.getLayers().remove(this.getLayers().indexOf(paramLayer));
        }
        if(this.getLayers().contains(paramLayer)){
            this.getLayers().remove(paramLayer);
        }
        if(this.getActiveLayer() == paramLayer) {
            if(this.getLayers().size() == 0){
                this.setActiveLayer(null);
            } else {
                this.setActiveLayer(this.getLayers().get(0));
            }
        }
        this.getInfiniteCanvasPlugin().getApplication().getNotificationManager().displayNotification(Notification.ColourModes.INFO, "Layer Deleted", paramLayer.getTitle() + " has been deleted.", 15 );
        this.getInfiniteCanvasPlugin().redrawLayers();
        this.getInfiniteCanvasPlugin().repaint();
    }

    public void moveLayerDown(Layer paramLayer) {
        if(!this.getLayers().contains(paramLayer)) {
            return;
        }

        if(this.getLayers().get(this.getLayers().size() - 1) == paramLayer) {
            return;
        }

        int locationInArray = this.getLayers().indexOf(paramLayer);
        this.getLayers().add( locationInArray+ 2, paramLayer);
        this.getLayers().remove(locationInArray);

        this.getInfiniteCanvasPlugin().repaint();
    }

    public void moveLayerUp(Layer paramLayer) {
        if(!this.getLayers().contains(paramLayer)) {
            return;
        }

        if(this.getLayers().get(0) == paramLayer) {
            return;
        }

        int locationInArray = this.getLayers().indexOf(paramLayer);
        this.getLayers().remove(locationInArray);
        this.getLayers().add( locationInArray - 1, paramLayer);

        this.getInfiniteCanvasPlugin().repaint();
    }

    public void createNewLayer() {
        this.setLayerNumberCounter(this.getLayerNumberCounter() + 1);

        Layer layer = new DrawingLayer(this);

        layer.setTitle("Layer " + this.getLayerNumberCounter());

        this.getLayers().add(0, layer);

        this.setActiveLayer(layer);

        this.getInfiniteCanvasPlugin().redrawLayers();
        this.getInfiniteCanvasPlugin().repaint();
    }

    /*

                LOADING AND SAVING

     */

    public JSONObject save() throws Exception {
        JSONObject jsonObject = new JSONObject();

        ArrayList<JSONObject> layerData = new ArrayList<>();
        for (Layer layer : this.getLayers()) {
            layerData.add(layer.save());
        }

        jsonObject.put("layerData", layerData);
        jsonObject.put("activeLayer", this.getLayerIndex(this.getActiveLayer()));
        jsonObject.put("layerNumberCounter", this.getLayerNumberCounter());

        return jsonObject;
    }

    public void load(JSONObject paramSavedJSON) throws Exception {

        this.setLayers(new ArrayList<>());

        this.setLayerNumberCounter(paramSavedJSON.getInt("layerNumberCounter"));

        for(Object layerDataObj : paramSavedJSON.getJSONArray("layerData")) {
            JSONObject layerData = (JSONObject) layerDataObj;

            Layer layer = null;
            switch(LayerType.valueOf(layerData.getString("layerType"))) {
                case DRAWING:
                    layer = new DrawingLayer(this);
                    layer.load(layerData);
                    break;
                default:
                    break;
            }

            this.getLayers().add(layer);

        }

        this.setActiveLayer(this.getLayers().get(paramSavedJSON.getInt("activeLayer")));
    }

    @Getter
    @Setter
    private int layerNumberCounter;

    public LayerManager(InfiniteCanvasPlugin paramInfiniteCanvasPlugin) {
        this.setInfiniteCanvasPlugin(paramInfiniteCanvasPlugin);
        this.setLayers(new ArrayList<>());
    }
}
