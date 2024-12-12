package com.continuum.vendor.service.util;

import com.continuum.vendor.service.exceptions.VendorException;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {
    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    public static final String DATE_FORMAT_PATTERN_CHARGEMOD = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private static final ZoneId IST_ZONE = ZoneId.of("Asia/Kolkata");

    public static Date parseDate(String dateString){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN);
            return sdf.parse(dateString);
        }catch (Exception e){
            throw new VendorException(ErrorConstants.SYSTEM_ERROR_CODE, e.getMessage());
        }
    }

    public static String formatDate(LocalDateTime date) {
        return new SimpleDateFormat(DATE_FORMAT_PATTERN).format(date);
    }

    public static Date dateFormat(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = date.format(formatter);
        LocalDateTime formattedLocalDateTime = LocalDateTime.parse(formattedDate + "T00:00:00");
        return Date.from(formattedLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime parseDateChargeMod(String dateString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN_CHARGEMOD);
            return LocalDateTime.parse(dateString, formatter);
        } catch (Exception e) {
            throw new VendorException(ErrorConstants.SYSTEM_ERROR_CODE, e.getMessage());
        }
    }
    public static String formatDateToString(Date date) {
        return new SimpleDateFormat(DATE_FORMAT_PATTERN).format(date);
    }

    public static String getISTToUTC(LocalDateTime localDateTime){
        return localDateTime.atZone(IST_ZONE).withZoneSameInstant(ZoneOffset.UTC).toString();
    }

}
