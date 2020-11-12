package com.alsayer.core.utils;

import com.alsayer.core.customer.services.GetCustomerDetails;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    private static final Logger LOG = Logger.getLogger(DateUtil.class);
    private static final String MM_DD_YYYY_DATE_FORMAT = "MM/dd/yyyy";

    public static Date convertStringToDate(final String date) {
        try {
            final SimpleDateFormat formatter = new SimpleDateFormat(MM_DD_YYYY_DATE_FORMAT);
            final Date parsedDate = formatter.parse(date);
            final Calendar cl = Calendar.getInstance();
            cl.setTime(parsedDate);
            cl.add(Calendar.HOUR_OF_DAY, 23);
            cl.add(Calendar.MINUTE, 59);
            cl.add(Calendar.SECOND, 59);

            return cl.getTime();
        } catch (final ParseException e) {
            LOG.error("Exception occurred while parsing date ", e);

        }
        return null;
    }
}
