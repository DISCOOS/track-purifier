package no.hvl.dowhile.utility;

import com.hs.gpxparser.modal.GPX;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility methods for formatting objects into strings.
 */
public class StringTools {
    /**
     * Removes all characters which is not alphabetic, digit or a space.
     *
     * @param s the string to purify.
     * @return the purified string.
     */
    static String purifyString(String s) {
        StringBuilder builder = new StringBuilder("");
        for (char c : s.toCharArray()) {
            if (Character.isAlphabetic(c) || Character.isDigit(c) || c == ' ') {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    /**
     * Formats a Date into a string.
     *
     * @param date the date to format.
     * @return the date formatted as a String.
     */
    public static String formatDate(Date date) {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date);
    }

    /**
     * Formats a Date into a string without spaces.
     *
     * @param date the date to format.
     * @return the date formatted as a String without spaces.
     */
    public static String formatDateForFile(Date date) {
        return new SimpleDateFormat("dd-MM-yy-HH-mm-ss").format(date);
    }

    /**
     * Formats a Date into a string without spaces, hours, minutes and seconds.
     *
     * @param date the date to format.
     * @return the date formatted as a String without spaces, hours, minutes and seconds.
     */
    static String formatDateForOrganizing(Date date) {
        return new SimpleDateFormat("dd-MM-yy").format(date);
    }

    /**
     * Formats a Date into a String without year.
     *
     * @param date the date to format.
     * @return the date formatted as a String without year.
     */
    public static String formatDateForFileProcessing(Date date) {
        return new SimpleDateFormat("dd-MM HH:mm").format(date);
    }

    /**
     * Checks if a given name is a valid operation name (contains only letters and/or numbers).
     *
     * @param operationName the name to check.
     * @return true if the name is valid, false if not.
     */
    public static boolean isValidOperationName(String operationName) {
        if (operationName == null || operationName.isEmpty()) {
            return false;
        }
        for (char c : operationName.toCharArray()) {
            if (!Character.isLetter(c) && !Character.isDigit(c) && !(c == ' ')) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the given operation name is between 2 and 50 characters.
     *
     * @param operationName The operation name to check.
     * @return True if the name has a valid length, false if not.
     */
    public static boolean operationNameLengthIsValid(String operationName) {
        return operationName.length() >= 2 && operationName.length() <= 50;
    }

    /**
     * Gets the track's start time and end time as one single String.
     *
     * @param gpx The file to get the times from.
     * @return The start time and end time.
     */
    public static String startTimeAndEndTimeToString(GPX gpx) {
        return Messages.TRACK_START.get() + TrackTools.getStartTimeFromTrack(gpx) + ", " + Messages.TRACK_END.get() + TrackTools.getEndTimeFromTrack(gpx);
    }

    /**
     * Takes the name of a raw waypoint file and injects the file's index into it.
     *
     * @param name  The name to replace.
     * @param index The index to inject into the name
     * @return The new name.
     */
    public static String renameRawWaypointName(String name, int index) {
        return new StringBuilder(name).insert(name.length() - 4, "_" + index).toString();
    }

}
