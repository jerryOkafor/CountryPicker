package me.jerryhanks.countrypicker;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.text.Collator;

/**
 * @author Jerry Hanks on 12/14/17.
 */

public class Country implements Comparable<Country>, Parcelable {
    static int DEFAULT_FLAG_RES = R.drawable.flag_transparent;
    static String TAG = "Class Country";
    @SerializedName("name")
    private String name;
    @SerializedName("dial_code")
    private String dialCode;
    @SerializedName("code")
    private String code;

    private @DrawableRes
    int flagResID = DEFAULT_FLAG_RES;

    public Country(String name, String dialCode, String code) {
        this.dialCode = dialCode;
        this.name = name;
        this.code = code;
        flagResID = R.drawable.flag_transparent;
    }


    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getDialCode() {
        return dialCode;
    }

    @Override
    public int compareTo(@NonNull Country o) {
        return Collator.getInstance().compare(getName(), o.getName());
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", dialCode='" + dialCode + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", flagResID=" + flagResID +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.dialCode);
        dest.writeString(this.code);
    }

    protected Country(Parcel in) {
        this.name = in.readString();
        this.dialCode = in.readString();
        this.code = in.readString();
    }

    public static final Creator<Country> CREATOR = new Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel source) {
            return new Country(source);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };
}
