package uk.pixtle.application.plugins.plugins.tools;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.Application;
import uk.pixtle.application.events.annotations.EventHandler;
import uk.pixtle.application.events.events.ColourChangeEvent;
import uk.pixtle.application.plugins.expansions.PluginDrawableExpansion;
import uk.pixtle.application.plugins.plugins.Plugin;
import uk.pixtle.application.plugins.plugins.canvas.drawing.Drawing;
import uk.pixtle.application.plugins.toolsettings.ToolSetting;
import uk.pixtle.application.plugins.toolsettings.ToolSettingEntry;
import uk.pixtle.application.plugins.expansions.PluginToolExpansion;
import uk.pixtle.application.plugins.toolsettings.inputdevices.DropDownInputDevice;
import uk.pixtle.application.plugins.toolsettings.inputdevices.InputDevice;
import uk.pixtle.application.plugins.toolsettings.inputdevices.SliderInputDevice;

import javax.swing.*;
import java.awt.*;

public class BrushToolPlugin extends ToolPlugin implements PluginToolExpansion, PluginDrawableExpansion {

    @Override
    public String getIconFilePath() {
        return "Brush-0003.png";
    }

    @ToolSetting
    private ToolSettingEntry<Integer> brushSize = new ToolSettingEntry<Integer>(75){

        @Override
        public void notifyVariableChange(Integer paramVar) {
            renderDrawing();
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
                    return 200;
                }

                @Override
                public void renderer(JSlider paramSlider) {

                }

                @Override
                public boolean paintCurrentValue() {
                    return true;
                }
            };
        }
    };

    @ToolSetting
    private ToolSettingEntry<String> brushType = new ToolSettingEntry<String>(){

        @Override
        public void notifyVariableChange(String paramVar) {
            renderDrawing();
        }

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


    @EventHandler
    public void onColourChange(ColourChangeEvent event) {
        this.renderDrawing();
    }

    @Getter
    @Setter
    Drawing renderedDrawing;

    private void renderDrawing() {
        System.out.println("Render drawing");

        if(!this.isPluginActive()) return;

        Drawing drawing = new Drawing(brushSize.getValue() - 1, brushSize.getValue() - 1);

        switch(brushType.getValue()) {
            case "Sphere":

                int centreX = drawing.getWidth() / 2;
                int centreY = drawing.getHeight() / 2;

                for(int i = 0; i < drawing.getHeight(); i++) {
                    for(int j = 0; j < drawing.getWidth(); j++) {
                        if(Math.sqrt(Math.pow((centreX - j), 2) + Math.pow((centreY - i), 2)) <= centreY) {
                            drawing.setColor(j, i, super.getApplication().getColourManager().getActiveColor());
                        }
                    }
                }

                break;
            case "Block":

                for(int i = 0; i < drawing.getHeight(); i++) {
                    for(int j = 0; j < drawing.getWidth(); j++) {
                        drawing.setColor(j, i, super.getApplication().getColourManager().getActiveColor());
                    }
                }

                break;
            default:
                break;
        }


        this.setRenderedDrawing(drawing);

    }

    @Override
    public void mouseCanvasEvent(int paramCalculatedX, int paramCalculatedY,  int paramDifferenceX, int paramDifferenceY) {

        if(this.getRenderedDrawing() == null) {
            renderDrawing();
        }
        super.getApplication().getPluginManager().getActiveCanvasPlugin().printImageOnCanvas(paramCalculatedX - (int) (brushSize.getValue() / 2.0), paramCalculatedY - (int) (brushSize.getValue() / 2.0), this.getRenderedDrawing());
    }

    public BrushToolPlugin(Application paramApplication) {
        super(paramApplication);

        super.getApplication().getEventManager().registerEvents(this);
    }
}
