package uk.pixtle.application.events.events;

import uk.pixtle.application.colour.ColourSlots;
import uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer.Layer;

import java.awt.*;
import java.time.LocalDateTime;

public record LayerChangeEvent(
    LocalDateTime creationTime,
    Layer layer

) implements Event {

    public LayerChangeEvent(Layer paramLayer) {
        this(LocalDateTime.now(), paramLayer);
    }


    @Override
    public LocalDateTime getCreationTime() {
        return this.creationTime();
    }

    @Override
    public Event.EventType getEventType() {
        return EventType.LAYER_CHANGE_EVENT;
    }

}
