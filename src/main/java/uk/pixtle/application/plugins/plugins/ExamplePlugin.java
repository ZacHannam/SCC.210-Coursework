package uk.pixtle.application.plugins.plugins;

import uk.pixtle.application.Application;
import uk.pixtle.application.events.annotations.EventHandler;
import uk.pixtle.application.events.events.ExampleEvent;
import uk.pixtle.application.plugins.annotations.MenuBarItem;

public class ExamplePlugin extends Plugin {

    @Override
    public String getToolIconLocation() {
        return null;
    }


    @MenuBarItem(PATH = "file:test")
    public void hello() {
        System.out.println("Test");
    }

    @EventHandler
    public void test(ExampleEvent paramEvent) {
        System.out.println(paramEvent.getCreationTime().toString());
    }



    public ExamplePlugin(Application paramApplication) {
        super(paramApplication);

        super.getApplication().getEventManager().registerEvents(this);
    }
}
