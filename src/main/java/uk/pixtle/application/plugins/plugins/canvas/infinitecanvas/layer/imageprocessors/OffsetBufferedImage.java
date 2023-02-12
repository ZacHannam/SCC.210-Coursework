package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.imageprocessors;

import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;

public class OffsetBufferedImage {

    @Getter
    @Setter
    BufferedImage bufferedImage;

    @Getter
    @Setter
    int offsetX, offsetY;

    public OffsetBufferedImage(int paramOffsetX, int paramOffsetY, BufferedImage paramBufferedImage) {
        this.setOffsetX(paramOffsetX);
        this.setOffsetY(paramOffsetY);
        this.setBufferedImage(paramBufferedImage);
    }
}
