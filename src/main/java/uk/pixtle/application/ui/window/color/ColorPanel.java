package uk.pixtle.application.ui.window.color;

import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;
import uk.pixtle.application.ui.window.WindowItem;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ColorPanel extends JPanel implements WindowItem {

    @Override
    public AnchoredComponent getAnchors() {

        AnchoredComponent anchoredComponent = new AnchoredComponent();
        anchoredComponent.createAnchor(Anchor.DirectionType.X, 20);
        anchoredComponent.createAnchor(Anchor.DirectionType.X, 80);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, 930);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, 970);

        return anchoredComponent;
    }

    public ColorPanel() {
        super.setBackground(Color.white);

        // creat JToggleButton
        JToggleButton button = new JToggleButton();

        // set the image in
        Icon onIcon = new ImageIcon("OffNew.jpg");
        Icon offIcon = new ImageIcon("OnNew.jpg");

        // set icon and margin
        button.setMargin(new Insets(0, 0, 0, 0));

        // set button on/off
        button.setIcon(offIcon);
        button.setSelectedIcon(onIcon);

        this.add(button);

        // get the width and height
        int containerWidth = this.getWidth();
        int containerHeight = this.getHeight();
        // calculate JPanel coordiante if you modify images you should change here
        int panelWidth = 60;
        int panelHeight = 30;
        int x = (containerWidth - panelWidth)/2;
        int y = (containerHeight - panelHeight)/2;

        // set button position and size
        button.setBounds(x, y, panelWidth, panelHeight);
        //button.setSize(panelHeight, panelWidth);
        // add ItemListener for button
        button.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    System.out.println("Button is ON");
                }
                else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    System.out.println("Button is OFF");
                }
            }
        });

    }
}
