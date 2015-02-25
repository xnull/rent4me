package realty.db.backup;

/**
 * @author dionis on 2/25/15.
 */
public class Main {
    public static void main(String[] args) throws Exception {
//        if(args.length == 0) {
//            args = new String[]{"backup", "/home/dionis/Desktop/hello_db_backups/dev_gisdb_201502240001.backup.tar.gz", "hello"};
//        }
        CommandRegistry commandItem = Command.findCommandItem(args);
        if (commandItem == null) {
            CommandRegistry.showCommands();
        } else {
            try {
                commandItem.performAction(args);
            } catch (DbToolException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public static enum CommandRegistry {
        BACKUP(new Command("backup", 2, "${fie name}  ${remote file name}", "Copy ${file name} into Dropbox's folder ${remote file name}") {
            @Override
            protected void doPerformAction(String[] args) throws Exception {
                final DropBoxBackup dropBoxBackup = new DropBoxBackup();
                dropBoxBackup.showAccountInfo();
                dropBoxBackup.backup(args[1], args[2]);
            }
        });
        public final Command command;

        CommandRegistry(Command command) {
            this.command = command;
        }

        public static void showCommands() {
            System.out.println("Program should be used as following: java -jar realty-db-tool-1.0.jar ${Command} ${Params}");
            System.out.println();
            System.out.println("Following command are available");
            System.out.println();
            String format = "| %-10s | %-50s | %-100s | ";
            showSeparatorLine();
            System.out.printf(format, "Command", "Params", "Description");
            System.out.println();
            showSeparatorLine();
            for (CommandRegistry commandRegistryItem : values()) {
                Command command = commandRegistryItem.command;
                System.out.printf(format, command.commandName, command.paramsFormat, command.description);
                System.out.println();
                showSeparatorLine();
            }
        }

        private static void showSeparatorLine() {
            String line = "+"+repeatChar('-', 12)+"+"+repeatChar('-', 52)+"+"+repeatChar('-', 102)+"+";
            System.out.println(line);
        }

        private static String repeatChar(char c, int n) {
            StringBuilder sb = new StringBuilder(n);
            for (int i = 0; i < n; i++) {
                sb.append(c);
            }
            return sb.toString();
        }

        public void performAction(String[] args) {
            command.performAction(args);
        }
    }

    public static abstract class Command {
        private final String commandName;
        private final int paramsCount;
        private final String paramsFormat;
        private final String description;

        public Command(String commandName, int paramsCount, String paramsFormat, String description) {
            this.commandName = commandName;
            this.paramsCount = paramsCount;
            this.paramsFormat = paramsFormat;
            this.description = description;
        }

        public static CommandRegistry findCommandItem(String[] args) {
            for (CommandRegistry commandRegistryItem : CommandRegistry.values()) {
                Command command = commandRegistryItem.command;
                boolean isValidCommand = args.length > 0 && command.commandName.equalsIgnoreCase(args[0]) && command.paramsCount == (args.length - 1/*exclude command name from count*/);
                if(isValidCommand) return commandRegistryItem;
            }
            return null;
        }

        public final void performAction(String[] args) {
            try {
                doPerformAction(args);
            } catch (Exception e) {
                if(e instanceof RuntimeException)
                    throw (RuntimeException)e;
                else
                    throw new RuntimeException(e);
            }
        };
        protected abstract void doPerformAction(String[] args) throws Exception;
    }
}
