package uk.pixtle.application.ui.window.minitoollist;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

public class ColourButton extends JPanel {

    @Getter
    @Setter
    JPanel backgroundColor;

    public void setColour(Color paramColour) {
        if(this.getBackgroundColor() != null) {
            this.getBackgroundColor().setBackground(paramColour);
        }
    }


    public ColourButton() {

        JPanel colour = new JPanel();
        colour.setOpaque(true);
        colour.setBackground(Color.lightGray);
        colour.setBorder(BorderFactory.createLineBorder(Color.black, 1, false));
        super.add(colour);

        this.setBackgroundColor(colour);

        JLabel text = new JLabel("Click to set colour");
        super.add(text);
    }

}
