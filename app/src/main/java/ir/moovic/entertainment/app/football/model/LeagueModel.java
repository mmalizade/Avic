package ir.moovic.entertainment.app.football.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LeagueModel implements Parcelable {

    @SerializedName("id")               public long id;
    @SerializedName("title")            public String title;
    @SerializedName("cover")            public String cover;
    @SerializedName("description")      public String description;
    @SerializedName("maxWeek")          public int maxWeek;
    @SerializedName("standing")         public StandingModel standing;
    @SerializedName("matches")          public List<MatchModel> matches;
    @SerializedName("periodShow")       public int periodShow = 1;
    @SerializedName("covers")           public Covers covers;

    public static class Covers {
        @SerializedName("prediction")       public String prediction;
        @SerializedName("prediction2")      public String prediction2;
        @SerializedName("ranking")          public String ranking;
        @SerializedName("standing")         public String standing;
    }

    protected LeagueModel(Parcel in) {
        id = in.readLong();
        title = in.readString();
        cover = in.readString();
        description = in.readString();
        maxWeek = in.readInt();
        standing = in.readParcelable(StandingModel.class.getClassLoader());
        matches = in.createTypedArrayList(MatchModel.CREATOR);
        periodShow = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(cover);
        dest.writeString(description);
        dest.writeInt(maxWeek);
        dest.writeParcelable(standing, flags);
        dest.writeTypedList(matches);
        dest.writeInt(periodShow);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LeagueModel> CREATOR = new Creator<LeagueModel>() {
        @Override
        public LeagueModel createFromParcel(Parcel in) {
            return new LeagueModel(in);
        }

        @Override
        public LeagueModel[] newArray(int size) {
            return new LeagueModel[size];
        }
    };
}
