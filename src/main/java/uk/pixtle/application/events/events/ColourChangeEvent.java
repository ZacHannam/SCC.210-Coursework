package uk.pixtle.application.events.events;

import uk.pixtle.application.colour.ColourSlots;

import java.awt.*;
import java.time.LocalDateTime;

public record ColourChangeEvent(
    LocalDateTime creationTime,
    ColourSlots activeSlot,
    Color newColour

) implements Event {

    public ColourChangeEvent( ColourSlots paramActiveSlot, Color paramNewColour) {
        this(LocalDateTime.now(), paramActiveSlot, paramNewColour);
    }


    @Override
    public LocalDateTime getCreationTime() {
        return this.creationTime();
    }

    @Override
    public EventType getEventType() {
        return EventType.COLOUR_CHANGE_EVENT;
    }

}
