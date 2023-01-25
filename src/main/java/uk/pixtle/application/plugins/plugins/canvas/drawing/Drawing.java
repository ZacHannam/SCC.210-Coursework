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
    Color[][] drawingMap;

    public void setColor(int paramX, int paramY, Color paramColor) {
        this.getDrawingMap()[paramX][paramY] = paramColor;
    }

    public Color getColor(int paramX, int paramY) {
        return this.getDrawingMap()[paramX][paramY];
    }

    public Drawing(int width, int height) {
        this.setHeight(height);
        this.setWidth(width);

        this.setDrawingMap(new Color[width][height]);
    }

}
