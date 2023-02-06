package uk.pixtle.application.plugins.plugins.tools;

import uk.pixtle.application.Application;
import uk.pixtle.application.plugins.annotations.MenuBarItem;
import uk.pixtle.application.plugins.expansions.PluginMiniToolExpansion;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.InfiniteCanvasPlugin;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.Layer;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.LayerManager;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.LayerType;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.drawinglayer.DrawingLayer;
import uk.pixtle.application.ui.window.minitoollist.MiniToolPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

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

    public void exportFile(String PATH){
        //final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.out.println("Hello");

        InfiniteCanvasPlugin infiniteCanvas = (InfiniteCanvasPlugin) super.getApplication().getPluginManager().getActiveCanvasPlugin();

        LayerManager lm = infiniteCanvas.getLayerManager();



        ArrayList<Layer> layers = lm.getLayers();
        System.out.print(layers);
        try{
            layers.forEach((l) -> {
                System.out.println("Reached");
                final long[] smallestX = { 999999999 };
                final long[] biggestX = { 0 };
                final long[] smallestY = { 999999999 };
                final long[] biggestY = { 0 };
                switch (l.getLayerType()) {
                    case DRAWING:
                        DrawingLayer currentLayer = (DrawingLayer)l;
                        HashMap chunkMap = currentLayer.getChunkMap();
                        chunkMap.forEach((s, c) -> {
                            //System.out.printf("Current chunk - %s : %s\n",s,c);
                            //System.out.printf("Current String working on: %s\n", s);
                            String[] ChunkString = s.toString().split(":");
                            Long currentX = Long.parseLong(ChunkString[0]);
                            Long currentY = Long.parseLong(ChunkString[1]);
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
                        break;
                    case IMAGE:

                        break;
                    default:

                        break;
                }
                System.out.printf("Big and small x %d : %d\n", biggestX[0], smallestX[0]);
                System.out.printf("Big and small y %d : %d\n", biggestY[0], smallestY[0]);
            });

        }
        catch (Exception e)
        {
            System.out.print(e);
        }


        //HashMap chunkMap = super.getApplication().getPluginManager().getActiveCanvasPlugin().getChunkMap();

        //Need to iterate through chunks Smallest x, biggest y
        //Iterate through layers
        int Y;
        int X;

/*
        System.out.print("Full chunkMap = " + chunkMap + "\n");

        try{
            chunkMap.forEach((s, c) -> {
                System.out.printf("Current chunk - %s : %s\n",s,c);
                System.out.printf("Current String working on: %s\n", s);
                String[] ChunkString = s.toString().split(":");
                Long CurrentX = Long.parseLong(ChunkString[0]);
                Long CurrentY = Long.parseLong(ChunkString[1]);
                System.out.printf("%l : %l\n", CurrentX, CurrentY);
            });
        }
        catch (Exception e)
        {
            System.out.print(e);
        }
*/
        //cip code
        //BufferedImage exportImg = new BufferedImage();
        //ImageIO.write(this.getActualImage(), "png", byteArrayOutputStream);
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
