package uk.pixtle.application.ui.window.canvas;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.image.BufferedImage;

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

    public void updateValues(double paramScale){

        if(getLastRenderedImage() != null && paramScale == this.getScale()) {
            this.setChanged(false);
            return;
        }

        this.setScale(paramScale);
        this.setChanged(true);
    }

    public void test() {

        int r = 0, g = 0, b = 0;

        for(int i = 0; i < this.getActualImage().getHeight(); i++) {
            for(int j = 0; j < this.getActualImage().getWidth(); j++) {

                if(i <= 10 || i >= this.getActualImage().getHeight() - 10 || j <= 10 || j > this.getActualImage().getWidth() - 10) {
                    this.getActualImage().setRGB(j, i, Color.green.getRGB());
                } else {

                    this.getActualImage().setRGB(j, i, new Color(r, g, b).getRGB());
                    r = ((r + 1) % (this.getActualImage().getWidth() - 20)) % 256;
                }

                if(i == 101 && j == 101) {
                    this.getActualImage().setRGB(j, i, Color.YELLOW.getRGB());
                }
            }
        }


        this.setLastRenderedImage(this.getActualImage());

    }


    public Chunk(int paramSize) {
        this.setActualImage(new BufferedImage(paramSize, paramSize, Image.SCALE_FAST));

        test(); // NEEDS TO LOAD FROM SAVE

    }
}
