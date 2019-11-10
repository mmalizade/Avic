package ir.moovic.entertainment.app.football.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PredictionModel implements Parcelable {
    @SerializedName("type")             public int type;
    @SerializedName("matchId")          public Long matchId;
    @SerializedName("hostScore")        public Integer hostScore;
    @SerializedName("guestScore")       public Integer guestScore;
    @SerializedName("earnedPoints")     public Integer earnedPoints;

    protected PredictionModel(Parcel in) {
        type = in.readInt();
        if (in.readByte() == 0) {
            matchId = null;
        } else {
            matchId = in.readLong();
        }
        if (in.readByte() == 0) {
            hostScore = null;
        } else {
            hostScore = in.readInt();
        }
        if (in.readByte() == 0) {
            guestScore = null;
        } else {
            guestScore = in.readInt();
        }
        if (in.readByte() == 0) {
            earnedPoints = null;
        } else {
            earnedPoints = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        if (matchId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(matchId);
        }
        if (hostScore == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(hostScore);
        }
        if (guestScore == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(guestScore);
        }
        if (earnedPoints == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(earnedPoints);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PredictionModel> CREATOR = new Creator<PredictionModel>() {
        @Override
        public PredictionModel createFromParcel(Parcel in) {
            return new PredictionModel(in);
        }

        @Override
        public PredictionModel[] newArray(int size) {
            return new PredictionModel[size];
        }
    };
}
