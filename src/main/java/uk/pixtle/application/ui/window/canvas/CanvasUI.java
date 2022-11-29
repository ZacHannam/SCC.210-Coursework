package uk.pixtle.application.ui.window.canvas;

import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;

import javax.swing.*;
import java.awt.*;

public class CanvasUI extends JPanel implements Canvas {

    /*
    -------------------- UIComponent Methods --------------------
     */

    @Override
    public AnchoredComponent getAnchors() {
        AnchoredComponent anchoredComponent = new AnchoredComponent();
        anchoredComponent.createAnchor(Anchor.DirectionType.X, 100);
        anchoredComponent.createAnchor(Anchor.DirectionType.X, -200);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, 20);
        anchoredComponent.createAnchor(AnchoredComponent.StandardY.BOTTOM);

        return anchoredComponent;
    }


    /*
    -------------------- Overwrite Methods --------------------
     */

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    /*
    -------------------- Constructor --------------------
     */

    public CanvasUI() {
        super.setBackground(Color.blue);
        super.addMouseListener(new CanvasListener());
    }
}
