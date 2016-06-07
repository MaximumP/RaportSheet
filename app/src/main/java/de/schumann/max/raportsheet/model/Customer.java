package de.schumann.max.raportsheet.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Customer model
 *
 * Created by max on 01.06.16.
 */
public class Customer implements Parcelable, Serializable {

    public static final String CUSTOMER_MODEL = "de.schumann.max.raportsheet.model.CUSTOMER";

    private long id;
    private String name;
    private String phone;
    private String city;
    private String street;
    private int houseNo;

    public Customer() {}
    public Customer(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }
    public int getHouseNo() { return houseNo; }
    public void setHouseNo(int houseNo) { this.houseNo = houseNo; }

    // Parcelable implementation

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this);
    }

    public static final Parcelable.Creator<Customer> CREATOR = new Parcelable.Creator<Customer>() {

        @Override
        public Customer createFromParcel(Parcel parcel) {
            return (Customer) parcel.readSerializable();
        }

        @Override
        public Customer[] newArray(int i) {
            return new Customer[i];
        }
    };
}
