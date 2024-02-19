package utilities;

import messaging.Event;

public class EventHelper {
    private final Event event;
    private final String id;

    public EventHelper(Event event, String id) {
        this.event = event;
        this.id = id;
    }

    public Event getEvent() {
        return this.event;
    }

    public String getId() {
        return this.id;
    }
}
