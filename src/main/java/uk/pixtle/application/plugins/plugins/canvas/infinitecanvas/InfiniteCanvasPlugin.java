package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import org.w3c.dom.Text;
import uk.pixtle.application.Application;
import uk.pixtle.application.plugins.annotations.MenuBarItem;
import uk.pixtle.application.plugins.expansions.PluginDrawableExpansion;
import uk.pixtle.application.plugins.expansions.PluginMiniToolExpansion;
import uk.pixtle.application.plugins.plugins.Plugin;
import uk.pixtle.application.plugins.plugins.canvas.CanvasPlugin;
import uk.pixtle.application.plugins.plugins.canvas.drawing.Drawing;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.Layer;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.drawinglayer.Chunk;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.imagelayer.ImageLayer;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.imageprocessors.LayerImageProcessor;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.LayerManager;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.LayerType;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.drawinglayer.DrawingLayer;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.textlayer.TextLayer;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.ui.BackgroundLayerUI;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.ui.LayerUIDrawer;
import uk.pixtle.application.plugins.plugins.tools.keylistenerplugin.KeyListener;
import uk.pixtle.application.plugins.plugins.tools.keylistenerplugin.PluginKeyListenerPolicy;
import uk.pixtle.application.plugins.policies.PluginSavePolicy;
import uk.pixtle.application.plugins.toolsettings.ToolSetting;
import uk.pixtle.application.plugins.toolsettings.ToolSettingEntry;
import uk.pixtle.application.plugins.toolsettings.inputdevices.InputDevice;
import uk.pixtle.application.plugins.toolsettings.inputdevices.SliderInputDevice;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;
import uk.pixtle.application.ui.window.canvas.Canvas;
import uk.pixtle.application.ui.window.canvas.CanvasUI;
import uk.pixtle.application.ui.window.minitoollist.MiniToolPanel;
import uk.pixtle.application.ui.window.notifications.Notification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class InfiniteCanvasPlugin extends CanvasPlugin implements PluginDrawableExpansion, PluginMiniToolExpansion, PluginSavePolicy, PluginKeyListenerPolicy {

    /*

                TOOL SETTINGS

     */

    @Getter
    @Setter
    private JSlider zoomToolSlider;

    @KeyListener(KEY = KeyEvent.VK_H, MODIFIERS = 0)
    public void hand() {super.getApplication().getPluginManager().activatePlugin(this);}
    @ToolSetting
    private ToolSettingEntry<Integer> zoomToolSetting = new ToolSettingEntry<Integer>(1) {

        @Override
        public void notifyVariableChange(Integer paramVar) {
            zoomAround(mapZoom(paramVar), 0, 0);
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
                    setZoomToolSlider(paramSlider);
                }
            };
        }
    };

    /*

                ONENABLE / DISABLE

     */

    @Override
    public void onEnable(){
        this.repaint(true);
    }

    @Override
    public void onDisable() {
        if(this.isFullScreenMode()) this.setFullScreenMode(false);
        this.repaint(true);
    }

    /*

                ZOOM / SCALE SETTINGS

     */

    public void moveCenterToPixel(Point paramPoint) {
        CanvasUI canvasUI = ((CanvasUI) super.getApplication().getUIManager().getWindow().getCanvas());
        int width = canvasUI.getWidth();
        int height = canvasUI.getHeight();

        int pixelsAcross = (int) Math.round(width / this.getZoom());
        int pixelsDown = (int) Math.round(height / this.getZoom());

        this.setCurrentPixelX((int) paramPoint.getX() - (pixelsAcross / 2));
        this.setCurrentPixelY((int) paramPoint.getY() - (pixelsDown / 2));
    }

    public void updateCurrentPixel(int paramCurrentPixelX, int paramCurrentPixelY) {
        if(paramCurrentPixelY == 0 && paramCurrentPixelX == 0) return;

        currentPixelX += (int) Math.ceil(((1.0 / this.getZoom()) * paramCurrentPixelX));
        currentPixelY += (int) Math.ceil(((1.0 / this.getZoom()) * paramCurrentPixelY));

        this.repaint(true);
    }

    public double mapZoom(int paramZoom) {
        return Math.pow(2, 0.07 * paramZoom);
    }

    public int inverseMapZoom(double paramMappedZoom) {
        return (int) Math.round(57.7457 * Math.log(paramMappedZoom) / Math.log(2));
    }

    @Getter
    @Setter
    public double zoom;

    public void zoomAround(double paramScale, int paramX, int paramY) {

        if(paramScale == this.getZoom()) return;

        CanvasUI canvasUI = ((CanvasUI) super.getApplication().getUIManager().getWindow().getCanvas());

        double pixelsBeforeX = canvasUI.getWidth() * (1/this.getZoom());
        double pixelsAfterX = canvasUI.getWidth() * (1/paramScale);

        double pixelsBeforeY = canvasUI.getHeight() * (1/this.getZoom());
        double pixelsAfterY = canvasUI.getHeight() * (1/paramScale);

        if(this.getZoom() == 0) {
            pixelsBeforeX = pixelsAfterX;
            pixelsBeforeY = pixelsAfterY;
        }

        int differenceX = (int) Math.floor((pixelsBeforeX - pixelsAfterX) / 2);
        int differenceY = (int) Math.floor((pixelsBeforeY - pixelsAfterY) / 2);

        this.setCurrentPixelX(this.getCurrentPixelX() + differenceX);
        this.setCurrentPixelY(this.getCurrentPixelY() + differenceY);

        this.setZoom(paramScale);
        this.repaint(true);
    }


    /*

                METHODS AND VARIABLES

     */

    @Getter
    @Setter
    private LayerManager layerManager;

    @Getter
    @Setter
    private ReentrantLock canvasLock;

    @Getter
    @Setter
    private int currentPixelX, currentPixelY;

    @Getter
    private Color backgroundColor;

    public void setBackgroundColor(Color paramBackgroundColor) {
        this.backgroundColor = paramBackgroundColor;

        if(this.getBackgroundLayerUI() != null) {
            this.getBackgroundLayerUI().updateBackgroundColourPreview(this.getBackgroundColor());
        }

        this.repaint(true);
    }


    private void loadDefaultVariables() {
        this.setLayerManager(new LayerManager(this));
        this.setCanvasLock(new ReentrantLock());
        this.setCurrentPixelX(Integer.MAX_VALUE / 2);
        this.setCurrentPixelY(Integer.MAX_VALUE / 2);
        this.setFullScreenMode(false);
    }

    public Point getCenterPixel() {

        int screenWidth = ((CanvasUI) super.getApplication().getUIManager().getWindow().getCanvas()).getWidth();
        int screenHeight = ((CanvasUI) super.getApplication().getUIManager().getWindow().getCanvas()).getHeight();

        return new Point((int) Math.floor(screenWidth/2) + this.getCurrentPixelX(), (int) Math.floor(screenHeight/2) + this.getCurrentPixelY());

    }

    /*

                PLUGIN DRAWABLE EXPANSION

     */

    @Override
    public void mouseCanvasEvent(int paramCalculatedX, int paramCalculatedY, int paramDifferenceX, int paramDifferenceY) {

        if(this.isFullScreenMode()) {
            this.setFullScreenMode(false);
            Point clickedPoint = this.translateScreenPixelInFullScreenMode(paramCalculatedX, paramCalculatedY);
            if(!clickedPoint.equals(new Point(-1, -1))) {
                this.moveCenterToPixel(clickedPoint);
            }
            return;
        }

        if(this.getLayerManager().getActiveLayer() == null) {
            updateCurrentPixel(-paramDifferenceX, -paramDifferenceY);
            return;
        }
        this.getLayerManager().getActiveLayer().mouseCanvasEvent(paramCalculatedX, paramCalculatedY, paramDifferenceX, paramDifferenceY);
    }

    /*

                PLUGIN MINI TOOL EXPANSION

     */

    @Getter
    @Setter
    private MiniToolPanel miniToolPanel;

    @Getter
    @Setter
    private LayerUIDrawer layerUIDrawer;

    @Getter
    @Setter
    BackgroundLayerUI backgroundLayerUI;

    @Override
    public int getMiniToolPanelHeight() {
        return 85;
    }

    @Override
    public void instanceMiniToolPanel(MiniToolPanel paramMiniToolPanel) {
        this.setMiniToolPanel(paramMiniToolPanel);

        /*
        Top Panel
         */

        AnchoredComponent addLayerButtonAnchors = new AnchoredComponent();
        addLayerButtonAnchors.createAnchor(Anchor.DirectionType.X, -35);
        addLayerButtonAnchors.createAnchor(AnchoredComponent.StandardX.RIGHT);
        addLayerButtonAnchors.createAnchor(Anchor.DirectionType.Y, 0);
        addLayerButtonAnchors.createAnchor(Anchor.DirectionType.Y, 35);

        JButton addLayerButton = new JButton();
        addLayerButton.setText("+");
        addLayerButton.setOpaque(false);
        addLayerButton.setBorder(BorderFactory.createEmptyBorder());

        addLayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getLayerManager().createNewLayer();
            }
        });

        paramMiniToolPanel.add(addLayerButton, addLayerButtonAnchors);

        AnchoredComponent textAnchors = new AnchoredComponent();
        textAnchors.createAnchor(Anchor.DirectionType.X, -35);
        textAnchors.createAnchor(Anchor.DirectionType.X, 5);
        textAnchors.createAnchor(Anchor.DirectionType.Y, 0);
        textAnchors.createAnchor(Anchor.DirectionType.Y, 35);

        JLabel textLabel = new JLabel();
        textLabel.setText("Layers");
        paramMiniToolPanel.add(textLabel, textAnchors);

        /*
        Layers
         */

        AnchoredComponent layersAnchor = new AnchoredComponent();
        layersAnchor.createAnchor(AnchoredComponent.StandardX.LEFT);
        layersAnchor.createAnchor(AnchoredComponent.StandardX.RIGHT);
        layersAnchor.createAnchor(Anchor.DirectionType.Y, 35);
        layersAnchor.createAnchor(Anchor.DirectionType.Y, -50);

        LayerUIDrawer layerUIDrawer = new LayerUIDrawer(this.getLayerManager());
        paramMiniToolPanel.add(layerUIDrawer, layersAnchor);
        this.setLayerUIDrawer(layerUIDrawer);


        /*
        Background Layer
         */

        AnchoredComponent backgroundAnchors = new AnchoredComponent();
        backgroundAnchors.createAnchor(AnchoredComponent.StandardX.LEFT);
        backgroundAnchors.createAnchor(AnchoredComponent.StandardX.RIGHT);
        backgroundAnchors.createAnchor(Anchor.DirectionType.Y, -50);
        backgroundAnchors.createAnchor(AnchoredComponent.StandardY.BOTTOM);

        BackgroundLayerUI background = new BackgroundLayerUI(this);
        background.updateBackgroundColourPreview(this.getBackgroundColor());
        paramMiniToolPanel.add(background, backgroundAnchors);

        this.setBackgroundLayerUI(background);

    }

    public void redrawLayers() {

        this.getMiniToolPanel().updateHeight(this.getLayerManager().getLayers().size() * 50 + 85);
        this.getLayerUIDrawer().repaint();
        this.getLayerUIDrawer().revalidate();

        this.getMiniToolPanel().revalidate();
        this.getMiniToolPanel().repaint();
    }

    /*

                PLUGIN SAVE POLICY

     */

    public static Color decodeColour(String paramColour) {

        String cut = paramColour.substring(15, paramColour.length() - 1);
        String[] parts = cut.split(",");

        int r = Integer.valueOf(parts[0].split("=")[1]), g = Integer.valueOf(parts[1].split("=")[1]), b = Integer.valueOf(parts[2].split("=")[1]);
        Color color = new Color(r, g, b);

        return color;
    }

    @Override
    public JSONObject save() throws Exception {

        JSONObject saveData = new JSONObject();

        saveData.put("layerManager", this.getLayerManager().save());
        saveData.put("currentX", this.getCurrentPixelX());
        saveData.put("currentY", this.getCurrentPixelY());
        saveData.put("backgroundColour", this.getBackgroundColor());
        saveData.put("zoom", this.getZoom());


        return saveData;
    }

    @Override
    public void load(JSONObject paramSavedJSON) throws Exception {

        this.setCurrentPixelX(paramSavedJSON.getInt("currentX"));
        this.setCurrentPixelY(paramSavedJSON.getInt("currentY"));
        this.setBackgroundColor(this.decodeColour(paramSavedJSON.getString("backgroundColour")));
        this.setZoom(paramSavedJSON.getInt("zoom"));

        this.getLayerManager().load(paramSavedJSON.getJSONObject("layerManager"));
        this.getZoomToolSlider().setValue(this.inverseMapZoom(this.getZoom()));

        this.repaint(false);
        this.redrawLayers();
    }

     /*

    ABSTRACT METHODS

     */

    public Point translateScreenPixel(int x, int y) {

        int cX = (int) Math.floor(this.getCurrentPixelX() + ((1 / this.getZoom()) * x));
        int cY = (int) Math.floor(this.getCurrentPixelY() + ((1 / this.getZoom()) * y));

        return new Point(cX, cY);
    }

    @Override
    public void printImageOnCanvas(int paramScreenX, int paramScreenY, Drawing paramDrawing, boolean paramCenter) {

        if(this.isFullScreenMode()) {
            return;
        }

        if(this.getLayerManager().getActiveLayer() == null) {
            super.getApplication().getNotificationManager().displayNotification(Notification.ColourModes.ERROR, "No Layer Found", "There is currently no active layer, please create or select one!", 10, false);
            return;
        }

        if(this.getLayerManager().getActiveLayer().getLayerType() != LayerType.DRAWING) {
            super.getApplication().getNotificationManager().displayNotification(Notification.ColourModes.ERROR, "Cannot draw on this layer", "You cannot draw on this type of layer. Create a drawing layer!", 10, false);
            return;
        }

        if(!this.getLayerManager().getActiveLayer().isVisible()) {
            super.getApplication().getNotificationManager().displayNotification(Notification.ColourModes.INFO, "Layer is hidden!", "The layer you're currently editing is hidden!", 3, false);
            return;
        }

        ((DrawingLayer) this.getLayerManager().getActiveLayer()).printImageOnCanvas(paramScreenX, paramScreenY, paramDrawing, paramCenter);
        this.repaint(false);
    }

    @Getter
    @Setter
    private BufferedImage lastRenderedImage;

    /**
     * Get the colour of an individual pixel on screen
     * @param paramScreenX
     * @param paramScreenY
     */
    public Color getPixelColour(int paramScreenX, int paramScreenY) {
        return new Color(this.getLastRenderedImage().getRGB(paramScreenX, paramScreenY));
    }

    /*

                CONSTRUCTOR

     */

    public void repaint(boolean paramReRender) {
        if(paramReRender) {
            this.getLayerManager().setReRender(true);
        }
        super.repaint();
    }

    @Override
    public void onLoadingFinish() {
        this.setBackgroundColor(Color.white);
        this.getLayerManager().createNewLayer(LayerType.DRAWING);
    }

    public InfiniteCanvasPlugin(Application paramApplication) {
        super(paramApplication);

        this.loadDefaultVariables();
        zoomToolSetting.notifyVariableChange(this.inverseMapZoom(this.getZoom()));
    }

    @Override
    public void reset() {
        this.setLayerManager(new LayerManager(this));
        this.getLayerUIDrawer().setLayerManager(this.getLayerManager());
        this.onLoadingFinish();

        this.repaint(true);
    }

    @Getter
    @Setter
    private double fullScreenModeScale;

    @Getter
    @Setter
    private Point fullScreenModeTopLeftPixel;

    public Point translateScreenPixelInFullScreenMode(int x, int y) {

        if(this.getFullScreenModeTopLeftPixel() == null) return new Point(-1, -1);

        int cX = (int) Math.floor(this.getFullScreenModeTopLeftPixel().getX() + ((1 / this.getFullScreenModeScale()) * x));
        int cY = (int) Math.floor(this.getFullScreenModeTopLeftPixel().getY() + ((1 / this.getFullScreenModeScale()) * y));

        return new Point(cX, cY);
    }

    public BufferedImage getFullImage(boolean paramShowEditMenu) {

        int top = Integer.MAX_VALUE, left = Integer.MAX_VALUE;
        int bottom = Integer.MIN_VALUE, right = Integer.MIN_VALUE;

        for(Layer layer : this.getLayerManager().getLayers()) {
            switch(layer.getLayerType()) {
                case DRAWING:

                    DrawingLayer drawingLayer = (DrawingLayer) layer;

                    for(Map.Entry<String, Chunk> chunk : drawingLayer.getChunkMap().entrySet()) {
                        String[] split = chunk.getKey().split(":");

                        // LEFT
                        if(Integer.valueOf(split[0]) * drawingLayer.getPixelsPerChunk() < left) {
                            int lCount = 0;
                            x: for(int x = 0; x < drawingLayer.getPixelsPerChunk(); x++, lCount++) {
                                y: for(int y = 0; y < drawingLayer.getPixelsPerChunk(); y++) {
                                    int alpha = chunk.getValue().getActualImage().getRGB(x, y) >> 24;
                                    if(alpha != 0) break x;
                                }
                            }

                            if(lCount != drawingLayer.getPixelsPerChunk()) {
                                left = Math.min(Integer.valueOf(split[0]) * drawingLayer.getPixelsPerChunk() + lCount, left);
                            }
                        }

                        // RIGHT
                        if(((Integer.valueOf(split[0])+1) * drawingLayer.getPixelsPerChunk()) - 1 > right) {
                            int rCount = 0;
                            x: for(int x = 0; x < drawingLayer.getPixelsPerChunk(); x++, rCount++) {
                                y: for(int y = 0; y < drawingLayer.getPixelsPerChunk(); y++) {
                                    int alpha = chunk.getValue().getActualImage().getRGB(drawingLayer.getPixelsPerChunk() - 1 - x, y) >> 24;
                                    if(alpha != 0) break x;
                                }
                            }

                            if(rCount != drawingLayer.getPixelsPerChunk()) {
                                right = Math.max(((Integer.valueOf(split[0])+1) * drawingLayer.getPixelsPerChunk()) - rCount, right);
                            }
                        }

                        // TOP
                        if(Integer.valueOf(split[1]) * drawingLayer.getPixelsPerChunk() < top) {
                            int tCount = 0;
                            y: for(int y = 0; y < drawingLayer.getPixelsPerChunk(); y++, tCount++) {
                                x: for(int x = 0; x < drawingLayer.getPixelsPerChunk(); x++) {
                                    int alpha = chunk.getValue().getActualImage().getRGB(x, y) >> 24;
                                    if(alpha != 0) break y;
                                }
                            }

                            if(tCount != drawingLayer.getPixelsPerChunk()) {
                                top = Math.min(Integer.valueOf(split[1]) * drawingLayer.getPixelsPerChunk() + tCount, top);
                            }
                        }

                        // BOTTOM
                        if(((Integer.valueOf(split[1])+1) * drawingLayer.getPixelsPerChunk()) - 1 > bottom) {
                            int bCount = 0;
                            y: for(int y = 0; y < drawingLayer.getPixelsPerChunk(); y++, bCount++) {
                                x: for(int x = 0; x < drawingLayer.getPixelsPerChunk(); x++) {
                                    int alpha = chunk.getValue().getActualImage().getRGB(x, drawingLayer.getPixelsPerChunk() - 1 - y) >> 24;
                                    if(alpha != 0) break y;
                                }
                            }

                            if(bCount != drawingLayer.getPixelsPerChunk()) {
                                bottom = Math.max(((Integer.valueOf(split[1])+1) * drawingLayer.getPixelsPerChunk()) - bCount, bottom);
                            }
                        }
                    }

                    break;
                case IMAGE:

                    ImageLayer imageLayer = (ImageLayer) layer;

                    left = Math.min((int) imageLayer.getTopLeftPixel().getX(), left);
                    right = Math.max((int) imageLayer.getBottomRightPixel().getX(), right);

                    top = Math.min((int) imageLayer.getTopLeftPixel().getY(), top);
                    bottom = Math.max((int) imageLayer.getBottomRightPixel().getY(), bottom);

                    break;
                case TEXT:

                    TextLayer textLayer = (TextLayer) layer;

                    left = Math.min((int) textLayer.getTopLeftPixel().getX(), left);
                    top = Math.min((int) textLayer.getTopLeftPixel().getY(), top);


                    right = Math.max((int) (textLayer.getDimensions().getWidth() + textLayer.getTopLeftPixel().getX()), right);
                    bottom = Math.max((int) (textLayer.getDimensions().getHeight() + textLayer.getTopLeftPixel().getY()), bottom);

                    break;
                default:
                    break;
            }
        }

        if(top == Integer.MAX_VALUE && left == Integer.MAX_VALUE && bottom == Integer.MIN_VALUE && right == Integer.MIN_VALUE) {
            // Image is empty
            return null;
        }

        int width = Math.abs(right - left);
        int height = Math.abs(bottom - top);
        CanvasUI canvasUI = (CanvasUI) super.getApplication().getUIManager().getWindow().getCanvas();

        double scale = Math.min((double) canvasUI.getWidth() / width, (double)canvasUI.getHeight() / height);

        int differenceX = (int) Math.round(canvasUI.getWidth() / scale - width);
        int differenceY = (int) Math.round(canvasUI.getHeight() / scale - height);

        int currentY = top - differenceY / 2;
        int currentX = left - differenceX / 2;

        this.setFullScreenModeScale(scale);
        this.setFullScreenModeTopLeftPixel(new Point(currentX, currentY));

        return getLayersAsImage(currentX, currentY, canvasUI.getWidth(), canvasUI.getHeight(), scale, paramShowEditMenu);

    }

    public BufferedImage getLayersAsImage(int paramCurrentPixelX, int paramCurrentPixelY, int paramWidth, int paramHeight, double paramZoom, boolean paramShowEditMenu) {
        try {

            ArrayList<LayerImageProcessor> layerImageProcessors = new ArrayList<>();

            // DRAW LAYERS
            for (int index = this.getLayerManager().getLayers().size() - 1; index >= 0; index--) {
                Layer layer = this.getLayerManager().getLayers().get(index);

                if (!layer.isVisible()) {
                    continue;
                }

                LayerImageProcessor layerImageProcessor = layer.getLayerImageProcessor();
                layerImageProcessor.setBounds(paramCurrentPixelX, paramCurrentPixelY, paramWidth, paramHeight, paramZoom, paramShowEditMenu);
                layerImageProcessor.start();
                layerImageProcessors.add(layerImageProcessor);
            }

            for (LayerImageProcessor layerImageProcessor : layerImageProcessors) {
                layerImageProcessor.join();
            }

            BufferedImage bufferedImage = new BufferedImage(paramWidth, paramHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics bufferedImageGraphics = bufferedImage.getGraphics();

            for (LayerImageProcessor layerImageProcessor : layerImageProcessors) {
                bufferedImageGraphics.drawImage(layerImageProcessor.getLayer().getLastRenderedImage(), 0, 0, null);
            }

            return bufferedImage;

        }catch (InterruptedException e) {
            // TO-DO exception
        }
        return null;
    }

    @Getter
    public boolean fullScreenMode;

    public void setFullScreenMode(boolean paramFullScreenMode) {
        this.fullScreenMode = paramFullScreenMode;

        if(paramFullScreenMode && !isPluginActive()) {
            super.getApplication().getPluginManager().activatePlugin(this);
        }

        this.repaint(true);
    }


    @KeyListener(KEY=KeyEvent.VK_X, MODIFIERS = 0) @MenuBarItem(PATH="Edit:X-Ray")
    public void toggleFullScreenMode() {
        this.setFullScreenMode(!this.isFullScreenMode());
    }

    @Override
    public void paint(Graphics paramGraphics) {
        try {
            this.getCanvasLock().lock();

            if (this.getCanvasLock().getQueueLength() > 0) { // Only updates enough to show the last image available instead of inbetween ones saving time and processor
                return;
            }

            CanvasUI canvasUI = (CanvasUI) super.getApplication().getUIManager().getWindow().getCanvas();
            int width = canvasUI.getWidth();
            int height = canvasUI.getHeight();

            Graphics2D graphics2D = (Graphics2D) paramGraphics;

            graphics2D.setPaint(this.getBackgroundColor());
            graphics2D.fillRect(0, 0, width, height);

            if(this.isFullScreenMode()) {

                // FILL BACKGROUND

                BufferedImage bufferedImage = getFullImage(false);

                graphics2D.drawImage(bufferedImage, 0, 0, null);
                this.setLastRenderedImage(bufferedImage);

                graphics2D.setPaint(Color.RED);
                graphics2D.fillRect(0, 0, width, 5);
                graphics2D.fillRect(0, height -5, width, 5);
                graphics2D.fillRect(0, 5, 5, height - 10);
                graphics2D.fillRect(width -5, 5, 5, height - 10);
            } else {

                BufferedImage bufferedImage = getLayersAsImage(this.getCurrentPixelX(), this.getCurrentPixelY(), width, height, this.getZoom(), true);

                graphics2D.drawImage(bufferedImage, 0, 0, null);
                this.setLastRenderedImage(bufferedImage);
            }

        } finally {
            this.getCanvasLock().unlock();
        }
    }
}
