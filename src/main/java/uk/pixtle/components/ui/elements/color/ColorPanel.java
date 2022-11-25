package uk.pixtle.components.ui.elements.color;

import uk.pixtle.components.ui.elements.Element;
import uk.pixtle.components.ui.layouts.anchorlayout.AnchoredComponent;
import uk.pixtle.components.ui.layouts.anchorlayout.anchors.Anchor;

import javax.swing.*;
import java.awt.*;

public class ColorPanel extends JPanel implements Element {

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
