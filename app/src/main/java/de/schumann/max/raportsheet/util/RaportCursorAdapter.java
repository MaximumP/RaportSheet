package de.schumann.max.raportsheet.util;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.schumann.max.raportsheet.R;

/**
 * Created by max on 09.06.16.
 */
public class RaportCursorAdapter extends SimpleCursorAdapter {
    public RaportCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
    }

    public RaportCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public void setViewText(TextView v, String text) {
        if (v.getId() == R.id.id) {
            long ticks = Long.parseLong(text);
            Date date = new Date(ticks);
            text = new SimpleDateFormat("dd.MM.yyyy", v.getResources().getConfiguration().locale).format(date);
        }
        super.setViewText(v, text);
    }
}
