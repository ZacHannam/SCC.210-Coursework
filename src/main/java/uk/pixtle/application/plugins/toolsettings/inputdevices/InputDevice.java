package uk.pixtle.application.plugins.toolsettings.inputdevices;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.plugins.toolsettings.ToolSettingEntry;

public abstract class InputDevice {

    public enum InputDeviceType {
        SLIDER,
        TEXT_AREA,
        DROP_DOWN,
        COLOUR_BUTTON;
    }

    @Getter
    @Setter
    private ToolSettingEntry toolSettingEntry;

    public abstract InputDeviceType getInputDeviceType();

    public InputDevice(ToolSettingEntry paramParentEntry) {
        this.setToolSettingEntry(paramParentEntry);
    }

}
