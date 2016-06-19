package de.schumann.max.raportsheet.dataaccess;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

public class RaportProvider extends ContentProvider {

    //TODO initialize
    private static final UriMatcher uriMatcher = buildMatcher();
    private RaportDbHelper dbHelper;

    static final int RAPORT                = 100;
    static final int RAPORTS               = 101;
    static final int RAPORTS_BETWEEN_DATES = 102;

    private static final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

    private static final String raportByIdSelection =
                RaportContract.RaportEntry._ID + " = ? ";

    private static final String raportsBetweenDatesSelection =
                RaportContract.RaportEntry.COLUMN_RAPORT_DATE + ">=? AND " +
                    RaportContract.RaportEntry.COLUMN_RAPORT_DATE + " <= ? ";

    private Cursor getRaport(Uri uri, String[] projection, String sortOrder) {
        String id = uri.getPathSegments().get(1);
        String[] selectionArgs = new String[]{ id };
        String selection = raportByIdSelection;
        queryBuilder.setTables(RaportContract.RaportEntry.TABLE_NAME);
        return queryBuilder.query(dbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    private Cursor getRaports(String[] projection, String sortOrder) {
        queryBuilder.setTables(RaportContract.RaportEntry.TABLE_NAME);
        return queryBuilder.query(dbHelper.getReadableDatabase(),
                projection,
                null,
                null,
                null,
                null,
                sortOrder);
    }

    private Cursor getRaportsBetween(Uri uri, String[] projection, String sortOrder) {
        String dateFrom = uri.getPathSegments().get(1);
        String dateTo = uri.getPathSegments().get(2);
        String[] selectionArgs = new String[]{ dateFrom, dateTo };
        String selection = raportsBetweenDatesSelection;
        queryBuilder.setTables(RaportContract.RaportEntry.TABLE_NAME);
        return queryBuilder.query(dbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    public RaportProvider() {
    }

    @Override
    public synchronized int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsDeleted = 0;

        if (selection == null)
            selection = "1";
        switch (match) {
            case RAPORT:
            case RAPORTS:
                rowsDeleted = db.delete(RaportContract.RaportEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcher.match(uri);

        switch (match) {
            case RAPORT:
                return RaportContract.RaportEntry.CONTENT_ITEM_TYPE;
            case RAPORTS:
            case RAPORTS_BETWEEN_DATES:
                return RaportContract.RaportEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public synchronized Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case RAPORT:
            case RAPORTS:
                long _id = db.insert(RaportContract.RaportEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri =  RaportContract.RaportEntry.buildRaportUri(_id);
                else
                    throw new SQLException("Failed to insert row into: " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new RaportDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor;
        int match = uriMatcher.match(uri);
        switch (match) {
            case RAPORT:
                cursor = getRaport(uri, projection, sortOrder);
                break;
            case RAPORTS:
                cursor = getRaports(projection, sortOrder);
                break;
            case RAPORTS_BETWEEN_DATES:
                cursor = getRaportsBetween(uri, projection, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public synchronized int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final int match = uriMatcher.match(uri);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updatedRows = 0;

        switch (match) {
            case RAPORTS:
                updatedRows = db.update(RaportContract.RaportEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        if (updatedRows != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return updatedRows;
    }

    @Override
    public synchronized int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);

        switch (match) {
            case RAPORT:
                database.beginTransaction();
                int retCount = 0;
                try {
                    for (ContentValues value: values) {
                        long _id = database.insert(RaportContract.RaportEntry.TABLE_NAME, null, value);
                        if (_id > 0)
                            retCount++;
                    }
                    database.setTransactionSuccessful();
                } finally {
                    database.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return retCount;
        }
        return super.bulkInsert(uri, values);
    }

    private static UriMatcher buildMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(RaportContract.CONTENT_AUTHORITY, RaportContract.PATH_RAPORT + "/#", RAPORT);
        matcher.addURI(RaportContract.CONTENT_AUTHORITY, RaportContract.PATH_RAPORT, RAPORTS);
        matcher.addURI(RaportContract.CONTENT_AUTHORITY, RaportContract.PATH_RAPORT + "/#/#", RAPORTS_BETWEEN_DATES);
        return matcher;
    }
}
