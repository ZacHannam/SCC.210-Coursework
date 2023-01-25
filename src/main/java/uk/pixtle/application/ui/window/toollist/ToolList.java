package uk.pixtle.application.ui.window.toollist;

import uk.pixtle.application.plugins.plugins.Plugin;
import uk.pixtle.application.ui.window.WindowItem;

public interface ToolList extends WindowItem {

    ToolButton createToolButton(Plugin paramPlugin, String paramIconName);
    public void clearActiveBorders();
    public void clearPluginBorder(Plugin paramPlugin);
    public void addActiveBorder(Plugin paramPlugin);
}
