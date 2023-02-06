package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.drawinglayer;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.Layer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.Map;

public class Chunk {

    @Getter
    @Setter
    private BufferedImage actualImage;

    @Getter
    @Setter
    private BufferedImage lastRenderedImage;

    @Getter
    @Setter
    private int size;

    @Getter
    @Setter
    private double scale;

    @Getter
    @Setter
    private boolean reRender;

    @Getter
    @Setter
    private Layer layer;

    /*

                LOADING AND SAVING

     */

    public JSONObject save() throws Exception {

        JSONObject jsonObject = new JSONObject();

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(this.getActualImage(), "png", byteArrayOutputStream);
        jsonObject.put("data", Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray()));

        return jsonObject;
    }

    public void load(JSONObject paramData) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(paramData.getString("data"));
        InputStream inputStream = new ByteArrayInputStream(bytes);
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        this.setActualImage(bufferedImage);

        this.setReRender(true);
    }

    /*

                UPDATING VALUES

     */

    public void updateValues(double paramScale) {

        if(this.getLastRenderedImage() != null && paramScale == this.getScale() ) {
            return;
        }

        this.setScale(paramScale);
    }

    /*

                ACTUAL IMAGE METHODS

     */

    public void createActualImage(int paramSize) {
        this.setActualImage(new BufferedImage(paramSize, paramSize, BufferedImage.TYPE_INT_ARGB | BufferedImage.SCALE_FAST));
    }

    public void setRGB(int paramX, int paramY, int paramRGB, float paramAlpha) {
        int alpha = (int) Math.floor(paramAlpha * 255);
        if(alpha > 255) return;

        int rgba = (alpha << 24) | (paramRGB & 0x00FFFFFF);
        this.getActualImage().setRGB(paramX, paramY, rgba);

        this.setReRender(true);
        this.getLayer().setReRender(true);
    }

    /*

                CONSTRUCTOR

     */

    public Chunk(Layer paramLayer, int paramSize) {
        this.setLayer(paramLayer);
        this.setSize(paramSize);
    }
}
