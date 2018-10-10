package com.monich.cost_price.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.apache.commons.lang3.StringUtils;

public class PhoneUtils {
    private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    public static boolean isValid(String value) {
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(value, null);
            return phoneNumberUtil.isPossibleNumber(phoneNumber);
        } catch (NumberParseException e) {
            return false;
        }
    }

    public static String toFormatted(String phone) {
        if (StringUtils.isBlank(phone)) {
            return null;
        }
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(phone, null);
            return phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
        } catch (NumberParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String fromFormatted(String phone){
        if (StringUtils.isBlank(phone)) {
            return null;
        }
        return phone.replaceAll("[\\s-()]+", "");
    }
}
