package uk.pixtle.application.plugins.plugins;

import uk.pixtle.application.Application;
import uk.pixtle.application.plugins.Plugin;
import uk.pixtle.application.plugins.annotations.MenuBarItem;

public class TestPlugin extends Plugin {

    @Override
    public String getToolIconLocation() {
        return null;
    }


    @MenuBarItem(PATH = "file:test")
    public void hello() {
        System.out.println("Test");
    }

    public TestPlugin(Application paramApplication) {
        super(paramApplication);
    }
}
