package uk.pixtle.application.plugins;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.plugins.plugins.*;

public enum Plugins {

    EXAMPLE(ExamplePlugin.class),
    HexPlugin(HexPlugin.class),
    ColourPreviewPlugin(uk.pixtle.application.plugins.plugins.ColourPreviewPlugin.class),
    RGBPlugin(uk.pixtle.application.plugins.plugins.RGBPlugin.class);
    ;

    @Getter
    @Setter
    Class<? extends Plugin> pluginClass;

    Plugins(Class<? extends Plugin> paramPluginClass) {
        this.setPluginClass(paramPluginClass);
    }
}
