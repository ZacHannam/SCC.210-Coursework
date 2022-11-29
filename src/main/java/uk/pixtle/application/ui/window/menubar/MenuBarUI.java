package uk.pixtle.application.ui.window.menubar;

import uk.pixtle.application.plugins.plugins.Plugin;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MenuBarUI extends JMenuBar implements MenuBar {

    private static final int HEIGHT = 20;

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
    -------------------- Util Methods --------------------
     */

    private String toCapitalCase(String paramString) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Character.toUpperCase(paramString.charAt(0)));
        stringBuilder.append(paramString.substring(1).toLowerCase());
        return stringBuilder.toString();
    }

    private String[] convertPathToCapital(String paramPath) {
        String[] oldPath = paramPath.split(":");
        String[] newPath = new String[oldPath.length];

        for(int index = 0; index < oldPath.length; index++) {
            newPath[index] = toCapitalCase(oldPath[index]);
        }

        return newPath;
    }

    /*
    -------------------- MenuBar Addition Methods --------------------
     */

    @Override
    public void addNewMenuItem(String paramPath, Plugin paramPlugin, Method paramMethod) {

        String[] path = convertPathToCapital(paramPath);

        if(path.length <= 0) {
            // TO-DO throw new exception
            return;
        }

        JMenuItem item = new JMenuItem(path[path.length - 1]);
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    paramMethod.invoke(paramPlugin);
                } catch (IllegalAccessException | InvocationTargetException exception) {
                    // TO-DO
                    return;
                }
            }
        });

        if(path.length == 1) {
            super.add(item);
            return;
        }

        JMenu lastMenu = null;

        for(int menuIndex = 0; menuIndex < super.getMenuCount(); menuIndex++){
            if(super.getMenu(menuIndex).getText().equals(path[0])) {
                lastMenu = super.getMenu(menuIndex);
                break;
            }
        }

        if(lastMenu == null) {
            JMenu newMenu = new JMenu(path[0]);
            super.add(newMenu);
            lastMenu = newMenu;
        }

        for(int index = 1; index < path.length-1; index++) {

            boolean found = false;

            for(int menuIndex = 0; menuIndex < lastMenu.getItemCount(); menuIndex++){
                if(super.getMenu(menuIndex).getText().equals(path[index])) {
                    lastMenu = super.getMenu(menuIndex);
                    found = true;
                    break;
                }
            }

            if(!found) {
                JMenu newMenu = new JMenu(path[index]);
                lastMenu.add(newMenu);
                lastMenu = newMenu;
            }
        }

        lastMenu.add(item);

        super.updateUI();
    }

    public void createDefaultMenuBarItems() {
        JMenu file = new JMenu("File");
        super.add(file);

        JMenu edit = new JMenu("Edit");
        super.add(edit);
    }

    /*
    -------------------- Constructor --------------------
     */

   public MenuBarUI() {
        createDefaultMenuBarItems();
   }
}
