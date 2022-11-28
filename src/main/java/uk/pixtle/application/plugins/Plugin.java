package uk.pixtle.application.plugins;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.Application;

public abstract class Plugin {

    @Getter
    @Setter
    Application application;

    public abstract String getToolIconLocation();

    public Plugin(Application paramApplication) {
        this.setApplication(paramApplication);
    }
}
