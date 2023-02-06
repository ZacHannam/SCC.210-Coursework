package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.ui;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.InfiniteCanvasPlugin;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchorLayout;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.DynamicAnchor;
import uk.pixtle.application.ui.window.notifications.Notification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BackgroundLayerUI extends JPanel {

    @Getter
    @Setter
    JPanel backgroundColourPreview;

    @Getter
    @Setter
    JLabel label;

    public void updatePreviewColor(Color paramColor) {
        this.getBackgroundColourPreview().setBackground(paramColor);
        this.repaint();
    }

    public void updateBackgroundColourPreview(Color paramColour) {
        backgroundColourPreview.setBackground(paramColour);
    }

    private void createBackgroundColourPreview() {

        AnchoredComponent backgroundAnchors = new AnchoredComponent();
        backgroundAnchors.createAnchor(Anchor.DirectionType.Y, -12);
        backgroundAnchors.createAnchor(Anchor.DirectionType.Y, 12);
        backgroundAnchors.createAnchor(Anchor.DirectionType.X, 12);
        backgroundAnchors.createAnchor(Anchor.DirectionType.X, DynamicAnchor.AnchorSize.MAX, 1, 1);

        JPanel backgroundColourPreview = new JPanel();
        backgroundColourPreview.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));

        super.add(backgroundColourPreview, backgroundAnchors);
        this.setBackgroundColourPreview(backgroundColourPreview);

        backgroundColourPreview.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switch(e.getButton()) {
                    case 1:
                        getInfiniteCanvasPlugin().setBackgroundColor(getInfiniteCanvasPlugin().getApplication().getColourManager().getActiveColor());
                        getInfiniteCanvasPlugin().repaint();
                        getInfiniteCanvasPlugin().getApplication().getNotificationManager().displayNotification(Notification.ColourModes.INFO, "Background change", "Successfully changed the colour of the background", 5);
                        break;
                    case 3:
                        getInfiniteCanvasPlugin().getApplication().getColourManager().setColorOfActiveSlot(backgroundColourPreview.getBackground());
                        break;
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    private void createLabel() {

        AnchoredComponent labelAnchors = new AnchoredComponent();
        labelAnchors.createAnchor(Anchor.DirectionType.Y, -12);
        labelAnchors.createAnchor(Anchor.DirectionType.Y, 12);
        labelAnchors.createAnchor(Anchor.DirectionType.X, 43);
        labelAnchors.createAnchor(AnchoredComponent.StandardX.RIGHT);

        JLabel backgroundText = new JLabel();
        backgroundText.setText("Background");


        super.add(backgroundText, labelAnchors);
        this.setLabel(backgroundText);

    }

    @Getter
    @Setter
    InfiniteCanvasPlugin infiniteCanvasPlugin;

    public BackgroundLayerUI(InfiniteCanvasPlugin paramInfiniteCanvasPlugin) {
        this.setInfiniteCanvasPlugin(paramInfiniteCanvasPlugin);

        super.setBackground(new Color(250, 249, 246));

        super.setBorder(BorderFactory.createLineBorder(Color.darkGray, 1, true));

        super.setLayout(new AnchorLayout());

        this.createBackgroundColourPreview();
        this.createLabel();
    }
}
