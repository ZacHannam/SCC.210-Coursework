package uk.pixtle.application.plugins;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.plugins.plugins.*;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.InfiniteCanvasPlugin;
import uk.pixtle.application.plugins.plugins.tools.BrushToolPlugin;
import uk.pixtle.application.plugins.plugins.tools.FileLoader;
import uk.pixtle.application.plugins.plugins.tools.colourplugin.ColourPlugin;

import java.io.File;

public enum Plugins {

    INFINITE_CANVAS_PLUGIN(InfiniteCanvasPlugin.class),
    COLOUR_PLUGIN(ColourPlugin.class),
    BRUSH_TOOL(BrushToolPlugin.class),
    FILE_LOADER(FileLoader .class);

    @Getter
    @Setter
    Class<? extends Plugin> pluginClass;

    Plugins(Class<? extends Plugin> paramPluginClass) {
        this.setPluginClass(paramPluginClass);
    }
}
