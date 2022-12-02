package uk.pixtle.application.ui.layouts.anchorlayout.anchors;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;

import java.awt.*;

public abstract class Anchor {

    public enum AnchorType {
        PIXEL, PERCENTAGE, STANDARD_X, STANDARD_Y, DYANMIC, DISPLACED, SLIDER;
    }

    public enum DirectionType {
        X, Y;
    }

    @Setter
    @Getter
    private AnchorType anchorType;

    @Getter
    @Setter
    private DirectionType directionType;

    @Getter
    @Setter
    private AnchoredComponent anchoredComponent;

    public abstract int getAnchorPixel(Dimension paramSize);

    public Anchor(AnchoredComponent paramAnchoredComponent, AnchorType paramAnchorType, DirectionType paramDirectionType) {
        this.setAnchoredComponent(paramAnchoredComponent);
        this.setAnchorType(paramAnchorType);
        this.setDirectionType(paramDirectionType);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("DirectionType=" + this.getDirectionType() +
                ", AnchorType=" + this.getAnchorType());

        return sb.toString();
    }

}
