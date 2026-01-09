package com.v8.pmoraes.chat_backend.util;

import java.util.regex.Pattern;

/**
 * Utility class for validation operations.
 */
public class ValidationUtils {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@(.+)$");
    
    /**
     * Validate email address format.
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Check if string is null or empty.
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * Validate message content.
     */
    public static boolean isValidMessage(String message) {
        return !isEmpty(message) && message.length() > 0 && message.length() < 10000;
    }
}
