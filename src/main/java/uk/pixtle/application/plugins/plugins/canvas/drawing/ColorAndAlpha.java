package uk.pixtle.application.plugins.plugins.canvas.drawing;

import lombok.Getter;

import java.awt.*;

public class ColorAndAlpha {

    @Getter
    Color color;

    @Getter
    float alpha;

    public ColorAndAlpha(Color paramColor, float paramAlpha) {
        if(paramAlpha < 0 || paramAlpha > 1) {
            // TO-DO exception
            return;
        }
        this.color = paramColor;
        this.alpha = paramAlpha;
    }
}
