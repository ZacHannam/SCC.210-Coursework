package uk.pixtle.application.plugins.toolsettings.inputdevices;

import uk.pixtle.application.plugins.toolsettings.ToolSettingEntry;

import javax.swing.*;

public abstract class TextAreaInputDevice extends InputDevice {
    public TextAreaInputDevice(ToolSettingEntry paramParentEntry) {
        super(paramParentEntry);
    }

    @Override
    public InputDeviceType getInputDeviceType() {
        return InputDeviceType.TEXT_AREA;
    }

    public abstract String defaultText();
    public abstract void renderer(JTextArea textArea);

}
