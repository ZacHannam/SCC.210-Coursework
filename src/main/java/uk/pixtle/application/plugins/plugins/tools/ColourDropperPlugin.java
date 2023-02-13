package uk.pixtle.application.plugins.plugins.tools;

import uk.pixtle.application.Application;
import uk.pixtle.application.plugins.expansions.PluginDrawableExpansion;
import uk.pixtle.application.plugins.expansions.PluginToolExpansion;
import uk.pixtle.application.plugins.plugins.tools.keylistenerplugin.KeyListener;
import uk.pixtle.application.plugins.plugins.tools.keylistenerplugin.KeyListenerPlugin;
import uk.pixtle.application.plugins.plugins.tools.keylistenerplugin.PluginKeyListenerPolicy;

import java.awt.*;
import java.awt.event.KeyEvent;

public class ColourDropperPlugin extends ToolPlugin implements PluginToolExpansion, PluginDrawableExpansion, PluginKeyListenerPolicy {
    public ColourDropperPlugin(Application paramApplication) {
        super(paramApplication);
    }
    @KeyListener(KEY = KeyEvent.VK_I, MODIFIERS = 0)
    public void dropper() {super.getApplication().getPluginManager().activatePlugin(this);}

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
