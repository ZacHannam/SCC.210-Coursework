package uk.pixtle.components.ui.elements.menubar;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.components.ui.layouts.anchorlayout.anchors.Anchor;
import uk.pixtle.components.ui.layouts.anchorlayout.AnchoredComponent;

import javax.swing.*;
import java.util.HashMap;

public class MenuBar extends JMenuBar implements MenuBarUI {

    private static final int HEIGHT = 20;

    @Getter
    @Setter
    private HashMap<String, JMenu> menus = new HashMap<>();

    /*
    -------------------- UIComponent Methods --------------------
     */

    @Override
    public AnchoredComponent getAnchors() {
        AnchoredComponent anchoredComponent = new AnchoredComponent();
        anchoredComponent.createAnchor(AnchoredComponent.StandardX.LEFT);
        anchoredComponent.createAnchor(AnchoredComponent.StandardX.RIGHT);
        anchoredComponent.createAnchor(AnchoredComponent.StandardY.TOP);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, 20);

        return anchoredComponent;
    }

    /*
    -------------------- ToolsUI Methods --------------------
     */

    /*
    -------------------- Layout Methods --------------------
     */

    private void addMenuBar(JMenu paramJMenu) {
        super.add(paramJMenu);
        this.getMenus().put(paramJMenu.getName(), paramJMenu);
    }

    public JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");

        JMenuItem newItem = new JMenuItem("New");
        fileMenu.add(newItem);
        JMenuItem openItem = new JMenuItem("Open");
        fileMenu.add(openItem);
        JMenuItem saveItem = new JMenuItem("Save");
        fileMenu.add(saveItem);

        return fileMenu;
    }

    public JMenu createEditMenu() {
        JMenu editMenu = new JMenu("Edit");

        return editMenu;
    }

    /*
    -------------------- Constructor --------------------
     */

    public MenuBar(){
        this.addMenuBar(this.createFileMenu());
        this.addMenuBar(this.createEditMenu());

    }


}
