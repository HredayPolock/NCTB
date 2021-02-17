package org.richit.nctb.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class OnlineData implements Parcelable {
    private int versionCode;
    private boolean cancellable;
    private String url;
    private ArrayList<ClassType> classes=new ArrayList<>();

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public boolean isCancellable() {
        return cancellable;
    }

    public void setCancellable(boolean cancellable) {
        this.cancellable = cancellable;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<ClassType> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<ClassType> classes) {
        this.classes = classes;
    }

    protected OnlineData(Parcel in) {
        versionCode = in.readInt();
        cancellable = in.readByte() != 0x00;
        url = in.readString();
        if (in.readByte() == 0x01) {
            classes = new ArrayList<ClassType>();
            in.readList(classes, ClassType.class.getClassLoader());
        } else {
            classes = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(versionCode);
        dest.writeByte((byte) (cancellable ? 0x01 : 0x00));
        dest.writeString(url);
        if (classes == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(classes);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<OnlineData> CREATOR = new Parcelable.Creator<OnlineData>() {
        @Override
        public OnlineData createFromParcel(Parcel in) {
            return new OnlineData(in);
        }

        @Override
        public OnlineData[] newArray(int size) {
            return new OnlineData[size];
        }
    };
}


