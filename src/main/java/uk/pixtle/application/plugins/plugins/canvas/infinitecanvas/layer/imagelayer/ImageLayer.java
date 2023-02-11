package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.imagelayer;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import uk.pixtle.application.plugins.plugins.canvas.drawing.Drawing;
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageLayer extends Layer {

    @Getter
    @Setter
    BufferedImage image;

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

            this.setImage(bufferedImage);

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

    /*

                    CONSTRUCTOR

     */

    public ImageLayer(LayerManager paramLayerManager) {
        super(paramLayerManager);
    }
}
