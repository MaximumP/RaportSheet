package de.schumann.max.raportsheet;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.schumann.max.raportsheet.dataaccess.RaportContract.RaportEntry;
import de.schumann.max.raportsheet.model.Raport;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link RaportDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RaportDetailFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_RAPORT = "Raport";

    private Raport raport;
    private Locale locale;

    //TODO: validate after focus leave
    private TextView date;
    private TextView customerName;
    private TextView customerCity;
    //private Button addCustomer;
    private TextView workDescription;
    private TextView workHours;

    public RaportDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param raport raport model.
     * @return A new instance of fragment RaportDetailFragment.
     */
    public static RaportDetailFragment newInstance(Raport raport) {
        RaportDetailFragment fragment = new RaportDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RAPORT, raport);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            raport = (Raport) args.getSerializable(ARG_RAPORT);
        } else {
            raport = null;
        }
        locale = getResources().getConfiguration().locale;
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.raport_detail_view, container, false);
        date = (TextView) view.findViewById(R.id.raport_date);
        customerName = (TextView) view.findViewById(R.id.raport_customer_text);
        customerCity = (TextView) view.findViewById(R.id.raport_city_text);
        workDescription = (TextView) view.findViewById(R.id.raport_work_description);
        workHours = (TextView) view.findViewById(R.id.raport_work_minutes);

        String sDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", locale);

        if (raport != null) {
            sDate = dateFormat.format(raport.getDate());
            date.setText(sDate);
            customerName.setText(raport.getCustomer());
            customerCity.setText(raport.getCity());
            workDescription.setText(raport.getWorkDescription());
            workHours.setText(String.format(getContext().getResources().getConfiguration().locale,
                    "%.1f", raport.getWorkHours()));
        } else {
            Date today = new Date();
            sDate = dateFormat.format(today);
            date.setText(sDate);
        }
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        if (raport == null || raport.getId() == 0)
            menu.findItem(R.id.action_delete_raport).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_save_raport:
                saveRaport();
                getActivity().onBackPressed();
                return true;
            case R.id.action_delete_raport:
                deleteRaport();
                getActivity().onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);

        try {
            raport.setDate(new SimpleDateFormat("dd.MM.yyyy", locale)
                    .parse(date.getText().toString()));
        } catch (ParseException e) {
            //TODO: handle exception
            e.printStackTrace();
        }
        raport.setWorkDescription(workDescription.getText().toString());
        raport.setWorkHours(Integer.parseInt(workHours.getText().toString()));
        raport.setCustomer(customerName.getText().toString());
        raport.setCity(customerCity.getText().toString());

        bundle.putSerializable(Raport.RAPORT_MODEL, raport);
    }

    private void saveRaport(){
        //TODO: validate
        Date inDate;
        double hours;
        long ticks;
        NumberFormat numberFormat = NumberFormat.
                getNumberInstance(getContext().getResources().getConfiguration().locale);
        try {
            inDate = new SimpleDateFormat("d.M.yy", locale).parse(date.getText().toString());
            Number number = numberFormat.parse(workHours.getText().toString());
            hours = number.doubleValue();
            ticks = inDate.getTime();
        } catch (ParseException e) {
            date.setError(getString(R.string.error_date_format));
            return;
        }
        ContentValues values = new ContentValues();
        values.put(RaportEntry.COLUMN_RAPORT_DATE, ticks);
        values.put(RaportEntry.COLUMN_RAPORT_CUSTOMER_CITY, customerCity.getText().toString());
        values.put(RaportEntry.COLUMN_RAPORT_CUSTOMER_NAME, customerName.getText().toString());
        values.put(RaportEntry.COLUMN_RAPORT_WORK_DESCRIPTION, workDescription.getText().toString());
        values.put(RaportEntry.COLUMN_RAPORT_WORK_HOURS, hours);
        if (raport == null || raport.getId() == 0)
            getContext().getContentResolver().insert(RaportEntry.CONTENT_URI, values);
        else
            getContext().getContentResolver()
                    .update(RaportEntry.CONTENT_URI, values, "_id = ?",
                            new String[] {Long.toString(raport.getId())});
    }

    private void deleteRaport() {
        if (raport != null && raport.getId() != 0){
            getContext().getContentResolver()
                    .delete(RaportEntry.CONTENT_URI, "_id = ?",
                            new String[] {Long.toString(raport.getId())});
        }
    }
}
