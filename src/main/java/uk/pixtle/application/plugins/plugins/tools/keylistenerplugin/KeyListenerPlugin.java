package uk.pixtle.application.plugins.plugins.tools.keylistenerplugin;

import uk.pixtle.application.Application;
import uk.pixtle.application.plugins.Plugins;
import uk.pixtle.application.plugins.plugins.Plugin;
import uk.pixtle.application.plugins.plugins.tools.ToolPlugin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class KeyListenerPlugin extends ToolPlugin implements PluginKeyListenerPolicy{

    // @KeyListener(KEY = KeyEvent.VK_LEFT, MODIFIERS = KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK) EXAMPLE

    public void loadAllKeyStrokes() {
        final JLayeredPane panel = super.getApplication().getUIManager().getWindow().getLayeredPane();

        for(Map.Entry<Plugins, Plugin> entry :  super.getApplication().getPluginManager().getPluginsByPolicy(PluginKeyListenerPolicy.class).entrySet()) {
            Plugin plugin = entry.getValue();
            for(Method method : plugin.getClass().getDeclaredMethods()) {
                for(Annotation annotation : method.getDeclaredAnnotationsByType(KeyListener.class)) {
                    if(method.getParameterCount() != 0) continue;

                    KeyListener a = (KeyListener) annotation;
                    KeyStroke keyStroke = KeyStroke.getKeyStroke(a.KEY(), a.MODIFIERS());
                    String mapKey = entry.getKey().toString() + "-" + method.getName();

                    panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, mapKey);

                    panel.getActionMap().put(mapKey, new AbstractAction()
                    {
                        public void actionPerformed(ActionEvent event)
                        {
                            try {

                                Component c = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
                                if(c instanceof JTextField || c instanceof JTextArea) return;

                                method.invoke(plugin);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onLoadingFinish(){
        this.loadAllKeyStrokes();
    }

    public KeyListenerPlugin(Application paramApplication) {
        super(paramApplication);
    }
}
