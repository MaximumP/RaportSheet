package de.schumann.max.raportsheet.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

import de.schumann.max.raportsheet.dataaccess.RaportContract.RaportEntry;

/**
 * Raport model
 *
 * Created by max on 01.06.16.
 */
public class Raport implements Serializable {

    public static final String RAPORT_MODEL = "de.schumann.max.raportsheet.model.RAPORT";

    private long   id;
    private Date   date;
    private String customer;
    private String city;
    private String workDescription;
    private double    workHours;

    public Raport(Cursor cursor) {
        id = cursor.getLong(cursor.getColumnIndex("_id"));
        customer = cursor.getString(cursor.getColumnIndex(RaportEntry.COLUMN_RAPORT_CUSTOMER_NAME));
        city = cursor.getString(cursor.getColumnIndex(RaportEntry.COLUMN_RAPORT_CUSTOMER_CITY));
        workDescription = cursor.getString(cursor.getColumnIndex(RaportEntry.COLUMN_RAPORT_WORK_DESCRIPTION));
        workHours = cursor.getDouble(cursor.getColumnIndex(RaportEntry.COLUMN_RAPORT_WORK_HOURS));
        long ticks = cursor.getLong(cursor.getColumnIndex(RaportEntry.COLUMN_RAPORT_DATE));
        date = new Date(ticks);

    }

    protected Raport(Parcel in) {
        id = in.readLong();
        customer = in.readParcelable(Customer.class.getClassLoader());
        workDescription = in.readString();
        workHours = in.readInt();
    }

    public long getId() { return id; }
    public Date getDate() { return date; }
    public String getCustomer() { return customer; }
    public String getWorkDescription() { return workDescription; }
    public String getCity() { return city; }
    public  double getWorkHours() { return workHours; }

    public void setDate(Date date) { this.date = date; }
    public void setCustomer(String customer) { this.customer = customer; }
    public void setWorkDescription(String workDescription) { this.workDescription = workDescription; }
    public void setCity(String city) {this.city = city; }
    public void setWorkHours(int workHours) { this.workHours = workHours; }

}
