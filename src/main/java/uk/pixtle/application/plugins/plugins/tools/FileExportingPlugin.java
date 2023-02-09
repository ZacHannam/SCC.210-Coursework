package uk.pixtle.application.plugins.plugins.tools;

import uk.pixtle.application.Application;
import uk.pixtle.application.plugins.annotations.MenuBarItem;
import uk.pixtle.application.plugins.expansions.PluginMiniToolExpansion;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.InfiniteCanvasPlugin;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.Layer;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.LayerManager;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.LayerType;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.drawinglayer.DrawingLayer;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.drawinglayer.DrawingLayerImageProcessor;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.imagelayer.ImageLayer;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.imagelayer.ImageLayerImageProcessor;
import uk.pixtle.application.ui.window.minitoollist.MiniToolPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.awt.image.*;

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
        InfiniteCanvasPlugin infiniteCanvas = (InfiniteCanvasPlugin) super.getApplication().getPluginManager().getActiveCanvasPlugin();
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
                default:
                    break;
            }

        });

        BufferedImage newImg = new BufferedImage(biggestWidth[0],biggestHeight[0],BufferedImage.TYPE_INT_ARGB | BufferedImage.SCALE_FAST);
        Graphics2D g2 = (Graphics2D) newImg.createGraphics();
        g2.setPaint(infiniteCanvas.getBackgroundColor());
        g2.fillRect(0,0,biggestWidth[0], biggestHeight[0]);

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
                default:
                    break;
                }

        });

        try
        {
            ImageIO.write(newImg, "PNG", new File(PATH));
        }
        catch (Exception e)
        {
            System.out.print(e);
        }
    }

    public void oldExport(String PATH){
        //final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final BufferedImage[] image = new BufferedImage[1];
        final DrawingLayerImageProcessor[] dlip = new DrawingLayerImageProcessor[1];
        InfiniteCanvasPlugin infiniteCanvas = (InfiniteCanvasPlugin) super.getApplication().getPluginManager().getActiveCanvasPlugin();
        LayerManager lm = infiniteCanvas.getLayerManager();

        ArrayList<Layer> layers = lm.getLayers();

        BufferedImage newImg = new BufferedImage(2000,2000,BufferedImage.TYPE_INT_ARGB | BufferedImage.SCALE_FAST);
        Graphics2D grap = (Graphics2D) newImg.createGraphics();
        DrawingLayerImageProcessor dlip2 = new DrawingLayerImageProcessor(layers.get(0));
        grap.drawImage(dlip2.getLayerAsBufferedImage(), null, 0,0);
        dlip2 = new DrawingLayerImageProcessor(layers.get(1));
        grap.drawImage(dlip2.getLayerAsBufferedImage(), null, 0,0);



        System.out.print(layers);
        try{
            layers.forEach((l) -> {
                System.out.println("Reached");
                final int[] smallestX = { 999999999 };
                final int[] biggestX = { 0 };
                final int[] smallestY = { 999999999 };
                final int[] biggestY = { 0 };
                switch (l.getLayerType()) {
                    case DRAWING:
                        DrawingLayer currentLayer = (DrawingLayer)l;
                        //dlip[0] = new DrawingLayerImageProcessor(currentLayer);
                        //image[0] = new BufferedImage(1000,1000, BufferedImage.TYPE_INT_ARGB | BufferedImage.SCALE_FAST);
                        //Graphics2D g2 = (Graphics2D) image[0].createGraphics();
                        //g2.drawImage(dlip[0].getLayerAsBufferedImage(), null, 0, 0);
                        HashMap chunkMap = currentLayer.getChunkMap(); //Get the chunk map of the current layer
                        chunkMap.forEach((s, c) -> { //Loop through the current chunk map
                            //System.out.printf("Current chunk - %s : %s\n",s,c);
                            //System.out.printf("Current String working on: %s\n", s);
                            String[] ChunkString = s.toString().split(":");
                            int currentX = Integer.parseInt(ChunkString[0]);
                            int currentY = Integer.parseInt(ChunkString[1]);
                            //System.out.printf("%d : %d\n", currentX, currentY);
                            if(currentX> biggestX[0])
                                biggestX[0] = currentX;

                            if(currentX< smallestX[0])
                                smallestX[0] = currentX;

                            if(currentY > biggestY[0])
                                biggestY[0] = currentY;

                            if(currentY < smallestY[0])
                                smallestY[0] = currentY;


                        });
                        int topPixelY = smallestY[0] * currentLayer.getPixelsPerChunk();
                        int topPixelX = smallestX[0] * currentLayer.getPixelsPerChunk();
                        int bottomPixelY = (biggestY[0]+1)  * (currentLayer.getPixelsPerChunk() - 1);
                        int bottomPixelX = (biggestX[0]+1)  * (currentLayer.getPixelsPerChunk() - 1);
                        System.out.printf("Big and small %d : %d\n",topPixelX, topPixelY);
                        System.out.printf("Big and small %d : %d\n", bottomPixelX, bottomPixelY);
                        break;
                    case IMAGE:
                        ImageLayer currentLayerImage = (ImageLayer) l;
                        Point topLeftPixel = currentLayerImage.getTopLeftPixel();
                        Point bottomRightPixel = currentLayerImage.getBottomRightPixel();
                        int height=bottomRightPixel.y - topLeftPixel.y;
                        int width=bottomRightPixel.x - topLeftPixel.x;
                        //image = new BufferedImage(width,height, BufferedImage.TYPE_INT_ARGB | BufferedImage.SCALE_FAST); //won't work as needs to be final for lambda.
                        break;
                    default:

                        break;
                }
                //System.out.printf("Big and small x %d : %d\n", biggestX[0], smallestX[0]);
                //System.out.printf("Big and small y %d : %d\n", biggestY[0], smallestY[0]);
            });

        }
        catch (Exception e)
        {
            System.out.print(e);
        }

        try
        {
            ImageIO.write(newImg, "PNG", new File(PATH));
        }
        catch (Exception e)
        {
            System.out.print(e);
        }


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
            System.out.println("Save as file: " + fileToSave.getAbsolutePath());
            String path = fileToSave.getAbsolutePath();
            if(!path.endsWith(".png")) {
                path += ".png";
            }
            exportFile(path);
        }
    }
}
