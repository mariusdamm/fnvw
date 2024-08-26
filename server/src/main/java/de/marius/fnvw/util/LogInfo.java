package de.marius.fnvw.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;

public class LogInfo {

    private LogInfo() {
    }

    public static String toJson(
            LogLevel logLevel,
            String place,
            Date time,
            String issue,
            String reason,
            String action,
            String user
    ) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        LoggingInformationHelper helper = new LoggingInformationHelper(
                logLevel,
                place,
                time,
                issue,
                reason,
                action,
                user
        );
        return mapper.writeValueAsString(helper);
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
