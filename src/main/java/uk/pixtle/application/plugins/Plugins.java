package uk.pixtle.application.plugins;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.plugins.plugins.*;

public enum Plugins {

    //EXAMPLE(ExamplePlugin.class),
    ColourPalette(ColourPalettePlugin.class),
    HexPlugin(HexPlugin.class),
    RGBPlugin(uk.pixtle.application.plugins.plugins.RGBPlugin.class),
    ColourPreviewPlugin(uk.pixtle.application.plugins.plugins.ColourPreviewPlugin.class),
    HistoryPlugin(uk.pixtle.application.plugins.plugins.HistoryPlugin.class);

    ;

    @Getter
    @Setter
    Class<? extends Plugin> pluginClass;

    Plugins(Class<? extends Plugin> paramPluginClass) {
        this.setPluginClass(paramPluginClass);
    }
}
