package uk.pixtle.application.colour;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.Application;
import uk.pixtle.application.ApplicationComponent;
import uk.pixtle.application.events.events.ColourChangeEvent;

import java.awt.*;

public class ColourManager extends ApplicationComponent {

    @Getter
    private Color color2;

    public void setColor2(Color paramColor) {
        super.getApplication().getUIManager().getWindow().getColorPanel().setButton2Colour(paramColor);
        this.color2 = paramColor;
    }

    @Getter
    private Color color1;

    public void setColor1(Color paramColor) {
        super.getApplication().getUIManager().getWindow().getColorPanel().setButton1Colour(paramColor);
        this.color1 = paramColor;
    }

    @Getter
    private ColourSlots activeColorSlot;

    public void setActiveColorSlot(ColourSlots paramColourSlot) {
        System.out.println(paramColourSlot);
        this.activeColorSlot = paramColourSlot;
        super.getApplication().getEventManager().callEvent(new ColourChangeEvent(
                this.getActiveColorSlot(), this.getActiveColor()
        ));
    }

    public void setColorOfActiveSlot(Color paramColor) {
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

        super.getApplication().getEventManager().callEvent(new ColourChangeEvent(
                this.getActiveColorSlot(), this.getActiveColor()
        ));
    }

    public Color getActiveColor() {
        if(this.getActiveColorSlot() == ColourSlots.COLOUR_1) {
            return this.getColor1();
        } else {
            return this.getColor2();
        }
    }

    public ColourManager(Application paramApplication) {
        super(paramApplication);

        this.setColor1(Color.WHITE);
        this.setColor2(Color.BLACK);
        this.setActiveColorSlot(ColourSlots.COLOUR_2);
    }
}
