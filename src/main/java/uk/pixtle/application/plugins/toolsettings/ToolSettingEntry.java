package uk.pixtle.application.plugins.toolsettings;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.plugins.toolsettings.inputdevices.InputDevice;
import uk.pixtle.application.plugins.toolsettings.inputdevices.SliderInputDevice;

public abstract class ToolSettingEntry<T> {

    /*
    VALUE OF ENTRY
     */

    @Getter
    @Setter
    T value;

    public ToolSettingEntry() {}

    public void notifyVariableChange(T paramValue) {}

    /*
        Return true if valid, return false if not
         */
    public abstract boolean validateInput(T paramInput);

    /*
    JComponent methods
     */

    @Getter
    @Setter
    public String errorMessage;

    @Getter
    @Setter
    public Boolean showErrorMessage;

    public abstract String getTitle();

        /*
    Input device methods
     */

    public abstract InputDevice getInputDevice();

    public ToolSettingEntry(T paramDefaultValue) {
        this.setValue(paramDefaultValue);
    }
}
