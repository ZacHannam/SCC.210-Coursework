package uk.pixtle.application.plugins.plugins.tools;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.Application;
import uk.pixtle.application.colour.ColourManager;
import uk.pixtle.application.events.annotations.EventHandler;
import uk.pixtle.application.events.events.ColourChangeEvent;
import uk.pixtle.application.events.events.ExampleEvent;
import uk.pixtle.application.plugins.expansions.PluginMiniToolExpansion;
import uk.pixtle.application.plugins.plugins.Plugin;
import uk.pixtle.application.ui.window.minitoollist.TextField;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;
import uk.pixtle.application.ui.window.minitoollist.ColorSquare;
import uk.pixtle.application.ui.window.minitoollist.MiniToolPanel;
import uk.pixtle.application.ui.window.minitoollist.PaletteButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ColourPlugin extends Plugin implements PluginMiniToolExpansion {

    // ---------------------- ABSTRACT METHODS ------------------

    // ---------------------- TEST METHODS ----------------------
    @EventHandler
    public void test(ExampleEvent paramEvent) {
        System.out.println(paramEvent.getCreationTime().toString());
    }

    // ---------------------- MINI TOOL EXPANSION METHODS ----------------------

    @Getter
    @Setter
    MiniToolPanel miniToolPanel;
    ColourManager colourManager;

    @Override
    public int getMiniToolPanelHeight() {
        return 450;
    }

    @Override
    public void instanceMiniToolPanel(MiniToolPanel paramMiniToolPanel) {
        this.setMiniToolPanel(paramMiniToolPanel);
        this.colourManager = super.getApplication().getColourManager();

        AnchoredComponent anchoredComponent = new AnchoredComponent();
        anchoredComponent.createAnchor(Anchor.DirectionType.X, 10);
        anchoredComponent.createAnchor(Anchor.DirectionType.X, -10);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, 10);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, -10);


        BoxLayout bl = new BoxLayout(paramMiniToolPanel, BoxLayout.Y_AXIS);

        GridLayout gl = new GridLayout(0,1);

        FlowLayout fl = new FlowLayout(FlowLayout.CENTER);

        paramMiniToolPanel.setLayout(gl); //Currently only works with gridlayout... don't know why

        ColourPalette(paramMiniToolPanel);
        HexText(paramMiniToolPanel);
        RGBText(paramMiniToolPanel);
        ColourPreview(paramMiniToolPanel);
        ColourGrid(paramMiniToolPanel);

        paramMiniToolPanel.setBackground(Color.WHITE);
    }

    // --------------------- PLUGINS ----------------------

    PaletteButton colourButtons[] = new PaletteButton[9];

    public void ColourPalette(MiniToolPanel paramMiniToolPanel)
    {
        //this.setMiniToolPanel(paramMiniToolPanel);
        JPanel componentPanel = new JPanel();
        componentPanel.setPreferredSize(new Dimension(20,150));

        GridLayout colourPalettLayout = new GridLayout(0,2);
        componentPanel.setLayout(colourPalettLayout);

        for(int i=0;i<8;i++)
        {
            colourButtons[i] = new PaletteButton();
            colourButtons[i].setCurrentColour(Color.WHITE);
            colourButtons[i].setBackground(colourButtons[i].getCurrentColour());
            componentPanel.add(colourButtons[i]);
        }




        for(int i=0;i<8;i++) {
            int finalI = i;
            colourButtons[i].addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {

                }

                @Override
                public void mousePressed(MouseEvent mouseEvent) {
                    if(SwingUtilities.isLeftMouseButton(mouseEvent))
                        colourManager.setColorOfActiveSlot(colourButtons[finalI].getCurrentColour());
                    if(SwingUtilities.isRightMouseButton(mouseEvent))
                    {
                        colourButtons[finalI].setCurrentColour(colourManager.getActiveColor());
                    }
                }

                @Override
                public void mouseReleased(MouseEvent mouseEvent) {

                }

                @Override
                public void mouseEntered(MouseEvent mouseEvent) {

                }

                @Override
                public void mouseExited(MouseEvent mouseEvent) {

                }
            });
        }
        miniToolPanel.add(componentPanel);
    }

    TextField hexJTextField;
    TextField RGBJTextField;

    Color colour;
    public void HexText(MiniToolPanel paramMiniToolPanel)
    {
        //this.setMiniToolPanel(paramMiniToolPanel);
        JPanel componentPanel = new JPanel();
        componentPanel.setPreferredSize(new Dimension(20,50));
        JLabel jLabel = new JLabel("#"); //Add action listner for text appearing
        //jLabel.setFont(f);
        //JTextField jTextField = new JTextField("Hex value");
        hexJTextField = new TextField("Hex value", "Hex value");
        //jLabel.setAutoscrolls(true);

        //paramMiniToolPanel.add(jLabel, anchoredComponent);
        //paramMiniToolPanel.add(jTextField, anchoredComponent);
        BorderLayout hexLayout = new BorderLayout();
        componentPanel.setLayout(hexLayout);
        componentPanel.add(jLabel, BorderLayout.WEST);
        componentPanel.add(hexJTextField, BorderLayout.CENTER);
        componentPanel.setBackground(Color.LIGHT_GRAY);

        hexJTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                hexJTextField.setForeground(Color.BLACK);
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if(HexValidate(hexJTextField.getText()))
                {
                    colourManager.setColorOfActiveSlot(colour);
                }
                else{
                    if(!hexJTextField.getText().equals("Hex value")) {
                        hexJTextField.setForeground(Color.RED);
                    }
                }
            }

        });
        miniToolPanel.add(componentPanel);
    }

    private boolean HexValidate(String input){
        input=input.trim(); //Gets rid of leading and trailing spaces.
        input=input.replace("#", ""); //Gets rid of any hashtags, as we put one in anyway.
        if(input.length()>6){
            return false;
        }
        try {
            colour=Color.decode('#' + input);
        }catch(NumberFormatException e){
            return false;
        }
        return true;
    }

    private String toHexString(int rgb) {

        StringBuilder s = new StringBuilder(Integer.toHexString(rgb));

        if(s.toString().length() == 1) {
            s.insert(0, "0");
        }

        return s.toString();
    }

    public void RGBText(MiniToolPanel paramMiniToolPanel)
    {
        //this.setMiniToolPanel(paramMiniToolPanel);
        JPanel componentPanel = new JPanel();
        componentPanel.setPreferredSize(new Dimension(20,50));
        miniToolPanel.add(componentPanel);
        JLabel jLabel = new JLabel("RGB");
        RGBJTextField = new TextField("RGB value: R,G,B", "RGB value: R,G,B");
        jLabel.setAutoscrolls(true);

        //paramMiniToolPanel.add(jLabel, anchoredComponent);
        BorderLayout rgbLayout = new BorderLayout();
        componentPanel.setLayout(rgbLayout);
        componentPanel.add(jLabel, BorderLayout.WEST);
        componentPanel.add(RGBJTextField, BorderLayout.CENTER);
        componentPanel.setBackground(Color.LIGHT_GRAY);

        RGBJTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                RGBJTextField.setForeground(Color.BLACK);
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if(validateRGB(RGBJTextField.getText()))
                {
                    colourManager.setColorOfActiveSlot(colour);
                }
                else{
                    if(!RGBJTextField.getText().equals("RGB value: R,G,B")) {
                        RGBJTextField.setForeground(Color.RED);
                    }
                }
            }

        });
    }

    public boolean validateRGB(String input){
        input=input.trim();
        String[] RGB_Sections = input.split("\\s*,\\s*", 0);
        int[] RGB_values=new int[3];
        for(int i=0;i<3;i++){
            try{
                int value=Integer.parseInt(RGB_Sections[i]);
                RGB_values[i]=value;
                if(value<0 || value>=256){
                    return false;
                }
            }catch(NumberFormatException e){
                return false;
            }
        }
        colour=new Color(RGB_values[0],RGB_values[1],RGB_values[2]);
        return true;
    }

    MiniToolPanel ColourPreviewPanel = new MiniToolPanel(new Dimension(1,100));
    public void ColourPreview(MiniToolPanel paramMiniToolPanel)
    {
        //this.setMiniToolPanel(paramMiniToolPanel);
        ColourPreviewPanel.setPreferredSize(new Dimension(20,100));
        miniToolPanel.add(ColourPreviewPanel);
        colourManager = super.getApplication().getColourManager();

        AnchoredComponent anchoredComponent = new AnchoredComponent();
        anchoredComponent.createAnchor(Anchor.DirectionType.X, 10);
        anchoredComponent.createAnchor(Anchor.DirectionType.X, -10);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, 10);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, -10);

        ColourPreviewPanel.setBackground(colourManager.getActiveColor());

    }

    public void ColourGrid(MiniToolPanel paramMiniToolPanel)
    {
        // this.setMiniToolPanel(paramMiniToolPanel);
        JPanel componentPanel = new JPanel();
        componentPanel.setPreferredSize(new Dimension(20,100));
        miniToolPanel.add(componentPanel);

        String[] rgba_Values = {"800000", "8B0000", "A52A2A", "B22222", "DC143C", "FF0000",
                "FF6347", "FF7F50", "CD5C5C", "F08080", "E9967A", "FA8072", "FFA07A", "FF4500",
                "FF8C00", "FFA500", "FFD700", "B8860B", "DAA520", "EEE8AA", "BDB76B", "F0E68C",
                "808000", "FFFF00", "9ACD32", "556B2F", "6B8E23", "7CFC00", "7FFF00", "ADFF2F",
                "006400", "008000", "228B22", "00FF00", "32CD32", "90EE90", "98FB98", "8FBC8F",
                "00FA9A", "00FF7F", "2E8B57", "66CDAA", "3CB371", "20B2AA", "2F4F4F", "008080",
                "008B8B", "00FFFF", "00FFFF", "E0FFFF", "00CED1", "40E0D0", "48D1CC", "AFEEEE",
                "7FFFD4", "B0E0E6", "5F9EA0", "4682B4", "6495ED", "00BFFF", "1E90FF", "ADD8E6",
                "87CEEB", "87CEFA", "191970", "000080", "00008B", "0000CD", "0000FF", "4169E1",
                "8A2BE2", "4B0082", "483D8B", "6A5ACD", "7B68EE", "9370DB", "8B008B", "9400D3",
                "9932CC", "BA55D3", "800080", "D8BFD8", "DDA0DD", "EE82EE", "FF00FF", "DA70D6",
                "C71585", "DB7093", "FF1493", "FF69B4", "FFB6C1", "FFC0CB", "FAEBD7", "F5F5DC",
                "FFE4C4", "FFEBCD", "F5DEB3", "FFF8DC", "FFFACD", "FAFAD2", "FFFFE0", "8B4513",
                "A0522D", "D2691E", "CD853F", "F4A460", "DEB887", "D2B48C", "BC8F8F", "FFE4B5",
                "FFDEAD", "FFDAB9", "FFE4E1", "FFF0F5", "FAF0E6", "FDF5E6", "FFEFD5", "FFF5EE",
                "F5FFFA", "708090", "778899", "B0C4DE", "E6E6FA", "FFFAF0", "F0F8FF", "F8F8FF",
                "F0FFF0", "FFFFF0", "F0FFFF", "FFFAFA", "000000", "696969", "808080", "A9A9A9",
                "C0C0C0", "D3D3D3", "DCDCDC", "F5F5F5", "FFFFFF"};


        GridLayout colourWheelGrid = new GridLayout(9,32);

        componentPanel.setLayout(colourWheelGrid);
        ColorSquare colourWheel[] = new ColorSquare[139];
        for(int i=0;i<138;i++){

            colourWheel[i] = new ColorSquare("#"+rgba_Values[i]);
            componentPanel.add(colourWheel[i]);
            int finalI = i;
            colourWheel[i].addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    colourManager.setColorOfActiveSlot(Color.decode("#"+rgba_Values[finalI]));
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    //colourManager.setColorOfActiveColor();
                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {
                    //colourPreview.setBackground(rgba_Values[finalI]);
                }
            });
        }

    }

    @EventHandler
    public void colourChangeEvent(ColourChangeEvent event){
        Color currentColour = colourManager.getActiveColor();
        int r = currentColour.getRed();
        int g = currentColour.getGreen();
        int b = currentColour.getBlue();

        String Hex_rgb = toHexString(r) + toHexString(g)  + toHexString(b);
        hexJTextField.setText(Hex_rgb.toUpperCase());
        RGBJTextField.setText(r+","+g+","+b);

        ColourPreviewPanel.setBackground(colourManager.getActiveColor());

    }

    // ---------------------- CONSTRUCTOR ----------------------

    public ColourPlugin(Application paramApplication) {
        super(paramApplication);

        super.getApplication().getEventManager().registerEvents(this);
    }

}
