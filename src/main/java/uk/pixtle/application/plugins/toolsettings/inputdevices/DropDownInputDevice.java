package uk.pixtle.application.plugins.toolsettings.inputdevices;

import uk.pixtle.application.plugins.toolsettings.ToolSettingEntry;

import javax.swing.*;
import java.util.List;

public abstract class DropDownInputDevice extends InputDevice {

    @Override
    public InputDeviceType getInputDeviceType() {
        return InputDeviceType.DROP_DOWN;
    }

    public DropDownInputDevice(ToolSettingEntry paramParentEntry) {
        super(paramParentEntry);
    }

    public abstract String[] getValues();
    public abstract void renderer(JComboBox<String> paramComboBox);
}
