public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
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

    public void markAsDone() {
        if (!isDone) {
            isDone = true;
        }
    }

    public void unmark() {
        if (isDone) {
            isDone = false;
        }
    }

    public String saveFormat() {
        return "T | " + this.getNumericIsDone() + " | " + this.description;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", this.getStatusIcon(), this.description);
    }
}
