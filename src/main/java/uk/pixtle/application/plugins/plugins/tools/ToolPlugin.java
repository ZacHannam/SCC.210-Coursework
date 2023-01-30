package uk.pixtle.application.plugins.plugins.tools;

import uk.pixtle.application.Application;
import uk.pixtle.application.plugins.plugins.Plugin;

public abstract class ToolPlugin extends Plugin {

    public void onEnable(){}

    public boolean isPluginActive() {
        return this.getApplication().getPluginManager().getActivatePlugin() == this;
    }


    public ToolPlugin(Application paramApplication) {
        super(paramApplication);
    }
}
