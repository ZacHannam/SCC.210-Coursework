package uk.pixtle.application.plugins.plugins.tools;

import uk.pixtle.application.Application;
import uk.pixtle.application.plugins.expansions.PluginDrawableExpansion;
import uk.pixtle.application.plugins.expansions.PluginToolExpansion;

import java.awt.*;

public class ColourDropperPlugin extends ToolPlugin implements PluginToolExpansion, PluginDrawableExpansion {
    public ColourDropperPlugin(Application paramApplication) {
        super(paramApplication);
    }

    @Override
    public String getIconFilePath() {
        return "ColourSelect-001.png";
    }

    @Override
    public void mouseCanvasEvent(int paramCalculatedX, int paramCalculatedY, int paramDifferenceX, int paramDifferenceY) {
        Color color = super.getApplication().getPluginManager().getActiveCanvasPlugin().getPixelColour(paramCalculatedX, paramCalculatedY);
        super.getApplication().getColourManager().setColorOfActiveSlot(color);
    }
}
