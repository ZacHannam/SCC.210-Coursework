package uk.pixtle.application.plugins;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.plugins.plugins.Plugin;
import uk.pixtle.application.plugins.plugins.ExamplePlugin;

public enum Plugins {

    EXAMPLE(ExamplePlugin.class);

    @Getter
    @Setter
    Class<? extends Plugin> pluginClass;

    Plugins(Class<? extends Plugin> paramPluginClass) {
        this.setPluginClass(paramPluginClass);
    }
}
