package uk.pixtle.components.ui.layouts.anchorlayout.anchors;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.components.ui.layouts.anchorlayout.AnchoredComponent;

import java.awt.*;

public class DisplacedAnchor extends Anchor {

    @Getter
    @Setter
    private Anchor parentAnchor;

    @Getter
    @Setter
    private int displacement;

    public DisplacedAnchor(AnchoredComponent paramAnchoredComponent, DirectionType paramDirectionType, Anchor paramParentAnchor, int paramDisplacement) {
        super(paramAnchoredComponent, AnchorType.DISPLACED, paramDirectionType);
        this.setParentAnchor(paramParentAnchor);
        this.setDisplacement(paramDisplacement);
    }

    @Override
    public int getAnchorPixel(Dimension paramSize) {

        return this.getParentAnchor().getAnchorPixel(paramSize) + this.getDisplacement();

    }
}
