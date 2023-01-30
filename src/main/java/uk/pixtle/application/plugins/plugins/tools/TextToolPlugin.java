package uk.pixtle.application.plugins.plugins.tools;

import uk.pixtle.application.Application;
import uk.pixtle.application.plugins.expansions.PluginToolExpansion;

public class TextToolPlugin extends ToolPlugin implements PluginToolExpansion {

    public TextToolPlugin(Application paramApplication) {
        super(paramApplication);
    }

    @Override
    public String getIconFilePath() {
        return "Text_Tool.png";
    }
}
