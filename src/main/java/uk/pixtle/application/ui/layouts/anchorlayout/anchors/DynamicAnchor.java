package uk.pixtle.application.ui.layouts.anchorlayout.anchors;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DynamicAnchor extends Anchor {

    public enum AnchorSize {
        MIN, MAX;
    }

    @Override
    public int getAnchorPixel(Dimension paramSize) {

        HashMap<Anchor, Integer> definedAnchors = new HashMap<>();

        for(Anchor anchor : super.getAnchoredComponent().getAnchors()) {
            if(!(anchor instanceof DynamicAnchor)) {
                definedAnchors.put(anchor, anchor.getAnchorPixel(paramSize));
            }
        }

        if(definedAnchors.size() != 3) {
            // TO-DO throw new exceptions
            return 0;
        }

        ArrayList<Integer> knownPixels = new ArrayList<>();
        int adjacentAnchor = 0;

        for(Anchor anchor : definedAnchors.keySet()) {
            if(anchor.getDirectionType() != this.getDirectionType()) {
                knownPixels.add(definedAnchors.get(anchor));
            } else {
                adjacentAnchor = definedAnchors.get(anchor);
            }
        }

        if(knownPixels.size() != 2) {
            // TO-Do throw new exception
            return 0;
        }

        int knownLength = Math.abs(knownPixels.get(0) - knownPixels.get(1));
        double size;

        switch(this.getDirectionType()) {
            case X:
                size = (knownLength / this.getRatioHeight()) * this.getRatioWidth();
                break;
            case Y:
                size = (knownLength / this.getRatioWidth()) * this.getRatioHeight();
                break;
            default:
                size = 0;
                break;
        }

        switch(this.getAnchorSize()){
            case MIN:
                return (int) Math.floor(adjacentAnchor - size);
            case MAX:
                return (int) Math.floor(adjacentAnchor + size);
            default:
                return 0;
        }
    }

    @Getter
    @Setter
    private ValueAnchor.DirectionType directionType;

    @Getter
    @Setter
    private AnchorSize anchorSize;

    @Getter
    @Setter
    private double ratioWidth;

    @Getter
    @Setter
    private double ratioHeight;

    public DynamicAnchor(AnchoredComponent anchoredComponent, Anchor.DirectionType paramDirectionType, AnchorSize paramAnchorSize, double paramRatioWidth, double paramRatioHeight) {
        super(anchoredComponent, AnchorType.DYANMIC, paramDirectionType);
        this.setDirectionType(paramDirectionType);
        this.setAnchorSize(paramAnchorSize);
        this.setRatioWidth(paramRatioWidth);
        this.setRatioHeight(paramRatioHeight);
    }
}
