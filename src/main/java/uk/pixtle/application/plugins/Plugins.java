package uk.pixtle.application.plugins;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.plugins.plugins.*;
import uk.pixtle.application.plugins.plugins.canvas.CanvasPlugin;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.InfiniteCanvasPlugin;

public enum Plugins {

    //EXAMPLE(ExamplePlugin.class),
    ColourPalette(ColourPalettePlugin.class),
    HexPlugin(HexPlugin.class),
    RGBPlugin(uk.pixtle.application.plugins.plugins.RGBPlugin.class),
    ColourPreviewPlugin(uk.pixtle.application.plugins.plugins.ColourPreviewPlugin.class),
    HistoryPlugin(uk.pixtle.application.plugins.plugins.HistoryPlugin.class),

    INFINITE_CANVAS_PLUGIN(InfiniteCanvasPlugin.class),
    BRUSH_TOOL(BrushToolPlugin.class);


    ;

    @Getter
    @Setter
    Class<? extends Plugin> pluginClass;

    Plugins(Class<? extends Plugin> paramPluginClass) {
        this.setPluginClass(paramPluginClass);
    }
}
