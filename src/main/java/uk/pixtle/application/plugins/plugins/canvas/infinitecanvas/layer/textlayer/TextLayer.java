package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.textlayer;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import org.w3c.dom.Text;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.InfiniteCanvasPlugin;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.Layer;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.LayerImageProcessor;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.LayerManager;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.LayerType;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.imagelayer.ImageLayer;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class TextLayer extends Layer {

    public static final String DEFAULT_TEXT = "The quick brown fox jumps over the lazy dog";
    public static final int DEFAULT_TEXT_SIZE = 25;
    public static final String DEFAULT_FONT = "Arial";
    public static final Color DEFAULT_FOREGROUND = Color.black;

    @Getter
    String text;

    @Getter
    int textSize;

    @Getter
    String fontType;

    @Getter
    Color foregroundColor;

    public Dimension getDimensions() {
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform,true,false);

        Font font = new Font(this.getFontType(), Font.PLAIN, this.getTextSize());
        int textWidth = (int)(font.getStringBounds(this.getText(), frc).getWidth());
        int textHeight = (int)(font.getStringBounds(this.getText(), frc).getHeight());

        return new Dimension(textWidth, textHeight);
    }

    private void loadDefaultVariables() {
        this.text = DEFAULT_TEXT;
        this.textSize = DEFAULT_TEXT_SIZE;
        this.fontType = DEFAULT_FONT;
        this.foregroundColor = DEFAULT_FOREGROUND;
    }

    public void setFont(String paramFont) {
        if(paramFont == this.getFontType()) return;

        this.fontType = paramFont;
        this.variableChangeRepaint();
    }

    public void setText(String paramText) {
        if(paramText == this.getText()) return;

        this.setTitle(paramText);

        this.text = paramText;
        this.variableChangeRepaint();
    }

    public void setTextSize(int paramTextSize) {
        if(paramTextSize == this.getTextSize()) return;

        this.textSize = paramTextSize;
        this.variableChangeRepaint();
    }

    public void setForegroundColor(Color paramColor) {
        if(paramColor == this.getForegroundColor()) return;

        this.foregroundColor = paramColor;
        this.variableChangeRepaint();
    }

    private void variableChangeRepaint() {
        this.setReRender(true);
        this.getLayerManager().getInfiniteCanvasPlugin().repaint(false);
    }

    @Getter
    @Setter
    Point2D topLeftPixel;

    @Override
    public LayerType getLayerType() {
        return LayerType.TEXT;
    }

    @Override
    public JSONObject saveLayerData() throws Exception {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text", this.getText());
        jsonObject.put("textSize", this.getTextSize());
        jsonObject.put("font", this.getFontType());
        jsonObject.put("foregroundColor", this.getForegroundColor());
        jsonObject.put("topLeftPixelX", (int) this.getTopLeftPixel().getX());
        jsonObject.put("topLeftPixelY", (int) this.getTopLeftPixel().getY());

        return jsonObject;
    }

    @Override
    public void loadLayerData(JSONObject paramSavedData) throws Exception {

        this.setText(paramSavedData.getString("text"));
        this.setTextSize(paramSavedData.getInt("textSize"));
        this.setFont(paramSavedData.getString("font"));
        this.setForegroundColor(InfiniteCanvasPlugin.decodeColour(paramSavedData.getString("foregroundColor")));
        this.setTopLeftPixel(new Point(paramSavedData.getInt("topLeftPixelX"), paramSavedData.getInt("topLeftPixelY")));

    }

    @Override
    public LayerImageProcessor getLayerImageProcessor() {
        return new TextLayerImageProcessor(this);
    }

    @Override
    public boolean loadNew() {

        this.loadDefaultVariables();


        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);

        Font font = new Font(DEFAULT_FONT, Font.PLAIN, DEFAULT_TEXT_SIZE);
        int textWidth = (int)(font.getStringBounds(DEFAULT_TEXT, frc).getWidth());
        int textHeight = (int)(font.getStringBounds(DEFAULT_TEXT, frc).getHeight());


        Point centerPixel = this.getLayerManager().getInfiniteCanvasPlugin().getCenterPixel();

        int topY = (int) centerPixel.getY() - (int) Math.floor(textHeight / 2);
        int leftX = (int) centerPixel.getX() - (int) Math.floor(textWidth / 2);

        this.setTopLeftPixel(new Point(leftX, topY));

        super.setTitle(DEFAULT_TEXT);

        return true;
    }

    public void armAWTEvents() {
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {

                if (event instanceof MouseEvent) {
                    if(!(getLayerManager().getInfiniteCanvasPlugin().getApplication().getPluginManager().getActivatePlugin() == getLayerManager().getInfiniteCanvasPlugin())) return;
                    if(!(getLayerManager().getActiveLayer() == TextLayer.this)) return;

                    if(((MouseEvent) event).getButton() != MouseEvent.BUTTON1) return;

                    if(event.getID() == MouseEvent.MOUSE_CLICKED) {
                        setMousePressed(false);
                    }

                    if(event.getID() == MouseEvent.MOUSE_PRESSED) {
                        setMousePressed(true);
                    }

                    if(event.getID() == MouseEvent.MOUSE_RELEASED) {
                        setMousePressed(false);
                    }
                }
            }

        }, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.WINDOW_EVENT_MASK);
    }

    @Getter
    boolean mousePressed;

    private void setMousePressed(boolean paramPressed) {
        this.mousePressed = paramPressed;

        if(!paramPressed) {
            this.setDragged(0);
        }
    }

    @Override
    public void onLayerEnable() {
        super.setReRender(true);
        super.getLayerManager().getInfiniteCanvasPlugin().repaint(false);
    }

    @Override
    public void onLayerDisable() {
        super.setReRender(true);
        super.getLayerManager().getInfiniteCanvasPlugin().repaint(false);
    }

    @Getter
    @Setter
    int dragged;

    private void repaintResize() {

        super.setReRender(true);
        this.getLayerManager().getInfiniteCanvasPlugin().repaint(false);
    }

    @Getter
    @Setter
    long lastClickTime;


    @Override
    public void mouseCanvasEvent(int paramCalculatedX, int paramCalculatedY, int paramDifferenceX, int paramDifferenceY) {

        if(System.currentTimeMillis() - this.getLastClickTime() > 250) {
            this.setDragged(0);
        }

        if(paramDifferenceX == 0 && paramDifferenceY == 0) return;

        InfiniteCanvasPlugin infiniteCanvasPlugin = this.getLayerManager().getInfiniteCanvasPlugin();
        Point translatedPixel = infiniteCanvasPlugin.translateScreenPixel(paramCalculatedX, paramCalculatedY);
        Dimension size = this.getDimensions();

        if (this.getDragged() == 5 || translatedPixel.getY() < this.getTopLeftPixel().getY() + size.getHeight() && translatedPixel.getY() > this.getTopLeftPixel().getY()
                && translatedPixel.getX() < this.getTopLeftPixel().getX() + size.getWidth() && translatedPixel.getX() > this.getTopLeftPixel().getX()) {

            int moveX = (int) Math.round(paramDifferenceX * (1 / infiniteCanvasPlugin.getZoom()));
            int moveY = (int) Math.round(paramDifferenceY * (1 / infiniteCanvasPlugin.getZoom()));
            this.setTopLeftPixel(new Point((int) this.getTopLeftPixel().getX() + moveX, (int) this.getTopLeftPixel().getY() + moveY));

            super.setReRender(true);
            infiniteCanvasPlugin.repaint(false);

            this.setDragged(5);

        } else {
            super.mouseCanvasEvent(paramCalculatedX, paramCalculatedY, paramDifferenceX, paramDifferenceY);
        }

        if(this.getDragged() != 0) {
            this.setLastClickTime(System.currentTimeMillis());
        }
    }

    public TextLayer(LayerManager paramLayerManager) {
        super(paramLayerManager);
    }
}
