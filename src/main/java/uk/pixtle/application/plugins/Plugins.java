package uk.pixtle.application.plugins;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.plugins.plugins.TestPlugin;

public enum Plugins {

    TEST(TestPlugin.class);

    @Getter
    @Setter
    Class<? extends Plugin> pluginClass;

    Plugins(Class<? extends Plugin> paramPluginClass) {
        this.setPluginClass(paramPluginClass);
    }
}
