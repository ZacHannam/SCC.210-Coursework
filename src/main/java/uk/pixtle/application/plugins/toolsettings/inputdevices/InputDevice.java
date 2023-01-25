package uk.pixtle.application.plugins.toolsettings.inputdevices;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.plugins.toolsettings.ToolSettingEntry;

public abstract class InputDevice {

    public enum InputDeviceType {
        SLIDER,
        DROP_DOWN;
    }

    @Getter
    @Setter
    private ToolSettingEntry toolSettingEntry;

    public abstract InputDeviceType getInputDeviceType();

    public InputDevice(ToolSettingEntry paramParentEntry) {
        this.setToolSettingEntry(paramParentEntry);
    }

}
