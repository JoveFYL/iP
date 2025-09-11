package gigachad.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Creates Task that has a deadline to be completed by and whether it is completed or not..
 */
public class Deadline extends Task {

    protected LocalDateTime by;

    /**
     * Deadline constructor
     * @param description description of the deadline
     * @param by date and time the dateline must be completed
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        assert description != null && !description.isBlank() : "Deadline description must not be null/blank";
        assert by != null : "Deadline datetime must not be null";
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
