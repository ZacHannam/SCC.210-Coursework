package uk.pixtle.application.plugins.plugins.tools;

import uk.pixtle.application.Application;
import uk.pixtle.application.plugins.annotations.MenuBarItem;
import uk.pixtle.application.plugins.expansions.PluginMiniToolExpansion;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.InfiniteCanvasPlugin;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.Layer;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.LayerManager;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.drawinglayer.DrawingLayer;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.imageprocessors.DrawingLayerImageProcessor;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.imagelayer.ImageLayer;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.imageprocessors.ImageLayerImageProcessor;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.textlayer.TextLayer;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.imageprocessors.TextLayerImageProcessor;
import uk.pixtle.application.ui.window.minitoollist.MiniToolPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

public class FileExportingPlugin extends ToolPlugin implements PluginMiniToolExpansion {
    public FileExportingPlugin(Application paramApplication) {
        super(paramApplication);
    }

    @Override
    public int getMiniToolPanelHeight() {


        return 0;
    }

    @Override
    public void instanceMiniToolPanel(MiniToolPanel paramMiniToolPanel) {

    }

    public void exportFile(String PATH)
    {
        if(!(super.getApplication().getPluginManager().getActivatePlugin() instanceof InfiniteCanvasPlugin)) {
            // TO-DO error
            return;
        }
        InfiniteCanvasPlugin infiniteCanvas = (InfiniteCanvasPlugin) super.getApplication().getPluginManager().getActiveCanvasPlugin();
        BufferedImage finalImg = infiniteCanvas.getFullImage(false);
        BufferedImage baseImage = new BufferedImage(finalImg.getWidth(),finalImg.getHeight(),BufferedImage.TYPE_INT_ARGB | BufferedImage.SCALE_FAST);

        Graphics2D g2 = (Graphics2D) baseImage.createGraphics();
        if(!Objects.equals(infiniteCanvas.getBackgroundColor(), new Color(255, 255, 255)))
        {
            g2.setPaint(infiniteCanvas.getBackgroundColor());
            g2.fillRect(0,0,finalImg.getWidth(), finalImg.getHeight());
        }

        g2.drawImage(finalImg,null,0,0);
        try
        {
            ImageIO.write(baseImage, "PNG", new File(PATH));
        }
        catch (Exception e)
        {
            System.out.print(e);
        }
    }

    public void oldExport(String PATH){
        /*
        InfiniteCanvasPlugin infiniteCanvas = (InfiniteCanvasPlugin) super.getApplication().getPluginManager().getActiveCanvasPlugin();
        BufferedImage finalImg = infiniteCanvas.getFullImage(false);
        BufferedImage newImg = new BufferedImage(finalImg.getWidth(),finalImg.getHeight(),BufferedImage.TYPE_INT_ARGB | BufferedImage.SCALE_FAST);
        LayerManager lm = infiniteCanvas.getLayerManager();
        ArrayList<Layer> layers = lm.getLayers();
        Collections.reverse(layers);

        final int[] biggestHeight = {0};
        final int[] biggestWidth = {0};

        layers.forEach((l) -> {
            switch (l.getLayerType()) {
                case DRAWING:
                    DrawingLayer currentLayer = (DrawingLayer) l;
                    DrawingLayerImageProcessor dlip = new DrawingLayerImageProcessor(currentLayer);
                    BufferedImage layerAsImage = dlip.getLayerAsBufferedImage();
                    if (layerAsImage.getHeight() > biggestHeight[0])
                        biggestHeight[0] = layerAsImage.getHeight();
                    if (layerAsImage.getWidth() > biggestWidth[0])
                        biggestWidth[0] = layerAsImage.getWidth();
                    break;
                case IMAGE:
                    ImageLayer currentLayerImage = (ImageLayer) l;
                    ImageLayerImageProcessor ilip = new ImageLayerImageProcessor(currentLayerImage);
                    BufferedImage layerAsImageimg = ilip.getLayerAsBufferedImage();
                    if (layerAsImageimg.getHeight() > biggestHeight[0])
                        biggestHeight[0] = layerAsImageimg.getHeight();
                    if (layerAsImageimg.getWidth() > biggestWidth[0])
                        biggestWidth[0] = layerAsImageimg.getWidth();
                    break;
                case TEXT:
                    TextLayer currentLayerText = (TextLayer) l;
                    TextLayerImageProcessor tlip = new TextLayerImageProcessor(currentLayerText);
                    BufferedImage layerAsImagetxt = tlip.getLayerAsBufferedImage();
                    if (layerAsImagetxt.getHeight() > biggestHeight[0])
                        biggestHeight[0] = layerAsImagetxt.getHeight();
                    if (layerAsImagetxt.getWidth() > biggestWidth[0])
                        biggestWidth[0] = layerAsImagetxt.getWidth();
                    break;
                default:
                    break;
            }

        });


        BufferedImage newImg = new BufferedImage(biggestWidth[0],biggestHeight[0],BufferedImage.TYPE_INT_ARGB | BufferedImage.SCALE_FAST);
        Graphics2D g2 = (Graphics2D) newImg.createGraphics();
        if(!Objects.equals(infiniteCanvas.getBackgroundColor(), new Color(255, 255, 255)))
        {
            g2.setPaint(infiniteCanvas.getBackgroundColor());
            g2.fillRect(0,0,biggestWidth[0], biggestHeight[0]);
        }


        layers.forEach((l) -> {
            switch (l.getLayerType()) {
                case DRAWING:
                    DrawingLayer currentLayer = (DrawingLayer)l;
                    DrawingLayerImageProcessor dlip = new DrawingLayerImageProcessor(currentLayer);
                    BufferedImage layerAsImage = dlip.getLayerAsBufferedImage();
                    g2.drawImage(layerAsImage, null, 0 ,0);
                    break;
                case IMAGE:
                    ImageLayer currentLayerImage = (ImageLayer) l;
                    ImageLayerImageProcessor ilip = new ImageLayerImageProcessor(currentLayerImage);
                    BufferedImage layerAsImageimg = ilip.getLayerAsBufferedImage();
                    g2.drawImage(layerAsImageimg,null,0,0);
                    break;
                case TEXT:
                    TextLayer currentLayerText = (TextLayer) l;
                    TextLayerImageProcessor tlip = new TextLayerImageProcessor(currentLayerText);
                    BufferedImage layerAsImagetxt = tlip.getLayerAsBufferedImage();
                    g2.drawImage(layerAsImagetxt, null,0,0);
                default:
                    break;
            }

        });
*/
    }



    @MenuBarItem(PATH = "file:Export")
    public void export() {
        JFileChooser fileChooser = new JFileChooser();
        FileFilter fileFilter = new FileNameExtensionFilter("PNG File", "png");
        fileChooser.setFileFilter(fileFilter);
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.setDragEnabled(true);
        int option = fileChooser.showSaveDialog(fileChooser);
        if(option == JFileChooser.APPROVE_OPTION)
        {
            File fileToSave = fileChooser.getSelectedFile();
            String path = fileToSave.getAbsolutePath();
            if(!path.endsWith(".png")) {
                path += ".png";
            }
            exportFile(path);
        }
    }
}
