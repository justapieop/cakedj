package me.justapie.cakedj.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {
    public static String formatDate(Date dateTime) {
        return new SimpleDateFormat("dd/MM/yyyy").format(dateTime);
    }
    public static String formatDate(OffsetDateTime dateTime) { return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")); }

    public static String formatTime(long sec) {
        Date date = new Date(sec);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }

    public static long parseTime(String input) {
        return Duration.parse("PT" + input).getSeconds() * 1000L;
    }
}
