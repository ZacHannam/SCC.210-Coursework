package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.textlayer;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import org.w3c.dom.Text;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.Layer;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.LayerImageProcessor;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.LayerManager;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.LayerType;

import java.awt.*;

public class TextLayer extends Layer {

    public static final String DEFAULT_TEXT = "The quick brown fox jumps over the lazy dog";
    public static final int DEFAULT_TEXT_SIZE = 100;
    public static final String DEFAULT_FONT = "Arial";

    @Getter
    @Setter
    Point caretPosition;

    @Getter
    String text;

    @Getter
    int textSize;

    @Getter
    String fontType;

    private void loadDefaultVariables() {
        this.text = DEFAULT_TEXT;
        this.textSize = DEFAULT_TEXT_SIZE;
        this.fontType = DEFAULT_FONT;
    }

    public void setFont(String paramFont) {
        if(paramFont == this.getFontType()) return;

        this.fontType = paramFont;
        this.variableChangeRepaint();
    }

    public void setText(String paramText) {
        if(paramText == this.getText()) return;

        this.text = paramText;
        this.variableChangeRepaint();
    }

    public void setTextSize(int paramTextSize) {
        if(paramTextSize == this.getTextSize()) return;

        this.textSize = paramTextSize;
        this.variableChangeRepaint();
    }

    private void variableChangeRepaint() {
        this.setReRender(true);
        this.getLayerManager().getInfiniteCanvasPlugin().repaint(false);
    }

    @Override
    public LayerType getLayerType() {
        return LayerType.TEXT;
    }

    @Override
    public JSONObject saveLayerData() throws Exception {
        return null;
    }

    @Override
    public void loadLayerData(JSONObject paramSavedData) throws Exception {

    }

    @Override
    public LayerImageProcessor getLayerImageProcessor() {
        return new TextLayerImageProcessor(this);
    }

    @Override
    public boolean loadNew() {
        return true;
    }

    public TextLayer(LayerManager paramLayerManager) {
        super(paramLayerManager);

        this.loadDefaultVariables();
    }
}
