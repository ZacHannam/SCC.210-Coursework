package uk.pixtle.application.plugins.plugins;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.Application;

public abstract class Plugin {

    @Getter
    @Setter
    Application application;

    public void onLoadingFinish() {}

    public void onEnable(){}

    public void onDisable(){
    }

    public Plugin(Application paramApplication) {
        this.setApplication(paramApplication);
    }

    public boolean isPluginActive() {
        return this.getApplication().getPluginManager().getActivatePlugin() == this;
    }

}
