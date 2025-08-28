package Gigachad.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String saveFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        String formattedFrom = from.format(formatter);
        String formattedTo = to.format(formatter);
        return "E | " + this.getNumericIsDone() + " | " + this.description + " | " + formattedFrom
                + " - " + formattedTo;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        String formattedFrom = from.format(formatter);
        String formattedTo = to.format(formatter);
        return "[E]" + super.toString() + " (from: " + formattedFrom + " to: " + formattedTo + ")";
    }
}
