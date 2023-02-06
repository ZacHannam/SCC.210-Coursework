package uk.pixtle.application.plugins;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.plugins.plugins.*;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.InfiniteCanvasPlugin;
import uk.pixtle.application.plugins.plugins.tools.BrushToolPlugin;
import uk.pixtle.application.plugins.plugins.tools.ColourDropperPlugin;
import uk.pixtle.application.plugins.plugins.tools.FileLoaderPlugin;
import uk.pixtle.application.plugins.plugins.tools.colourplugin.ColourPlugin;

public enum Plugins {

    INFINITE_CANVAS_PLUGIN(InfiniteCanvasPlugin.class),
    COLOUR_PLUGIN(ColourPlugin.class),
    BRUSH_TOOL(BrushToolPlugin.class),
    FILE_LOADER(FileLoaderPlugin.class),
    COLOUR_DROPPER(ColourDropperPlugin.class);
    ERASER_TOOL(EraserToolPlugin.class);

    @Getter
    @Setter
    Class<? extends Plugin> pluginClass;

    Plugins(Class<? extends Plugin> paramPluginClass) {
        this.setPluginClass(paramPluginClass);
    }
}
