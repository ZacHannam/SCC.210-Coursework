package uk.pixtle.application.plugins.plugins.tools;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import uk.pixtle.application.Application;
import uk.pixtle.application.events.annotations.EventHandler;
import uk.pixtle.application.events.events.CanvasResetEvent;
import uk.pixtle.application.plugins.Plugins;
import uk.pixtle.application.plugins.annotations.MenuBarItem;
import uk.pixtle.application.plugins.expansions.PluginMiniToolExpansion;
import uk.pixtle.application.plugins.plugins.Plugin;
import uk.pixtle.application.plugins.plugins.tools.keylistenerplugin.KeyListener;
import uk.pixtle.application.plugins.plugins.tools.keylistenerplugin.PluginKeyListenerPolicy;
import uk.pixtle.application.plugins.policies.PluginSavePolicy;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchorLayout;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;
import uk.pixtle.application.ui.window.minitoollist.MiniToolPanel;
import uk.pixtle.application.ui.window.notifications.Notification;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class FileLoaderPlugin extends ToolPlugin implements PluginMiniToolExpansion, PluginKeyListenerPolicy{

    @Getter
    @Setter
    private String currentFile;


    // ---------------------- TEST METHODS ----------------------

    public void saveFile(String path) {
        try {
            JSONObject savedJSON = new JSONObject();

            for(Map.Entry<Plugins, Plugin> entry :  super.getApplication().getPluginManager().getPluginsByPolicy(PluginSavePolicy.class).entrySet()) {
                PluginSavePolicy savePlugin = (PluginSavePolicy)  entry.getValue();
                savedJSON.put(entry.getKey().toString(), savePlugin.save());
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
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(savedJSON.toString());
            fileWriter.flush();
            this.getApplication().getNotificationManager().displayNotification(Notification.ColourModes.INFO, "File Saved", "Successfully saved file: " + path + ".", 5);

        } catch(Exception exception) {
            this.getApplication().getNotificationManager().displayNotification(Notification.ColourModes.ERROR, "Error Saving File", "Error saving file: " + path + ".\n More details:\n" + exception.getMessage() + "\n" + ExceptionUtils.getStackTrace(exception), -1);
        }

        this.setCurrentFile(path);
    }

    public void loadFile(String path){
        try {
            StringBuilder jsonDataAsString = new StringBuilder();

            File file = new File(path);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                jsonDataAsString.append(data);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(jsonDataAsString.toString());

            for (String key : jsonObject.keySet()) {
                Plugins pluginType = Plugins.valueOf(key);
                Plugin plugin = super.getApplication().getPluginManager().getPluginByPluginType(pluginType);
                if (plugin instanceof PluginSavePolicy) {
                    ((PluginSavePolicy) plugin).load(jsonObject.getJSONObject(key));
                }
                this.getApplication().getNotificationManager().displayNotification(Notification.ColourModes.INFO, "File Loaded", "Successfully loaded file: " + path + ".", 10);
            }

            this.setCurrentFile(path);

        } catch(Exception exception) {
            exception.printStackTrace();
            this.getApplication().getNotificationManager().displayNotification(Notification.ColourModes.ERROR, "Error Loading File", "Error loading file: " + path + ".\n More details:\n" + exception.getMessage() + "\n" + ExceptionUtils.getStackTrace(exception), -1);
            return;
        }
    }

    @MenuBarItem(PATH = "file:Save") @KeyListener(KEY=KeyEvent.VK_S, MODIFIERS = KeyEvent.CTRL_DOWN_MASK)
    public void save() {
        if(this.getCurrentFile() == null) {
            saveAs();
        } else {
            saveFile(this.getCurrentFile());
        }
    }


    @MenuBarItem(PATH = "file:Save As")
    public void saveAs() {
        JFileChooser fileChooser = new JFileChooser();
        FileFilter fileFilter = new FileNameExtensionFilter("Pixtle File", "pix");
        fileChooser.setFileFilter(fileFilter);
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.setDragEnabled(true);

        int option = fileChooser.showSaveDialog(fileChooser);
        if(option == JFileChooser.APPROVE_OPTION)
        {
            File fileToSave = fileChooser.getSelectedFile();
            String path = fileToSave.getAbsolutePath();
            if(!path.endsWith(".pix")) {
                path += ".pix";
            }
            saveFile(path);
        }

    }

    @MenuBarItem(PATH = "file:Open File") @KeyListener(KEY=KeyEvent.VK_L, MODIFIERS = KeyEvent.CTRL_DOWN_MASK)
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
            loadFile(fileToOpen.getAbsolutePath());
        }

    }

    @EventHandler
    public void canvasResetEvent(CanvasResetEvent event) {
        this.setCurrentFile(null);
    }

    @MenuBarItem(PATH = "file:New File")
    public void newFile() {

        JDialog dialog = new JDialog(this.getApplication().getUIManager().getWindow(), true);
        dialog.setSize(new Dimension(300, 140));
        dialog.setLocationRelativeTo(this.getApplication().getUIManager().getWindow());
        dialog.setLayout(new AnchorLayout());

        AnchoredComponent textAnchors = new AnchoredComponent();
        textAnchors.createAnchor(AnchoredComponent.StandardX.RIGHT);
        textAnchors.createAnchor(AnchoredComponent.StandardX.LEFT);
        textAnchors.createAnchor(Anchor.DirectionType.Y, 40);
        textAnchors.createAnchor(AnchoredComponent.StandardY.TOP);

        JLabel text = new JLabel("<html><center>Are you sure you would like to create a new file without saving?<center></html>", SwingConstants.CENTER);
        dialog.add(text, textAnchors);

        AnchoredComponent saveFirstButtonAnchors = new AnchoredComponent();
        saveFirstButtonAnchors.createAnchor(Anchor.DirectionType.X, 100);
        saveFirstButtonAnchors.createAnchor(AnchoredComponent.StandardX.LEFT);
        saveFirstButtonAnchors.createAnchor(Anchor.DirectionType.Y, 40);
        saveFirstButtonAnchors.createAnchor(AnchoredComponent.StandardY.BOTTOM);

        JButton saveFirstButton = new JButton("Save File");
        dialog.add(saveFirstButton, saveFirstButtonAnchors);

        saveFirstButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
                getApplication().getPluginManager().getActiveCanvasPlugin().reset();
                dialog.setVisible(false);
            }
        });

        AnchoredComponent yesButtonAnchors = new AnchoredComponent();
        yesButtonAnchors.createAnchor(Anchor.DirectionType.X, 200);
        yesButtonAnchors.createAnchor(Anchor.DirectionType.X, 100);
        yesButtonAnchors.createAnchor(Anchor.DirectionType.Y, 40);
        yesButtonAnchors.createAnchor(AnchoredComponent.StandardY.BOTTOM);

        JButton yesButton = new JButton("Confirm");
        dialog.add(yesButton, yesButtonAnchors);

        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getApplication().getPluginManager().getActiveCanvasPlugin().reset();
                dialog.setVisible(false);
            }
        });

        AnchoredComponent cancelButtonAnchors = new AnchoredComponent();
        cancelButtonAnchors.createAnchor(Anchor.DirectionType.X, 200);
        cancelButtonAnchors.createAnchor(Anchor.DirectionType.X, 300);
        cancelButtonAnchors.createAnchor(Anchor.DirectionType.Y, 40);
        cancelButtonAnchors.createAnchor(AnchoredComponent.StandardY.BOTTOM);

        JButton cancelButton = new JButton("Cancel");
        dialog.add(cancelButton, cancelButtonAnchors);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        });

        dialog.setVisible(true);
    }

    // ---------------------- MINI TOOL EXPANSION METHODS ----------------------

    @Getter
    @Setter
    MiniToolPanel miniToolPanel;

    @Override
    public int getMiniToolPanelHeight() {
        return 110;
    }

    @Override
    public void instanceMiniToolPanel(MiniToolPanel paramMiniToolPanel) {
        this.setMiniToolPanel(paramMiniToolPanel);

        AnchoredComponent fileLoaderText = new AnchoredComponent();
        fileLoaderText.createAnchor(Anchor.DirectionType.Y, 0);
        fileLoaderText.createAnchor(AnchoredComponent.StandardX.LEFT);
        fileLoaderText.createAnchor(AnchoredComponent.StandardX.RIGHT);
        fileLoaderText.createAnchor(Anchor.DirectionType.Y, 20);

        JLabel text = new JLabel("Files");
        text.setHorizontalAlignment(0);
        text.setFont(new Font ("Consolas", Font.PLAIN, 18));
        text.setForeground(Color.black);

        paramMiniToolPanel.add(text, fileLoaderText);

        AnchoredComponent saveButtonAnchoredComponent = new AnchoredComponent();
        saveButtonAnchoredComponent.createAnchor(Anchor.DirectionType.Y, 20);
        saveButtonAnchoredComponent.createAnchor(AnchoredComponent.StandardX.RIGHT);
        saveButtonAnchoredComponent.createAnchor(AnchoredComponent.StandardX.LEFT);
        saveButtonAnchoredComponent.createAnchor(Anchor.DirectionType.Y, 50);

        JButton saveButton = new JButton("Save");
        saveButton.setBorder(BorderFactory.createLineBorder(Color.lightGray, 3, true));
        paramMiniToolPanel.add(saveButton, saveButtonAnchoredComponent);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });

        AnchoredComponent saveAsButtonAnchoredComponent = new AnchoredComponent();
        saveAsButtonAnchoredComponent.createAnchor(Anchor.DirectionType.Y, 50);
        saveAsButtonAnchoredComponent.createAnchor(AnchoredComponent.StandardX.RIGHT);
        saveAsButtonAnchoredComponent.createAnchor(AnchoredComponent.StandardX.LEFT);
        saveAsButtonAnchoredComponent.createAnchor(Anchor.DirectionType.Y, 80);

        JButton saveAsButton = new JButton("Save as");
        saveAsButton.setBorder(BorderFactory.createLineBorder(Color.lightGray, 3, true));
        paramMiniToolPanel.add(saveAsButton, saveAsButtonAnchoredComponent);

        saveAsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAs();
            }
        });

        AnchoredComponent loadButtonAnchoredComponent = new AnchoredComponent();
        loadButtonAnchoredComponent.createAnchor(Anchor.DirectionType.Y, 80);
        loadButtonAnchoredComponent.createAnchor(AnchoredComponent.StandardX.RIGHT);
        loadButtonAnchoredComponent.createAnchor(AnchoredComponent.StandardX.LEFT);
        loadButtonAnchoredComponent.createAnchor(Anchor.DirectionType.Y, 110);

        JButton loadButton = new JButton("Load File");
        loadButton.setBorder(BorderFactory.createLineBorder(Color.lightGray, 3, true));
        paramMiniToolPanel.add(loadButton, loadButtonAnchoredComponent);

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                load();
            }
        });
    }

    // ---------------------- CONSTRUCTOR ----------------------

    public FileLoaderPlugin(Application paramApplication) {
        super(paramApplication);

        super.getApplication().getEventManager().registerEvents(this);

        this.setCurrentFile(null);
    }
}

