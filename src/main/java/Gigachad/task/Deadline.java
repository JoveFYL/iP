package Gigachad.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Creates Task that has a deadline to be completed by and whether it is completed or not..
 */
public class Deadline extends Task {

    protected LocalDateTime by;

    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Saves the task in the correct format to be stored in the storage file.
     * @return task String in the form:
     * "D | (1 or 0) | (task description) | (formatted time string)"
     */
    @Override
    public String saveFormat() {
        String formattedBy = by.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        return "D | " + this.getNumericIsDone() + " | " + this.description + " | " + formattedBy;
    }

    @Override
    public String toString() {
        String formattedBy = by.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        return "[D]" + super.toString() + " (by: " + formattedBy + ")";
    }
}
