package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.ui;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.Layer;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.LayerManager;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchorLayout;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.ValueAnchor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

public class LayerUIDrawer extends JLayeredPane{

    @Getter
    @Setter
    public JPopupMenu popupMenuShowing;

    @Getter
    @Setter
    private LayerManager layerManager;


    @Getter
    @Setter
    private ArrayList<LayerUI> layerUIs;

    @Getter
    @Setter
    private LayerUI draggedLayerUI;

    public void paintDragged(LayerUI paramLayerUI, int paramLastY, int paramY) {

        if(!this.getStartPosAnchor().containsKey(paramLayerUI)) {
            return;
        }
        int i = paramLastY <= paramY ? 1 : -1;
        for(int y = paramLastY; y != paramY; y = y + i) {
            this.getStartPosAnchor().get(paramLayerUI).setValue(y);

            for (ValueAnchor anchor : this.getStartPosAnchor().values()) {
                if (anchor == this.getStartPosAnchor().get(paramLayerUI)) continue;

                if ((int) anchor.getValue() - y < 25 && (int) anchor.getValue() - y >= 0) {
                    anchor.setValue((int) anchor.getValue() - 50);
                    this.getLayerManager().moveLayerDown(paramLayerUI.getLayer());
                }

                if (y - (int) anchor.getValue() < 25 && (int) anchor.getValue() - y <= 0) {
                    anchor.setValue((int) anchor.getValue() + 50);
                    this.getLayerManager().moveLayerUp(paramLayerUI.getLayer());
                }

            }
        }
        this.repaint();
        this.revalidate();
    }

    @Getter
    @Setter
    private HashMap<LayerUI, ValueAnchor> startPosAnchor;

    public void repaint() {
        super.repaint();

        if(this.getDraggedLayerUI() == null) {

            for (LayerUI layerUI : this.getLayerUIs()) {
                this.remove(layerUI);
            }

            this.getLayerUIs().clear();
            this.getStartPosAnchor().clear();

            int index = 0;
            for (Layer layer : this.getLayerManager().getLayers()) {

                AnchoredComponent layerAnchors = new AnchoredComponent();
                layerAnchors.createAnchor(AnchoredComponent.StandardX.LEFT);
                layerAnchors.createAnchor(AnchoredComponent.StandardX.RIGHT);
                ValueAnchor anchor = (ValueAnchor) layerAnchors.createAnchor(Anchor.DirectionType.Y, (index * 50));
                layerAnchors.createAnchor(Anchor.DirectionType.Y, anchor, 50);

                LayerUI layerUI = new LayerUI(this, layer);
                setLayer(layerUI, 1);
                this.add(layerUI, layerAnchors);

                this.getLayerUIs().add(layerUI);
                this.getStartPosAnchor().put(layerUI, anchor);

                int finalIndex = index;
                layerUI.addMouseListener(new MouseListener() {

                    private TimerTask task;
                    private long clickDownTime;
                    private MouseEvent lastEvent;

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        switch(e.getButton()) {
                            case 1:
                                getLayerManager().setActiveLayer(layer);
                                break;
                            case 3:
                                layerUI.leftClick(e);
                                break;
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                        clickDownTime = System.currentTimeMillis();
                        lastEvent = e;

                        setLayer(layerUI, 3);
                        setDraggedLayerUI(layerUI);

                        int differenceY = (finalIndex * 50);
                        final int[] lastY = {differenceY};

                        int startY = (int) MouseInfo.getPointerInfo().getLocation().getY();
                        int startX = (int) MouseInfo.getPointerInfo().getLocation().getX();
                        java.util.Timer timer = new java.util.Timer();
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                int newY = (int) MouseInfo.getPointerInfo().getLocation().getY();
                                int newX = (int) MouseInfo.getPointerInfo().getLocation().getX();
                                int relativeMousePositionY = finalIndex * 50 + e.getY() - (startY - newY);
                                int relativeMousePositionX = e.getX() - (startX - newX);
                                int y = newY - startY + differenceY;
                                if(y < 0 || relativeMousePositionY < 0 || relativeMousePositionY > (50 * getLayerManager().getLayers().size() - 1) || relativeMousePositionX < 0 || relativeMousePositionX > 160) {
                                    mouseReleased(e);
                                    return;
                                }
                                paintDragged(layerUI, lastY[0], y);
                                lastY[0] = y;
                            }
                        };

                        timer.schedule(task, 0, 30);

                        this.task = task;
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                        setLayer(layerUI, 1);

                        setDraggedLayerUI(null);
                        if (this.task != null) {
                            this.task.cancel();
                        }

                        if(System.currentTimeMillis() - clickDownTime <= 250) {
                            mouseClicked(lastEvent);
                        }
                        clickDownTime = 0;

                        repaint();
                        revalidate();

                        getLayerManager().getInfiniteCanvasPlugin().repaint();
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                    }
                });

                index++;
            }
        }
    }

    public LayerUIDrawer(LayerManager paramLayerManager) {
        this.setLayerManager(paramLayerManager);
        this.setLayerUIs(new ArrayList<>());
        this.setStartPosAnchor(new HashMap<>());
        this.setPopupMenuShowing(null);

        this.setLayout(new AnchorLayout());

    }
}
