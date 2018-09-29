package com.module.checkview.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wubo on 2018/9/28.
 */

public class TimeBean implements Parcelable {
    private String time;
    private boolean isSelected;

    public TimeBean(String time, boolean isSelected) {
        this.time = time;
        this.isSelected = isSelected;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.time);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    protected TimeBean(Parcel in) {
        this.time = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<TimeBean> CREATOR = new Parcelable.Creator<TimeBean>() {
        @Override
        public TimeBean createFromParcel(Parcel source) {
            return new TimeBean(source);
        }

        @Override
        public TimeBean[] newArray(int size) {
            return new TimeBean[size];
        }
    };
}
