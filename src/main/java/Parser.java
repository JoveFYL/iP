public class Parser {
    public static Command parse(String input) throws GigachadException {
        if (input.isEmpty()) throw new GigachadException("Input cannot be blank!");

        String[] parts = input.trim().split(" ");
        String firstWord = parts[0].toLowerCase();

        String[] args = new String[parts.length];
        if (parts.length > 1) {
            System.arraycopy(parts, 1, args, 0, args.length - 1);
        }
        return new Command(firstWord, input, args);
    }
}
