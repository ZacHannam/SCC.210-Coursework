package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.chunk;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.InfiniteCanvasPlugin;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.Layer;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.xml.datatype.DatatypeConstants;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Chunk {

    // For the layers
    @Getter
    @Setter
    private HashMap<Integer, BufferedImage> actualImages;

    @Getter
    @Setter
    private BufferedImage actualImage;

    @Getter
    @Setter
    private int size;

    @Getter
    @Setter
    private BufferedImage lastRenderedImage;

    @Getter
    @Setter
    private boolean changed;

    @Getter
    @Setter
    private double scale;

    @Getter
    @Setter
    private ArrayList<Integer> visibleLayersInOrder;

    @Getter
    @Setter
    private boolean renderingChange;

    @Getter
    @Setter
    private InfiniteCanvasPlugin infiniteCanvasPlugin;


    public void updateValues(double paramScale, ArrayList<Integer> paramVisibleLayersInOrder){

        if(!this.renderingChange && this.getLastRenderedImage() != null && paramScale == this.getScale() && this.getVisibleLayersInOrder().equals(paramVisibleLayersInOrder)) {
            this.setChanged(false);
            return;
        }

        this.setScale(paramScale);
        this.setVisibleLayersInOrder(paramVisibleLayersInOrder);
        this.setChanged(true);
    }

    public BufferedImage getActualImageByLayer(int layerID) {
        return this.getActualImages().get(layerID);
    }

    public boolean isActualImageForLayer(int layerID) {
        return this.getActualImages().containsKey(layerID);
    }

    public void createActualImageForLayer(int layerID) {

        BufferedImage bufferedImage = new BufferedImage(this.getSize(), this.getSize(), BufferedImage.TYPE_INT_ARGB | BufferedImage.SCALE_FAST);
        this.getActualImages().put(layerID, bufferedImage);

    }

    public Chunk(InfiniteCanvasPlugin paramInfiniteCanvasPlugin, int paramSize) {
        //this.setActualImage(new BufferedImage(paramSize, paramSize, Image.SCALE_FAST));
        this.setInfiniteCanvasPlugin(paramInfiniteCanvasPlugin);
        this.setSize(paramSize);
        this.setActualImages(new HashMap<>());
    }

    /*
    LOADING AND SAVING
     */

    public JSONObject save() throws Exception {

        JSONObject jsonObject = new JSONObject();

        for(Map.Entry<Integer, BufferedImage> entry : this.getActualImages().entrySet()) {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(entry.getValue(), "png", byteArrayOutputStream);
            jsonObject.put(String.valueOf(entry.getKey()), Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray()));
        }

        return jsonObject;
    }

    public void load(JSONObject paramData) throws Exception {
        for(String key : paramData.keySet()) {
            byte[] bytes = Base64.getDecoder().decode(paramData.getString(key));
            InputStream inputStream = new ByteArrayInputStream(bytes);
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            this.getActualImages().put(Integer.valueOf(key), bufferedImage);
        }
        this.setChanged(true);
        this.setRenderingChange(true);

    }

    public void setRGB(Layer paramLayer, int paramX, int paramY, int paramRGB, float paramAlpha) {
        int alpha = (int) Math.floor(paramAlpha * 255);
        if(alpha > 255) return;

        int rgba = (alpha << 24) | (paramRGB & 0x00FFFFFF);
        this.getActualImageByLayer(paramLayer.getLayerID()).setRGB(paramX, paramY, rgba);
    }
}
