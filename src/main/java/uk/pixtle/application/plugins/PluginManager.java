package uk.pixtle.application.plugins;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.Application;
import uk.pixtle.application.ApplicationComponent;
import uk.pixtle.application.plugins.annotations.MenuBarItem;
import uk.pixtle.application.plugins.plugins.canvas.CanvasPlugin;
import uk.pixtle.application.plugins.toolsettings.ToolSetting;
import uk.pixtle.application.plugins.expansions.PluginMiniToolExpansion;
import uk.pixtle.application.plugins.expansions.PluginToolExpansion;
import uk.pixtle.application.plugins.plugins.Plugin;
import uk.pixtle.application.plugins.toolsettings.ToolSettingEntry;
import uk.pixtle.application.ui.window.minitoollist.MiniToolPanel;
import uk.pixtle.application.ui.window.toollist.ToolButton;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class PluginManager extends ApplicationComponent {

    @Getter
    public HashMap<Plugins, Plugin> pluginRegistry = new HashMap<>();

    @Getter
    @Setter
    private CanvasPlugin canvasPlugin;

    private void registerAllPlugins() {

        final Class<?>[] constructorParam = {Application.class};

        for(Plugins pluginValue : Plugins.values()) {
            Class<? extends Plugin> pluginClass = pluginValue.getPluginClass();

            try {
                Plugin plugin = Objects.requireNonNull(pluginClass).getConstructor(constructorParam).newInstance(super.getApplication());
                this.registerPlugin(plugin);
                this.getPluginRegistry().put(pluginValue, plugin);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException exception) {
                // TO-DO print exception
                return;
            }


        }

        ((JPanel) super.getApplication().getUIManager().getWindow().getCanvas()).updateUI();


    }

    private void registerPlugin(Plugin paramPlugin) {

        // Load into Tool List

        if(getCanvasPlugin() == null && paramPlugin instanceof CanvasPlugin) {
            setCanvasPlugin((CanvasPlugin) paramPlugin);
        }

        // Load In All implements
        if(paramPlugin instanceof PluginMiniToolExpansion) {
            PluginMiniToolExpansion plugin = (PluginMiniToolExpansion) paramPlugin;

            MiniToolPanel miniToolPanel = this.getApplication().getUIManager().getWindow().getMiniToolList().createMiniToolPanel(plugin.getMiniToolPanelHeight());
            plugin.instanceMiniToolPanel(miniToolPanel);
            miniToolPanel.updateUI();
        }

        if(paramPlugin instanceof PluginToolExpansion) {
            this.getApplication().getUIManager().getWindow().getToolList().createToolButton(paramPlugin, ((PluginToolExpansion) paramPlugin).getIconFilePath());
        }

        // Load All Annotations
        for(Method method : paramPlugin.getClass().getDeclaredMethods()) {

            // MENU BAR ITEMS
            for(Annotation annotation : method.getDeclaredAnnotationsByType(MenuBarItem.class)) {
                if(!method.getReturnType().equals(Void.TYPE) || method.getParameterCount() != 0) {
                    // TO-DO exception
                    continue;
                }

                this.getApplication().getUIManager().getMenuBar().addNewMenuItem(
                        ((MenuBarItem) annotation).PATH(),
                        paramPlugin,
                        method);
            }
        }
    }

    public ArrayList<ToolSettingEntry<?>> getToolSettingEntries(Plugin paramPlugin) {

        ArrayList<ToolSettingEntry<?>> toolSettingEntries = new ArrayList<>();

        for(Field field : paramPlugin.getClass().getDeclaredFields()) {

            for (Annotation annotation : field.getDeclaredAnnotationsByType(ToolSetting.class)) {
                if (!field.getType().equals(ToolSettingEntry.class)) {
                    // TO-DO exception
                    continue;
                }

                field.setAccessible(true);

                try {

                    toolSettingEntries.add((ToolSettingEntry<?>) field.get(paramPlugin));

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return toolSettingEntries;
    }

    public Plugin getPluginByPluginType(Plugins paramPluginType) {
        if(this.getPluginRegistry().containsKey(paramPluginType)) {
            return this.getPluginRegistry().get(paramPluginType);
        }
        return null;
    }

    public void pluginClick(Plugin paramPlugin) {

        // SET ACTIVE
        ArrayList<ToolSettingEntry<?>> toolSettingEntries = getToolSettingEntries(paramPlugin);

        if(toolSettingEntries.size() != 0) {
            super.getApplication().getUIManager().getWindow().getMiniToolList().createToolSettingsPanel(toolSettingEntries);
        } else {
            super.getApplication().getUIManager().getWindow().getMiniToolList().removeToolSettingsPanel();
        }
    }

    public PluginManager(Application paramApplication) {
        super(paramApplication);

        registerAllPlugins();
    }

}
