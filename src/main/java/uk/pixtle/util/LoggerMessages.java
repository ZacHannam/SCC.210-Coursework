package uk.pixtle.util;

import lombok.Getter;
import lombok.Setter;

public enum LoggerMessages {

    PLUGIN_TOOL_REGISTERED("Registered Tool: %s");

    @Getter
    @Setter
    String logMessage;

    LoggerMessages(String paramLogMessages) {
        this.setLogMessage(paramLogMessages);
    }
}
