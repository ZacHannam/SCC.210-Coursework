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
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {

        if(!(getParentCanvas().getApplication().getPluginManager().getActivatePlugin() instanceof PluginDrawableExpansion)) return;

        this.setTimer(new Timer());

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
                        new Point(-1, -1),
                        new Point(0, -1),
                        new Point(1, -1),
                        new Point(-1, 0),
                        new Point(0, 0),
                        new Point(1, 0),
                        new Point(-1, 1),
                        new Point(0, 1),
                        new Point(1, 1)};

                int currentPixelX = lastX;
                int currentPixelY = lastY;

                ((PluginDrawableExpansion) getParentCanvas().getApplication().getPluginManager().getActivatePlugin()).mouseCanvasEvent(lastX, lastY, differenceX, differenceY);

                differenceY = 0;
                differenceX = 0;

                while(true) {

                    double smallestDistance = -1;

                    int smallestNewLocationX = 0;
                    int smallestNewLocationY = 0;

                    for(Point point : addedPoints) {
                        int newLocationX = currentPixelX + (int) point.getX();
                        int newLocationY = currentPixelY + (int) point.getY();

                        double distance = Math.sqrt(Math.pow(newLocationX - calculatedX, 2) + Math.pow(newLocationY - calculatedY, 2));
                        if(smallestDistance == -1 || distance < smallestDistance) {
                            smallestDistance = distance;
                            smallestNewLocationY = newLocationY;
                            smallestNewLocationX = newLocationX;
                        }


                    }
                    
                    ((PluginDrawableExpansion) getParentCanvas().getApplication().getPluginManager().getActivatePlugin()).mouseCanvasEvent(smallestNewLocationX, smallestNewLocationY, differenceX, differenceY);

                    if(currentPixelX == calculatedX && currentPixelY == calculatedY) {
                        break;
                    }
                    currentPixelX = smallestNewLocationX;
                    currentPixelY = smallestNewLocationY;
                }


                lastPoint.setLocation( new Point(calculatedX, calculatedY));
            }
        });

        this.getTimer().schedule(this.getTimerTask(), 0, 5);
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
    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e) {
        this.mouseReleased(e);
    }

    public CanvasListener(Canvas paramParentCanvas) {
        this.setParentCanvas(paramParentCanvas);
        this.setTimer(new Timer());
    }
}
