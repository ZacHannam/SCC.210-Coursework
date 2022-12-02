package uk.pixtle.application.ui.window.canvas;

import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Parameter;

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

    private BufferedImage test( int width, int height) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int y = 0; y < super.getHeight(); y++) {
            for(int x = 0; x < super.getWidth(); x++) {

                Color color;

                if(Math.floor(x / 20) % 2 == 0) {
                    color = Color.black;
                } else {
                    color = Color.white;
                }

                bufferedImage.setRGB(x, y, color.getRGB());
            }
        }
        return bufferedImage;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(test(super.getWidth(), super.getHeight()), null, null);;
    }

    /*
    -------------------- Constructor --------------------
     */

    public CanvasUI() {
        super.addMouseListener(new CanvasListener());
    }
}
