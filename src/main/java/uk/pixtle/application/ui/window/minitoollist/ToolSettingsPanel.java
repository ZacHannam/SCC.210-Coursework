package uk.pixtle.application.ui.window.minitoollist;

import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Text;
import uk.pixtle.application.plugins.toolsettings.ToolSetting;
import uk.pixtle.application.plugins.toolsettings.ToolSettingEntry;
import uk.pixtle.application.plugins.toolsettings.inputdevices.DropDownInputDevice;
import uk.pixtle.application.plugins.toolsettings.inputdevices.SliderInputDevice;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;

public final class ToolSettingsPanel extends JPanel {

    @Getter
    @Setter
    private int defaultWidth;

    @Getter
    @Setter
    private ArrayList<ToolSettingEntry<?>> toolSettingEntries;


    private static JLabel createDefaultLabel(String paramText, int paramSize, Color paramColor) {
        JLabel text = new JLabel(paramText);
        text.setAlignmentX(Component.CENTER_ALIGNMENT);
        text.setFont(new Font ("Consolas", Font.PLAIN, paramSize));
        text.setForeground(paramColor);

        return text;
    }

    public void draw() {

        boolean isFirstEntry = true;
        for(ToolSettingEntry<?> toolSettingEntry : this.getToolSettingEntries()) {

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            if(toolSettingEntry.getTitle() != null) {
                panel.add(createDefaultLabel(toolSettingEntry.getTitle(), 15, Color.BLACK));
            }

            switch(toolSettingEntry.getInputDevice().getInputDeviceType()) {
                case SLIDER:
                    JSlider slider = new JSlider();
                    slider.setMaximum(((SliderInputDevice) toolSettingEntry.getInputDevice()).getMaxValue());
                    slider.setMinimum(((SliderInputDevice) toolSettingEntry.getInputDevice()).getMinValue());
                    slider.setValue((int) toolSettingEntry.getValue());

                    ((SliderInputDevice) toolSettingEntry.getInputDevice()).renderer(slider);

                    panel.add(slider);

                    slider.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            ((ToolSettingEntry<Integer>) toolSettingEntry).setValue(slider.getValue());
                            ((ToolSettingEntry<Integer>) toolSettingEntry).notifyVariableChange(slider.getValue());
                        }
                    });

                    if(((SliderInputDevice) toolSettingEntry.getInputDevice()).paintCurrentValue()) {
                        JLabel sliderLabel = createDefaultLabel(String.valueOf(slider.getValue()), 10, Color.black);
                        panel.add(sliderLabel);

                        slider.addChangeListener(new ChangeListener() {
                            @Override
                            public void stateChanged(ChangeEvent e) {
                                sliderLabel.setText(String.valueOf(slider.getValue()));
                            }
                        });
                    }

                    ((ToolSettingEntry<Integer>) toolSettingEntry).notifyVariableChange(slider.getValue());

                    break;

                case DROP_DOWN:

                    JComboBox<String> comboBox = new JComboBox<String>(((DropDownInputDevice) toolSettingEntry.getInputDevice()).getValues());

                    panel.add(comboBox);

                    ((ToolSettingEntry<String>) toolSettingEntry).setValue((String) comboBox.getSelectedItem());
                    ((ToolSettingEntry<String>) toolSettingEntry).notifyVariableChange((String) comboBox.getSelectedItem());

                    comboBox.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            ((ToolSettingEntry<String>) toolSettingEntry).setValue((String) comboBox.getSelectedItem());
                            ((ToolSettingEntry<String>) toolSettingEntry).notifyVariableChange((String) comboBox.getSelectedItem());
                        }
                    });

                    break;

                default:
                    break;
            }

            if(isFirstEntry) {
                isFirstEntry = false;
            } else {
                super.add(Box.createRigidArea(new Dimension(0, 15)));
            }


            super.add(panel);
        }

        super.setSize(this.getPreferredSize());
    }

    public ToolSettingsPanel(int paramWidth, ArrayList<ToolSettingEntry<?>> paramToolSettingEntries) {
        this.setToolSettingEntries(paramToolSettingEntries);
        this.setDefaultWidth(paramWidth);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.draw();
    }
}
