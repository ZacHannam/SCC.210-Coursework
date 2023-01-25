package uk.pixtle.application.plugins.plugins;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.Application;
import uk.pixtle.application.events.annotations.EventHandler;
import uk.pixtle.application.events.events.ExampleEvent;
import uk.pixtle.application.plugins.annotations.MenuBarItem;
import uk.pixtle.application.plugins.expansions.PluginMiniToolExpansion;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;
import uk.pixtle.application.ui.window.minitoollist.MiniToolPanel;

import javax.swing.*;
import java.awt.*;

public class ColourPreviewPlugin extends Plugin implements PluginMiniToolExpansion{

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
        return 100;
    }

    @Override
    public void instanceMiniToolPanel(MiniToolPanel paramMiniToolPanel) {
        this.setMiniToolPanel(paramMiniToolPanel);

        AnchoredComponent anchoredComponent = new AnchoredComponent();
        anchoredComponent.createAnchor(Anchor.DirectionType.X, 10);
        anchoredComponent.createAnchor(Anchor.DirectionType.X, -10);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, 10);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, -10);

        JLabel jLabel = new JLabel("Colour Preview");
        jLabel.setAutoscrolls(true);

        paramMiniToolPanel.add(jLabel, anchoredComponent);

        paramMiniToolPanel.setBackground(Color.WHITE);
    }
    // ---------------------- OWN METHODS ----------------------
    public void setColour(Color colour){ //method for updating the colour
        this.getMiniToolPanel().setBackground(colour);
    }
    // ---------------------- CONSTRUCTOR ----------------------

    public ColourPreviewPlugin(Application paramApplication){
        super(paramApplication);

        super.getApplication().getEventManager().registerEvents(this);
    }
}
