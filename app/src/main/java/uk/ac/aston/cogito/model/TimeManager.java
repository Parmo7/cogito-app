package uk.ac.aston.cogito.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

}
