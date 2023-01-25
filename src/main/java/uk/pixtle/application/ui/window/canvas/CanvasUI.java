package uk.pixtle.application.ui.window.canvas;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.Application;
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

    @Getter
    @Setter
    Application application;


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if(application.getPluginManager() == null) return;
        application.getPluginManager().getCanvasPlugin().paint(g);
    }

    /*
    -------------------- Constructor --------------------
     */

    public CanvasUI(Application paramApplication) {
        super(true); // double buffered;

        this.setApplication(paramApplication);
        super.addMouseListener(new CanvasListener(this));
    }
}
