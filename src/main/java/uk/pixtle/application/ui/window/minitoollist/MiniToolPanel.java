package uk.pixtle.application.ui.window.minitoollist;

import uk.pixtle.application.ui.layouts.anchorlayout.AnchorLayout;

import javax.swing.*;
import java.awt.*;

public class MiniToolPanel extends JPanel {

    public MiniToolPanel(Dimension paramSize) {
        this.setSize(paramSize);

        this.setLayout(new AnchorLayout(true, true));
    }
}

