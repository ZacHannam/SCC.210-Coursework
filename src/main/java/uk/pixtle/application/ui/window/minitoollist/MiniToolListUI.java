package uk.pixtle.application.ui.window.minitoollist;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.plugins.toolsettings.ToolSettingEntry;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchorLayout;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.DisplacedAnchor;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.DynamicAnchor;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.ValueAnchor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MiniToolListUI extends JScrollPane implements MiniToolList {

    private static final int WIDTH = 200;
    private static final int HEIGHT = 0;

    private static final int PADDING = 10;
    private static final int BOTTOM_TAB_HEIGHT = PADDING;

    @Getter
    @Setter
    private JPanel miniToolListPanel;

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

        this.setViewportView(this.getMiniToolListPanel());
    }

    public void createMiniToolList() {

        this.setMiniToolListPanel(new JPanel());
        this.getMiniToolListPanel().setBackground(new Color(211,211,211));
        this.getMiniToolListPanel().setPreferredSize(new Dimension(WIDTH, 1));

        AnchorLayout toolsLayout = new AnchorLayout(true, false);
        this.getMiniToolListPanel().setLayout(toolsLayout);

    }

    @Getter
    @Setter
    Anchor lastYAnchor;

    @Getter
    @Setter
    Anchor miniToolsBeginningAnchor;

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

        this.getMiniToolListPanel().add(this.getBottomTab(), anchoredComponent);
    }

    public MiniToolPanel createMiniToolPanel(int paramHeight) {

        MiniToolPanel miniToolPanel = new MiniToolPanel(new Dimension(this.getWidth() - 2 * PADDING, paramHeight));

        AnchoredComponent anchoredComponent = new AnchoredComponent();
        anchoredComponent.createAnchor(Anchor.DirectionType.X, PADDING);
        anchoredComponent.createAnchor(Anchor.DirectionType.X, -PADDING);

        if(this.getLastYAnchor() == null) {
            anchoredComponent.getAnchors().add(this.getMiniToolsBeginningAnchor());
            this.setLastYAnchor(anchoredComponent.createAnchor(Anchor.DirectionType.Y,  this.getMiniToolsBeginningAnchor(),PADDING+paramHeight));
        } else {
            anchoredComponent.createAnchor(Anchor.DirectionType.Y, this.getLastYAnchor(), PADDING);
            this.setLastYAnchor(anchoredComponent.createAnchor(Anchor.DirectionType.Y, this.getLastYAnchor(), PADDING+paramHeight));
        }
        this.getMiniToolListPanel().add(miniToolPanel, anchoredComponent);

        this.updateBottomTab();

        this.getMiniToolListPanel().setPreferredSize(new Dimension(this.getWidth(), Integer.MAX_VALUE));

        this.repaint();

        return miniToolPanel;



    }

    @Getter
    @Setter
    ToolSettingsPanel toolSettingsPanel;

    public ToolSettingsPanel createToolSettingsPanel(ArrayList<ToolSettingEntry<?>> paramToolSettingEntries) {

        if(this.getToolSettingsPanel() != null) {
            this.getMiniToolListPanel().remove(toolSettingsPanel);
        }

        ToolSettingsPanel panel = new ToolSettingsPanel(this.getWidth() - 2 * PADDING, paramToolSettingEntries);

        AnchoredComponent anchoredComponent = new AnchoredComponent();
        anchoredComponent.createAnchor(Anchor.DirectionType.X, PADDING);
        anchoredComponent.createAnchor(Anchor.DirectionType.X, -PADDING);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, PADDING);
        Anchor last = anchoredComponent.createAnchor(Anchor.DirectionType.Y,  PADDING + (int) panel.getSize().getHeight());

        ((ValueAnchor) this.getMiniToolsBeginningAnchor()).setValue(2 * PADDING + (int) panel.getSize().getHeight());

        this.getMiniToolListPanel().add(panel, anchoredComponent);

        this.getMiniToolListPanel().setPreferredSize(new Dimension(this.getWidth(), Integer.MAX_VALUE));

        getMiniToolListPanel().updateUI();

        this.setToolSettingsPanel(panel);
        return panel;
    }

    @Override
    public void removeToolSettingsPanel() {

        // TO-DO

        if(this.getToolSettingsPanel() != null) {
            this.getMiniToolListPanel().remove(toolSettingsPanel);
        }

        ((ValueAnchor) this.getMiniToolsBeginningAnchor()).setValue(PADDING);

        getMiniToolListPanel().updateUI();
    }

    /*
    -------------------- Constructor --------------------
     */

    public MiniToolListUI() {

        this.createMiniToolList();
        this.createScrollPane();

        this.setLastYAnchor(null);
        this.setMiniToolsBeginningAnchor(new ValueAnchor(null, Anchor.DirectionType.Y, PADDING));
        this.setBottomTab(null);
    }
}
