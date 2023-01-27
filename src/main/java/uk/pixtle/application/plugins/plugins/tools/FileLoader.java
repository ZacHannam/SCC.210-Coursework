package uk.pixtle.application.plugins.plugins.tools;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import uk.pixtle.application.Application;
import uk.pixtle.application.events.annotations.EventHandler;
import uk.pixtle.application.events.events.ExampleEvent;
import uk.pixtle.application.plugins.Plugins;
import uk.pixtle.application.plugins.annotations.MenuBarItem;
import uk.pixtle.application.plugins.expansions.PluginMiniToolExpansion;
import uk.pixtle.application.plugins.expansions.PluginSavableExpansion;
import uk.pixtle.application.plugins.plugins.Plugin;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;
import uk.pixtle.application.ui.window.minitoollist.MiniToolPanel;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FileLoader extends Plugin implements PluginMiniToolExpansion {

    // ---------------------- TEST METHODS ----------------------

    public void saveFile(String path) {
        JSONObject savedJSON = new JSONObject();

        for(Map.Entry<Plugins, Plugin> entry :  super.getApplication().getPluginManager().getPluginRegistry().entrySet()) {
            if(entry.getValue() instanceof PluginSavableExpansion) {
                PluginSavableExpansion savePlugin = (PluginSavableExpansion)  entry.getValue();
                savedJSON.put(entry.getKey().toString(), savePlugin.save());
            }
        }

        File file = new File(path);
        if(file == null) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        file.setWritable(true);
        try {
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(savedJSON.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @MenuBarItem(PATH = "file:Save")
    public void save() {

    }


    @MenuBarItem(PATH = "file:Save As")
    public void saveAs() throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        FileFilter fileFilter = new FileNameExtensionFilter("Pixtle File", "pix");
        fileChooser.setFileFilter(fileFilter);
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.setDragEnabled(true);

        int option = fileChooser.showSaveDialog(fileChooser);
        if(option == JFileChooser.APPROVE_OPTION)
        {
            File fileToSave = fileChooser.getSelectedFile();
            System.out.println("Save as file: " + fileToSave.getAbsolutePath());
            //Write code here to save our canvas.
            if(fileToSave.createNewFile())
                System.out.println("File saved");
        }

    }

    @MenuBarItem(PATH = "file:Load")
    public void load() {
        JFileChooser fileChooser = new JFileChooser();
        FileFilter fileFilter = new FileNameExtensionFilter("Pixtle File", "pix");
        fileChooser.setFileFilter(fileFilter);
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.setDragEnabled(true);

        int option = fileChooser.showOpenDialog(fileChooser);
        if(option == JFileChooser.APPROVE_OPTION)
        {
            File fileToOpen = fileChooser.getSelectedFile();
            System.out.println("Load file: " + fileToOpen.getAbsolutePath());
            //Write code here to save our canvas.

        }

    }

    @EventHandler
    public void test(ExampleEvent paramEvent) {
        System.out.println(paramEvent.getCreationTime().toString());
    }

    // ---------------------- MINI TOOL EXPANSION METHODS ----------------------

    @Getter
    @Setter
    MiniToolPanel miniToolPanel;

    @Override
    public int getMiniToolPanelHeight() {
        return 200;
    }

    @Override
    public void instanceMiniToolPanel(MiniToolPanel paramMiniToolPanel) {
        this.setMiniToolPanel(paramMiniToolPanel);

        AnchoredComponent anchoredComponent = new AnchoredComponent();
        anchoredComponent.createAnchor(Anchor.DirectionType.X, 10);
        anchoredComponent.createAnchor(Anchor.DirectionType.X, -10);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, 10);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, -10);

        JLabel jLabel = new JLabel("Example Plugin");
        jLabel.setAutoscrolls(true);

        paramMiniToolPanel.add(jLabel, anchoredComponent);

        paramMiniToolPanel.setBackground(Color.WHITE);
    }

    // ---------------------- CONSTRUCTOR ----------------------

    public FileLoader(Application paramApplication) {
        super(paramApplication);

        super.getApplication().getEventManager().registerEvents(this);
    }

}

