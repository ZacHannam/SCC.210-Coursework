package uk.pixtle.application.plugins.plugins.tools;

import uk.pixtle.application.Application;
import uk.pixtle.application.plugins.annotations.MenuBarItem;
import uk.pixtle.application.plugins.expansions.PluginMiniToolExpansion;
import uk.pixtle.application.ui.window.minitoollist.MiniToolPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        HashMap map=super.getApplication().getPluginManager().getActiveCanvasPlugin().getChunkMap();
        //Need to iterate through chunks?

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
