package de.schumann.max.raportsheet;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Messenger;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import de.schumann.max.raportsheet.model.Raport;

public class DetailActivity extends AppCompatActivity
        implements RaportDetailFragment.OnFragmentInteractionListener {

    public static final String EXTRA_RAPORT_HANDLER = "de.schumann.max.raportsheet.EXTRA_RAPORT_HANDLER";
    public static final String EXTRA_RAPORT_ITEM = "de.schumann.max.raportsheet.EXTRA_RAPORT_ITEM";

    private Messenger reveiver;
    private Raport raport;
    private Context context = this;

    private EditText date;
    private EditText customer;
    private EditText city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onFragmentInteraction(Raport raport) {
        Log.v("Something", "happened");
    }
}
