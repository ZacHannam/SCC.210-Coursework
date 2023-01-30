package uk.pixtle.application.ui.window;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.Application;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;
import uk.pixtle.application.ui.window.canvas.Canvas;
import uk.pixtle.application.ui.window.canvas.CanvasUI;
import uk.pixtle.application.ui.window.color.ColorPanel;
import uk.pixtle.application.ui.window.menubar.MenuBar;
import uk.pixtle.application.ui.window.menubar.MenuBarUI;
import uk.pixtle.application.ui.window.minitoollist.MiniToolList;
import uk.pixtle.application.ui.window.minitoollist.MiniToolListUI;
import uk.pixtle.application.ui.window.notifications.Notifications;
import uk.pixtle.application.ui.window.notifications.NotificationsUI;
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

    @Getter
    @Setter
    public Notifications notifications;

    public AnchoredComponent getLayeredPaneAnchors() {
        AnchoredComponent anchoredComponent = new AnchoredComponent();
        anchoredComponent.createAnchor(AnchoredComponent.StandardX.LEFT);
        anchoredComponent.createAnchor(AnchoredComponent.StandardX.RIGHT);
        anchoredComponent.createAnchor(AnchoredComponent.StandardY.TOP);
        anchoredComponent.createAnchor(AnchoredComponent.StandardY.BOTTOM);
        return anchoredComponent;
    }

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

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(new AnchorLayout());


        ToolListUI toolList = new ToolListUI();
        layeredPane.add(toolList, toolList.getAnchors());
        this.setToolList(toolList);
        layeredPane.setLayer(toolList, 1);

        ColorPanel colorPanel = new ColorPanel(paramApplication);
        layeredPane.add(colorPanel, colorPanel.getAnchors());
        this.setColorPanel(colorPanel);
        layeredPane.setLayer(colorPanel, 1);

        MiniToolListUI miniToolList = new MiniToolListUI();
        layeredPane.add(miniToolList, miniToolList.getAnchors());
        this.setMiniToolList(miniToolList);
        layeredPane.setLayer(miniToolList, 1);

        MenuBarUI menuBar = new MenuBarUI();
        layeredPane.add(menuBar, menuBar.getAnchors());
        this.setMenuBarElement(menuBar);
        layeredPane.setLayer(menuBar, 1);

        CanvasUI canvas = new CanvasUI(paramApplication);
        layeredPane.add(canvas, canvas.getAnchors());
        this.setCanvas(canvas);
        layeredPane.setLayer(canvas, 1);

        NotificationsUI notifications = new NotificationsUI(paramApplication);
        layeredPane.add(notifications, notifications.getAnchors());
        this.setNotifications(notifications);
        layeredPane.setLayer(notifications, 5);

        super.add(layeredPane, this.getLayeredPaneAnchors());

        super.setVisible(true);
    }
}
