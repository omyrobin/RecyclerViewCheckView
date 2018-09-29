package com.module.checkview.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by wubo on 2018/9/28.
 */

public class ChildBean implements Parcelable {
    //日期
    private String date;
    //时间集合
    private List<TimeBean> times;
    //子布局选中状态
    private int status;

    public ChildBean(String date, List<TimeBean> times) {
        this.date = date;
        this.times = times;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<TimeBean> getTimes() {
        return times;
    }

    public void setTimes(List<TimeBean> times) {
        this.times = times;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeTypedList(this.times);
        dest.writeInt(this.status);
    }

    protected ChildBean(Parcel in) {
        this.date = in.readString();
        this.times = in.createTypedArrayList(TimeBean.CREATOR);
        this.status = in.readInt();
    }

    public static final Creator<ChildBean> CREATOR = new Creator<ChildBean>() {
        @Override
        public ChildBean createFromParcel(Parcel source) {
            return new ChildBean(source);
        }

        @Override
        public ChildBean[] newArray(int size) {
            return new ChildBean[size];
        }
    };
}
