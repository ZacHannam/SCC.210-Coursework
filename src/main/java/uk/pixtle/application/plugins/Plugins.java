package uk.pixtle.application.plugins;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.plugins.plugins.*;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.InfiniteCanvasPlugin;
import uk.pixtle.application.plugins.plugins.tools.*;
import uk.pixtle.application.plugins.plugins.tools.colourplugin.ColourPlugin;
import uk.pixtle.application.plugins.plugins.tools.colourplugin.EraserToolPlugin;
import uk.pixtle.application.plugins.plugins.tools.keylistenerplugin.KeyListenerPlugin;

public enum Plugins {

    INFINITE_CANVAS_PLUGIN(InfiniteCanvasPlugin.class),
    COLOUR_PLUGIN(ColourPlugin.class),
    BRUSH_TOOL(BrushToolPlugin.class),
    FILE_LOADER(FileLoaderPlugin.class),
    COLOUR_DROPPER(ColourDropperPlugin.class),
    ERASER_TOOL(EraserToolPlugin.class),
    TEXT_TOOL(TextToolPlugin.class),
    FILE_EXPORTER(FileExportingPlugin.class),
    PEN_TOOL(PenToolPlugin.class),
    KEY_LISTENER(KeyListenerPlugin.class);

    @Getter
    @Setter
    Class<? extends Plugin> pluginClass;

    Plugins(Class<? extends Plugin> paramPluginClass) {
        this.setPluginClass(paramPluginClass);
    }
}
