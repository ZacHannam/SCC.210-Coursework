package uk.pixtle.application.plugins.plugins.tools;
import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.Application;
import uk.pixtle.application.events.annotations.EventHandler;
import uk.pixtle.application.events.events.ColourChangeEvent;
import uk.pixtle.application.plugins.expansions.PluginDrawableExpansion;
import uk.pixtle.application.plugins.plugins.Plugin;
import uk.pixtle.application.plugins.plugins.canvas.drawing.Drawing;
import uk.pixtle.application.plugins.plugins.tools.ToolPlugin;
import uk.pixtle.application.plugins.plugins.tools.keylistenerplugin.KeyListener;
import uk.pixtle.application.plugins.plugins.tools.keylistenerplugin.PluginKeyListenerPolicy;
import uk.pixtle.application.plugins.toolsettings.ToolSetting;
import uk.pixtle.application.plugins.toolsettings.ToolSettingEntry;
import uk.pixtle.application.plugins.expansions.PluginToolExpansion;
import uk.pixtle.application.plugins.toolsettings.inputdevices.DropDownInputDevice;
import uk.pixtle.application.plugins.toolsettings.inputdevices.InputDevice;
import uk.pixtle.application.plugins.toolsettings.inputdevices.SliderInputDevice;
import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.KeyEvent;

public class PenToolPlugin extends ToolPlugin implements PluginToolExpansion, PluginDrawableExpansion, PluginKeyListenerPolicy {


    @Override
    public String getIconFilePath() {
        return "Pen-0001.png";
    }

    @KeyListener(KEY = KeyEvent.VK_P, MODIFIERS = 0)
    public void pen() {super.getApplication().getPluginManager().activatePlugin(this);}

    @ToolSetting
    private ToolSettingEntry<Integer> penSize = new ToolSettingEntry<Integer>(75){

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
            return "Pen Size";
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
    private ToolSettingEntry<Integer> penOpacity = new ToolSettingEntry<Integer>(100){

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
            return "Pen Opacity";
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

        Drawing drawing = new Drawing(penSize.getValue(), penSize.getValue());
        int x0 = 0;
        int y0 = 0;
        int x1 = drawing.getWidth() - 1;
        int y1 = drawing.getHeight() - 1;
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;
        while (true) {
            Color color = super.getApplication().getColourManager().getActiveColor();
            float opacity = (float) this.penOpacity.getValue() / 100;
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if(x0 + i >= penSize.getValue()|| y0 + j >= penSize.getValue()|| x0 + i <= 0|| y0 + j <= 0){
                        continue;
                    }
                    drawing.setColor(x0 + i, y0 + j, color, opacity);
                }
            }
            if (x0 == x1 && y0 == y1) {
                break;
            }
            int e2 = 2 * err;
            if (e2 > -dy) {
                err = err - dy;
                x0 = x0 + sx;
            }
            if (e2 < dx) {
                err = err + dx;
                y0 = y0 + sy;
            }
        }
        this.setRenderedDrawing(drawing);
        this.changed = true;
    }

    int lastX = -1, lastY = -1;
    boolean changed = true;

    // private Object brushOpacity;
    @Override
    public void mouseCanvasEvent(int paramCalculatedX, int paramCalculatedY,  int paramDifferenceX, int paramDifferenceY) {

        if(this.getRenderedDrawing() == null) {
            renderDrawing();
        }
        if(paramCalculatedX != lastX || paramCalculatedY != lastY || changed) {
            int x = paramCalculatedX, y = paramCalculatedY;
            super.getApplication().getPluginManager().getActiveCanvasPlugin().printImageOnCanvas(x, y,this.getRenderedDrawing(),true);
            //printpointOnCanvas(x, y, this.getRenderedDrawing(), true);
            lastX = paramCalculatedX;
            lastY = paramCalculatedY;
            changed = false;
        }
    }
    public PenToolPlugin(Application paramApplication) {
        super(paramApplication);
        super.getApplication().getEventManager().registerEvents(this);
    }
}

