package uk.pixtle.application.events.events;

import uk.pixtle.application.events.events.Event;
import uk.pixtle.application.plugins.plugins.canvas.CanvasPlugin;

import java.time.LocalDateTime;

public record CanvasResetEvent(
        LocalDateTime creationTime,
        CanvasPlugin canvasPlugin

) implements Event {

    public CanvasResetEvent(CanvasPlugin paramCanvasPlugin) {
        this(LocalDateTime.now(), paramCanvasPlugin);
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