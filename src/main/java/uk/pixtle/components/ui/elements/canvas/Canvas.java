package uk.pixtle.components.ui.elements.canvas;

import javax.swing.*;
import java.awt.*;

public class Canvas extends JPanel {

    public Canvas() {
        super.setBackground(Color.blue);
        super.addMouseListener(new CanvasListener());
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }
}
