package org.rinatzzak.service.enums;

public enum ServiceCommands {
    HELP("/help"),
    REGISTRATION("/registration"),
    CANCEL("/cancel"),
    START("/start");

    private final String cmd;

    ServiceCommands(String cmd) {
        this.cmd = cmd;
    }

    @Override
    public String toString() {
        return cmd;
    }

    public static ServiceCommands fromValue(String v) {
        for (ServiceCommands commands : ServiceCommands.values()) {
            if (commands.cmd.equals(v)) {
                return commands;
            }
        }
        return null;
    }
}
