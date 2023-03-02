package uk.pixtle.application.plugins.plugins.tools.colourplugin;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.Application;
import uk.pixtle.application.events.annotations.EventHandler;
import uk.pixtle.application.events.events.ColourChangeEvent;
import uk.pixtle.application.plugins.expansions.PluginDrawableExpansion;
import uk.pixtle.application.plugins.expansions.PluginToolExpansion;
import uk.pixtle.application.plugins.expansions.PluginToolTipExpansion;
import uk.pixtle.application.plugins.plugins.canvas.drawing.Drawing;
import uk.pixtle.application.plugins.plugins.tools.ToolPlugin;
import uk.pixtle.application.plugins.plugins.tools.keylistenerplugin.KeyListener;
import uk.pixtle.application.plugins.plugins.tools.keylistenerplugin.PluginKeyListenerPolicy;
import uk.pixtle.application.plugins.toolsettings.ToolSetting;
import uk.pixtle.application.plugins.toolsettings.ToolSettingEntry;
import uk.pixtle.application.plugins.toolsettings.inputdevices.InputDevice;
import uk.pixtle.application.plugins.toolsettings.inputdevices.SliderInputDevice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class EraserToolPlugin extends ToolPlugin implements PluginToolExpansion, PluginDrawableExpansion, PluginKeyListenerPolicy, PluginToolTipExpansion {

    @Override
    public String getIconFilePath() {
        return "Eraser-0001.png";
    }

    @KeyListener(KEY = KeyEvent.VK_E, MODIFIERS = 0)
    public void eraser() {super.getApplication().getPluginManager().activatePlugin(this);}
    @ToolSetting
    private ToolSettingEntry<Integer> eraserSize = new ToolSettingEntry<Integer>(30){

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
            return "Eraser Size";
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

    @EventHandler
    public void onColourChange(ColourChangeEvent event) {
        this.renderDrawing();
    }

    @Override
    public void onEnable() {
        this.renderDrawing();
    }

    private void renderDrawing() {
        if(!this.isPluginActive()) return;
        Drawing drawing = new Drawing(eraserSize.getValue(), eraserSize.getValue());
        int centreX = drawing.getWidth() / 2;
        int centreY = drawing.getHeight() / 2;

        for(int i = 0; i < drawing.getHeight(); i++) {
            for(int j = 0; j < drawing.getWidth(); j++) {
                if(Math.sqrt(Math.pow((centreX - j), 2) + Math.pow((centreY - i), 2)) <= centreY) {
                    drawing.setColor(j, i, Color.WHITE,0f);
                }
            }
        }
        for(int i = 0; i < drawing.getHeight(); i++) {
            for(int j = 0; j < drawing.getWidth(); j++) {
                drawing.setColor(j, i, Color.WHITE,0f);
            }
        }
        this.setRenderedDrawing(drawing);
        //this.changed = true;
    }

    @Getter
    @Setter
    Drawing renderedDrawing;
    @Override
    public void mouseCanvasEvent(int paramCalculatedX, int paramCalculatedY, int paramDifferenceX, int paramDifferenceY) {
        if (this.getRenderedDrawing() == null) {
            renderDrawing();
            //changed = true;
        }
        int x = paramCalculatedX, y = paramCalculatedY;
        super.getApplication().getPluginManager().getActiveCanvasPlugin().printImageOnCanvas(x, y, this.getRenderedDrawing(), true);
    }

    public EraserToolPlugin(Application paramApplication) {
        super(paramApplication);
        super.getApplication().getEventManager().registerEvents(this);
    }

    @Override
    public String getToolTip() {
        return "Eraser Tool (E)";
    }
}