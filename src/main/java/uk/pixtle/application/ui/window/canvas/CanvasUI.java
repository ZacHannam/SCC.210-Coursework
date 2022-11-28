package uk.pixtle.application.ui.window.canvas;

import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;

import javax.swing.*;
import java.awt.*;

public class CanvasUI extends JPanel implements Canvas {

    public CanvasUI() {
        super.setBackground(Color.blue);
        super.addMouseListener(new CanvasListener());
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    @Override
    public AnchoredComponent getAnchors() {
        return null;
    }
}
