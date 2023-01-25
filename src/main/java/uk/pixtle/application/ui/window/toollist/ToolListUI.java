package uk.pixtle.application.ui.window.toollist;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.Application;
import uk.pixtle.application.plugins.plugins.Plugin;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.DynamicAnchor;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchorLayout;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;
import uk.pixtle.application.ui.window.minitoollist.MiniToolPanel;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

public class ToolListUI extends JScrollPane implements ToolList {

    private static final int PADDING = 10;
    private static final int WIDTH = 100;
    private static final int HEIGHT = 0;
    private static final int BOTTOM_TAB_HEIGHT = PADDING;

    @Getter
    @Setter
    private JPanel toolPanel;

    @Getter
    private ArrayList<ToolButton> toolButtons = new ArrayList<>();

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
        super.getVerticalScrollBar().setUnitIncrement(50);

        super.setViewportView(this.getToolPanel());
    }

    private void createToolPanel() {

        this.setToolPanel(new JPanel());
        this.getToolPanel().setBackground(new Color(211, 211, 211));
        this.getToolPanel().setPreferredSize(new Dimension(WIDTH, 1));

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

        this.setLastYAnchor(null);
        this.setBottomTab(null);

    }

    @Getter
    @Setter
    Anchor lastYAnchor;

    @Getter
    @Setter
    JLabel bottomTab;

    private void updateBottomTab() {
        if(this.getLastYAnchor() == null) return;

        if(this.getBottomTab() == null) {
            this.setBottomTab(new JLabel());
        }

        AnchoredComponent anchoredComponent = new AnchoredComponent();
        anchoredComponent.createAnchor(Anchor.DirectionType.X, 0);
        anchoredComponent.createAnchor(Anchor.DirectionType.X, 100);

        anchoredComponent.createAnchor(Anchor.DirectionType.Y, lastYAnchor, 0);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, lastYAnchor, BOTTOM_TAB_HEIGHT);

        this.getToolPanel().add(this.getBottomTab(), anchoredComponent);
    }

    public ToolButton createToolButton(Plugin paramPlugin, String paramIconName) {

        ToolButton toolButton = new ToolButton(this, paramPlugin, paramIconName);

        AnchoredComponent anchoredComponent = new AnchoredComponent();

        anchoredComponent.createAnchor(Anchor.DirectionType.X, PADDING);
        anchoredComponent.createAnchor(Anchor.DirectionType.X, -PADDING);
        if (this.getLastYAnchor() == null) {
            anchoredComponent.createAnchor(Anchor.DirectionType.Y, PADDING);
        } else {
            anchoredComponent.createAnchor(Anchor.DirectionType.Y, this.getLastYAnchor() , PADDING);
        }
        this.setLastYAnchor(anchoredComponent.createAnchor(Anchor.DirectionType.Y, DynamicAnchor.AnchorSize.MAX, 1, 1));

        this.getToolPanel().add(toolButton, anchoredComponent);
        this.updateBottomTab();

        this.getToolPanel().setPreferredSize(new Dimension(this.getWidth(), Integer.MAX_VALUE));

        this.getToolButtons().add(toolButton);



        return toolButton;

    }

    public void clearActiveBorders() {
        for(ToolButton toolButton : this.getToolButtons()) {
            toolButton.setBorder(BorderFactory.createEmptyBorder());
        }
    }

    public void clearPluginBorder(Plugin paramPlugin) {
        for(ToolButton toolButton : this.getToolButtons()) {
            if(toolButton.getParentPlugin() == paramPlugin) {
                toolButton.setBorder(BorderFactory.createEmptyBorder());
            }
        }
    }

    public void addActiveBorder(Plugin paramPlugin) {
        for(ToolButton toolButton : this.getToolButtons()) {
            if(toolButton.getParentPlugin() == paramPlugin) {
                toolButton.setBorder(BorderFactory.createLineBorder(Color.gray, 3));

            }
        }

    }

    public void toolClick(ToolButton paramToolButton, Plugin paramPlugin ) {
        paramPlugin.getApplication().getPluginManager().activatePlugin(paramPlugin);
    }
}
