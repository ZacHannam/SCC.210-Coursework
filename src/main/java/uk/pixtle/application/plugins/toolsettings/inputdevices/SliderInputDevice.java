package uk.pixtle.application.plugins.toolsettings.inputdevices;

import uk.pixtle.application.plugins.toolsettings.ToolSettingEntry;

public abstract class SliderInputDevice extends InputDevice {

    public SliderInputDevice(ToolSettingEntry paramParentEntry) {
        super(paramParentEntry);
    }

    @Override
    public InputDeviceType getInputDeviceType() {
        return InputDeviceType.SLIDER;
    }

    public abstract int getMinValue();
    public abstract int getMaxValue();
}
