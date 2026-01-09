package com.v8.pmoraes.chat_backend.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for date and time operations.
 */
public class DateUtils {
    
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    
    /**
     * Format LocalDateTime to ISO 8601 string.
     */
    public static String formatToIso(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(ISO_FORMATTER) : null;
    }
    
    /**
     * Get current timestamp in ISO 8601 format.
     */
    public static String getCurrentTimestampIso() {
        return formatToIso(LocalDateTime.now());
    }
}
