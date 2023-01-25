package uk.pixtle.application;

import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import uk.pixtle.application.colour.ColourManager;
import uk.pixtle.application.events.EventManager;
import uk.pixtle.application.events.events.ExampleEvent;
import uk.pixtle.application.plugins.PluginManager;
import uk.pixtle.application.ui.UIManager;

public class Application {

    @Getter
    @Setter
    public Logger logger;

    @Getter
    @Setter
    public UIManager UIManager;

    @Getter
    @Setter
    public EventManager eventManager;

    @Getter
    @Setter
    public PluginManager pluginManager;

    @Getter
    @Setter
    public ColourManager colourManager;

    public Application() {
        this.setLogger(LogManager.getLogger("Pixtle"));

        this.setEventManager(new EventManager(this));
        this.setUIManager(new UIManager(this));
        this.setColourManager(new ColourManager(this));
        this.setPluginManager(new PluginManager(this));
    }
}
