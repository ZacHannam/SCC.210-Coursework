package uk.pixtle.application.ui.window.color;

import uk.pixtle.application.Application;
import uk.pixtle.application.colour.ColourSlots;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;
import uk.pixtle.application.ui.window.WindowItem;

import javax.swing.*;
import java.awt.*;

public class ColorPanelUI extends JPanel implements ColorPanel {

    @Override
    public AnchoredComponent getAnchors() {

        AnchoredComponent anchoredComponent = new AnchoredComponent();
        anchoredComponent.createAnchor(Anchor.DirectionType.X, 0);
        anchoredComponent.createAnchor(Anchor.DirectionType.X, 100);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, -100);
        anchoredComponent.createAnchor(AnchoredComponent.StandardY.BOTTOM);
        return anchoredComponent;
    }

    JButton button1;

    public void setButton1Colour(Color color) {
        button1.setBackground(color);
    }
    JButton button2;

    public void setButton2Colour(Color color) {
        button2.setBackground(color);
    }


    public ColorPanelUI(Application paramApplication) {
        super.setBackground(Color.white);


        setLayout(new BorderLayout());
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0,0,100,100);

        button1 = new JButton();
        button1.setOpaque(true);
        button1.setPreferredSize(new Dimension(50,50));
        button1.setBounds(15,15,50,50);
        layeredPane.add(button1, Integer.valueOf(1));
        button1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        button1.setBorder(BorderFactory.createEtchedBorder());
        button1.setBackground(Color.RED);
        button1.setOpaque(true);

        button2 = new JButton();
        button2.setOpaque(true);
        button2.setPreferredSize(new Dimension(50,50));
        button2.setBounds(40,40,50,50);
        layeredPane.add(button2, Integer.valueOf(2));
        button2.setBackground(Color.white);
        button2.setOpaque(true);
        button2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));


        this.add(layeredPane);


        button1.addActionListener(e -> {
            layeredPane.setLayer(button1,3,0);
            paramApplication.getColourManager().setActiveColorSlot(ColourSlots.COLOUR_1);

        });

        button2.addActionListener(e -> {
            layeredPane.setLayer(button2,3,0);
            paramApplication.getColourManager().setActiveColorSlot(ColourSlots.COLOUR_2);

        });
    }
}
