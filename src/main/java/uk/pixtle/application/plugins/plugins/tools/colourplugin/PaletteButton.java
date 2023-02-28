package uk.pixtle.application.plugins.plugins.tools.colourplugin;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

public class PaletteButton extends JButton
{
    public PaletteButton()
    {
        super.setOpaque(true);
        super.setBorder(BorderFactory.createLineBorder(Color.black, 1, false));
        super.setToolTipText("Left click to get the colour or right click to set the colour");
    }


    @Getter
    public Color currentColour;

    public void setCurrentColour(Color col)
    {
        currentColour = col;
        this.setBackground(col);
    }
}
