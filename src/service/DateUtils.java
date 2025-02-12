package service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String formatDate(LocalDate date) {
        return date.format(formatter);
    }

    public static LocalDate parseDate(String dateStr) {
        return LocalDate.parse(dateStr, formatter);
    }

    public static boolean isBeforeToday(LocalDate date) {
        return date.isBefore(LocalDate.now());
    }

    public static boolean isAfterToday(LocalDate date) {
        return date.isAfter(LocalDate.now());
    }
}
