package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import uk.pixtle.application.Application;
import uk.pixtle.application.plugins.expansions.PluginDrawableExpansion;
import uk.pixtle.application.plugins.expansions.PluginMiniToolExpansion;
import uk.pixtle.application.plugins.plugins.canvas.CanvasPlugin;
import uk.pixtle.application.plugins.plugins.canvas.drawing.ColorAndAlpha;
import uk.pixtle.application.plugins.plugins.canvas.drawing.Drawing;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.Layer;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.LayerImageProcessor;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.LayerManager;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.drawinglayer.Chunk;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.ui.BackgroundLayerUI;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.ui.LayerUIDrawer;
import uk.pixtle.application.plugins.policies.PluginSavePolicy;
import uk.pixtle.application.plugins.toolsettings.ToolSetting;
import uk.pixtle.application.plugins.toolsettings.ToolSettingEntry;
import uk.pixtle.application.plugins.toolsettings.inputdevices.InputDevice;
import uk.pixtle.application.plugins.toolsettings.inputdevices.SliderInputDevice;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;
import uk.pixtle.application.ui.window.minitoollist.MiniToolPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class InfiniteCanvasPlugin extends CanvasPlugin implements PluginDrawableExpansion, PluginMiniToolExpansion, PluginSavePolicy {

    /*

                TOOL SETTINGS

     */

    @Getter
    @Setter
    private JSlider zoomToolSlider;

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

                ZOOM / SCALE SETTINGS

     */

    public void updateCurrentPixel(int paramCurrentPixelX, int paramCurrentPixelY) {
        if(paramCurrentPixelY == 0 && paramCurrentPixelX == 0) return;

        currentPixelX += (int) Math.ceil(((1.0 / this.getZoom()) * paramCurrentPixelX));
        currentPixelY += (int) Math.ceil(((1.0 / this.getZoom()) * paramCurrentPixelY));

        this.repaint();
    }

    public double mapZoom(int paramZoom) {
        return Math.pow(2, 0.07 * paramZoom);
    }

    public int inverseMapZoom(double paramMappedZoom) {
        return (int) Math.round(57.7457 * Math.log(paramMappedZoom) / Math.log(2));
    }

    @Getter
    @Setter
    private double zoom;

    public void zoomAround(double paramScale, int paramX, int paramY) {
        this.setZoom(paramScale);
        this.repaint();
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

        super.repaint();
    }


    private void loadDefaultVariables() {
        this.setLayerManager(new LayerManager(this));
        this.setCanvasLock(new ReentrantLock());
        this.setCurrentPixelX(Integer.MAX_VALUE / 2);
        this.setCurrentPixelY(Integer.MAX_VALUE / 2);
    }

    /*

                PLUGIN DRAWABLE EXPANSION

     */

    @Override
    public void mouseCanvasEvent(int paramCalculatedX, int paramCalculatedY, int paramDifferenceX, int paramDifferenceY) {

        this.updateCurrentPixel(-paramDifferenceX, -paramDifferenceY);
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

    private Color decodeColour(String paramColour) {

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

        this.repaint();
        this.redrawLayers();
    }

     /*

    ABSTRACT METHODS

     */

    @Getter
    @Setter
    private BufferedImage lastRenderedImage;

    @Override
    public void paint(Graphics paramGraphics) {
        try {
            this.getCanvasLock().lock();

            if (this.getCanvasLock().getQueueLength() > 0) { // Only updates enough to show the last image available instead of inbetween ones saving time and processor
                return;
            }

            int height = ((JPanel) super.getApplication().getUIManager().getWindow().getCanvas()).getHeight();
            int width = ((JPanel) super.getApplication().getUIManager().getWindow().getCanvas()).getWidth();
            Graphics2D graphics2D = (Graphics2D) paramGraphics;

            // FILL BACKGROUND
            BufferedImage backgroundImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D backgroundGraphics = (Graphics2D) backgroundImage.getGraphics();
            backgroundGraphics.setPaint(this.getBackgroundColor());
            backgroundGraphics.fillRect(0, 0, backgroundImage.getWidth(), backgroundImage.getHeight());

            graphics2D.drawImage(backgroundImage, 0, 0, null);

            ArrayList<LayerImageProcessor> layerImageProcessors = new ArrayList<>();

            // DRAW LAYERS
            for(int index = this.getLayerManager().getLayers().size() - 1; index >= 0; index--) {
                Layer layer = this.getLayerManager().getLayers().get(index);

                if(!layer.isVisible()) {
                    continue;
                }

                LayerImageProcessor layerImageProcessor = layer.getLayerImageProcessor();
                layerImageProcessor.start();
                layerImageProcessors.add(layerImageProcessor);
            }

            for(LayerImageProcessor layerImageProcessor : layerImageProcessors) {
                layerImageProcessor.join();
            }

            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics newImageGraphics = (Graphics2D) bufferedImage.getGraphics();

            for(LayerImageProcessor layerImageProcessor : layerImageProcessors) {
                newImageGraphics.drawImage(layerImageProcessor.getLayer().getLastRenderedImage(), 0, 0, null);
            }

            graphics2D.drawImage(bufferedImage, 0, 0, null);
            this.setLastRenderedImage(bufferedImage);

        } catch (InterruptedException e) {
            // TO-DO exception
        } finally {
            this.getCanvasLock().unlock();
        }
    }

    @Override
    public void printImageOnCanvas(int paramScreenX, int paramScreenY, Drawing paramDrawing, boolean paramCenter) {
        this.getLayerManager().getActiveLayer().printImageOnCanvas(paramScreenX, paramScreenY, paramDrawing, paramCenter);
        super.repaint();
    }

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

    public void repaint() {
        this.getLayerManager().setReRender(true);
        super.repaint();
    }

    @Override
    public void onLoadingFinish() {
        this.getLayerManager().createNewLayer();
        this.setBackgroundColor(Color.white);

        System.out.println("A: " + currentPixelX + " " + currentPixelY);
    }

    public InfiniteCanvasPlugin(Application paramApplication) {
        super(paramApplication);

        // Layers
        this.loadDefaultVariables();
    }
}
