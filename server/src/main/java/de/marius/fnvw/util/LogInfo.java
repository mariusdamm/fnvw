package de.marius.fnvw.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;

public class LogInfo {

    private LogInfo() {
    }

    public static String toJson(LogLevel logLevel, String place, String issue, String reason, String action, String user) {
        ObjectMapper mapper = new ObjectMapper();
        LoggingInformationHelper helper = new LoggingInformationHelper(
                logLevel,
                place,
                new Date(),
                issue,
                reason,
                action,
                user
        );
        try {
            return mapper.writeValueAsString(helper);
        } catch (JsonProcessingException e) {
            return "!!!ERROR!!! JsonMapper threw JsonProcessingException: " + e.getMessage();
        }
    }
}

record LoggingInformationHelper(
        LogLevel logLevel,
        String place,
        Date time,
        String issue,
        String reason,
        String action,
        String user
){}
