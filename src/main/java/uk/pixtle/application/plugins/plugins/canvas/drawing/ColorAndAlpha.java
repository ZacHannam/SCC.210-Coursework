package uk.pixtle.application.plugins.plugins.canvas.drawing;

import lombok.Getter;

import java.awt.*;

public class ColorAndAlpha {

    @Getter
    Color color;

    @Getter
    int alpha;

    public ColorAndAlpha(Color paramColor, int paramAlpha) {
        this.color = paramColor;
        this.alpha = paramAlpha;
    }
}
