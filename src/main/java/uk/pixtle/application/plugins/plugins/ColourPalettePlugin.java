package uk.pixtle.application.plugins.plugins;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.Application;
import uk.pixtle.application.events.annotations.EventHandler;
import uk.pixtle.application.events.events.ExampleEvent;
import uk.pixtle.application.plugins.expansions.PluginMiniToolExpansion;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;
import uk.pixtle.application.ui.window.minitoollist.MiniToolPanel;

import javax.swing.*;
import java.awt.*;

public class ColourPalettePlugin extends Plugin implements PluginMiniToolExpansion {

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

    @Override
    public int getMiniToolPanelHeight() {
        return 400;
    }

    @Override
    public void instanceMiniToolPanel(MiniToolPanel paramMiniToolPanel) {
        this.setMiniToolPanel(paramMiniToolPanel);

        AnchoredComponent anchoredComponent = new AnchoredComponent();
        anchoredComponent.createAnchor(Anchor.DirectionType.X, 10);
        anchoredComponent.createAnchor(Anchor.DirectionType.X, -10);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, 10);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, -10);

        //JLabel jLabel = new JLabel("Colour Palette");
        //jLabel.setAutoscrolls(true);

        GridLayout colourPalettLayout = new GridLayout(0,2);

        paramMiniToolPanel.setLayout(colourPalettLayout);

        JButton jb1 = new JButton("Button 1");
        JButton jb2 = new JButton("Button 2");
        JButton jb3 = new JButton("Button 3");
        JButton jb4 = new JButton("Button 4");
        JButton jb5 = new JButton("Button 5");
        JButton jb6 = new JButton("Button 6");

        paramMiniToolPanel.add(jb1);
        paramMiniToolPanel.add(jb2);
        paramMiniToolPanel.add(jb3);
        paramMiniToolPanel.add(jb4);
        paramMiniToolPanel.add(jb5);
        paramMiniToolPanel.add(jb6);


        //paramMiniToolPanel.add(jLabel, anchoredComponent);

        paramMiniToolPanel.setBackground(Color.WHITE);
    }

    // ---------------------- CONSTRUCTOR ----------------------

    public ColourPalettePlugin(Application paramApplication) {
        super(paramApplication);

        super.getApplication().getEventManager().registerEvents(this);
    }

}
