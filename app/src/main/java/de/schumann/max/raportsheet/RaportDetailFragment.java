package de.schumann.max.raportsheet;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.schumann.max.raportsheet.dataaccess.RaportContract.RaportEntry;
import de.schumann.max.raportsheet.dataaccess.RaportProvider;
import de.schumann.max.raportsheet.model.Raport;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RaportDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RaportDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RaportDetailFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_RAPORT = "Raport";
    private DateFormat dateFormat;

    private Raport raport;

    //TODO: validate after focus leave
    private TextView date;
    private TextView customerName;
    private TextView customerCity;
    private Button addCustomer;
    private TextView workDescription;
    private TextView workMinutes;

    private OnFragmentInteractionListener mListener;

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
        args.putParcelable(ARG_RAPORT, raport);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            raport = (Raport) getArguments().getSerializable(ARG_RAPORT);
        } else {
            raport = null;
        }
        dateFormat = new SimpleDateFormat("d.M.yy", getResources().getConfiguration().locale);
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
        workMinutes = (TextView) view.findViewById(R.id.raport_work_minutes);
        addCustomer = (Button) view.findViewById(R.id.add_customer_button);

        if (raport != null) {
            date.setText(raport.getDate().toString());
            customerName.setText(raport.getCustomer().getName());
            customerCity.setText(raport.getCustomer().getCity());
            workDescription.setText(raport.getWorkDescription());
            workMinutes.setText(raport.getWorkMinutes());
        } else {
            Date today = new Date();
            String sDate = dateFormat.format(today);
            date.setText(sDate);
        }
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_save_raport:
                //TODO: validate
                ContentValues values = new ContentValues();
                values.put(RaportEntry.COLUMN_RAPORT_DATE, date.getText().toString());
                values.put(RaportEntry.COLUMN_RAPORT_CUSTOMER_CITY, customerCity.getText().toString());
                values.put(RaportEntry.COLUMN_RAPORT_CUSTOMER_NAME, customerName.getText().toString());
                values.put(RaportEntry.COLUMN_RAPORT_WORK_DESCRIPTION, workDescription.getText().toString());
                values.put(RaportEntry.COLUMN_RAPORT_WORK_MINUTES, workMinutes.getText().toString());
                Uri uri = getContext().getContentResolver().insert(RaportEntry.CONTENT_URI, values);
                Toast.makeText(getContext(), "inserted" + uri, Toast.LENGTH_LONG).show();
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);

        try {
            raport.setDate(dateFormat.parse(date.getText().toString()));
        } catch (ParseException e) {
            //TODO: handle exception
            e.printStackTrace();
        }
        raport.setWorkDescription(workDescription.getText().toString());
        raport.setWorkMinutes(Integer.parseInt(workMinutes.getText().toString()));
        if (raport.getCustomer() != null) {
            raport.getCustomer().setName(customerName.getText().toString());
            raport.getCustomer().setCity(customerCity.getText().toString());
        }

        bundle.putSerializable(Raport.RAPORT_MODEL, raport);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Raport raport);
    }
}
