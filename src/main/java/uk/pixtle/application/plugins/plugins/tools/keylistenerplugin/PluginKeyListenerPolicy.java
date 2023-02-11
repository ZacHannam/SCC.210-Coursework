package uk.pixtle.application.plugins.plugins.tools.keylistenerplugin;

import uk.pixtle.application.plugins.policies.PluginPolicy;

import javax.swing.*;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@interface KeyListener {
    int KEY();
    int MODIFIERS();
}

public interface PluginKeyListenerPolicy extends PluginPolicy {
}
