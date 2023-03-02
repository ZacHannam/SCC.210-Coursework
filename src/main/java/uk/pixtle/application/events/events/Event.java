package uk.pixtle.application.events.events;

import java.time.LocalDateTime;

public interface Event {

    enum EventType {
        EXAMPLE_EVENT,
        COLOUR_CHANGE_EVENT,
        LAYER_CHANGE_EVENT,
        CANVAS_RESET_EVENT;
    }

    LocalDateTime getCreationTime();
    EventType getEventType();

}
