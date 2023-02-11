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
    private ToolSettingEntry<Integer> brushSize = new ToolSettingEntry<Integer>(25){

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
    private ToolSettingEntry<Integer> brushOpacity = new ToolSettingEntry<Integer>(100){

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
            return "Brush Opacity";
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
                    return new String[]{"Sphere", "Block", "Triangle", "Crown", "Diamond"};
                }
            };
        }
    };


    @EventHandler
    public void onColourChange(ColourChangeEvent event) {
        this.renderDrawing();
    }

    @Override
    public void onEnable() {
        this.renderDrawing();
    }

    @Getter
    @Setter
    Drawing renderedDrawing;

    private void renderDrawing() {
        if(!this.isPluginActive()) return;

        Drawing drawing = new Drawing(brushSize.getValue(), brushSize.getValue());

        switch(brushType.getValue()) {
            case "Sphere":

                int centreX = drawing.getWidth() / 2;
                int centreY = drawing.getHeight() / 2;

                for(int i = 0; i < drawing.getHeight(); i++) {
                    for(int j = 0; j < drawing.getWidth(); j++) {
                        if(Math.sqrt(Math.pow((centreX - j), 2) + Math.pow((centreY - i), 2)) <= centreY) {
                            drawing.setColor(j, i, super.getApplication().getColourManager().getActiveColor(), (float) this.brushOpacity.getValue() / 100);
                        }
                    }
                }

                break;
            case "Block":

                for(int i = 0; i < drawing.getHeight(); i++) {
                    for(int j = 0; j < drawing.getWidth(); j++) {
                        drawing.setColor(j, i, super.getApplication().getColourManager().getActiveColor(), (float) this.brushOpacity.getValue() / 100);
                    }
                }

                break;

            case "Triangle":
                int start = 0;
                int end = drawing.getWidth();

                for(int i = drawing.getHeight()-2; i>=0; i=i-2){

                    for(int j = start; j<end; j++){
                        drawing.setColor(j, i, super.getApplication().getColourManager().getActiveColor(), (float) this.brushOpacity.getValue() / 100);
                        int next = i+1;
                        drawing.setColor(j, next, super.getApplication().getColourManager().getActiveColor(), (float) this.brushOpacity.getValue() / 100);

                    }
                    start = start + 1;
                    end = end -1;
                }
                break;
            case "Crown":
                int start3 = drawing.getWidth()/2;
                int end3 = drawing.getWidth()/2;
                for(int i =drawing.getHeight()/2-1; i>=0; i--){
                    for(int j = start3; j >=0; j--){
                        drawing.setColor(j, i, super.getApplication().getColourManager().getActiveColor(), (float) this.brushOpacity.getValue() / 100);
                    }
                    for(int j = end3; j < drawing.getWidth(); j++){
                        drawing.setColor(j, i, super.getApplication().getColourManager().getActiveColor(), (float) this.brushOpacity.getValue() / 100);
                    }
                    start3 --;
                    end3++;
                }
                int start2 = 0;
                int end2 = drawing.getWidth();

                for(int i = drawing.getHeight()/2; i>=0; i=i-1){

                    for(int j = start2; j<end2; j++){
                        drawing.setColor(j, i, super.getApplication().getColourManager().getActiveColor(), (float) this.brushOpacity.getValue() / 100);
                        int next = i+1;
                        drawing.setColor(j, next, super.getApplication().getColourManager().getActiveColor(), (float) this.brushOpacity.getValue() / 100);

                    }
                    start2 = start2 + 1;
                    end2 = end2 -1;

                }
                break;

            case "Diamond":
                int start4 = 0;
                int end4 = drawing.getWidth();

                for(int i = drawing.getHeight()/2-1; i>=0; i=i-1){

                    for(int j = start4; j<end4; j++){
                        drawing.setColor(j, i, super.getApplication().getColourManager().getActiveColor(), (float) this.brushOpacity.getValue() / 100);
                        int next = i+1;
                        drawing.setColor(j, next, super.getApplication().getColourManager().getActiveColor(), (float) this.brushOpacity.getValue() / 100);

                    }
                    start4 = start4 + 1;
                    end4 = end4 -1;

                }
                int start5 = 0;
                int end5 = drawing.getWidth();
                for(int i = drawing.getHeight()/2+1; i< drawing.getHeight()-1; i++){
                    for(int j = start5; j<end5; j++){
                        drawing.setColor(j, i, super.getApplication().getColourManager().getActiveColor(), (float) this.brushOpacity.getValue() / 100);
                        int next = i+1;
                        drawing.setColor(j, next, super.getApplication().getColourManager().getActiveColor(), (float) this.brushOpacity.getValue() / 100);

                    }
                    start5 = start5 + 1;
                    end5 = end5 -1;

                }
            default:
                break;
        }


        this.setRenderedDrawing(drawing);
        this.changed = true;

    }

    int lastX = -1, lastY = -1;
    boolean changed = true;
    @Override
    public void mouseCanvasEvent(int paramCalculatedX, int paramCalculatedY,  int paramDifferenceX, int paramDifferenceY) {

        if(this.getRenderedDrawing() == null) {
            renderDrawing();
        }

        if(paramCalculatedX != lastX || paramCalculatedY != lastY || changed) {
            int x = paramCalculatedX, y = paramCalculatedY;

            super.getApplication().getPluginManager().getActiveCanvasPlugin().printImageOnCanvas(x, y, this.getRenderedDrawing(), true);

            lastX = paramCalculatedX;
            lastY = paramCalculatedY;
            changed = false;
        }

    }

    public BrushToolPlugin(Application paramApplication) {
        super(paramApplication);

        super.getApplication().getEventManager().registerEvents(this);
    }
}
