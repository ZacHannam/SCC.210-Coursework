package uk.pixtle.application.plugins.expansions;

import uk.pixtle.application.ui.window.minitoollist.MiniToolPanel;

public interface PluginMiniToolExpansion extends PluginExpansion {

    int getMiniToolPanelHeight();
    void instanceMiniToolPanel(MiniToolPanel paramMiniToolPanel);

}
