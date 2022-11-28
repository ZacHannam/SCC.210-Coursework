package uk.pixtle.application.ui.window;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.ui.window.color.ColorPanel;
import uk.pixtle.application.ui.window.menubar.MenuBar;
import uk.pixtle.application.ui.window.menubar.MenuBarUI;
import uk.pixtle.application.ui.window.minitoollist.MiniToolListUI;
import uk.pixtle.application.ui.window.toollist.ToolListUI;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchorLayout;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    @Getter
    @Setter
    public MenuBar menuBarElement;

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

        ToolListUI toolList = new ToolListUI();
        super.add(toolList, toolList.getAnchors());

        ColorPanel colorPanel = new ColorPanel();
        super.add(colorPanel, colorPanel.getAnchors());

        MiniToolListUI miniToolListUI = new MiniToolListUI();
        super.add(miniToolListUI, miniToolListUI.getAnchors());

        MenuBarUI menuBar = new MenuBarUI();
        super.add(menuBar, menuBar.getAnchors());
        this.setMenuBarElement(menuBar);

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
