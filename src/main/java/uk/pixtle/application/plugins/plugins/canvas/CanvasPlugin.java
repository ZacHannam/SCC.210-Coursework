package uk.pixtle.application.plugins.plugins.canvas;

import uk.pixtle.application.Application;
import uk.pixtle.application.plugins.expansions.PluginToolExpansion;
import uk.pixtle.application.plugins.plugins.Plugin;
import uk.pixtle.application.plugins.plugins.canvas.drawing.ColorAndAlpha;
import uk.pixtle.application.plugins.plugins.canvas.drawing.Drawing;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.Layer;
import uk.pixtle.application.ui.window.canvas.CanvasUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class CanvasPlugin extends Plugin implements PluginToolExpansion {

    @Override
    public String getIconFilePath() {
        return "Canvas_Tool_Hand.jpg";
    }

    public abstract void paint(Graphics paramGraphics);
    public abstract void printImageOnCanvas(int paramScreenX, int paramScreenY, Drawing paramDrawing, boolean paramCenter);
    public abstract Color getPixelColour(int paramScreenX, int paramScreenY);

    public void repaint() {
        ((JPanel) super.getApplication().getUIManager().getWindow().getCanvas()).repaint();
    }

    public CanvasPlugin(Application paramApplication) {
        super(paramApplication);
    }
}
