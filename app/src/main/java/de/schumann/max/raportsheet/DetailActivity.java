package de.schumann.max.raportsheet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import de.schumann.max.raportsheet.model.Raport;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_RAPORT_ITEM = "de.schumann.max.raportsheet.EXTRA_RAPORT_ITEM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (savedInstanceState == null) {
            Raport raport = null;
            if (extras != null)
                raport = (Raport) extras.getSerializable(EXTRA_RAPORT_ITEM);
            RaportDetailFragment detailFragment = RaportDetailFragment.newInstance(raport);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_container, detailFragment).commit();
        }
    }
}
