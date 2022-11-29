package uk.pixtle.application.events.events;

import java.time.LocalDateTime;

public record ExampleEvent(
        LocalDateTime creationTime,
        String name


) implements Event {

    public ExampleEvent(
            String paramName
    )
    {
        this(LocalDateTime.now(), paramName);
    }

    @Override
    public LocalDateTime getCreationTime() {
        return this.creationTime();
    }

    @Override
    public EventType getEventType() {
        return EventType.EXAMPLE_EVENT;
    }
}
