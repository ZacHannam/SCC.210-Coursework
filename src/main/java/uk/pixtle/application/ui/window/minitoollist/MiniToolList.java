package uk.pixtle.application.ui.window.minitoollist;

import uk.pixtle.application.plugins.plugins.Plugin;
import uk.pixtle.application.plugins.toolsettings.ToolSettingEntry;
import uk.pixtle.application.ui.window.WindowItem;

import java.util.ArrayList;

public interface MiniToolList extends WindowItem {

    MiniToolPanel createMiniToolPanel(Plugin paramPlugin, int paramHeight);
    ToolSettingsPanel createToolSettingsPanel(ArrayList<ToolSettingEntry<?>> paramToolSettingEntries);
    void removeToolSettingsPanel();
    void updateHeightOfMMiniToolPanel(Plugin paramPlugin, int paramNewHeight);
}
