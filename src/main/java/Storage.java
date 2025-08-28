import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    protected Path filePath;
    protected ArrayList<Task> tasks;

    public Storage(Path filePath) {
        this.filePath = filePath;
        this.tasks = new ArrayList<>();
    }

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
                    String command = parts[0];
                    String isDone = parts[1];
                    String todoDescription = parts[2];

                    switch (command) {
                    case "T":
                        try {
                            if (parts.length == 3) {
                                ToDo todo = new ToDo(todoDescription);
                                listOfTasks.add(todo);
                                if (Integer.parseInt(isDone) == 1) todo.markAsDone();
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
                            if (Integer.parseInt(isDone) == 1) deadline.markAsDone();
                        } catch (GigachadException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case "E":
                        try {
                            if (parts.length != 4 && parts[3].split(" - ").length != 2) {
                                throw new GigachadException("Invalid format! Corrupted file!");
                            }

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
                            if (Integer.parseInt(isDone) == 1) event.markAsDone();
                        } catch (GigachadException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    }
                }
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
