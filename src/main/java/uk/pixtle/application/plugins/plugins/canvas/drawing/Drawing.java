package uk.pixtle.application.plugins.plugins.canvas.drawing;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public class Drawing {

    @Getter
    @Setter
    int height, width;

    @Getter
    @Setter
    ColorAndAlpha[][] drawingMap;

    public void setColor(int paramX, int paramY, Color paramColor, int paramAlpha) {
        this.getDrawingMap()[paramX][paramY] = new ColorAndAlpha(paramColor, paramAlpha);
    }

    public ColorAndAlpha getColor(int paramX, int paramY) {
        return this.getDrawingMap()[paramX][paramY];
    }

    public Drawing(int width, int height) {
        this.setHeight(height);
        this.setWidth(width);

        this.setDrawingMap(new ColorAndAlpha[width][height]);
    }

}
