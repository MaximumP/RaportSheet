package de.schumann.max.raportsheet;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.schumann.max.raportsheet.model.Customer;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CustomerDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CustomerDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerDetailFragment extends Fragment {

    private static final String ARG_CUSTOMER = "argument_customer";
    private TextView nameTextView;
    private TextView phoneNoTextView;
    private TextView cityTextView;
    private TextView streetTextView;
    private TextView houseNoTextView;

    private Customer customer;

    private OnFragmentInteractionListener mListener;

    public CustomerDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param customer The Customer to be displayed.
     * @return A new instance of fragment CustomerDetailFragment.
     */
    public static CustomerDetailFragment newInstance(Customer customer) {
        CustomerDetailFragment fragment = new CustomerDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CUSTOMER, customer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            customer = getArguments().getParcelable(ARG_CUSTOMER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        nameTextView = (TextView) getView().findViewById(R.id.customer_name_text);
        phoneNoTextView = (TextView) getView().findViewById(R.id.customer_phone_no);
        cityTextView = (TextView) getView().findViewById(R.id.customer_city_text);
        streetTextView = (TextView) getView().findViewById(R.id.customer_street_text);
        houseNoTextView = (TextView) getView().findViewById(R.id.customer_house_no);

        if (customer != null) {
            nameTextView.setText(customer.getName());
            phoneNoTextView.setText(customer.getPhone());
            cityTextView.setText(customer.getCity());
            streetTextView.setText(customer.getStreet());
            houseNoTextView.setText(customer.getHouseNo());
        } else {
            customer = new Customer();
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.customer_detail, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);

        customer.setName(nameTextView.getText().toString());
        customer.setPhone(phoneNoTextView.getText().toString());
        customer.setCity(cityTextView.getText().toString());
        customer.setStreet(streetTextView.getText().toString());
        customer.setHouseNo(Integer.parseInt(houseNoTextView.getText().toString()));

        bundle.putParcelable(Customer.CUSTOMER_MODEL, customer);
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
        void onFragmentInteraction(Uri uri);
    }
}
