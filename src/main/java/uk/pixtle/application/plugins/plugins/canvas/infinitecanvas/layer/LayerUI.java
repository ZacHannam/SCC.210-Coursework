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
    private uk.pixtle.application.plugins.plugins.tools.colourplugin.TextField title;

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
        titleAnchors.createAnchor(Anchor.DirectionType.X, 100);
        titleAnchors.createAnchor(Anchor.DirectionType.X, 3);

        uk.pixtle.application.plugins.plugins.tools.colourplugin.TextField title = new uk.pixtle.application.plugins.plugins.tools.colourplugin.TextField (this.getLayer().getTitle(), this.getLayer().getOriginalTitle());
        title.setText(this.getLayer().getTitle());
        title.setFocusCycleRoot(false);
        this.setTitle(title);
        super.add(title, titleAnchors);

        title.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getLayer().setTitle(title.getText());

            }
        });
    }



    @Getter
    @Setter
    private JButton upButton;

    @Getter
    @Setter
    private JButton downButton;

    private void createUpAndDownButtons() {

        if(this.getLayer().getLayerManager().getLayerOrder().get(0) != this.getLayer().getLayerID()) {

            AnchoredComponent upButtonAnchors = new AnchoredComponent();
            upButtonAnchors.createAnchor(Anchor.DirectionType.Y, 1);
            upButtonAnchors.createAnchor(Anchor.DirectionType.Y, 0.5);
            upButtonAnchors.createAnchor(AnchoredComponent.StandardX.RIGHT);
            upButtonAnchors.createAnchor(Anchor.DirectionType.X, DynamicAnchor.AnchorSize.MIN, 1, 1);

            JButton upButton = new JButton();
            upButton.setText("▲");
            this.setUpButton(upButton);
            super.add(upButton, upButtonAnchors);

            upButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getLayer().getLayerManager().moveLayerUp(getLayer().getLayerID());
                }
            });
        }

        if(this.getLayer().getLayerManager().getLayerOrder().get(this.getLayer().getLayerManager().getLayerOrder().size() - 1) != this.getLayer().getLayerID()) {

            AnchoredComponent downButtonAnchors = new AnchoredComponent();
            downButtonAnchors.createAnchor(Anchor.DirectionType.Y, -1);
            downButtonAnchors.createAnchor(Anchor.DirectionType.Y, 0.5);
            downButtonAnchors.createAnchor(AnchoredComponent.StandardX.RIGHT);
            downButtonAnchors.createAnchor(Anchor.DirectionType.X, DynamicAnchor.AnchorSize.MIN, 1, 1);


            JButton downButton = new JButton();
            downButton.setText("▼");
            this.setDownButton(downButton);
            super.add(downButton, downButtonAnchors);

            downButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getLayer().getLayerManager().moveLayerDown(getLayer().getLayerID());
                }
            });
        }

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
        visibleButtonAnchors.createAnchor(Anchor.DirectionType.X, -28);
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

    private void createListener() {
        MouseListener mouseListener = new  MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switch(e.getButton()) {
                    case 3:
                        getPopupMenu().show(getSelfReference(), e.getX(), e.getY());
                        break;
                    case 1:
                    default:
                        getLayer().getLayerManager().setActiveLayer(getLayer());
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
        };

        super.addMouseListener(mouseListener);
        //this.getTitle().addMouseListener(mouseListener);
    }

    public LayerUI(Layer paramLayer) {
        System.out.println(paramLayer);
        this.setLayer(paramLayer);
        this.setSelfReference(this);

        super.setBackground(new Color(250, 249, 246));
        super.setBorder(BorderFactory.createLineBorder(Color.darkGray, 1, true));

        super.setLayout(new AnchorLayout());

        this.createUpAndDownButtons();
        this.createVisibleButton();
        this.createPopUpMenu();
        this.createTitle();
        this.activeCheck();
        this.createListener();
    }
}
