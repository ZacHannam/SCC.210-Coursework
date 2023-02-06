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

        int originalCanvasPositionX = e.getX();
        int originalCanvasPositionY = e.getY();

        int originalMousePointerX = (int) MouseInfo.getPointerInfo().getLocation().getX();
        int originalMousePointerY = (int) MouseInfo.getPointerInfo().getLocation().getY();

        int differenceX = originalMousePointerX - originalCanvasPositionX;
        int differenceY = originalMousePointerY - originalCanvasPositionY;

        this.setCurrentlyPressed(true);
        this.setTimer(new Timer());

        Point lastPoint = new Point(e.getX(), e.getY());

        this.setTimerTask(new TimerTask() {
            @Override
            public void run() {
                int currentMousePointerX = (int) MouseInfo.getPointerInfo().getLocation().getX();
                int currentMousePointerY = (int) MouseInfo.getPointerInfo().getLocation().getY();

                // Bresenham's Line Algorithm (https://www.javatpoint.com/computer-graphics-bresenhams-line-algorithm)

                int x1 = (int) lastPoint.getX();
                int y1 = (int) lastPoint.getY();

                int x2 = currentMousePointerX - differenceX;
                int y2 = currentMousePointerY - differenceY;

                ((PluginDrawableExpansion) getParentCanvas().getApplication().getPluginManager().getActivatePlugin()).mouseCanvasEvent(x2, y2, x2 - x1, y2 - y1);

                int d = 0;

                int dx = Math.abs(x2 - x1);
                int dy = Math.abs(y2 - y1);

                int dx2 = 2 * dx;
                int dy2 = 2 * dy;

                int ix = x1 < x2 ? 1 : -1;
                int iy = y1 < y2 ? 1 : -1;

                int x = x1;
                int y = y1;

                if (dx >= dy) {
                    while (true) {
                        ((PluginDrawableExpansion) getParentCanvas().getApplication().getPluginManager().getActivatePlugin()).mouseCanvasEvent(x, y, 0, 0);
                        if (x == x2)
                            break;
                        x += ix;
                        d += dy2;
                        if (d > dx) {
                            y += iy;
                            d -= dx2;
                        }
                    }
                } else {
                    while (true) {
                        ((PluginDrawableExpansion) getParentCanvas().getApplication().getPluginManager().getActivatePlugin()).mouseCanvasEvent(x, y, 0, 0);

                        if (y == y2)
                            break;
                        y += iy;
                        d += dx2;
                        if (d > dy) {
                            x += ix;
                            d -= dy2;
                        }
                    }
                }

                lastPoint.setLocation( new Point(x2, y2));
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

/*
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
                    currentPixelX = smallestNewLocationX;
                    currentPixelY = smallestNewLocationY;

                    ((PluginDrawableExpansion) getParentCanvas().getApplication().getPluginManager().getActivatePlugin()).mouseCanvasEvent(smallestNewLocationX, smallestNewLocationY, differenceX, differenceY);

                    if(currentPixelX == calculatedX && currentPixelY == calculatedY) {
                        break;
                    }

                }
 */
