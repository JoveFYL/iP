package gigachad.task;

/**
 * Creates Todo task that contains a task description and whether it is completed or not.
 * Task can be marked as complete or incomplete.
 */
public class ToDo extends Task {
    public ToDo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
