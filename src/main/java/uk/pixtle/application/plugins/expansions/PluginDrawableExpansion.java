package uk.pixtle.application.plugins.expansions;

import java.awt.*;
import java.awt.event.MouseEvent;

public interface PluginDrawableExpansion extends PluginExpansion{

    public void mouseCanvasEvent(int paramCalculatedX, int paramCalculatedY, int paramDifferenceX, int paramDifferenceY);
}
