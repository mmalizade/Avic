package ir.moovic.entertainment.app.football.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TeamModel implements Parcelable {
    @SerializedName("id")           public long id;
    @SerializedName("title")        public String title;
    @SerializedName("logo")         public String logo;
    @SerializedName("isNational")   public boolean isNational;
    @SerializedName("countryId")    public int countryId;
    @SerializedName("long")         public long tagId;
    @SerializedName("rank")         public int rank;
    @SerializedName("form")         public String form;

    protected TeamModel(Parcel in) {
        id = in.readLong();
        title = in.readString();
        logo = in.readString();
        isNational = in.readByte() != 0;
        countryId = in.readInt();
        tagId = in.readLong();
        rank = in.readInt();
        form = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(logo);
        dest.writeByte((byte) (isNational ? 1 : 0));
        dest.writeInt(countryId);
        dest.writeLong(tagId);
        dest.writeInt(rank);
        dest.writeString(form);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TeamModel> CREATOR = new Creator<TeamModel>() {
        @Override
        public TeamModel createFromParcel(Parcel in) {
            return new TeamModel(in);
        }

        @Override
        public TeamModel[] newArray(int size) {
            return new TeamModel[size];
        }
    };
}
