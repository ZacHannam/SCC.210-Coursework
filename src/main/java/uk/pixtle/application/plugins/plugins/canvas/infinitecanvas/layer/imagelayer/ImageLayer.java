package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.imagelayer;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import uk.pixtle.application.plugins.plugins.canvas.drawing.Drawing;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.InfiniteCanvasPlugin;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.Layer;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.LayerImageProcessor;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.LayerManager;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.LayerType;
import uk.pixtle.application.ui.window.notifications.Notification;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

public class ImageLayer extends Layer {

    @Getter
    @Setter
    BufferedImage actualImage;

    @Getter
    @Setter
    BufferedImage scaledImage;

    @Getter
    @Setter
    Point topLeftPixel;

    @Getter
    @Setter
    Point bottomRightPixel;

    @Override
    public LayerType getLayerType() {
        return LayerType.IMAGE;
    }

    @Override
    public JSONObject saveLayerData() throws Exception {
        return null;
    }

    @Override
    public void loadLayerData(JSONObject paramSavedData) throws Exception {

    }

    @Override
    public LayerImageProcessor getLayerImageProcessor() {
        return new ImageLayerImageProcessor(this);
    }

    public boolean loadNew() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter fileChooseFilter = new FileNameExtensionFilter("Image File", "png", "jpg", "jpeg");
        fileChooser.setFileFilter(fileChooseFilter);
        int result = fileChooser.showOpenDialog(this.getLayerManager().getInfiniteCanvasPlugin().getApplication().getUIManager().getWindow());

        if(result != JFileChooser.APPROVE_OPTION) {
            return false;
        }

        super.setTitle(fileChooser.getSelectedFile().getName());

        try {
            Image image = ImageIO.read(fileChooser.getSelectedFile());

            BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB | BufferedImage.SCALE_SMOOTH);
            Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();
            g2d.drawImage(image, 0, 0, null);

            this.setActualImage(bufferedImage);

            Point centerPixel = this.getLayerManager().getInfiniteCanvasPlugin().getCenterPixel();
            int topY = (int) centerPixel.getY() - (int) Math.floor(bufferedImage.getHeight() / 2);
            int bottomY = (int) centerPixel.getY() + (int) Math.floor(bufferedImage.getHeight() / 2);
            int leftX = (int) centerPixel.getX() - (int) Math.floor(bufferedImage.getWidth() / 2);
            int rightX = (int) centerPixel.getX() + (int) Math.floor(bufferedImage.getWidth() / 2);

            this.setTopLeftPixel(new Point(leftX, topY));
            this.setBottomRightPixel(new Point(rightX, bottomY));

            this.getLayerManager().getInfiniteCanvasPlugin().getApplication().getNotificationManager().displayNotification(Notification.ColourModes.INFO, "Image Loaded", "Successfully loaded image: "+ fileChooser.getSelectedFile().getName() + "!", 5);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        this.recreateScaledImage();

        return true;
    }

    @Override
    public void onLayerEnable() {
        super.setReRender(true);
        super.getLayerManager().getInfiniteCanvasPlugin().repaint(false);
    }

    @Override
    public void onLayerDisable() {
        super.setReRender(true);
        super.getLayerManager().getInfiniteCanvasPlugin().repaint(false);
    }

    @Getter
    @Setter
    ReentrantLock scaledLock;

    public void recreateScaledImage() {

        try {
            this.getScaledLock().lock();

            if (this.getScaledLock().getQueueLength() > 0) { // Only updates enough to show the last image available instead of inbetween ones saving time and processor
                return;
            }

            int height = (int) Math.abs(this.getBottomRightPixel().getY() - this.getTopLeftPixel().getY());
            int width = (int) Math.abs(this.getBottomRightPixel().getX() - this.getTopLeftPixel().getX());

            if(height == 0 || width == 0) {
                return;
            }

            if (this.getActualImage().getHeight() == height && this.getActualImage().getWidth() == width) {
                this.setScaledImage(this.getActualImage());
                return;
            }

            if (this.getScaledImage() != null && this.getScaledImage().getWidth() == width && this.getScaledImage().getHeight() == height) {
                return;
            }

            Image scaledInstance = this.getActualImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = newImage.createGraphics();
            g2d.drawImage(scaledInstance, 0, 0, null);
            g2d.dispose();

            this.setScaledImage(newImage);
        } finally {
            this.getScaledLock().unlock();
        }
    }

    public void armAWTEvents() {
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {

                if (event instanceof MouseEvent) {
                    if(!(getLayerManager().getInfiniteCanvasPlugin().getApplication().getPluginManager().getActivatePlugin() == getLayerManager().getInfiniteCanvasPlugin())) return;
                    if(!(getLayerManager().getActiveLayer() == ImageLayer.this)) return;

                   if(((MouseEvent) event).getButton() != MouseEvent.BUTTON1) return;

                   if(event.getID() == MouseEvent.MOUSE_CLICKED) {
                       setMousePressed(false);
                   }

                   if(event.getID() == MouseEvent.MOUSE_PRESSED) {
                       setMousePressed(true);
                   }

                   if(event.getID() == MouseEvent.MOUSE_RELEASED) {
                       setMousePressed(false);
                   }
                }
            }

        }, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.WINDOW_EVENT_MASK);
    }

    @Getter
    boolean mousePressed;

    private void setMousePressed(boolean paramPressed) {
        this.mousePressed = paramPressed;

        if(!paramPressed) {
            this.setDragged(0);
        }
    }

    /*
    0 = none
    1 - 2
    |   |
    3 - 4
     */

    @Getter
    @Setter
    int dragged;

    private void repaintResize() {

        this.recreateScaledImage();
        super.setReRender(true);
        this.getLayerManager().getInfiniteCanvasPlugin().repaint(false);
    }

    @Getter
    @Setter
    long lastClickTime;

    @Override
    public void mouseCanvasEvent(int paramCalculatedX, int paramCalculatedY, int paramDifferenceX, int paramDifferenceY) {

        if(System.currentTimeMillis() - this.getLastClickTime() > 250) {
            this.setDragged(0);
        }

        if(paramDifferenceX == 0 && paramDifferenceY == 0) return;

        InfiniteCanvasPlugin infiniteCanvasPlugin = this.getLayerManager().getInfiniteCanvasPlugin();
        Point translatedPixel = infiniteCanvasPlugin.translateScreenPixel(paramCalculatedX, paramCalculatedY);

        /*
        0 = none
        1 - 2
        |   |
        3 - 4
         */

        if(this.getTopLeftPixel().distance(translatedPixel) < 15 || this.getDragged() == 1) {
            this.setTopLeftPixel(translatedPixel);
            this.repaintResize();

            this.setDragged(1);
        }

        if(this.getBottomRightPixel().distance(translatedPixel) < 15 || this.getDragged() == 4) {
            this.setBottomRightPixel(translatedPixel);
            this.repaintResize();

            this.setDragged(4);
        }

        Point topRightPixel = new Point((int) this.getBottomRightPixel().getX(), (int) this.getTopLeftPixel().getY());

        if(topRightPixel.distance(translatedPixel) < 15 || this.getDragged() == 2) {
            this.setBottomRightPixel(new Point((int) translatedPixel.getX(), (int) this.getBottomRightPixel().getY()));
            this.setTopLeftPixel(new Point((int) this.getTopLeftPixel().getX(), (int) translatedPixel.getY()));
            this.repaintResize();

            this.setDragged(2);
        }

        Point bottomLeftPixel = new Point((int) this.getTopLeftPixel().getX(), (int) this.getBottomRightPixel().getY());

        if(bottomLeftPixel.distance(translatedPixel) < 15 || this.getDragged() == 3) {
            this.setBottomRightPixel(new Point((int) this.getBottomRightPixel().getX(), (int) translatedPixel.getY()));
            this.setTopLeftPixel(new Point((int) translatedPixel.getX(), (int) this.getTopLeftPixel().getY()));
            this.repaintResize();

            this.setDragged(3);
        }


        if(this.getDragged() == 5 || (translatedPixel.getY() < this.getBottomRightPixel().getY() && translatedPixel.getY() > this.getTopLeftPixel().getY()
                && translatedPixel.getX() < this.getBottomRightPixel().getX() && translatedPixel.getX() > this.getTopLeftPixel().getX())) {

            int moveX = (int) Math.round(paramDifferenceX * (1 / infiniteCanvasPlugin.getZoom()));
            int moveY = (int) Math.round(paramDifferenceY * (1 / infiniteCanvasPlugin.getZoom()));
            this.setBottomRightPixel(new Point((int) this.getBottomRightPixel().getX() + moveX, (int) this.getBottomRightPixel().getY() + moveY));
            this.setTopLeftPixel(new Point((int) this.getTopLeftPixel().getX() + moveX, (int) this.getTopLeftPixel().getY() + moveY));

            super.setReRender(true);
            infiniteCanvasPlugin.repaint(false);

            this.setDragged(5);

        }

        if(this.getDragged() != 0) {
            this.setLastClickTime(System.currentTimeMillis());
        } else {
            super.mouseCanvasEvent(paramCalculatedX, paramCalculatedY, paramDifferenceX, paramDifferenceY);
        }
    }
    /*

                    CONSTRUCTOR

     */

    public ImageLayer(LayerManager paramLayerManager) {
        super(paramLayerManager);
        this.setScaledLock(new ReentrantLock());
        this.armAWTEvents();
    }
}
