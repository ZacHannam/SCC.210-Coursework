package uk.pixtle.application.ui.layouts.anchorlayout.anchors;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;

import java.awt.*;

public class ValueAnchor extends Anchor {

    @Setter
    @Getter
    private Object value;

    public int getAnchorPixel(Dimension paramSize) {

        switch(this.getAnchorType()) {
            case PIXEL:
                if((int) this.getValue() < 0) {
                    switch(this.getDirectionType()) {
                        case X:
                            return (int) (paramSize.getWidth() + ((int) this.getValue()));
                        case Y:
                            return (int) (paramSize.getHeight() + ((int) this.getValue()));
                    }
                }
                return (int) this.getValue();

            case PERCENTAGE:
                switch(this.getDirectionType()) {
                    case X:
                        return (int) Math.floor(paramSize.getWidth() * ((double) this.getValue()));
                    case Y:
                        return (int) Math.floor(paramSize.getHeight() * ((double) this.getValue()));
                }

            case STANDARD_X:
                switch((AnchoredComponent.StandardX) this.getValue()) {
                    case LEFT:
                        return 0;
                    case RIGHT:
                        return (int) paramSize.getWidth();
                    case MIDDLE:
                        return (int) paramSize.getWidth() / 2;
                }

            case STANDARD_Y:
                switch((AnchoredComponent.StandardY) this.getValue()) {
                    case TOP:
                        return 0;
                    case BOTTOM:
                        return (int) paramSize.getHeight();
                    case MIDDLE:
                        return (int) paramSize.getHeight() / 2;
                }
        }

        return 0;
    }

    public ValueAnchor(AnchoredComponent paramAnchoredComponent, DirectionType paramDirectionType, double paramAnchor) {
        super(paramAnchoredComponent, AnchorType.PERCENTAGE, paramDirectionType);

        if(paramAnchor < 0 || paramAnchor > 1) {
            // TO-DO throw new error
        }

        this.setValue(paramAnchor);
    }

    /**
     * Use negative number to traverse RIGHT to LEFT
     * @param paramAnchor
     */
    public ValueAnchor(AnchoredComponent paramAnchoredComponent, DirectionType paramDirectionType, int paramAnchor) {
        super(paramAnchoredComponent, AnchorType.PIXEL, paramDirectionType);
        this.setValue(paramAnchor);
    }

    public ValueAnchor(AnchoredComponent paramAnchoredComponent, AnchoredComponent.StandardX paramAnchor) {
        super(paramAnchoredComponent, AnchorType.STANDARD_X, DirectionType.X);
        this.setValue(paramAnchor);
    }

    public ValueAnchor(AnchoredComponent paramAnchoredComponent, AnchoredComponent.StandardY paramAnchor) {
        super(paramAnchoredComponent, AnchorType.STANDARD_Y, DirectionType.Y);
        this.setValue(paramAnchor);
    }
}
