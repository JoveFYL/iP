package seedu.Gigachad;  //same package as the class being tested

import gigachad.Storage;
import gigachad.TaskList;
import gigachad.task.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static java.nio.file.Files.readString;
import static org.junit.jupiter.api.Assertions.*;

public class StorageTest {
    @TempDir
    File tempDir;

    static class TaskListStub extends TaskList {
        public TaskListStub() {
            super();
        }

        public TaskListStub(ArrayList<Task> listOfTasks) {
            super(listOfTasks);
        }

        public void addTask(Task task) {
            listOfTasks.add(task);
        }

        public ArrayList<Task> allTasks() {
            return this.listOfTasks;
        }
    }

    static class TaskStub extends Task {
        private final boolean done;
        public TaskStub(String description, boolean done) {
            super(description);
            this.done = done;
        }

        @Override
        public int getNumericIsDone() {
            return this.done ? 1 : 0;
        }
    }

    static class TodoStub extends TaskStub {
        public TodoStub(String description, boolean done) {
            super(description, done);
        }
    }

    static class DeadlineStub extends TaskStub {
        private final LocalDateTime by;

        public DeadlineStub(String description, LocalDateTime by, boolean done) {
            super(description, done);
            this.by = by;
        }

        @Override
        public String saveFormat() {
            String formattedBy = by.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            return "D | " + this.getNumericIsDone() + " | " + this.description + " | " + formattedBy;
        }
    }

    static class EventStub extends TaskStub {
        private final LocalDateTime from;
        private final LocalDateTime to;

        public EventStub(String description, LocalDateTime from, LocalDateTime to, boolean done) {
            super(description, done);
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
    }

    @Test
    public void initStorageCreatesCorrectTaskList() throws IOException {
        Path filePath = tempDir.toPath().resolve("tasks.txt");
        Storage storage = new Storage(filePath);

        ArrayList<Task> tasksToSave = new ArrayList<>();
        // Add a Todo task
        tasksToSave.add(new TodoStub("read book", false));

        // Add Deadline tasks
        tasksToSave.add(new DeadlineStub("submit assignment",
                LocalDateTime.of(2025, 12, 15, 23, 59), false));

        // Add Event tasks
        tasksToSave.add(new EventStub("conference",
                LocalDateTime.of(2025, 9, 9, 18, 0),     // 2025-09-09 1800
                LocalDateTime.of(2025, 9, 9, 20, 30),
                true));  // 2025-09-09 2030

        TaskListStub taskListToSave = new TaskListStub(tasksToSave);
        storage.saveToStorage(taskListToSave);
        String content = readString(filePath);
        assertAll(
                () -> assertTrue(content.contains("T | 0 | read book")),
                () -> assertTrue(content.contains("D | 0 | submit assignment | 2025-12-15 2359")),
                () -> assertTrue(content.contains("E | 1 | conference | 2025-09-09 1800 - 2025-09-09 2030"))
        );
    }

    @Test
    public void anotherDummyTest() {
        assertEquals(4, 4);
    }
}
