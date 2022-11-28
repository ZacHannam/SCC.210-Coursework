package uk.pixtle.application.ui.window.color;

import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;
import uk.pixtle.application.ui.window.WindowItem;

import javax.swing.*;
import java.awt.*;

public class ColorPanel extends JPanel implements WindowItem {

    @Override
    public AnchoredComponent getAnchors() {

        AnchoredComponent anchoredComponent = new AnchoredComponent();
        anchoredComponent.createAnchor(Anchor.DirectionType.X, 0);
        anchoredComponent.createAnchor(Anchor.DirectionType.X, 100);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, -100);
        anchoredComponent.createAnchor(AnchoredComponent.StandardY.BOTTOM);

        return anchoredComponent;
    }

    public ColorPanel() {
        super.setBackground(Color.red);
    }
}
