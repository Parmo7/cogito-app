package uk.ac.aston.cogito.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeManager {

    private static final SimpleDateFormat TIME_FORMAT_24 = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat TIME_FORMAT_12 = new SimpleDateFormat("hh:mm aa");

    public static String date24to12(String stringTime24) {
        try {
            Date date24 = TIME_FORMAT_24.parse(stringTime24);
            String stringTime12 = TIME_FORMAT_12.format(date24);
            return stringTime12;

        } catch (ParseException ignored) {}

        return null;
    }

    public static long date24toMillisNext(String stringTime24) {
        try {
            // Get the time
            Calendar tmpTime = Calendar.getInstance();
            tmpTime.setTime(TIME_FORMAT_24.parse(stringTime24));

            // Get today's scheduled time
            Calendar scheduledTime = Calendar.getInstance();
            scheduledTime.set(Calendar.HOUR_OF_DAY, tmpTime.get(Calendar.HOUR_OF_DAY));
            scheduledTime.set(Calendar.MINUTE, tmpTime.get(Calendar.MINUTE));
            scheduledTime.set(Calendar.SECOND, 0);

            // Check if the scheduled time has passed already;
            Calendar todayTime = Calendar.getInstance();
            if (scheduledTime.before(todayTime)) {
                // Increase the time by 24h
                long scheduledTimeMillis = scheduledTime.getTimeInMillis();
                long newScheduledTimeMillis = scheduledTimeMillis + 1000 * 60 * 60 * 24;
                scheduledTime.setTimeInMillis(newScheduledTimeMillis);
            }


            return scheduledTime.getTimeInMillis();

        } catch (ParseException ignored) {}

        return -1;
    }
}
