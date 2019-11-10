package ir.moovic.entertainment.app.academy.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class RateModel implements Parcelable {
    @SerializedName("average")      public float average;
    @SerializedName("count")        public int count;

    protected RateModel(Parcel in) {
        average = in.readFloat();
        count = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(average);
        dest.writeInt(count);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RateModel> CREATOR = new Creator<RateModel>() {
        @Override
        public RateModel createFromParcel(Parcel in) {
            return new RateModel(in);
        }

        @Override
        public RateModel[] newArray(int size) {
            return new RateModel[size];
        }
    };
}
