package de.schumann.max.raportsheet;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.Calendar;
import java.util.Date;

import de.schumann.max.raportsheet.dataaccess.RaportContract;
import de.schumann.max.raportsheet.model.Raport;
import de.schumann.max.raportsheet.util.Printer;
import de.schumann.max.raportsheet.util.RaportCursorAdapter;

/**
 * A fragment representing a list of Items.
 */
public class RaportListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private RaportCursorAdapter cursorAdapter;
    private DatePicker          datePicker;
    private ScrollView          datePickerControl;
    private RaportListFragment _this = this;

    private final static int WEEK_LOADER = 0;
    private final static int DAY_LOADER = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RaportListFragment() {
    }


    public static RaportListFragment newInstance() {
        RaportListFragment fragment = new RaportListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.raport_list_view, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list);//inflater.inflate(R.layout.raport_list_view, container, false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) cursorAdapter.getItem(i);
                final Intent detailIntent = new Intent(getContext(), DetailActivity.class);
                detailIntent.putExtra(DetailActivity.EXTRA_RAPORT_ITEM, new Raport(cursor));
                startActivity(detailIntent);
            }
        });

        datePickerControl = (ScrollView) view.findViewById(R.id.date_picker_control);

        datePicker = (DatePicker) datePickerControl.findViewById(R.id.datePicker);
        Button selectWeekBtn = (Button) datePickerControl.findViewById(R.id.select_week_btn);
        Button selectDayBtn  = (Button) datePickerControl.findViewById(R.id.select_day_btn);

        selectWeekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLoaderManager().restartLoader(WEEK_LOADER, null, _this);
                datePickerControl.setVisibility(View.INVISIBLE);
            }
        });

        selectDayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLoaderManager().restartLoader(DAY_LOADER, null, _this);
                datePickerControl.setVisibility(View.INVISIBLE);
            }
        });

        setAdapter(listView);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_print:
                Cursor cursor = cursorAdapter.getCursor();
                Printer printer = new Printer(getContext());
                printer.printHtml(cursor);
                markPrinted(cursor);
                return true;
            case R.id.action_select_date:
                if (datePickerControl.getVisibility() == View.VISIBLE)
                    datePickerControl.setVisibility(View.INVISIBLE);
                else
                    datePickerControl.setVisibility(View.VISIBLE);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void markPrinted(Cursor cursor) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = new String[]{
                "_id",
                RaportContract.RaportEntry.COLUMN_RAPORT_DATE,
                RaportContract.RaportEntry.COLUMN_RAPORT_CUSTOMER_NAME,
                RaportContract.RaportEntry.COLUMN_RAPORT_CUSTOMER_CITY,
                RaportContract.RaportEntry.COLUMN_RAPORT_WORK_DESCRIPTION,
                RaportContract.RaportEntry.COLUMN_RAPORT_WORK_HOURS,
                RaportContract.RaportEntry.COLUMN_RAPORT_PRINTED
        };

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        Calendar calendar = Calendar.getInstance(getContext().getResources().getConfiguration().locale);
        calendar.clear();
        calendar.set(year, month, day);
        Date from;
        Date to;
        Uri uri = null;
        switch (id) {
            case WEEK_LOADER:
                while (Calendar.MONDAY != calendar.get(Calendar.DAY_OF_WEEK)) {
                    calendar.add(Calendar.DATE, -1);
                }
                from = calendar.getTime();
                long oneWeekTicks = 86400L * 7L * 1000L;
                to = new Date(from.getTime() + oneWeekTicks);
                uri = RaportContract.RaportEntry.buildRaportUriWithDates(from, to);
                break;
            case DAY_LOADER:
                from = calendar.getTime();
                long oneDayTicks = 84600L * 1000L;
                to = new Date(from.getTime() + oneDayTicks);
                uri = RaportContract.RaportEntry.buildRaportUriWithDates(from, to);
                break;
        }
        String orderBy = RaportContract.RaportEntry.COLUMN_RAPORT_DATE + " ASC";
        return new CursorLoader(getContext(),
                uri,
                projection,
                null,
                null,
                orderBy);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i("Row count", Integer.toString(data.getCount()));
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    private void setAdapter(ListView listView) {
        String[] projection = new String[]{
                RaportContract.RaportEntry.COLUMN_RAPORT_DATE,
                RaportContract.RaportEntry.COLUMN_RAPORT_CUSTOMER_NAME,
                RaportContract.RaportEntry.COLUMN_RAPORT_WORK_HOURS
        };
        int [] to = new int[]{ R.id.id, R.id.content, R.id.time };

        // Set the adapter
        getLoaderManager().initLoader(WEEK_LOADER, null, this);
        cursorAdapter = new RaportCursorAdapter(
                listView.getContext(),
                R.layout.raport_list_item,
                null,
                projection,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        listView.setAdapter(cursorAdapter);
    }
}
