package uk.pixtle.application.ui.window.toollist;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.Application;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.DynamicAnchor;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchorLayout;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;

import javax.swing.*;
import java.awt.*;

public class ToolListUI extends JScrollPane implements ToolList {

    private static final int PADDING = 10;
    private static final int WIDTH = 100;
    private static final int HEIGHT = 0;

    @Getter
    @Setter
    private JPanel toolPanel;

    /*
    -------------------- UIComponent Methods --------------------
     */

    @Override
    public AnchoredComponent getAnchors() {
        AnchoredComponent anchoredComponent = new AnchoredComponent();
        anchoredComponent.createAnchor(AnchoredComponent.StandardX.LEFT);
        anchoredComponent.createAnchor(Anchor.DirectionType.X, WIDTH);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, 20);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, -100);

        return anchoredComponent;
    }

    /*
    -------------------- ToolsUI Methods --------------------
     */

    /*
    -------------------- Layout Methods --------------------
     */

    private void createScrollPane() {
        super.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        super.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        super.setViewportView(this.getToolPanel());
    }

    private void createToolPanel() {

        this.setToolPanel(new JPanel());
        this.getToolPanel().setBackground(new Color(211, 211, 211));
        this.getToolPanel().setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // ---

        AnchorLayout toolsLayout = new AnchorLayout(true, false);
        this.getToolPanel().setLayout(toolsLayout);

        // ---
    }

    /*
    -------------------- Constructor --------------------
     */

    public ToolListUI() {

        this.createToolPanel();
        this.createScrollPane();

        test();


    }

    /*
    -------------------- Deleted Later --------------------
     */

    private synchronized void test() {
        Anchor lastAnchor = null;
        for (int x = 0; x < 50; x++) {

            JButton button = new JButton(); // Change to tool button

            AnchoredComponent ac = new AnchoredComponent();
            ac.createAnchor(Anchor.DirectionType.X, PADDING);
            ac.createAnchor(Anchor.DirectionType.X, -PADDING);
            if (lastAnchor == null) {
                ac.createAnchor(Anchor.DirectionType.Y, PADDING);
            } else {
                ac.createAnchor(Anchor.DirectionType.Y, lastAnchor, PADDING);
            }
            lastAnchor = ac.createAnchor(Anchor.DirectionType.Y, DynamicAnchor.AnchorSize.MAX, 1, 1);

            this.getToolPanel().add(button, ac);
        }

        this.getToolPanel().setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

}
