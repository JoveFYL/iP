import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {

    protected LocalDateTime by;

    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

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
