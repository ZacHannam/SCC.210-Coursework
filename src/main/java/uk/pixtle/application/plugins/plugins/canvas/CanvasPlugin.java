package uk.pixtle.application.plugins.plugins.canvas;

import uk.pixtle.application.Application;
import uk.pixtle.application.plugins.expansions.PluginToolExpansion;
import uk.pixtle.application.plugins.plugins.Plugin;
import uk.pixtle.application.ui.window.canvas.CanvasUI;

import javax.swing.*;
import java.awt.*;

public abstract class CanvasPlugin extends Plugin implements PluginToolExpansion {

    @Override
    public String getIconFilePath() {
        return "icons/canvas.png";
    }

    public abstract void paint(Graphics paramGraphics);

    public void repaint() {
        ((JPanel) super.getApplication().getUIManager().getWindow().getCanvas()).repaint();
    }

    public CanvasPlugin(Application paramApplication) {
        super(paramApplication);
    }
}
