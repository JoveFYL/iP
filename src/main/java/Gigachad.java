import java.util.ArrayList;
import java.util.Scanner;

public class Gigachad {
    public static void main(String[] args) {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("Hello! I'm Gigachad");
        System.out.println("What can I do for you?");
        System.out.println("Bye. Hope to see you again soon!");

        Scanner scanner = new Scanner(System.in);
        ArrayList<String> listOfTasks = new ArrayList<>();

        System.out.println("Enter a command: ");
        String command = "";
        while (!command.equals("bye")) {
            command = scanner.nextLine();

            // if command == list, list everything out
            // else if command != "bye", add to list
            // if command == "bye", exit
            if (command.equals("list")) {
                for (int i = 0; i < listOfTasks.size(); i++) {
                   System.out.println((i + 1) + ". " + listOfTasks.get(i));
                }
            } else {
                listOfTasks.add(command);
                System.out.println("added: " + command);
            }
        }

        System.out.println("Bye. Hope to see you again soon!");
        scanner.close();
    }
}
