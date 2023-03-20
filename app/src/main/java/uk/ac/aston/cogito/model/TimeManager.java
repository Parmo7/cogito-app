package uk.ac.aston.cogito.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class TimeManager {

    public static final SimpleDateFormat TIME_FORMAT_24 = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat TIME_FORMAT_12 = new SimpleDateFormat("hh:mm aa");

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


    public static long date24toMillisTomorrow(String stringTime24) {
        try {
            // Get the time
            Calendar tmpTime = Calendar.getInstance();
            tmpTime.setTime(TIME_FORMAT_24.parse(stringTime24));

            // Get today's scheduled time
            Calendar scheduledTime = Calendar.getInstance();

            // Increase the time by 24h
            long scheduledTimeMillis = scheduledTime.getTimeInMillis();
            long newScheduledTimeMillis = scheduledTimeMillis + 1000 * 60 * 60 * 24;
            scheduledTime.setTimeInMillis(newScheduledTimeMillis);
            scheduledTime.set(Calendar.HOUR_OF_DAY, tmpTime.get(Calendar.HOUR_OF_DAY));
            scheduledTime.set(Calendar.MINUTE, tmpTime.get(Calendar.MINUTE));
            scheduledTime.set(Calendar.SECOND, 0);

            return scheduledTime.getTimeInMillis();

        } catch (ParseException ignored) {}

        return -1;
    }


    public static String randomTime24() {
        // Generate random hour
        Random r = new Random();
        int minHour = 8;
        int maxHour = 21;
        int rdmHour = r.nextInt(maxHour - minHour) + minHour;

        // Generate random minute
        int minMinute = 0;
        int maxMinute = 60;
        int rdmMinute = r.nextInt(maxHour-minHour) + minHour;

        String hourString = String.format("%02d", rdmHour);
        String minString = String.format("%02d", rdmMinute);

        return hourString + ":" + minString;
    }
}
