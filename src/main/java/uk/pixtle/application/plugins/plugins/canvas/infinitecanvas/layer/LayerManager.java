package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.InfiniteCanvasPlugin;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.drawinglayer.DrawingLayer;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.imagelayer.ImageLayer;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchorLayout;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;
import uk.pixtle.application.ui.window.notifications.Notification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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

        if(this.getActiveLayer() != null){
            this.getActiveLayer().onLayerDisable();
        }

        this.activeLayer = paramLayer;
        this.getActiveLayer().onLayerEnable();

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

    @Getter
    @Setter
    JPopupMenu popupMenu;

    @Getter
    @Setter
    Dimension popupSize = new Dimension(450, 170);

    public void createLayerSelectorPopup() {


        JPopupMenu typeSelector = new JPopupMenu();
        typeSelector.setPreferredSize(this.getPopupSize());
        typeSelector.setLayout(new AnchorLayout());

        AnchoredComponent imageLayerAnchors = new AnchoredComponent();
        imageLayerAnchors.createAnchor(Anchor.DirectionType.Y, 20);
        imageLayerAnchors.createAnchor(AnchoredComponent.StandardY.BOTTOM);
        imageLayerAnchors.createAnchor(AnchoredComponent.StandardX.LEFT);
        imageLayerAnchors.createAnchor(Anchor.DirectionType.X, 150);

        JButton imageLayerButton = new JButton("Image");
        imageLayerButton.setOpaque(false);
        imageLayerButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, false));

        imageLayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                typeSelector.setVisible(false);
                createNewLayer(LayerType.IMAGE);
            }
        });

        typeSelector.add(imageLayerButton, imageLayerAnchors);

        AnchoredComponent drawingLayerAnchors = new AnchoredComponent();
        drawingLayerAnchors.createAnchor(Anchor.DirectionType.Y, 20);
        drawingLayerAnchors.createAnchor(AnchoredComponent.StandardY.BOTTOM);
        drawingLayerAnchors.createAnchor(Anchor.DirectionType.X, 150);
        drawingLayerAnchors.createAnchor(Anchor.DirectionType.X, 300);

        JButton drawingLayerButton = new JButton("Drawing");
        drawingLayerButton.setOpaque(false);
        drawingLayerButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, false));

        drawingLayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                typeSelector.setVisible(false);
                createNewLayer(LayerType.DRAWING);
            }
        });

        typeSelector.add(drawingLayerButton, drawingLayerAnchors);

        AnchoredComponent textLayerAnchors = new AnchoredComponent();
        textLayerAnchors.createAnchor(Anchor.DirectionType.Y, 20);
        textLayerAnchors.createAnchor(AnchoredComponent.StandardY.BOTTOM);
        textLayerAnchors.createAnchor(Anchor.DirectionType.X, 450);
        textLayerAnchors.createAnchor(Anchor.DirectionType.X, 300);

        JButton textLayerButton = new JButton("Text");
        textLayerButton.setOpaque(false);
        textLayerButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, false));

        textLayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //createNewLayer(LayerType.TEXT);
                typeSelector.setVisible(false);
            }
        });

        typeSelector.add(textLayerButton, textLayerAnchors);

        AnchoredComponent exitButtonAnchors = new AnchoredComponent();
        exitButtonAnchors.createAnchor(AnchoredComponent.StandardY.TOP);
        exitButtonAnchors.createAnchor(Anchor.DirectionType.Y, 20);
        exitButtonAnchors.createAnchor(Anchor.DirectionType.X, 450);
        exitButtonAnchors.createAnchor(Anchor.DirectionType.X, 430);

        JButton exitButton = new JButton("x");
        exitButton.setOpaque(false);
        exitButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, false));
        typeSelector.add(exitButton, exitButtonAnchors);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                typeSelector.setVisible(false);
            }
        });

        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {

                if (event instanceof MouseEvent) {
                    MouseEvent m = (MouseEvent) event;
                    if (m.getID() == MouseEvent.MOUSE_CLICKED) {
                        typeSelector.setVisible(false);
                        Toolkit.getDefaultToolkit().removeAWTEventListener(this);
                    }
                }
                if (event instanceof WindowEvent) {
                    WindowEvent we = (WindowEvent) event;
                    if (we.getID() == WindowEvent.WINDOW_DEACTIVATED || we.getID() == WindowEvent.WINDOW_STATE_CHANGED) {
                        typeSelector.setVisible(false);
                        Toolkit.getDefaultToolkit().removeAWTEventListener(this);
                    }
                }
            }

        }, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.WINDOW_EVENT_MASK);

        this.setPopupMenu(typeSelector);
    }

    public void createNewLayer() {

        if(this.getPopupMenu().isVisible()) {
            return;
        }

        JFrame window = this.getInfiniteCanvasPlugin().getApplication().getUIManager().getWindow();

        int height = window.getHeight();
        int width = window.getWidth();
        int x = (int) window.getLocationOnScreen().getX();
        int y = (int) window.getLocationOnScreen().getY();

        int locationX = x + (int) Math.floor(width / 2) - (int) Math.floor(this.getPopupSize().getWidth() / 2);
        int locationY = y + (int) Math.floor(height / 2) - (int) Math.floor(this.getPopupSize().getHeight() / 2);

        this.getPopupMenu().show(null, locationX, locationY);
    }

    public void createNewLayer(LayerType paramLayerType) {

        Layer layer = null;
        switch(paramLayerType) {
            case DRAWING:
                layer = new DrawingLayer(this);
                break;
            case IMAGE:
                layer = new ImageLayer(this);
                break;
        }

        this.setLayerNumberCounter(this.getLayerNumberCounter() + 1);
        layer.setTitle("Layer " + this.getLayerNumberCounter());

        if(!layer.loadNew()) {
            this.setLayerNumberCounter(this.getLayerNumberCounter() - 1);
            return;
        }

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
        this.createLayerSelectorPopup();
    }
}
