package uk.pixtle.application.ui.window.minitoollist;
import uk.pixtle.application.colour.ColourManager;

import javax.swing.*;
import java.awt.*;


public class ColorSquare extends JButton {

    protected String color;

    public ColorSquare(String col){
            this.color = col;
            this.setBackground(java.awt.Color.decode(col));
    }




}
