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
        System.out.println("Enter a command: ");
        String command = "";
        while (!command.equals("bye")) {
            command = scanner.nextLine();
            System.out.println(command);
        }

        System.out.println("Bye. Hope to see you again soon!");
        scanner.close();
    }
}
