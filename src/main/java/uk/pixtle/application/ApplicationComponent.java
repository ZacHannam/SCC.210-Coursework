package uk.pixtle.application;

import lombok.Getter;
import lombok.Setter;

public abstract class ApplicationComponent {

    @Getter
    @Setter
    Application application;

    public ApplicationComponent(Application paramApplication) {
        this.setApplication(paramApplication);
    }
}
