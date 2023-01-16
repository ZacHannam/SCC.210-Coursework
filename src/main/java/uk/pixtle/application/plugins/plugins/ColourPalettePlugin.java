package uk.pixtle.application.plugins.plugins;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.Application;
import uk.pixtle.application.colour.ColourManager;
import uk.pixtle.application.events.annotations.EventHandler;
import uk.pixtle.application.events.events.ExampleEvent;
import uk.pixtle.application.plugins.expansions.PluginMiniToolExpansion;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;
import uk.pixtle.application.ui.window.minitoollist.MiniToolPanel;
import uk.pixtle.application.ui.window.minitoollist.PaletteButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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

    ColourManager colourManager;
    PaletteButton colourButtons[] = new PaletteButton[9];
    @Override
    public int getMiniToolPanelHeight() {
        return 150;
    }

    @Override
    public void instanceMiniToolPanel(MiniToolPanel paramMiniToolPanel) {
        this.setMiniToolPanel(paramMiniToolPanel);
        this.colourManager = super.getApplication().getColourManager();

        AnchoredComponent anchoredComponent = new AnchoredComponent();
        anchoredComponent.createAnchor(Anchor.DirectionType.X, 10);
        anchoredComponent.createAnchor(Anchor.DirectionType.X, -10);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, 10);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, -10);

        GridLayout colourPalettLayout = new GridLayout(0,2);

        paramMiniToolPanel.setLayout(colourPalettLayout);

        for(int i=0;i<8;i++)
        {
            colourButtons[i] = new PaletteButton();
            colourButtons[i].setCurrentColour(Color.BLUE);
            colourButtons[i].setBackground(colourButtons[i].getCurrentColour());
            paramMiniToolPanel.add(colourButtons[i]);
        }


        colourButtons[4].setCurrentColour(Color.CYAN);
        colourButtons[4].setBackground(colourButtons[4].getCurrentColour());


        for(int i=0;i<8;i++) {
            int finalI = i;
            colourButtons[i].addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {

                }

                @Override
                public void mousePressed(MouseEvent mouseEvent) {
                    if(SwingUtilities.isLeftMouseButton(mouseEvent))
                        colourManager.setColorOfActiveColor(colourButtons[finalI].getCurrentColour());
                    if(SwingUtilities.isRightMouseButton(mouseEvent))
                    {
                        colourButtons[finalI].setCurrentColour(colourManager.getColor1());

                    }
                }

                @Override
                public void mouseReleased(MouseEvent mouseEvent) {

                }

                @Override
                public void mouseEntered(MouseEvent mouseEvent) {

                }

                @Override
                public void mouseExited(MouseEvent mouseEvent) {

                }
            });
        }



        paramMiniToolPanel.setBackground(Color.WHITE);
    }

    // ---------------------- CONSTRUCTOR ----------------------

    public ColourPalettePlugin(Application paramApplication) {
        super(paramApplication);

        super.getApplication().getEventManager().registerEvents(this);
    }

}
