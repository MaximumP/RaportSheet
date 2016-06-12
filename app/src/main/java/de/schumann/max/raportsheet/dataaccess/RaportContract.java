package de.schumann.max.raportsheet.dataaccess;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by max on 02.06.16.
 */
public class RaportContract {

    public static final String CONTENT_AUTHORITY = "de.schumann.max.raportsheet";
    public static final Uri    BASE_CONTENT_URI  = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_RAPORT = "raport";

    public static final class RaportEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RAPORT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +PATH_RAPORT;

        public static final String TABLE_NAME = "raport";

        //Columns
        public static final String COLUMN_RAPORT_DATE = "raport_date";
        public static final String COLUMN_RAPORT_CUSTOMER_NAME = "raport_customer_name";
        public static final String COLUMN_RAPORT_CUSTOMER_CITY = "raport_customer_city";
        public static final String COLUMN_RAPORT_WORK_DESCRIPTION = "raport_work_description";
        public static final String COLUMN_RAPORT_WORK_HOURS = "raport_work_hours";

        public static Uri buildRaportUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildRaportUriWithDates(Date from , Date to) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(from.getTime()))
                    .appendPath(Long.toString(to.getTime()))
                    .build();
        }
    }
}
