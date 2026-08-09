package com.servicehub.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public final class DateUtils {
    private DateUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static int calculateAge(LocalDate dob) {

        if (dob == null) {
            return 0;
        }

        return Period.between(dob, LocalDate.now()).getYears();
    }

    public static boolean isAdult(LocalDate dob) {
        return calculateAge(dob) >= 18;
    }

    public static boolean isMinimumAge(LocalDate dob, int age) {
        return calculateAge(dob) >= age;
    }

    public static boolean isMaximumAge(LocalDate dob, int age) {
        return calculateAge(dob) <= age;
    }

    public static boolean isFuture(LocalDate date) {
        return date != null && date.isAfter(LocalDate.now());
    }

    public static boolean isPast(LocalDate date) {
        return date != null && date.isBefore(LocalDate.now());
    }

    public static boolean isToday(LocalDate date) {
        return date != null && date.equals(LocalDate.now());
    }

    public static boolean isFuture(LocalDateTime dateTime) {
        return dateTime != null && dateTime.isAfter(LocalDateTime.now());
    }

    public static boolean isPast(LocalDateTime dateTime) {
        return dateTime != null && dateTime.isBefore(LocalDateTime.now());
    }
}

