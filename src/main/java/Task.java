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

    public String getDescription() {
        return this.description;
    }

    public void markAsDone() {
        if (!isDone) {
            isDone = true;
            System.out.println("Nice! I've marked this task as done:");
            System.out.println(this);
        } else {
            System.out.println("I already marked this task as done!");
        }
    }

    public void unmark() {
        if (isDone) {
            isDone = false;
            System.out.println("OK, I've marked this task as not done yet:");
            System.out.println(this);
        } else {
            System.out.println("I already marked this task as undone!");
        }
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", this.getStatusIcon(), this.description);
    }
}
