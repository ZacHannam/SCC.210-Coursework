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
        return 150;
    }

    @Override
    public void instanceMiniToolPanel(MiniToolPanel paramMiniToolPanel) {
        this.setMiniToolPanel(paramMiniToolPanel);

        AnchoredComponent anchoredComponent = new AnchoredComponent();
        anchoredComponent.createAnchor(Anchor.DirectionType.X, 10);
        anchoredComponent.createAnchor(Anchor.DirectionType.X, -10);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, 10);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, -10);

        GridLayout colourPalettLayout = new GridLayout(0,2);

        paramMiniToolPanel.setLayout(colourPalettLayout);

        JButton colourButtons[] = new JButton[9];
        for(int i=0;i<8;i++)
            colourButtons[i] = new JButton();

        for(int i=0;i<8;i++)
            colourButtons[i].setBackground(Color.BLACK);

        for(int i=0;i<8;i++)
            paramMiniToolPanel.add(colourButtons[i]);





        paramMiniToolPanel.setBackground(Color.WHITE);
    }

    // ---------------------- CONSTRUCTOR ----------------------

    public ColourPalettePlugin(Application paramApplication) {
        super(paramApplication);

        super.getApplication().getEventManager().registerEvents(this);
    }

}
