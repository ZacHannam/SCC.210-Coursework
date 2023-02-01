package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchorLayout;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.DynamicAnchor;
import uk.pixtle.util.ResourceHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class LayerUI extends JPanel {

    @Getter
    @Setter
    private Layer layer;

    @Getter
    @Setter
    private JTextField title;

    private void activeCheck() {
        if(this.getLayer().getLayerManager().getActiveLayer() == this.getLayer()) {
            super.setBackground(Color.white.darker());
        } else {
            super.setBackground(new Color(250, 249, 246));
        }
    }

    private void createTitle() {

        AnchoredComponent titleAnchors = new AnchoredComponent();
        titleAnchors.createAnchor(Anchor.DirectionType.Y, -12);
        titleAnchors.createAnchor(Anchor.DirectionType.Y, 12);
        titleAnchors.createAnchor(Anchor.DirectionType.X, 125);
        titleAnchors.createAnchor(Anchor.DirectionType.X, 8);

        JTextField title = new JTextField(this.getLayer().getTitle());
        title.setText(this.getLayer().getTitle());
        this.setTitle(title);
        super.add(title, titleAnchors);

        title.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                getLayer().setTitle(title.getText());

                title.setText(title.getText());
                KeyboardFocusManager.getCurrentKeyboardFocusManager().clearFocusOwner();
            }
        });
    }

    @Getter
    @Setter
    private JButton visibleButton;

    private void updateVisibleIcon() {
        Image image = null;
        if(this.getLayer().isShown()) {
            image = new ImageIcon(ResourceHandler.getResourceURL("Visible.png")).getImage();
        } else {
            image = new ImageIcon(ResourceHandler.getResourceURL("notVisible.png")).getImage();

        }
        Image transformedImage = image.getScaledInstance(25
                , 25, Image.SCALE_SMOOTH);


        this.getVisibleButton().setIcon(new ImageIcon(transformedImage));
    }

    private void createVisibleButton() {

        AnchoredComponent visibleButtonAnchors = new AnchoredComponent();
        visibleButtonAnchors.createAnchor(Anchor.DirectionType.Y, -12);
        visibleButtonAnchors.createAnchor(Anchor.DirectionType.Y, 12);
        visibleButtonAnchors.createAnchor(Anchor.DirectionType.X, -8);
        visibleButtonAnchors.createAnchor(Anchor.DirectionType.X, DynamicAnchor.AnchorSize.MIN, 1, 1);

        JButton visibleButton = new JButton();
        this.setVisibleButton(visibleButton);
        super.add(visibleButton, visibleButtonAnchors);
        updateVisibleIcon();

        visibleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getLayer().getLayerManager().toggleLayerShown(getLayer().getLayerID());
                updateVisibleIcon();
            }
        });
    }

    @Getter
    @Setter
    private JPopupMenu popupMenu;

    private void createPopUpMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Delete");
        popupMenu.add(deleteItem);

        this.setPopupMenu(popupMenu);

        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getLayer().getLayerManager().deleteLayer(getLayer());
            }
        });

    }

    @Getter
    @Setter
    LayerUI selfReference;

    public void leftClick(MouseEvent e) {
        try {
            this.getPopupMenu().show(getSelfReference(), e.getX(), e.getY());
        } catch (IllegalComponentStateException exception) {}
    }

    public LayerUI(Layer paramLayer) {
        this.setLayer(paramLayer);
        this.setSelfReference(this);

        super.setBackground(new Color(250, 249, 246));
        super.setBorder(BorderFactory.createLineBorder(Color.darkGray, 1, true));

        super.setLayout(new AnchorLayout());

        this.createVisibleButton();
        this.createPopUpMenu();
        this.createTitle();
        this.activeCheck();
    }
}
