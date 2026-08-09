package com.servicehub.common.util;

import java.util.regex.Pattern;

public final class RegexPatternsUtil {
    private RegexPatternsUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static final Pattern INDIAN_MOBILE = Pattern.compile("^[6-9]\\d{9}$");

    public static final Pattern PIN_CODE = Pattern.compile("^[1-9][0-9]{5}$");

    public static final Pattern PAN = Pattern.compile("^[A-Z]{5}[0-9]{4}[A-Z]$");

    public static final String GST =
            "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$";

    public static final String IFSC = "^[A-Z]{4}0[A-Z0-9]{6}$";

    public static final Pattern AADHAAR = Pattern.compile("^[2-9][0-9]{11}$");

    public static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public static final String USERNAME = "^[a-zA-Z0-9_]{4,20}$";

    public static final String PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!*()_\\-]).{8,}$";

    public static final String ALPHABET = "^[A-Za-z ]+$";

    public static final String ALPHANUMERIC = "^[A-Za-z0-9]+$";

    public static final String NUMERIC = "^\\d+$";

    public static final String SLUG = "^[a-z0-9]+(?:-[a-z0-9]+)*$";

    public static final String VEHICLE_NUMBER =  "^[A-Z]{2}\\d{1,2}[A-Z]{1,2}\\d{4}$";

    public static final String HEX_COLOR = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";

    public static final String UUID =
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$";

}
