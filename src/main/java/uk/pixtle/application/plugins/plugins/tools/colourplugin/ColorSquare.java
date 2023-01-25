package uk.pixtle.application.plugins.plugins.tools.colourplugin;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;


public class ColorSquare extends JButton {

    protected String color;

    public ColorSquare(String col){
        super.setBorder(BorderFactory.createLineBorder(Color.black, 1, false));
        super.setOpaque(true);
        this.color = col;
        this.setBackground(java.awt.Color.decode(col));
    }




}
