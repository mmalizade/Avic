package ir.moovic.entertainment.app.football.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StandingModel implements Parcelable {
    @SerializedName("leagueId")             public long leagueId;
    @SerializedName("title")                public String title;
    @SerializedName("leagueTitle")          public String leagueTitle;
    @SerializedName("groupTitle")           public String groupTitle;
    @SerializedName("stageFullName")        public String stageFullName;
    @SerializedName("table")                public List<StandingTableItemModel> table;


    protected StandingModel(Parcel in) {
        leagueId = in.readLong();
        title = in.readString();
        leagueTitle = in.readString();
        groupTitle = in.readString();
        stageFullName = in.readString();
        table = in.createTypedArrayList(StandingTableItemModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(leagueId);
        dest.writeString(title);
        dest.writeString(leagueTitle);
        dest.writeString(groupTitle);
        dest.writeString(stageFullName);
        dest.writeTypedList(table);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StandingModel> CREATOR = new Creator<StandingModel>() {
        @Override
        public StandingModel createFromParcel(Parcel in) {
            return new StandingModel(in);
        }

        @Override
        public StandingModel[] newArray(int size) {
            return new StandingModel[size];
        }
    };



    public static class StandingTableItemModel implements Parcelable{
        @SerializedName("rank")             public int rank;
        @SerializedName("id")               public long id;
        @SerializedName("team")             public TeamModel team;
        @SerializedName("played")           public int played;
        @SerializedName("victories")        public int victories;
        @SerializedName("draws")            public int draws;
        @SerializedName("defeats")          public int defeats;
        @SerializedName("made")             public int made;
        @SerializedName("let")              public int let;
        @SerializedName("diff")             public int diff;
        @SerializedName("points")           public int points;

        protected StandingTableItemModel(Parcel in) {
            rank = in.readInt();
            id = in.readLong();
            team = in.readParcelable(TeamModel.class.getClassLoader());
            played = in.readInt();
            victories = in.readInt();
            draws = in.readInt();
            defeats = in.readInt();
            made = in.readInt();
            let = in.readInt();
            diff = in.readInt();
            points = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(rank);
            dest.writeLong(id);
            dest.writeParcelable(team, flags);
            dest.writeInt(played);
            dest.writeInt(victories);
            dest.writeInt(draws);
            dest.writeInt(defeats);
            dest.writeInt(made);
            dest.writeInt(let);
            dest.writeInt(diff);
            dest.writeInt(points);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<StandingTableItemModel> CREATOR = new Creator<StandingTableItemModel>() {
            @Override
            public StandingTableItemModel createFromParcel(Parcel in) {
                return new StandingTableItemModel(in);
            }

            @Override
            public StandingTableItemModel[] newArray(int size) {
                return new StandingTableItemModel[size];
            }
        };
    }
}
