package uk.pixtle.application.ui.layouts.anchorlayout;

import lombok.Getter;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.DisplacedAnchor;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.DynamicAnchor;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.ValueAnchor;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class AnchoredComponent {


    public enum StandardY {
        TOP, BOTTOM, MIDDLE;
    }

    public enum StandardX {
        LEFT, RIGHT, MIDDLE;
    }

    @Getter
    public ArrayList<Anchor> anchors = new ArrayList<>();

    /*
    -------------------- Value Anchors --------------------
     */

    /**
     * Percentage Based Anchor
     * @param paramValue percentage of screen width
     */
    public Anchor createAnchor(Anchor.DirectionType paramDirectionType, double paramValue) {
        Anchor anchor = new ValueAnchor(this, paramDirectionType, paramValue);
        this.getAnchors().add(anchor);

        return anchor;
    }

    /**
     * Pixel Based Anchor
     * @param paramValue pixels in to the anchor
     */
    public Anchor createAnchor(Anchor.DirectionType paramDirectionType, int paramValue) {
        Anchor anchor = new ValueAnchor(this, paramDirectionType, paramValue);
        this.getAnchors().add(anchor);

        return anchor;
    }

    /**
     * Standard Based Anchor Y
     * @param paramY the standard for the y
     */
    public Anchor createAnchor(StandardY paramY) {
       Anchor anchor = new ValueAnchor(this, paramY);
        this.getAnchors().add(anchor);

        return anchor;
    }

    /**
     * Standard Based Anchor X
     * @param paramX the standard for the x
     */
    public Anchor createAnchor(StandardX paramX) {
        Anchor anchor = new ValueAnchor(this, paramX);
        this.getAnchors().add(anchor);

        return anchor;
    }

    /*
    -------------------- Dyanmic Anchors --------------------
     */

    public Anchor createAnchor(Anchor.DirectionType paramDirectionType, DynamicAnchor.AnchorSize paramAnchorSize, double paramRatioWidth, double paramRatioHeight) {
        Anchor anchor = new DynamicAnchor(this, paramDirectionType, paramAnchorSize, paramRatioWidth, paramRatioHeight);
        this.getAnchors().add(anchor);

        return anchor;
    }

    /*
    -------------------- Displacement Anchors --------------------
     */

    public Anchor createAnchor(Anchor.DirectionType paramDirectionType, Anchor paramParentAnchor, int paramDisplacement) {
        Anchor anchor = new DisplacedAnchor(this, paramDirectionType, paramParentAnchor, paramDisplacement);
        this.getAnchors().add(anchor);

        return anchor;

    }

    /*
    -------------------- Methods --------------------
     */

    public boolean integrity() {
        return (this.getAnchorCount() == 4) && (this.getDynamicAnchorCount() <= 1);
    }

    private int getAnchorCount() {
        return this.getAnchors().size();
    }

    private int getDynamicAnchorCount() {
        int n = 0;
        for(Anchor anchor : this.getAnchors()) {
            if(anchor instanceof DynamicAnchor) n++;
        }
        return n;
    }

    public HashMap<Anchor, Integer> getAnchorPixels(Dimension paramSize) {

        HashMap<Anchor, Integer> pixelLocations = new HashMap<>();

        if(!this.integrity()) {
            // TO-DO throw new exception
            return null;
        }

        for(Anchor anchor : this.getAnchors()) {
            pixelLocations.put(anchor, anchor.getAnchorPixel(paramSize));
        }

        return pixelLocations;

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int index = 0; index < this.getAnchors().size(); index++) {
            sb.append(this.getAnchors().get(index).toString());
            if(index != this.getAnchors().size() - 1) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}
