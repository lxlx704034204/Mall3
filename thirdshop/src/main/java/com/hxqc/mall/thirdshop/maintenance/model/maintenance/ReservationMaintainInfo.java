package com.hxqc.mall.thirdshop.maintenance.model.maintenance;

import android.os.Parcel;
import android.os.Parcelable;

import com.hxqc.mall.thirdshop.maintenance.model.ApppintmentDateNew;
import com.hxqc.mall.thirdshop.maintenance.model.Mechanic;
import com.hxqc.mall.thirdshop.maintenance.model.ServiceAdviser;

import java.util.ArrayList;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 21
 * Des: 原本的预约信息
 * FIXME
 * Todo
 */
public class ReservationMaintainInfo implements Parcelable{

    public ArrayList<String> apppintmentDate; //可预约维修时间，最合适放在一个
    public ArrayList<ApppintmentDateNew> apppintmentDateNew; //可预约维修时间，最合适放在一个
    public ArrayList<ServiceAdviser>serviceAdviser; //可预约服务顾问，最合适放在一个服务顾问
    public ArrayList<Mechanic>mechanic; //可预约维修技师，最合适放在一个技师
    public RMMyAuto myAuto;

    protected ReservationMaintainInfo(Parcel in) {
        apppintmentDate = in.createStringArrayList();
        apppintmentDateNew = in.createTypedArrayList(ApppintmentDateNew.CREATOR);
        serviceAdviser = in.createTypedArrayList(ServiceAdviser.CREATOR);
        mechanic = in.createTypedArrayList(Mechanic.CREATOR);
        myAuto = in.readParcelable(RMMyAuto.class.getClassLoader());
    }

    public static final Creator<ReservationMaintainInfo> CREATOR = new Creator<ReservationMaintainInfo>() {
        @Override
        public ReservationMaintainInfo createFromParcel(Parcel in) {
            return new ReservationMaintainInfo(in);
        }

        @Override
        public ReservationMaintainInfo[] newArray(int size) {
            return new ReservationMaintainInfo[size];
        }
    };

    public ArrayList<String> getApppintmentDate() {
        return apppintmentDate;
    }

    public void setApppintmentDate(ArrayList<String> apppintmentDate) {
        this.apppintmentDate = apppintmentDate;
    }

    public ArrayList<Mechanic> getMechanic() {
        return mechanic;
    }

    public void setMechanic(ArrayList<Mechanic> mechanic) {
        this.mechanic = mechanic;
    }

    public ArrayList<ServiceAdviser> getServiceAdviser() {
        return serviceAdviser;
    }

    public void setServiceAdviser(ArrayList<ServiceAdviser> serviceAdviser) {
        this.serviceAdviser = serviceAdviser;
    }

    public RMMyAuto getMyAuto() {
        return myAuto;
    }

    public void setMyAuto(RMMyAuto myAuto) {
        this.myAuto = myAuto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(apppintmentDate);
        dest.writeTypedList(apppintmentDateNew);
        dest.writeTypedList(serviceAdviser);
        dest.writeTypedList(mechanic);
        dest.writeParcelable(myAuto, flags);
    }
}
