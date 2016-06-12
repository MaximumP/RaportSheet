package de.schumann.max.raportsheet;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import de.schumann.max.raportsheet.dataaccess.RaportContract;
import de.schumann.max.raportsheet.util.Printer;
import de.schumann.max.raportsheet.util.RaportCursorAdapter;

/**
 * A fragment representing a list of Items.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class RaportListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private RaportCursorAdapter cursorAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RaportListFragment() {
    }

    @SuppressWarnings("unused")
    public static RaportListFragment newInstance(int columnCount) {
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
        ListView listView = (ListView) inflater.inflate(R.layout.raport_list_view, container, false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object test = cursorAdapter.getItem(i);
            }
        });

        Context context = listView.getContext();
        String[] projection = new String[]{
                RaportContract.RaportEntry.COLUMN_RAPORT_DATE,
                RaportContract.RaportEntry.COLUMN_RAPORT_CUSTOMER_NAME
        };
        int [] to = new int[]{ R.id.id, R.id.content };

        // Set the adapter
        getLoaderManager().initLoader(0, null, this);
        cursorAdapter = new RaportCursorAdapter(
                context,
                R.layout.raport_list_item,
                null,
                projection,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(cursorAdapter);
        return listView;
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
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            //mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = new String[]{
                "_id",
                RaportContract.RaportEntry.COLUMN_RAPORT_DATE,
                RaportContract.RaportEntry.COLUMN_RAPORT_CUSTOMER_NAME,
                RaportContract.RaportEntry.COLUMN_RAPORT_CUSTOMER_CITY,
                RaportContract.RaportEntry.COLUMN_RAPORT_WORK_DESCRIPTION,
                RaportContract.RaportEntry.COLUMN_RAPORT_WORK_HOURS
        };
        CursorLoader loader = new CursorLoader(getContext(),
                RaportContract.RaportEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Cursor item);
    }
}
