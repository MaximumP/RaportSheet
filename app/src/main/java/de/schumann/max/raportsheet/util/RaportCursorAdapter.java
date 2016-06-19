package de.schumann.max.raportsheet.util;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.schumann.max.raportsheet.R;

/**
 * Created by max on 09.06.16.
 *
 * Overrides the SimpleCursorAdapter to format the date output since it is a long(ticks) value
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

    @Override
    public void setViewImage(ImageView v, String value) {
        if (v.getId() == R.id.printed && !value.equals("0")) {
            v.setVisibility(View.VISIBLE);
        } else
            super.setViewImage(v, value);
    }
}
