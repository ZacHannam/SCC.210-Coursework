package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas;

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

    public void initialiseChunkImage() {

        for(int i = 0; i < this.getActualImage().getHeight(); i++) {
            for (int j = 0; j < this.getActualImage().getWidth(); j++) {
                this.getActualImage().setRGB(j, i, Color.white.getRGB());
            }
        }
    }


    public Chunk(int paramSize) {
        this.setActualImage(new BufferedImage(paramSize, paramSize, Image.SCALE_FAST));

        initialiseChunkImage();

    }
}
