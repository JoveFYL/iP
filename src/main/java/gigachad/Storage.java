package gigachad;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import gigachad.exception.GigachadException;
import gigachad.task.Deadline;
import gigachad.task.Event;
import gigachad.task.Task;
import gigachad.task.ToDo;


/**
 * Represents a Storage object file to store the tasks of the user.
 */
public class Storage {
    protected Path filePath;
    protected ArrayList<Task> tasks;

    /**
     * Constructs a Storage object with the specified file path.
     * Initialises an empty ArrayList of tasks.
     *
     * @param filePath the path to the file where tasks will be stored
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
        this.tasks = new ArrayList<>();
        assert filePath != null : "Deadline description must not be null/blank";
    }

    /**
     * Initialises the storage system by creating directories and files,
     * then loads existing tasks from the storage file if it exists.
     * Creates the parent directories if they do not exist, creates the storage file if storage file does not exist
     * and parses existing task data from the file format:
     * "T | isDone | description" for ToDo tasks
     * "D | isDone | description | deadlineDateTime" for Deadline tasks
     * "E | isDone | description | startDateTime - endDateTime" for Event tasks
     * Prints initialisation status and current task list to console.
     * Handles corrupted file formats by catching and printing error messages.
     *
     * @return an ArrayList of Task objects loaded from storage, or empty list if file doesn't exist or error occurs
     */
    public ArrayList<Task> initStorage() {
        try {
            ArrayList<Task> listOfTasks = new ArrayList<>();
            // Ensure parent directories exist
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }

            // Create file if not exists
            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
                System.out.println("File created: " + filePath.toAbsolutePath());
            } else {
                System.out.println("File already exists: " + filePath.toAbsolutePath());

                // init to do list
                Scanner scanner = new Scanner(filePath.toFile());
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    String[] parts = line.split(" \\| ");
                    assert parts.length >= 3 : "Line must contain at least 3 parts: " + line;

                    String command = parts[0];
                    String isDone = parts[1];
                    String todoDescription = parts[2];

                    switch (command) {
                    case "T":
                        try {
                            if (parts.length == 3) {
                                ToDo todo = new ToDo(todoDescription);
                                listOfTasks.add(todo);
                                if (Integer.parseInt(isDone) == 1) {
                                    todo.markAsDone();
                                }
                            } else {
                                throw new GigachadException("Invalid format! Corrupted file!");
                            }
                        } catch (GigachadException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case "D":
                        try {
                            String deadlineDueDate = parts[3];
                            if (parts.length != 4) {
                                throw new GigachadException("Invalid format! Corrupted file!");
                            }

                            Deadline deadline = new Deadline(todoDescription,
                                    LocalDateTime.parse(deadlineDueDate,
                                            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm")));
                            listOfTasks.add(deadline);
                            if (Integer.parseInt(isDone) == 1) {
                                deadline.markAsDone();
                            }
                        } catch (GigachadException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case "E":
                        try {
                            if (parts.length != 4 && parts[3].split(" - ").length != 2) {
                                throw new GigachadException("Invalid format! Corrupted file!");
                            }
                            assert parts.length == 4 : "Event must have 4 parts: " + line;
                            assert parts[3].contains(" - ") : "Event datetime part must contain ' - ': " + parts[3];

                            String[] fromToDates = parts[3].split(" - ");
                            String from = fromToDates[0];
                            String to = fromToDates[1];

                            if (todoDescription.isEmpty() || from.isEmpty() || to.isEmpty()) {
                                throw new GigachadException("Invalid format! Task description or date missing.");
                            }

                            Event event = new Event(todoDescription,
                                    LocalDateTime.parse(from, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm")),
                                    LocalDateTime.parse(to, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm")));

                            listOfTasks.add(event);

                            if (Integer.parseInt(isDone) == 1) {
                                event.markAsDone();
                            }
                        } catch (GigachadException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    default:
                        assert false : command;
                    }
                }
                scanner.close();
            }

            System.out.println("Storage initialised");
            System.out.println("You have the following tasks: ");
            for (int i = 0; i < listOfTasks.size(); i++) {
                System.out.print(i + 1 + ". ");
                System.out.println(listOfTasks.get(i));
            }
            return listOfTasks;
        } catch (IOException e) {
            System.out.println("An error occurred while initialising storage: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Saves all tasks from the given TaskList to the storage file.
     * Overwrites the existing file content with the current task data.
     * Each task is written in its save format on a separate line.
     *
     * @param taskList the TaskList containing tasks to be saved to storage
     * @throws IOException if an error occurs while writing to the file (handled internally)
     */
    public void saveToStorage(TaskList taskList) {
        try {
            FileWriter fw = new FileWriter(filePath.toString());
            for (Task task : taskList.allTasks()) {
                fw.write(task.saveFormat());
                fw.write("\n");
            }
            fw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
