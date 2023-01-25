package uk.pixtle.application.ui.window;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.Application;
import uk.pixtle.application.ui.window.canvas.Canvas;
import uk.pixtle.application.ui.window.canvas.CanvasUI;
import uk.pixtle.application.ui.window.color.ColorPanel;
import uk.pixtle.application.ui.window.menubar.MenuBar;
import uk.pixtle.application.ui.window.menubar.MenuBarUI;
import uk.pixtle.application.ui.window.minitoollist.MiniToolList;
import uk.pixtle.application.ui.window.minitoollist.MiniToolListUI;
import uk.pixtle.application.ui.window.toollist.ToolList;
import uk.pixtle.application.ui.window.toollist.ToolListUI;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchorLayout;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    @Getter
    @Setter
    public MenuBar menuBarElement;

    @Getter
    @Setter
    public ToolList toolList;

    @Getter
    @Setter
    public MiniToolList miniToolList;

    @Getter
    @Setter
    public ColorPanel colorPanel;

    @Getter
    @Setter
    public Canvas canvas;

    public Window(Application paramApplication) {
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
        this.setToolList(toolList);

        ColorPanel colorPanel = new ColorPanel(paramApplication);
        super.add(colorPanel, colorPanel.getAnchors());
        this.setColorPanel(colorPanel);

        MiniToolListUI miniToolList = new MiniToolListUI();
        super.add(miniToolList, miniToolList.getAnchors());
        this.setMiniToolList(miniToolList);

        MenuBarUI menuBar = new MenuBarUI();
        super.add(menuBar, menuBar.getAnchors());
        this.setMenuBarElement(menuBar);

        CanvasUI canvas = new CanvasUI(paramApplication);
        super.add(canvas, canvas.getAnchors());
        this.setCanvas(canvas);



        super.setVisible(true);
    }
}
