package org.richit.nctb.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ClassType implements Parcelable {
    private String className;
    private String classNameBn;
    private ArrayList<Book> books = new ArrayList<>();

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassNameBn() {
        return classNameBn;
    }

    public void setClassNameBn(String classNameBn) {
        this.classNameBn = classNameBn;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    protected ClassType(Parcel in) {
        className = in.readString();
        classNameBn = in.readString();
        if (in.readByte() == 0x01) {
            books = new ArrayList<Book>();
            in.readList(books, Book.class.getClassLoader());
        } else {
            books = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(className);
        dest.writeString(classNameBn);
        if (books == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(books);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ClassType> CREATOR = new Parcelable.Creator<ClassType>() {
        @Override
        public ClassType createFromParcel(Parcel in) {
            return new ClassType(in);
        }

        @Override
        public ClassType[] newArray(int size) {
            return new ClassType[size];
        }
    };
}
