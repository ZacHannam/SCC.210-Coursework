package uk.pixtle.application.plugins.plugins.tools.keylistenerplugin;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface KeyListener {
    int KEY();

    int MODIFIERS();
}
