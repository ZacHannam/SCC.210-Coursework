package uk.pixtle.application.ui.window.minitoollist;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class TextField extends JTextField implements FocusListener {

    private String ptext;

    public TextField(String text, String presetText){
        super(text);
        this.addFocusListener(this);
        ptext = presetText;
    }

    public void focusGained(FocusEvent e){
        if(this.getText().equals(ptext)){
            this.setText("");
        }
    }

    public void focusLost(FocusEvent e){
        if(this.getText().equals("")){
            this.setText(ptext);
        }
    }
}


