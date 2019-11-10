package ir.moovic.entertainment.app.academy.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class InstructorModel implements Parcelable {
    @SerializedName("id")               public long id;
    @SerializedName("name")             public String name;
    @SerializedName("profile")          public String profile;
    @SerializedName("bio")              public String bio;

    protected InstructorModel(Parcel in) {
        id = in.readLong();
        name = in.readString();
        profile = in.readString();
        bio = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(profile);
        dest.writeString(bio);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InstructorModel> CREATOR = new Creator<InstructorModel>() {
        @Override
        public InstructorModel createFromParcel(Parcel in) {
            return new InstructorModel(in);
        }

        @Override
        public InstructorModel[] newArray(int size) {
            return new InstructorModel[size];
        }
    };
}
