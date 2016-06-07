package de.schumann.max.raportsheet.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

/**
 * Raport model
 *
 * Created by max on 01.06.16.
 */
public class Raport implements Parcelable, Serializable {

    public static final String RAPORT_MODEL = "de.schumann.max.raportsheet.model.RAPORT";

    private long id;
    private Date date;
    private Customer customer;
    private String workDescription;
    private int workMinutes;

    public Raport() {}
    public Raport(Date date, Customer customer, String workDescription, int workMinutes) {
        this.date = date;
        this.customer = customer;
        this.workDescription = workDescription;
        this.workMinutes = workMinutes;
    }

    protected Raport(Parcel in) {
        id = in.readLong();
        customer = in.readParcelable(Customer.class.getClassLoader());
        workDescription = in.readString();
        workMinutes = in.readInt();
    }

    public long getId() { return id; }
    public Date getDate() { return date; }
    public Customer getCustomer() { return customer; }
    public String getWorkDescription() { return workDescription; }
    public  int getWorkMinutes() { return workMinutes; }

    public void setDate(Date date) { this.date = date; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public void setWorkDescription(String workDescription) { this.workDescription = workDescription; }
    public void setWorkMinutes(int workMinutes) { this.workMinutes = workMinutes; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSerializable(this);
    }

    public static final Creator<Raport> CREATOR = new Creator<Raport>() {
        @Override
        public Raport createFromParcel(Parcel in) {
            return new Raport(in);
        }

        @Override
        public Raport[] newArray(int size) {
            return new Raport[size];
        }
    };
}
