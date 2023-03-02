package uk.pixtle.application.plugins.plugins.tools.colourplugin;

import javax.swing.*;
import java.awt.*;


public class ColorSquare extends JButton {

    protected String color;

    public ColorSquare(String col){
        super.setBorder(BorderFactory.createLineBorder(Color.black, 1, false));
        super.setOpaque(true);
        this.color = col;
        this.setBackground(Color.decode(col));
    }




}
