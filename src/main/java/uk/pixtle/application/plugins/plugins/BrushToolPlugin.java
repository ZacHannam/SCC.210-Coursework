package uk.pixtle.application.plugins.plugins;

import uk.pixtle.application.Application;
import uk.pixtle.application.plugins.toolsettings.ToolSetting;
import uk.pixtle.application.plugins.toolsettings.ToolSettingEntry;
import uk.pixtle.application.plugins.expansions.PluginToolExpansion;
import uk.pixtle.application.plugins.toolsettings.inputdevices.DropDownInputDevice;
import uk.pixtle.application.plugins.toolsettings.inputdevices.InputDevice;
import uk.pixtle.application.plugins.toolsettings.inputdevices.SliderInputDevice;

public class BrushToolPlugin extends Plugin implements PluginToolExpansion {

    @ToolSetting
    private ToolSettingEntry<Integer> brushSize = new ToolSettingEntry<Integer>(75){

        @Override
        public void notifyVariableChange(Integer paramVar) {
            //ZZZ: ((CanvasUI) getApplication().getUIManager().getWindow().getCanvas()).updateZoom(((paramVar - 50) * 0.01) + 1);
        }

        @Override
        public boolean validateInput(Integer paramInput) {
            return true;
        }

        @Override
        public String getTitle() {
            return "Brush Size";
        }

        @Override
        public InputDevice getInputDevice() {
            return new SliderInputDevice(this) {

                @Override
                public int getMinValue() {
                    return 1;
                }

                @Override
                public int getMaxValue() {
                    return 100;
                }
            };
        }
    };

    @ToolSetting
    private ToolSettingEntry<String> brushType = new ToolSettingEntry<String>(){

        @Override
        public boolean validateInput(String paramInput) {
            return true;
        }

        @Override
        public String getTitle() {
            return "Brush Type";
        }

        @Override
        public InputDevice getInputDevice() {
            return new DropDownInputDevice(this) {

                @Override
                public String[] getValues() {
                    return new String[]{"Sphere", "Block"};
                }
            };
        }
    };

    public BrushToolPlugin(Application paramApplication) {
        super(paramApplication);
    }

    @Override
    public String getIconFilePath() {
        return "icons/brush.png";
    }

}
