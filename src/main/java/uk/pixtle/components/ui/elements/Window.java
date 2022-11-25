package uk.pixtle.components.ui.elements;

import uk.pixtle.components.ui.elements.color.ColorPanel;
import uk.pixtle.components.ui.elements.menubar.MenuBar;
import uk.pixtle.components.ui.elements.toollist.ToolList;
import uk.pixtle.components.ui.layouts.anchorlayout.AnchorLayout;
import uk.pixtle.components.ui.elements.minitoollist.MiniToolList;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    public Window() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // full screen size
        Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

        // set the size
        super.setSize(rect.getSize());

        //--------------------------------------------------------------

        AnchorLayout anchorLayout = new AnchorLayout(false, false);
        this.setLayout(anchorLayout);

        //--------------------------------------------------------------

        ToolList toolList = new ToolList();
        super.add(toolList, toolList.getAnchors());

        ColorPanel colorPanel = new ColorPanel();
        super.add(colorPanel, colorPanel.getAnchors());

        MiniToolList miniToolList = new MiniToolList();
        super.add(miniToolList, miniToolList.getAnchors());

        uk.pixtle.components.ui.elements.menubar.MenuBar menuBar = new MenuBar();
        super.add(menuBar, menuBar.getAnchors());

        //--------------------------------------------------------------

        /**
        AnchoredComponent anchoredComponent2 = new AnchoredComponent();
        anchoredComponent2.createAnchor(Anchor.DirectionType.X, 100);
        anchoredComponent2.createAnchor(Anchor.DirectionType.X, -200);
        anchoredComponent2.createAnchor(Anchor.DirectionType.Y, 20);
        anchoredComponent2.createAnchor(AnchoredComponent.StandardY.BOTTOM);

        JPanel canvas = new JPanel();
        canvas.setBackground(Color.green);
        this.add(canvas, anchoredComponent2);
         */

        //--------------------------------------------------------------



        super.setVisible(true);


    }
}
