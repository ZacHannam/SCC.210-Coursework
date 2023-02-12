package uk.pixtle.application.plugins.toolsettings.inputdevices;

import uk.pixtle.application.colour.ColourManager;
import uk.pixtle.application.plugins.toolsettings.ToolSettingEntry;
import uk.pixtle.application.ui.window.minitoollist.ColourButton;

import java.awt.*;

public abstract class ColourButtonInputDevice extends InputDevice {

    public ColourButtonInputDevice(ToolSettingEntry paramParentEntry) {
        super(paramParentEntry);
    }

    @Override
    public InputDeviceType getInputDeviceType() {
        return InputDeviceType.COLOUR_BUTTON;
    }

    public abstract Color defaultColour();
    public abstract ColourManager colourManager();
    public abstract void renderer(ColourButton paramColourButton);
}
