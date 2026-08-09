package com.servicehub.common.util;

import java.util.Collection;
import java.util.Objects;
import java.util.regex.Pattern;

public final class ValidationUtils {

    private ValidationUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public static boolean isNotBlank(String value) {
        return !isBlank(value);
    }

    public static boolean matches(String value, String regex) {

        if (isBlank(value)) {
            return false;
        }

        return Pattern.matches(regex, value.trim());
    }

    public static boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isPositive(Number number) {

        if (number == null) {
            return false;
        }

        return number.doubleValue() > 0;
    }

    public static boolean isNonNegative(Number number) {

        if (number == null) {
            return false;
        }

        return number.doubleValue() >= 0;
    }

    public static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean areEqual(Object first, Object second) {
        return Objects.equals(first, second);
    }

}
