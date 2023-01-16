package uk.pixtle.application.ui.window.minitoollist;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

public class PaletteButton extends JButton
{
    public PaletteButton()
    {
        new JButton();
    }


    @Getter
    public Color currentColour;

    public void setCurrentColour(Color col)
    {
        currentColour = col;
        this.setBackground(col);
    }
}
