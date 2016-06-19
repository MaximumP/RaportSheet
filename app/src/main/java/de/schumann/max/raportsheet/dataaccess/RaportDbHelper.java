package de.schumann.max.raportsheet.dataaccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import de.schumann.max.raportsheet.dataaccess.RaportContract.RaportEntry;

/**
 * Created by max on 02.06.16.
 */
public class RaportDbHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "raport.db";

    public RaportDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_RAPORT_TABLE = "CREATE TABLE " + RaportEntry.TABLE_NAME  + " (" +
                RaportEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RaportEntry.COLUMN_RAPORT_DATE + " INTEGER NOT NULL, " +
                RaportEntry.COLUMN_RAPORT_CUSTOMER_NAME + " TEXT NOT NULL, " +
                RaportEntry.COLUMN_RAPORT_CUSTOMER_CITY + " TEXT NOT NULL, " +
                RaportEntry.COLUMN_RAPORT_WORK_DESCRIPTION + " TEXT NOT NULL, " +
                RaportEntry.COLUMN_RAPORT_WORK_HOURS + " REAL NOT NULL," +
                RaportEntry.COLUMN_RAPORT_PRINTED + " INTEGER DEFAULT 0);";

        sqLiteDatabase.execSQL(SQL_CREATE_RAPORT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RaportEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
