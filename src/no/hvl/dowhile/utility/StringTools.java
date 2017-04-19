package no.hvl.dowhile.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility methods for formatting objects into strings.
 */
public class StringTools {
    /**
     * Formatting a Date into a string.
     *
     * @param date the date to format.
     * @return the date formatted as a String.
     */
    public static String formatDate(Date date) {
        return new SimpleDateFormat("dd-MM/yyyy HH:mm z").format(date);
    }

    /**
     * Formatting a Date into a string without spaces.
     *
     * @param date the date to format.
     * @return the date formatted as a String without spaces.
     */
    public static String formatDateForFile(Date date) {
        return new SimpleDateFormat("dd-MM-yy-HH-mm-ss").format(date);
    }

    /**
     * Checks if a given name is a valid operation name (contains only letters and/or numbers).
     * @param opName
     * @return true if the name is valid, false if not.
     */
    public static boolean isValidOperationName(String opName) {
        boolean isValid = true;

        if(opName == null || opName.isEmpty()) {
            return false;
        }
        for(char c : opName.toCharArray()) {
            if(!Character.isLetter(c) && !Character.isDigit(c)) {
                isValid = false;
            }
        }

        return isValid;
    }
}
