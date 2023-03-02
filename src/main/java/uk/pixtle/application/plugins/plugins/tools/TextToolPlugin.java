package uk.pixtle.application.plugins.plugins.tools;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.Application;
import uk.pixtle.application.colour.ColourManager;
import uk.pixtle.application.events.annotations.EventHandler;
import uk.pixtle.application.events.events.LayerChangeEvent;
import uk.pixtle.application.plugins.expansions.PluginToolExpansion;
import uk.pixtle.application.plugins.expansions.PluginToolTipExpansion;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.InfiniteCanvasPlugin;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.Layer;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.LayerType;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.textlayer.TextLayer;
import uk.pixtle.application.plugins.plugins.tools.keylistenerplugin.KeyListener;
import uk.pixtle.application.plugins.plugins.tools.keylistenerplugin.PluginKeyListenerPolicy;
import uk.pixtle.application.plugins.toolsettings.ToolSetting;
import uk.pixtle.application.plugins.toolsettings.ToolSettingEntry;
import uk.pixtle.application.plugins.toolsettings.inputdevices.*;
import uk.pixtle.application.ui.window.minitoollist.ColourButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class TextToolPlugin extends ToolPlugin implements PluginToolExpansion, PluginKeyListenerPolicy, PluginToolTipExpansion {

    @Getter
    @Setter
    JSlider textSizeSlider;

    @Getter
    @Setter
    JComboBox fontComboBox;

    @Getter
    @Setter
    JTextArea text;

    @Getter
    @Setter
    ColourButton foregroundColourButton;

    @KeyListener(KEY = KeyEvent.VK_T, MODIFIERS = 0)
    public void Text() {super.getApplication().getPluginManager().activatePlugin(this);}

    @ToolSetting
    private ToolSettingEntry<Integer> textSize = new ToolSettingEntry<Integer>(25){

        @Override
        public void notifyVariableChange(Integer paramVar) {
            if(getActiveTextLayer() != null)
            getActiveTextLayer().setTextSize(paramVar);
        }

        @Override
        public boolean validateInput(Integer paramInput) {
            return true;
        }

        @Override
        public String getTitle() {
            return "Text Size";
        }

        @Override
        public InputDevice getInputDevice() {
            return new SliderInputDevice(this) {

                @Override
                public int getMinValue() {
                    return 1;
                }

                @Override
                public int getMaxValue() {
                    return 200;
                }

                @Override
                public void renderer(JSlider paramSlider) {
                    setTextSizeSlider(paramSlider);
                }

                @Override
                public boolean paintCurrentValue() {
                    return true;
                }
            };
        }
    };


    @ToolSetting
    private ToolSettingEntry<String> fontType = new ToolSettingEntry<String>(){

        @Override
        public void notifyVariableChange(String paramVar) {
            if(getActiveTextLayer() != null)
                getActiveTextLayer().setFont(paramVar);
        }

        @Override
        public boolean validateInput(String paramInput) {
            return true;
        }

        @Override
        public String getTitle() {
            return "Font";
        }

        @Override
        public InputDevice getInputDevice() {
            return new DropDownInputDevice(this) {

                @Override
                public String[] getValues() {
                    return new String[]{"Arial", "Agency FB","Aharoni","Aldhabi","Andalus","Angsana New","AngsanaUPC","Aparajita","Arabic Typesetting","Bahnschrift","Batang","BatangChe","BIZ UDGothic, BIZ UDPGothic","BIZ UDMincho, BIZ UDPMincho","Book Antiqua","Browallia New","BrowalliaUPC","Calibri","Calisto MT","Cambria","Cambria Math","Candara","Cascadia Code","Century Gothic","Comic Sans MS","Consolas","Constantia","Copperplate Gothic","Corbel","Cordia New","CordiaUPC","Courier New","DaunPenh","David","DengXian","DilleniaUPC","DFKai-SB","DokChampa","Dotum","DotumChe","Ebrima","Estrangelo Edessa","EucrosiaUPC","Euphemia","FangSong","Franklin Gothic","FrankRuehl","FreesiaUPC","Gabriola","Gadugi","Gautami","Georgia","Gill Sans MT","Gisha","Gulim","GulimChe","Gungsuh","GungsuhChe","HoloLens MDL2 Assets","Impact","Ink Free","IrisUPC","Iskoola Pota","JasmineUPC","Javanese Text","KaiTi","Kalinga","Kartika","Khmer UI","KodchiangUPC","Kokila","Lao UI","Latha","Leelawadee","Leelawadee UI","Levenim MT","LilyUPC","Lucida Console","Lucida Handwriting","Lucida Sans Unicode","Malgun Gothic","Mangal","Marlett","Meiryo, Meiryo UI","Microsoft Himalaya","Microsoft JhengHei","Microsoft JhengHei UI","Microsoft New Tai Lue","Microsoft PhagsPa","Microsoft Sans Serif","Microsoft Tai Le","Microsoft Uighur","Microsoft YaHei","Microsoft YaHei UI","Microsoft Yi Baiti","MingLiU, PMingLiU","MingLiU-ExtB, PMingLiU-ExtB","MingLiU_HKSCS","MingLiU_HKSCS-ExtB","Miriam","Miriam Fixed","Mongolian Baiti","MoolBoran","MS Gothic","MS PGothic","MS Mincho","MS PMincho","MS UI Gothic","MV Boli","Myanmar Text","Narkisim","Nirmala UI","NSimSun","Nyala","OCR-A Extended","Palatino Linotype","Plantagenet Cherokee","Raavi","Rod","Sakkal Majalla","Sanskrit Text","Segoe MDL2 Assets","Segoe Print","Segoe Script","Segoe SD","Segoe UI","Segoe UI Emoji","Segoe UI Historic","Segoe UI Symbol","Segoe UI Variable","Segoe Fluent Icons","Shonar Bangla","Shruti","SimHei","Simplified Arabic","SimSun","SimSun-ExtB","Sitka Banner","Sitka Display","Sitka Heading","Sitka Small","Sitka Subheading","Sitka Text","Sylfaen","Symbol","Tahoma","Times New Roman","Traditional Arabic","Trebuchet MS","Tw Cen MT","Tunga","UD Digi Kyokasho N-R","UD Digi Kyokasho N-B","UD Digi Kyokasho NK-R","UD Digi Kyokasho NK-B","UD Digi Kyokasho NP-R","UD Digi Kyokasho NP-B","Urdu Typesetting","Utsaah","Vani","Verdana","Vijaya","Vrinda","Webdings","Wingdings","Yu Gothic","Yu Gothic UI","Yu Mincho"};
                }

                @Override
                public void renderer(JComboBox<String> paramComboBox) {
                    setFontComboBox(paramComboBox);
                }
            };
        }
    };


    @ToolSetting
    private ToolSettingEntry<String> textArea = new ToolSettingEntry<String>(){

        @Override
        public void notifyVariableChange(String paramVar) {
            if(getActiveTextLayer() != null)
                getActiveTextLayer().setText(paramVar);
        }

        @Override
        public boolean validateInput(String paramInput) {
            return true;
        }

        @Override
        public String getTitle() {
            return "Text";
        }

        @Override
        public InputDevice getInputDevice() {
            return new TextAreaInputDevice(this) {

                @Override
                public String defaultText() {
                    if(getActiveTextLayer() == null) {
                        return "Text Layer";
                    } else {
                        return getActiveTextLayer().getText();
                    }
                }

                @Override
                public void renderer(JTextArea paramTextArea) {
                    setText(paramTextArea);
                }
            };
        }
    };

    @ToolSetting
    private ToolSettingEntry<Color> foregroundColor = new ToolSettingEntry<Color>(){

        @Override
        public void notifyVariableChange(Color paramVar) {
            if(getActiveTextLayer() != null)
                getActiveTextLayer().setForegroundColor(paramVar);
        }

        @Override
        public boolean validateInput(Color paramInput) {
            return false;
        }

        @Override
        public String getTitle() {
            return "Foreground Color";
        }

        @Override
        public InputDevice getInputDevice() {
            return new ColourButtonInputDevice(this) {

                @Override
                public Color defaultColour() {
                    return Color.BLACK;
                }

                @Override
                public ColourManager colourManager() {
                    return getApplication().getColourManager();
                }

                @Override
                public void renderer(ColourButton paramColourButton) {
                    setForegroundColourButton(paramColourButton);
                }
            };
        }
    };



    public TextLayer getActiveTextLayer() {
        if(!(super.getApplication().getPluginManager().getActivatePlugin() instanceof InfiniteCanvasPlugin)) {
            // TO-DO error
            return null;
        }
        Layer layer =  ((InfiniteCanvasPlugin) this.getApplication().getPluginManager().getActiveCanvasPlugin()).getLayerManager().getActiveLayer();
        if(layer instanceof TextLayer) return (TextLayer) layer;
        return null;
    }

    public void repaint() {

    }

    public void setSliderValues() {

        TextLayer textLayer = getActiveTextLayer();
        if(textLayer == null) return;

        this.getTextSizeSlider().setValue(textLayer.getTextSize());

        for(int i = 0; i < this.getFontComboBox().getItemCount(); i++) {
            if(((String) this.getFontComboBox().getItemAt(i)).equals(textLayer.getFontType())) {
                this.getFontComboBox().setSelectedIndex(i);
                break;
            }
        }

        this.getText().setText(textLayer.getText());
        this.getForegroundColourButton().setColour(textLayer.getForegroundColor());
    }

    @Override
    public void onEnable() {

        if(!(super.getApplication().getPluginManager().getActivatePlugin() instanceof InfiniteCanvasPlugin)) {
            // TO-DO error
            return;
        }

        InfiniteCanvasPlugin infiniteCanvasPlugin = (InfiniteCanvasPlugin) super.getApplication().getPluginManager().getActiveCanvasPlugin();
        if(infiniteCanvasPlugin.getLayerManager().getActiveLayer().getLayerType() != LayerType.TEXT) {
            infiniteCanvasPlugin.getLayerManager().createNewLayer(LayerType.TEXT);
        }

        setSliderValues();
    }

    @EventHandler
    public void onLayerChange(LayerChangeEvent event) {
        if(!super.isPluginActive()) return;
        if(event.layer() instanceof TextLayer) {
            setSliderValues();
        } else {
            super.getApplication().getPluginManager().activatePlugin(super.getApplication().getPluginManager().getActiveCanvasPlugin());
        }
    }

    @Override
    public String getIconFilePath() {
        return "Text_Tool.png";
    }

    public TextToolPlugin(Application paramApplication) {
        super(paramApplication);

        super.getApplication().getEventManager().registerEvents(this);
    }

    @Override
    public String getToolTip() {
        return "Text (T)";
    }
}
