package me.macronis.blucooldown.util;

public class TimeUtil {

    public static String format(long seconds) {

        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;

        if (minutes <= 0) {
            return remainingSeconds + "s";
        }

        return minutes + "m " + remainingSeconds + "s";
    }
}