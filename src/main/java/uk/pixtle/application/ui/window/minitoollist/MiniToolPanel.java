package uk.pixtle.application.ui.window.minitoollist;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.plugins.plugins.Plugin;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchorLayout;

import javax.swing.*;
import java.awt.*;

public class MiniToolPanel extends JPanel {

    @Getter
    @Setter
    private Plugin plugin;

    public MiniToolPanel(Plugin paramPlugin, Dimension paramSize) {
        super.setSize(paramSize);
        this.setPlugin(paramPlugin);

        super.setLayout(new AnchorLayout(true, true));
    }

    public void updateHeight(int paramNewHeight) {
        this.getPlugin().getApplication().getUIManager().getWindow().getMiniToolList().updateHeightOfMMiniToolPanel(this.getPlugin(), paramNewHeight);
    }
}

