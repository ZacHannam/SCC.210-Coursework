package uk.pixtle.application;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.plugins.PluginManager;
import uk.pixtle.application.ui.UIManager;

public class Application {

    @Getter
    @Setter
    public UIManager UIManager;

    @Getter
    @Setter
    public PluginManager pluginManager;

    public Application() {
        this.setUIManager(new UIManager(this));
        this.setPluginManager(new PluginManager(this));
    }
}
