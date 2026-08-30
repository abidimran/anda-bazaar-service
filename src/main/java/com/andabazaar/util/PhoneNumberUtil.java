package com.andabazaar.util;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.Phonenumber;

public final class PhoneNumberUtil {

    private static final com.google.i18n.phonenumbers.PhoneNumberUtil PHONE_UTIL =
            com.google.i18n.phonenumbers.PhoneNumberUtil.getInstance();

    private static final String DEFAULT_REGION = "IN";

    private PhoneNumberUtil() {}

    public static boolean isValid(String number) {
        return isValid(number, DEFAULT_REGION);
    }

    public static boolean isValid(String number, String regionCode) {
        if (number == null || number.isBlank()) return false;
        try {
            Phonenumber.PhoneNumber parsed = PHONE_UTIL.parse(number, regionCode);
            return PHONE_UTIL.isValidNumber(parsed);
        } catch (NumberParseException e) {
            return false;
        }
    }

    public static String formatE164(String number) {
        return formatE164(number, DEFAULT_REGION);
    }

    public static String formatE164(String number, String regionCode) {
        if (number == null || number.isBlank()) return number;
        try {
            Phonenumber.PhoneNumber parsed = PHONE_UTIL.parse(number, regionCode);
            return PHONE_UTIL.format(parsed, com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat.E164);
        } catch (NumberParseException e) {
            return number;
        }
    }

    public static String getCountryCode(String number) {
        return getCountryCode(number, DEFAULT_REGION);
    }

    public static String getCountryCode(String number, String regionCode) {
        if (number == null || number.isBlank()) return null;
        try {
            Phonenumber.PhoneNumber parsed = PHONE_UTIL.parse(number, regionCode);
            return String.valueOf(parsed.getCountryCode());
        } catch (NumberParseException e) {
            return null;
        }
    }
}
