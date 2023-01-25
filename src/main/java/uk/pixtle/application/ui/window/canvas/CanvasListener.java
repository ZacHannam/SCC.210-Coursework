package uk.pixtle.application.ui.window.canvas;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.plugins.expansions.PluginDrawableExpansion;
import uk.pixtle.application.plugins.expansions.PluginToolExpansion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Timer;
import java.util.TimerTask;

public class CanvasListener implements MouseListener {

    @Getter
    @Setter
    Canvas parentCanvas;

    @Getter
    @Setter
    private boolean currentlyPressed;

    @Getter
    @Setter
    private Timer timer;

    @Getter
    @Setter
    private TimerTask timerTask;

    private void stopTimerTask() {
        if(this.isCurrentlyPressed()) {
            this.getTimerTask().cancel();
        }

        this.setCurrentlyPressed(false);
    }


    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Mouse Start: " + e.getX() + " " + e.getY());
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {

        if(!(getParentCanvas().getApplication().getPluginManager().getActivatePlugin() instanceof PluginDrawableExpansion)) return;

        Point lastPoint = new Point(e.getX(), e.getY());

        int originalCanvasPositionX = e.getX();
        int originalCanvasPositionY = e.getY();

        int originalMousePointerX = (int) MouseInfo.getPointerInfo().getLocation().getX();
        int originalMousePointerY = (int) MouseInfo.getPointerInfo().getLocation().getY();

        int differenceX = originalMousePointerX - originalCanvasPositionX;
        int differenceY = originalMousePointerY - originalCanvasPositionY;

        this.setCurrentlyPressed(true);

        this.setTimerTask(new TimerTask() {
            @Override
            public void run() {

                int currentMousePointerX = (int) MouseInfo.getPointerInfo().getLocation().getX();
                int currentMousePointerY = (int) MouseInfo.getPointerInfo().getLocation().getY();

                int calculatedX = currentMousePointerX - differenceX;
                int calculatedY = currentMousePointerY - differenceY;

                int lastX = (int) lastPoint.getX();
                int lastY = (int) lastPoint.getY();

                int differenceX = calculatedX - (int) lastPoint.getX();
                int differenceY = calculatedY -  (int) lastPoint.getY();

                final Point[] addedPoints = {
                        new Point(0, 0),
                        new Point(-1, -1),
                        new Point(0, -1),
                        new Point(1, -1),
                        new Point(-1, 0),
                        new Point(0, 0),
                        new Point(1, 0),
                        new Point(-1, 1),
                        new Point(0, 1),
                        new Point(1, 1)};

                while(true) {

                    Point smallestDistancePoint = null;
                    double smallestDistance = -1;

                    for(int index = 0; index < addedPoints.length; index++) {
                        double distance = Math.sqrt(Math.pow(calculatedX - (lastX - addedPoints[index].getX()), 2) +
                                Math.pow(calculatedY - (lastY - addedPoints[index].getY()), 2));
                        if(smallestDistance == -1 || distance < smallestDistance) {
                            smallestDistance = distance;
                            smallestDistancePoint = addedPoints[index];
                        }
                    }

                    if(lastX != calculatedX) lastX -= (int) smallestDistancePoint.getX();
                    if(lastY != calculatedY) lastY -= (int) smallestDistancePoint.getY();

                    ((PluginDrawableExpansion) getParentCanvas().getApplication().getPluginManager().getActivatePlugin()).mouseCanvasEvent(lastX, lastY, differenceX, differenceY);

                    differenceY = 0;
                    differenceX = 0;

                    if(lastX == calculatedX && lastY == calculatedY) {
                        break;
                    }
                }


                lastPoint.setLocation( new Point(calculatedX, calculatedY));
            }
        });

        this.getTimer().schedule(this.getTimerTask(), 0, 10);
    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        this.stopTimerTask();
    }

    /**
     * Invoked when the mouse enters a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        System.out.println("Mouse Entered Canvas");
    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e) {
        this.stopTimerTask();
    }

    public CanvasListener(Canvas paramParentCanvas) {
        this.setParentCanvas(paramParentCanvas);
        this.setTimer(new Timer());
    }
}
