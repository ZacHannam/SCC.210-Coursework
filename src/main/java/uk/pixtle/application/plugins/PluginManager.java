package uk.pixtle.application.plugins;

import uk.pixtle.application.Application;
import uk.pixtle.application.ApplicationComponent;
import uk.pixtle.application.plugins.annotations.MenuBarItem;
import uk.pixtle.application.plugins.expansions.PluginMiniToolExpansion;
import uk.pixtle.application.plugins.plugins.Plugin;
import uk.pixtle.application.ui.window.minitoollist.MiniToolPanel;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class PluginManager extends ApplicationComponent {

    private void registerAllPlugins() {

        final Class<?>[] constructorParam = {Application.class};

        for(Plugins pluginValue : Plugins.values()) {
            Class<? extends Plugin> pluginClass = pluginValue.getPluginClass();

            try {
                Plugin plugin = Objects.requireNonNull(pluginClass).getConstructor(constructorParam).newInstance(super.getApplication());
                this.registerPlugin(plugin);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException exception) {
                // TO-DO print exception
                return;
            }
        }
    }

    private void registerPlugin(Plugin paramPlugin) {

        // Load into Tool List

        // Load In All implements
        if(paramPlugin instanceof PluginMiniToolExpansion) {
            PluginMiniToolExpansion plugin = (PluginMiniToolExpansion) paramPlugin;

            MiniToolPanel miniToolPanel = this.getApplication().getUIManager().getWindow().getMiniToolList().createMiniToolPanel(plugin.getMiniToolPanelHeight());
            plugin.instanceMiniToolPanel(miniToolPanel);
        }

        // Load All Annotations
        for(Method method : paramPlugin.getClass().getDeclaredMethods()) {
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

    public PluginManager(Application paramApplication) {
        super(paramApplication);

        registerAllPlugins();
    }

}
