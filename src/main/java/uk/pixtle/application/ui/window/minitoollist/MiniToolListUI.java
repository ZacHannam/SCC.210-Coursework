package uk.pixtle.application.ui.window.minitoollist;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchorLayout;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;

import javax.swing.*;
import java.awt.*;

public class MiniToolListUI extends JScrollPane implements MiniToolList {

    private static final int PADDING = 10;
    private static final int WIDTH = 200;
    private static final int HEIGHT = 0;

    @Getter
    @Setter
    private JPanel miniToolPanel;

    /*
    -------------------- UIComponent Methods --------------------
     */

    @Override
    public AnchoredComponent getAnchors() {
        AnchoredComponent anchoredComponent = new AnchoredComponent();
        anchoredComponent.createAnchor(Anchor.DirectionType.X, -WIDTH);
        anchoredComponent.createAnchor(AnchoredComponent.StandardX.RIGHT);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, 20);
        anchoredComponent.createAnchor(AnchoredComponent.StandardY.BOTTOM);

        return anchoredComponent;
    }

    /*
    -------------------- ToolsUI Methods --------------------
     */

    /*
    -------------------- Layout Methods --------------------
     */

    private void createScrollPane() {
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.setViewportView(this.getMiniToolPanel());
    }

    private void createMiniToolPanel() {

        this.setMiniToolPanel(new JPanel());
        this.getMiniToolPanel().setBackground(new Color(211,211,211));

        // -- Replace

        this.getMiniToolPanel().setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // ---

        AnchorLayout toolsLayout = new AnchorLayout();
        this.getMiniToolPanel().setLayout(toolsLayout);

        // ---
    }

    /*
    -------------------- Constructor --------------------
     */

    public MiniToolListUI() {

        this.createMiniToolPanel();
        this.createScrollPane();

    }
}
