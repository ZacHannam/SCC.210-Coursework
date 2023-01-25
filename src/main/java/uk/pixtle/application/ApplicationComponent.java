package uk.pixtle.application;

import lombok.Getter;
import lombok.Setter;

/**
 * Abstract class for all components added to the Application
 */
public abstract class ApplicationComponent {

    @Getter
    @Setter
    Application application;

    public ApplicationComponent(Application paramApplication) {
        this.setApplication(paramApplication);
    }
}
