/*
 * Copyright (c) 2015 Stilavia
 */

package org.wanderingnet.data.jdbc.arch;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by guillermoblascojimenez on 11/12/15.
 */
public final class DatabaseTypesTranslator {

    private DatabaseTypesTranslator() {
    }

    public static Calendar toCalendar(Timestamp timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        return calendar;
    }

    public static Timestamp toTimestamp(Calendar calendar) {
        if (calendar == null) {
            return null;
        } else {
            return new Timestamp(calendar.getTimeInMillis());
        }
    }
}
