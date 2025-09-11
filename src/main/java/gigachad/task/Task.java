package gigachad.task;

/**
 * Creates Task contains a task description and whether it is completed or not.
 * Task can be marked as complete or incomplete.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Task constructor
     * @param description description of task
     */
    public Task(String description) {
        assert description != null && !description.isBlank() : "Deadline description must not be null/blank";
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public int getNumericIsDone() {
        return isDone ? 1 : 0;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean getIsDone() {
        return this.isDone;
    }

    /**
     * Mark a task as complete
     */
    public void markAsDone() {
        if (!isDone) {
            isDone = true;
        }
    }

    /**
     * Unmark a task as complete
     */
    public void unmark() {
        if (isDone) {
            isDone = false;
        }
    }

    /**
     * Saves the task in the correct format to be stored in the storage file.
     * @return task String in the form:
     * "T | (1 or 0) | (task description)"
     */
    public String saveFormat() {
        return "T | " + this.getNumericIsDone() + " | " + this.description;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", this.getStatusIcon(), this.description);
    }
}
