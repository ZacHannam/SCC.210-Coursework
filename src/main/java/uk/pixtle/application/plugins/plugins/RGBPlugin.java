package uk.pixtle.application.plugins.plugins;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.Application;
import uk.pixtle.application.colour.ColourManager;
import uk.pixtle.application.events.annotations.EventHandler;
import uk.pixtle.application.events.events.ColourChangeEvent;
import uk.pixtle.application.events.events.ExampleEvent;
import uk.pixtle.application.plugins.annotations.MenuBarItem;
import uk.pixtle.application.plugins.expansions.PluginMiniToolExpansion;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;
import uk.pixtle.application.ui.window.minitoollist.MiniToolPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class RGBPlugin extends Plugin implements PluginMiniToolExpansion{

    // ---------------------- ABSTRACT METHODS ----------------------

    @Override
    public String getToolIconLocation() {
        return null;
    }

    // ---------------------- TEST METHODS ----------------------



    @EventHandler
    public void test(ExampleEvent paramEvent) {
        System.out.println(paramEvent.getCreationTime().toString());
    }

    // ---------------------- MINI TOOL EXPANSION METHODS ----------------------

    @Getter
    @Setter
    MiniToolPanel miniToolPanel;
    ColourManager colourManager;
    TextField jTextField;
    Color colour;
    @Override
    public int getMiniToolPanelHeight() {
        return 25;
    }

    @Override
    public void instanceMiniToolPanel(MiniToolPanel paramMiniToolPanel) {
        this.setMiniToolPanel(paramMiniToolPanel);
        colourManager = super.getApplication().getColourManager();

        AnchoredComponent anchoredComponent = new AnchoredComponent();
        anchoredComponent.createAnchor(Anchor.DirectionType.X, 10);
        anchoredComponent.createAnchor(Anchor.DirectionType.X, -10);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, 10);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, -10);

        JLabel jLabel = new JLabel("RGB Plugin");
        jTextField = new TextField("RGB value", "RGB value");
        jLabel.setAutoscrolls(true);

        //paramMiniToolPanel.add(jLabel, anchoredComponent);
        BorderLayout rgbLayout = new BorderLayout();
        paramMiniToolPanel.setLayout(rgbLayout);
        paramMiniToolPanel.add(jLabel, BorderLayout.WEST);
        paramMiniToolPanel.add(jTextField, BorderLayout.CENTER);
        paramMiniToolPanel.setBackground(Color.LIGHT_GRAY);

        jTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {

            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if(validateRGB(jTextField.getText()))
                {
                    colourManager.setColorOfActiveColor(colour);
                }
            }

        });
    }

    // ---------------------- CONSTRUCTOR ----------------------

    public RGBPlugin(Application paramApplication){
        super(paramApplication);

        super.getApplication().getEventManager().registerEvents(this);
    }

    public boolean validateRGB(String input){
     String[] RGB_Sections = input.split("[,]", 0);
     int[] RGB_values=new int[3];
     for(int i=0;i<3;i++){
         int value=Integer.parseInt(RGB_Sections[i]);
         RGB_values[i]=value;
         if(value<0 || value>=256){
             return false;
         }
     }
     colour=new Color(RGB_values[0],RGB_values[1],RGB_values[2]);
     return true;
    }

    @EventHandler
    public void colourChangeEvent(ColourChangeEvent event){
        Color currentColour = colourManager.getColor1();
        int R = currentColour.getRed();
        int G = currentColour.getGreen();
        int B = currentColour.getBlue();

        jTextField.setText(R+","+G+","+B);
    }
}
