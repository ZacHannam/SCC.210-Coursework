package uk.pixtle.application.plugins.plugins.canvas;

import uk.pixtle.application.Application;
import uk.pixtle.application.plugins.expansions.PluginToolExpansion;
import uk.pixtle.application.plugins.plugins.Plugin;

import java.awt.*;

public abstract class CanvasPlugin extends Plugin implements PluginToolExpansion {

    @Override
    public String getIconFilePath() {
        return "icons/canvas.png";
    }

    public abstract void paint(Graphics paramGraphics);

    public CanvasPlugin(Application paramApplication) {
        super(paramApplication);
    }
}
