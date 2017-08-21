package com.aip.targascan.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aipxperts-ubuntu-01 on 1/8/17.
 */

public class ProductDetail implements Parcelable{
    String date;
    String carton_num1;
    String record_created;
    String driver_id1;
    String cust_name;
    String work_order_num ;
    String delivery_name;
    String delivery_address;
    String zip;
    String quantity ;
    String weight ;
    String co_type1 ;
    String redeliver1;
    String assigned_route ;
    String osd;
    String comment;
    String uid;
    String delivery_address3;
    String pupname;
    String pupaddress;
    String pupaddress2;
    String pupcity;
    String pupzip;
    String city;
    String pickup;
    String destination;
    String sig_string;

    public ProductDetail(Parcel in) {
        date = in.readString();
        carton_num1 = in.readString();
        record_created = in.readString();
        driver_id1 = in.readString();
        cust_name = in.readString();
        work_order_num = in.readString();
        delivery_name = in.readString();
        delivery_address = in.readString();
        zip = in.readString();
        quantity = in.readString();
        weight = in.readString();
        co_type1 = in.readString();
        redeliver1 = in.readString();
        assigned_route = in.readString();
        osd = in.readString();
        comment = in.readString();
        uid = in.readString();
        delivery_address3 = in.readString();
        pupname = in.readString();
        pupaddress = in.readString();
        pupaddress2 = in.readString();
        pupcity = in.readString();
        pupzip = in.readString();
        city = in.readString();
        pickup = in.readString();
        destination = in.readString();
        sig_string = in.readString();
    }

    public static final Creator<ProductDetail> CREATOR = new Creator<ProductDetail>() {
        @Override
        public ProductDetail createFromParcel(Parcel in) {
            return new ProductDetail(in);
        }

        @Override
        public ProductDetail[] newArray(int size) {
            return new ProductDetail[size];
        }
    };

    public ProductDetail() {

    }

    public String getSig_string() {
        return sig_string;
    }

    public void setSig_string(String sig_string) {
        this.sig_string = sig_string;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCarton_num1() {
        return carton_num1;
    }

    public void setCarton_num1(String carton_num1) {
        this.carton_num1 = carton_num1;
    }

    public String getRecord_created() {
        return record_created;
    }

    public void setRecord_created(String record_created) {
        this.record_created = record_created;
    }

    public String getDriver_id1() {
        return driver_id1;
    }

    public void setDriver_id1(String driver_id1) {
        this.driver_id1 = driver_id1;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getWork_order_num() {
        return work_order_num;
    }

    public void setWork_order_num(String work_order_num) {
        this.work_order_num = work_order_num;
    }

    public String getDelivery_name() {
        return delivery_name;
    }

    public void setDelivery_name(String delivery_name) {
        this.delivery_name = delivery_name;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }



    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }


    public String getCo_type1() {
        return co_type1;
    }

    public void setCo_type1(String co_type1) {
        this.co_type1 = co_type1;
    }

    public String getRedeliver1() {
        return redeliver1;
    }

    public void setRedeliver1(String redeliver1) {
        this.redeliver1 = redeliver1;
    }

    public String getAssigned_route() {
        return assigned_route;
    }

    public void setAssigned_route(String assigned_route) {
        this.assigned_route = assigned_route;
    }

    public String getOsd() {
        return osd;
    }

    public void setOsd(String osd) {
        this.osd = osd;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDelivery_address3() {
        return delivery_address3;
    }

    public void setDelivery_address3(String delivery_address3) {
        this.delivery_address3 = delivery_address3;
    }

    public String getPupname() {
        return pupname;
    }

    public void setPupname(String pupname) {
        this.pupname = pupname;
    }

    public String getPupaddress() {
        return pupaddress;
    }

    public void setPupaddress(String pupaddress) {
        this.pupaddress = pupaddress;
    }

    public String getPupaddress2() {
        return pupaddress2;
    }

    public void setPupaddress2(String pupaddress2) {
        this.pupaddress2 = pupaddress2;
    }

    public String getPupcity() {
        return pupcity;
    }

    public void setPupcity(String pupcity) {
        this.pupcity = pupcity;
    }

    public String getPupzip() {
        return pupzip;
    }

    public void setPupzip(String pupzip) {
        this.pupzip = pupzip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(date);
        parcel.writeString(carton_num1);
        parcel.writeString(record_created);
        parcel.writeString(driver_id1);
        parcel.writeString(cust_name);
        parcel.writeString(work_order_num);
        parcel.writeString(delivery_name);
        parcel.writeString(delivery_address);
        parcel.writeString(zip);
        parcel.writeString(quantity);
        parcel.writeString(weight);
        parcel.writeString(co_type1);
        parcel.writeString(redeliver1);
        parcel.writeString(assigned_route);
        parcel.writeString(osd);
        parcel.writeString(comment);
        parcel.writeString(uid);
        parcel.writeString(delivery_address3);
        parcel.writeString(pupname);
        parcel.writeString(pupaddress);
        parcel.writeString(pupaddress2);
        parcel.writeString(pupcity);
        parcel.writeString(pupzip);
        parcel.writeString(city);
        parcel.writeString(pickup);
        parcel.writeString(destination);
        parcel.writeString(sig_string);
    }
}
