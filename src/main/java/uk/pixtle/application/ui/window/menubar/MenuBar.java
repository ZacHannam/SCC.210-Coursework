package uk.pixtle.application.ui.window.menubar;

import uk.pixtle.application.plugins.Plugin;
import uk.pixtle.application.ui.window.WindowItem;

import java.lang.reflect.Method;

public interface MenuBar extends WindowItem {

    void addNewMenuItem(String path, Plugin paramPlugin, Method method);
}
