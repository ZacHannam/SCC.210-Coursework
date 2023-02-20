package uk.pixtle.application.ui.window.toollist;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.plugins.expansions.PluginToolTipExpansion;
import uk.pixtle.application.plugins.plugins.Plugin;
import uk.pixtle.util.ResourceHandler;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

class ToolButtonComponentAdapter extends ComponentAdapter {

    @Getter
    @Setter
    Image originalImage;

    @Getter
    @Setter
    ImageIcon imageIcon;

    @Getter
    @Setter
    ToolButton toolButton;

    @Override
    public void componentResized(ComponentEvent e) {
        Image transformedImage = this.getOriginalImage().getScaledInstance(this.getToolButton().getWidth()
                , this.getToolButton().getHeight(), Image.SCALE_SMOOTH);
        imageIcon.setImage(transformedImage);

        this.getToolButton().setIcon(new ImageIcon(transformedImage));
    }

    ToolButtonComponentAdapter(ToolButton paramToolButton, ImageIcon paramImageIcon, Image paramOriginalImage) {
        this.setImageIcon(paramImageIcon);
        this.setOriginalImage(paramOriginalImage);
        this.setToolButton(paramToolButton);
    }
}

public class ToolButton extends JButton {

    @Getter
    @Setter
    ToolButtonComponentAdapter toolButtonComponentAdapter;

    @Getter
    @Setter
    Plugin parentPlugin;

    @Getter
    @Setter
    ToolListUI parentToolList;

    public ToolButton(ToolListUI paramParentToolList, Plugin paramParentPlugin, String paramIconName) {

        this.setParentToolList(paramParentToolList);
        this.setParentPlugin(paramParentPlugin);

        ImageIcon imageIcon = new ImageIcon(ResourceHandler.getResourceURL(paramIconName));

        this.setToolButtonComponentAdapter(new ToolButtonComponentAdapter(this, imageIcon, imageIcon.getImage()));
        super.addComponentListener(this.getToolButtonComponentAdapter());

        ToolButton toolButton = this;
        super.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                getParentToolList().toolClick(toolButton, getParentPlugin());
            }
        });

        if(paramParentPlugin instanceof PluginToolTipExpansion) {
            toolButton.setToolTipText(((PluginToolTipExpansion) paramParentPlugin).getToolTip());
        }
    }
}