package uk.pixtle.application.plugins;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.Application;
import uk.pixtle.application.ApplicationComponent;
import uk.pixtle.application.plugins.annotations.MenuBarItem;
import uk.pixtle.application.plugins.plugins.canvas.CanvasPlugin;
import uk.pixtle.application.plugins.plugins.tools.ToolPlugin;
import uk.pixtle.application.plugins.policies.PluginPolicy;
import uk.pixtle.application.plugins.toolsettings.ToolSetting;
import uk.pixtle.application.plugins.expansions.PluginMiniToolExpansion;
import uk.pixtle.application.plugins.expansions.PluginToolExpansion;
import uk.pixtle.application.plugins.plugins.Plugin;
import uk.pixtle.application.plugins.toolsettings.ToolSettingEntry;
import uk.pixtle.application.ui.window.minitoollist.MiniToolPanel;

import javax.swing.*;
import java.awt.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class PluginManager extends ApplicationComponent {

    @Getter
    public HashMap<Plugins, Plugin> pluginRegistry = new HashMap<>();

    /**
     * Search for plugins by a policy
     * @param paramPolicy
     * @return
     */
    public HashMap<Plugins, Plugin> getPluginsByPolicy(Class<? extends PluginPolicy> paramPolicy) {
        HashMap<Plugins, Plugin> plugins = new HashMap<>();

        for(Map.Entry<Plugins, Plugin> entry: this.getPluginRegistry().entrySet()) {
            if(Arrays.stream(entry.getValue().getClass().getInterfaces()).toList().contains(paramPolicy)) {
                plugins.put(entry.getKey(), entry.getValue());
            }
        }

        return plugins;
    }

    /**
     * Get the plugin by the plugin type
     * @param paramPluginType
     * @return
     */
    public Plugin getPluginByPluginType(Plugins paramPluginType) {
        if(this.getPluginRegistry().containsKey(paramPluginType)) {
            return this.getPluginRegistry().get(paramPluginType);
        }
        return null;
    }

    @Getter
    @Setter
    private CanvasPlugin activeCanvasPlugin;

    @Getter
    @Setter
    private Plugin activatePlugin;

    /**
     * Instantiates each plugin and adds them to the plugin registry
     */
    private void registerAllPlugins() {

        // Loads the default constructor of the plugins
        final Class<?>[] constructorParam = {Application.class};

        // Iterates through all the plugins to load each individually
        for(Plugins pluginValue : Plugins.values()) {

            Class<? extends Plugin> pluginClass = pluginValue.getPluginClass();

            try {
                Plugin plugin = Objects.requireNonNull(pluginClass).getConstructor(constructorParam).newInstance(super.getApplication());
                this.getPluginRegistry().put(pluginValue, plugin);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException exception) {
                exception.printStackTrace();
                return;
            }


        }
    }

    /**
     * Loads all the plugins in the plugin registry
     */
    private void loadAllPlugins() {
        for(Plugins pluginType : Plugins.values()) {
            this.registerPlugin(pluginRegistry.get(pluginType));
        }

        ((JPanel) super.getApplication().getUIManager().getWindow().getCanvas()).updateUI();

        if(this.getActiveCanvasPlugin() == null) {
            throw new RuntimeException(); // TO-DO exception
        }
    }

    /**
     * Loads the plugins events and annotations
     * @param paramPlugin
     */
    private void registerPlugin(Plugin paramPlugin) {

        // Select the canvas plugin
        if(this.getActiveCanvasPlugin() == null && paramPlugin instanceof CanvasPlugin) {
            this.setActiveCanvasPlugin((CanvasPlugin) paramPlugin);
        }

        // Plugin Mini Tool Expansion
        if(paramPlugin instanceof PluginMiniToolExpansion) {
            PluginMiniToolExpansion plugin = (PluginMiniToolExpansion) paramPlugin;

            // Creates the mini tool panel
            MiniToolPanel miniToolPanel = this.getApplication().getUIManager().getWindow().getMiniToolList().createMiniToolPanel(paramPlugin, plugin.getMiniToolPanelHeight());
            plugin.instanceMiniToolPanel(miniToolPanel);
            miniToolPanel.updateUI();
        }

        // Plugin Tool Expansion
        if(paramPlugin instanceof PluginToolExpansion) {
            // Creates the tool button on the side
            this.getApplication().getUIManager().getWindow().getToolList().createToolButton(paramPlugin, ((PluginToolExpansion) paramPlugin).getIconFilePath());
        }

        // Load annotated methods
        for(Method method : paramPlugin.getClass().getDeclaredMethods()) {

            // Load menu bar items
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

    /**
     * Get all the tool setting entries
     * @param paramPlugin
     * @return list of tool settings in the plugin
     */
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

    /**
     *
     * @param paramPlugin
     */
    public void activatePlugin(Plugin paramPlugin) {

        // Sets the active plugin border
        if(this.getActivatePlugin() != null) {
            super.getApplication().getUIManager().getWindow().getToolList().clearPluginBorder(this.getActivatePlugin());
        }

        super.getApplication().getUIManager().getWindow().getToolList().addActiveBorder(paramPlugin);

        // Load tool settings entry panel
        ArrayList<ToolSettingEntry<?>> toolSettingEntries = getToolSettingEntries(paramPlugin);

        if(toolSettingEntries.size() != 0) {
            super.getApplication().getUIManager().getWindow().getMiniToolList().createToolSettingsPanel(toolSettingEntries);
        } else {
            super.getApplication().getUIManager().getWindow().getMiniToolList().removeToolSettingsPanel();
        }

        this.setActivatePlugin(paramPlugin);

        if(paramPlugin instanceof ToolPlugin) {
            ((ToolPlugin) paramPlugin).onEnable();
        }
    }

    public void callOnLoadingFinish() {
        for(Plugin plugin : this.getPluginRegistry().values()) {
            plugin.onLoadingFinish();
        }
    }

    public void updateCanvas(Graphics paramGraphics) {
        this.getActiveCanvasPlugin().paint(paramGraphics);
    }

    private void setDefaultPlugin() {
        if(this.getPluginRegistry().size() > 0) {
            this.activatePlugin(this.getPluginByPluginType(Plugins.values()[0]));
        }
    }

    public PluginManager(Application paramApplication) {
        super(paramApplication);

        this.registerAllPlugins();
        this.loadAllPlugins();
        this.setDefaultPlugin();

        this.callOnLoadingFinish();

    }

}
