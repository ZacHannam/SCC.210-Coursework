package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas;

import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.xml.datatype.DatatypeConstants;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class Chunk {

    @Getter
    @Setter
    private BufferedImage actualImage;

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
    private boolean renderingChange;

    public void updateValues(double paramScale){

        if(!this.renderingChange && this.getLastRenderedImage() != null && paramScale == this.getScale()) {
            this.setChanged(false);
            return;
        }

        this.setScale(paramScale);
        this.setChanged(true);
    }

    public void initialiseChunkImage(String text) {

        for(int i = 0; i < this.getActualImage().getHeight(); i++) {
            for (int j = 0; j < this.getActualImage().getWidth(); j++) {
                this.getActualImage().setRGB(j, i, Color.green.getRGB());

                if(i < 10 || i > 245 || j < 10 || j > 245) {
                    this.getActualImage().setRGB(j, i, Color.black.getRGB());
                }
            }
        }

        Graphics2D gO = this.getActualImage().createGraphics();
        gO.setColor(Color.red);
        gO.setFont(new Font( "SansSerif", Font.BOLD, 18 ));
        String[] t = text.split(":");

        for(int i = 0; i <t.length ; i++) {
            gO.drawString(t[i], 15, 100 +i * 20);
        }

    }

    public void initialiseChunkImage(Color paramBackgroundColor) {
        for(int i = 0; i < this.getActualImage().getHeight(); i++) {
            for (int j = 0; j < this.getActualImage().getWidth(); j++) {
                this.getActualImage().setRGB(j, i, paramBackgroundColor.getRGB());
            }
        }
    }


    public Chunk(int paramSize, String text) {
        this.setActualImage(new BufferedImage(paramSize, paramSize, Image.SCALE_FAST));

        initialiseChunkImage(text);

    }

    public Chunk(int paramSize, Color paramBackgroundColor) {
        this.setActualImage(new BufferedImage(paramSize, paramSize, Image.SCALE_FAST));

        initialiseChunkImage(paramBackgroundColor);
    }

    /*
    LOADING AND SAVING
     */

    public String getSaveData() {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(this.getActualImage(), "png", byteArrayOutputStream);

            return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void load(String paramData) {
        try {
            byte[] bytes = Base64.getDecoder().decode(paramData);
            InputStream inputStream = new ByteArrayInputStream(bytes);
            BufferedImage bufferedImage = ImageIO.read(inputStream);

            this.setActualImage(bufferedImage);
            this.setChanged(true);
            this.setRenderingChange(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
