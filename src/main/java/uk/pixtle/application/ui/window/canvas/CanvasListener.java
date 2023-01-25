package uk.pixtle.application.ui.window.canvas;

import lombok.Getter;
import lombok.Setter;

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

                int differenceX = calculatedX - (int) lastPoint.getX();
                int differenceY = calculatedY -  (int) lastPoint.getY();

                lastPoint.setLocation(new Point(calculatedX, calculatedY));

                ((CanvasUI) getParentCanvas()).updateCurrentPixel(-differenceX, -differenceY);

            }
        });

        this.getTimer().schedule(this.getTimerTask(), 0, 50);
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
