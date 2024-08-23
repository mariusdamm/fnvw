package de.marius.fnvw.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;

public record LoggingInformation(
        LogLevel logLevel,
        String place,
        Date time,
        String issue,
        String reason,
        String action,
        String user
) {

    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}
