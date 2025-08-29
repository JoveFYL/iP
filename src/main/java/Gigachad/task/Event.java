package Gigachad.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Creates Task that has a start datetime and end datetime and whether it is completed or not.
 */
public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Saves the task in the correct format to be stored in the storage file.
     * @return task String in the form:
     * "E | (1 or 0) | (task description) | (formatted start time string) - (formatted end time string)"
     */
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
