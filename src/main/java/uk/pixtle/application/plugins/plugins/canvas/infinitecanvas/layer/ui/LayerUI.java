package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.ui;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.Layer;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchorLayout;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.DynamicAnchor;
import uk.pixtle.util.ResourceHandler;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;

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

    @Getter
    @Setter
    JLabel typeText;

    private void createTypeText() {

        AnchoredComponent typeTextAnchors = new AnchoredComponent();
        typeTextAnchors.createAnchor(Anchor.DirectionType.Y, -16);
        typeTextAnchors.createAnchor(AnchoredComponent.StandardY.BOTTOM);
        typeTextAnchors.createAnchor(Anchor.DirectionType.X, 125);
        typeTextAnchors.createAnchor(Anchor.DirectionType.X, 10);

        Font font = new Font("Courier", Font.PLAIN, 12);
        JLabel typeText = new JLabel(this.getLayer().getLayerType().getDescription());
        typeText.setAlignmentX(0);
        typeText.setAlignmentY(1);
        typeText.setFont(font);

        this.setTypeText(typeText);
        super.add(typeText, typeTextAnchors);


    }

    private void createTitle() {

        AnchoredComponent titleAnchors = new AnchoredComponent();
        titleAnchors.createAnchor(Anchor.DirectionType.Y, -18);
        titleAnchors.createAnchor(Anchor.DirectionType.Y, 6);
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
        if(this.getLayer().isVisible()) {
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
                getLayer().getLayerManager().toggleLayerShown(getLayer());
                updateVisibleIcon();
            }
        });
    }

    @Getter
    @Setter
    private JPopupMenu popupMenu;

    private void createPopUpMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBorder(BorderFactory.createLineBorder(Color.black, 1, false));

        popupMenu.setLayout(new AnchorLayout());
        popupMenu.setPopupSize(new Dimension(200, 100));

        AnchoredComponent closeButtonAnchors = new AnchoredComponent();
        closeButtonAnchors.createAnchor(AnchoredComponent.StandardY.TOP);
        closeButtonAnchors.createAnchor(Anchor.DirectionType.Y, 15);
        closeButtonAnchors.createAnchor(AnchoredComponent.StandardX.RIGHT);
        closeButtonAnchors.createAnchor(Anchor.DirectionType.X, -15);

        JButton closeButton = new JButton("x");
        popupMenu.add(closeButton, closeButtonAnchors);

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeActivePopupLayer();
            }
        });

        AnchoredComponent opacityTextAnchors = new AnchoredComponent();
        opacityTextAnchors.createAnchor(AnchoredComponent.StandardX.LEFT);
        opacityTextAnchors.createAnchor(AnchoredComponent.StandardX.RIGHT);
        opacityTextAnchors.createAnchor(Anchor.DirectionType.Y, 10);
        opacityTextAnchors.createAnchor(Anchor.DirectionType.Y, 30);

        JLabel opacityText = new JLabel("Opacity", SwingConstants.CENTER);
        popupMenu.add(opacityText, opacityTextAnchors);

        AnchoredComponent opacitySliderAnchors = new AnchoredComponent();
        opacitySliderAnchors.createAnchor(AnchoredComponent.StandardX.LEFT);
        opacitySliderAnchors.createAnchor(AnchoredComponent.StandardX.RIGHT);
        opacitySliderAnchors.createAnchor(Anchor.DirectionType.Y, 35);
        opacitySliderAnchors.createAnchor(Anchor.DirectionType.Y, 55);

        JSlider opacitySlider = new JSlider();
        opacitySlider.setMinimum(100);
        opacitySlider.setMinimum(0);
        opacitySlider.setValue((int) Math.floor(getLayer().getOpacity() * 100));
        popupMenu.add(opacitySlider, opacitySliderAnchors);

        opacitySlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                getLayer().setOpacity((float) ((float) opacitySlider.getValue() / 100.0));
            }
        });

        AnchoredComponent deleteButtonAnchors = new AnchoredComponent();
        deleteButtonAnchors.createAnchor(Anchor.DirectionType.X, 40);
        deleteButtonAnchors.createAnchor(Anchor.DirectionType.X, -40);
        deleteButtonAnchors.createAnchor(Anchor.DirectionType.Y, 60);
        deleteButtonAnchors.createAnchor(Anchor.DirectionType.Y, 90);

        JButton deleteButton = new JButton();

        deleteButton.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                super.paint(g, c);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.RED);
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 30, 30);
                g.setColor(Color.WHITE);

                Font font = new Font("Calabri", Font.BOLD, 14);
                g.setFont(font);

                String text = "Delete";

                FontMetrics metrics = g.getFontMetrics(font);
                int x = (c.getWidth() - metrics.stringWidth(text)) / 2;
                int y = ((c.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();

                g.drawString(text, x, y);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeActivePopupLayer();

                getLayer().getLayerManager().deleteLayer(getLayer());
                getPopupMenu().setVisible(false);
            }
        });

        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {

                if (event instanceof MouseEvent) {
                    MouseEvent m = (MouseEvent) event;
                    if (m.getID() == MouseEvent.MOUSE_CLICKED) {
                        closeActivePopupLayer();
                        Toolkit.getDefaultToolkit().removeAWTEventListener(this);
                    }
                }
                if (event instanceof WindowEvent) {
                    WindowEvent we = (WindowEvent) event;
                    if (we.getID() == WindowEvent.WINDOW_DEACTIVATED || we.getID() == WindowEvent.WINDOW_STATE_CHANGED) {
                        closeActivePopupLayer();
                        Toolkit.getDefaultToolkit().removeAWTEventListener(this);
                    }
                }
            }

        }, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.WINDOW_EVENT_MASK);

        popupMenu.add(deleteButton, deleteButtonAnchors);

        this.setPopupMenu(popupMenu);
    }

    @Getter
    @Setter
    LayerUI selfReference;

    @Getter
    @Setter
    LayerUIDrawer layerUIDrawer;

    private void closeActivePopupLayer() {
        if(this.getLayerUIDrawer().getPopupMenuShowing() == null) return;

        this.getLayerUIDrawer().getPopupMenuShowing().setVisible(false);
        this.getLayerUIDrawer().setPopupMenuShowing(null);

        if(this.getPopupMenu() != null && this.getLayerUIDrawer().getPopupMenuShowing() == this.getPopupMenu()) {
            this.setPopupMenu(null);
        }
    }


    public void leftClick(MouseEvent e) {
        try {
            if(this.getLayerUIDrawer().getPopupMenuShowing() != null) {
                this.closeActivePopupLayer();
            }
            this.createPopUpMenu();
            this.getLayerUIDrawer().setPopupMenuShowing(this.getPopupMenu());
            this.getPopupMenu().show(null, (int) MouseInfo.getPointerInfo().getLocation().getX(), (int) MouseInfo.getPointerInfo().getLocation().getY());
        } catch (IllegalComponentStateException exception) {}
    }

    public LayerUI(LayerUIDrawer paramLayerUIDrawer, Layer paramLayer) {
        this.setLayer(paramLayer);
        this.setSelfReference(this);
        this.setLayerUIDrawer(paramLayerUIDrawer);

        super.setBackground(new Color(250, 249, 246));
        super.setBorder(BorderFactory.createLineBorder(Color.darkGray, 1, true));

        super.setLayout(new AnchorLayout());

        this.createVisibleButton();
        this.createTitle();
        this.createTypeText();
        this.activeCheck();
    }
}
