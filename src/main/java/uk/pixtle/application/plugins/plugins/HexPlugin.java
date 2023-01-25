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
import uk.pixtle.application.plugins.expansions.PluginToolExpansion;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;
import uk.pixtle.application.ui.window.minitoollist.MiniToolPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class HexPlugin extends Plugin implements PluginMiniToolExpansion {

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

        //Font f = new Font(Font.SERIF, Font.BOLD, 12);

        JLabel jLabel = new JLabel("#"); //Add action listner for text appearing
        //jLabel.setFont(f);
        //JTextField jTextField = new JTextField("Hex value");
        jTextField = new TextField("Hex value", "Hex value");
        //jLabel.setAutoscrolls(true);

        //paramMiniToolPanel.add(jLabel, anchoredComponent);
        //paramMiniToolPanel.add(jTextField, anchoredComponent);
        BorderLayout hexLayout = new BorderLayout();
        paramMiniToolPanel.setLayout(hexLayout);
        paramMiniToolPanel.add(jLabel, BorderLayout.WEST);
        paramMiniToolPanel.add(jTextField, BorderLayout.CENTER);
        paramMiniToolPanel.setBackground(Color.LIGHT_GRAY);

        jTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                jTextField.setForeground(Color.BLACK);
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if(validate(jTextField.getText()))
                {
                    colourManager.setColorOfActiveColor(colour);
                }
                else{
                    if(!jTextField.getText().equals("Hex value")) {
                        jTextField.setForeground(Color.RED);
                    }
                }
            }

        });
    }

    // ---------------------- CONSTRUCTOR ----------------------

    public HexPlugin(Application paramApplication){
        super(paramApplication);
        super.getApplication().getEventManager().registerEvents(this);
    }

    public boolean validate(String input){
        input=input.trim(); //Gets rid of leading and trailing spaces.
        input=input.replace("#", ""); //Gets rid of any hashtags, as we put one in anyway.
        if(input.length()>6){
            return false;
        }
        try {
            colour=Color.decode('#' + input);
        }catch(NumberFormatException e){
            return false;
        }
        return true;
    }

    private String toHexString(int rgb) {

        StringBuilder s = new StringBuilder(Integer.toHexString(rgb));

        if(s.toString().length() == 1) {
            s.insert(0, "0");
        }

        return s.toString();
    }

    @EventHandler
    public void colourChangeEvent(ColourChangeEvent event){
        Color currentColour = colourManager.getColor1();
        int r = currentColour.getRed();
        int g = currentColour.getGreen();
        int b = currentColour.getBlue();

        String Hex_rgb = toHexString(r) + toHexString(g)  + toHexString(b);
        jTextField.setText(Hex_rgb.toUpperCase());
    }
}
