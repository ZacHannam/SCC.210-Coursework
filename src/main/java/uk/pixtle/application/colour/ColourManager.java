package uk.pixtle.application.colour;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.Application;
import uk.pixtle.application.ApplicationComponent;
import uk.pixtle.application.events.events.ColourChangeEvent;

import java.awt.*;

public class ColourManager extends ApplicationComponent {

    @Getter
    @Setter
    private Color color2;

    @Setter
    @Getter
    private Color color1;

    @Getter
    @Setter
    private ColourSlots activeColorSlot;

    public void setColorOfActiveColor(Color paramColor) {
        switch(this.getActiveColorSlot()) {
            case COLOUR_1:
                this.setColor1(paramColor);
                break;
            case COLOUR_2:
                this.setColor2(paramColor);
                break;
        }

        super.getApplication().getEventManager().callEvent(new ColourChangeEvent(
                this.getActiveColorSlot(), paramColor
        ));
    }

    public void swapActiveColourSlot() {
        if(this.getActiveColorSlot() == ColourSlots.COLOUR_1) {
            this.setActiveColorSlot(ColourSlots.COLOUR_2);
        } else {
            this.setActiveColorSlot(ColourSlots.COLOUR_1);
        }
    }

    public ColourManager(Application paramApplication) {
        super(paramApplication);

        this.setColor1(Color.WHITE);
        this.setColor2(Color.BLACK);
        this.setActiveColorSlot(ColourSlots.COLOUR_1);
    }
}
