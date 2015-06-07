package com.biegajmy.user;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import org.joda.time.LocalDate;
import org.joda.time.Years;

public class UserUtils {

    private static final int DEFAULT_PHOTO_HEIGHT = 400;
    private static final int DEFAULT_PHOTO_WIDTH = 400;
    private static final Formatter STRING_FORMATTER = new Formatter();
    private static final String PHOTO_URL_FORMAT = "https://graph.facebook.com/%s/picture?hieght=%d&width=%d";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    public static int getAge(String birthDay) {
        try {
            Date dateOfBirth = DATE_FORMAT.parse(birthDay);
            LocalDate now = new LocalDate();
            LocalDate birth = LocalDate.fromDateFields(dateOfBirth);
            return Years.yearsBetween(birth, now).getYears();
        } catch (ParseException e) {
            return 0;
        }
    }

    public static String getPhotoUrl(String id) {
        return getPhotoUrl(id, DEFAULT_PHOTO_HEIGHT, DEFAULT_PHOTO_WIDTH);
    }

    public static String getPhotoUrl(String id, int height, int width) {
        return STRING_FORMATTER.format(PHOTO_URL_FORMAT, id, height, width).toString();
    }

}
