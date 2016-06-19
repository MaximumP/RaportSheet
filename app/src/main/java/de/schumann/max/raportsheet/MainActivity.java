package de.schumann.max.raportsheet;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null && findViewById(R.id.fragment_container) != null) {
            final RaportListFragment listFragment = RaportListFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, listFragment).commit();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            final Intent showDetail = new Intent(this, DetailActivity.class);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(showDetail);
                }
            });
        }
    }
}
