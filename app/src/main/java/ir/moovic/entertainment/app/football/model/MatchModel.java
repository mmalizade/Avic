package ir.moovic.entertainment.app.football.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@SuppressLint("UnknownNullness")
public class MatchModel implements Parcelable {
    @SerializedName("id")                       public long id;
    @SerializedName("leagueId")                 public long leagueId;
    @SerializedName("hostTeamId")               public long hostTeamId;
    @SerializedName("guestTeamId")               public long guestTeamId;
    @SerializedName("date")                     public String date;
    @SerializedName("time")                     public String time;
    @SerializedName("matchDate")                public String matchDate;
    @SerializedName("dayOfWeek")                public int dayOfWeek;
    @SerializedName("period")                   public Integer period;
    @SerializedName("jalaliDate")               public String jalaliDate;
    @SerializedName("isFinal")                  public boolean isFinal;
    @SerializedName("hostScore")                public Integer hostScore;
    @SerializedName("guestScore")               public Integer guestScore;
    @SerializedName("status")                   public Integer status;
    @SerializedName("varzesh3hasVideo")         public boolean varzesh3hasVideo;
    @SerializedName("varzesh3videoId")          public long varzesh3videoId;
    @SerializedName("varzesh3videoUrl")         public String varzesh3videoUrl;
    @SerializedName("caption")                  public String caption;
    @SerializedName("leagueName")               public String leagueName;
    @SerializedName("stageName")                public String stageName;
    @SerializedName("description")              public String description;
    @SerializedName("predictionType")           public Integer predictionType;
    @SerializedName("predictionValidTime")      public String predictionValidTime;
    @SerializedName("hostTeam")                 public TeamModel hostTeam;
    @SerializedName("guestTeam")                public TeamModel guestTeam;
    @SerializedName("prediction")               public PredictionModel prediction;


    protected MatchModel(Parcel in) {
        id = in.readLong();
        leagueId = in.readLong();
        hostTeamId = in.readLong();
        guestTeamId = in.readLong();
        date = in.readString();
        time = in.readString();
        matchDate = in.readString();
        dayOfWeek = in.readInt();
        if (in.readByte() == 0) {
            period = null;
        } else {
            period = in.readInt();
        }
        jalaliDate = in.readString();
        isFinal = in.readByte() != 0;
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
            status = null;
        } else {
            status = in.readInt();
        }
        varzesh3hasVideo = in.readByte() != 0;
        varzesh3videoId = in.readLong();
        varzesh3videoUrl = in.readString();
        caption = in.readString();
        leagueName = in.readString();
        stageName = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            predictionType = null;
        } else {
            predictionType = in.readInt();
        }
        predictionValidTime = in.readString();
        hostTeam = in.readParcelable(TeamModel.class.getClassLoader());
        guestTeam = in.readParcelable(TeamModel.class.getClassLoader());
        prediction = in.readParcelable(PredictionModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(leagueId);
        dest.writeLong(hostTeamId);
        dest.writeLong(guestTeamId);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(matchDate);
        dest.writeInt(dayOfWeek);
        if (period == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(period);
        }
        dest.writeString(jalaliDate);
        dest.writeByte((byte) (isFinal ? 1 : 0));
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
        if (status == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(status);
        }
        dest.writeByte((byte) (varzesh3hasVideo ? 1 : 0));
        dest.writeLong(varzesh3videoId);
        dest.writeString(varzesh3videoUrl);
        dest.writeString(caption);
        dest.writeString(leagueName);
        dest.writeString(stageName);
        dest.writeString(description);
        if (predictionType == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(predictionType);
        }
        dest.writeString(predictionValidTime);
        dest.writeParcelable(hostTeam, flags);
        dest.writeParcelable(guestTeam, flags);
        dest.writeParcelable(prediction, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MatchModel> CREATOR = new Creator<MatchModel>() {
        @Override
        public MatchModel createFromParcel(Parcel in) {
            return new MatchModel(in);
        }

        @Override
        public MatchModel[] newArray(int size) {
            return new MatchModel[size];
        }
    };

    public String realScore() {
        if(hostScore == null || guestScore == null) {
            return "";
        }
        return guestScore + " - " + hostScore;
    }

    public String predictionScore() {
        if(prediction == null || prediction.guestScore == null || prediction.hostScore == null) {
            return "";
        }
        return prediction.guestScore + " - " + prediction.hostScore;
    }

    public static class PredictionStatus {
        public static final int PREDICTED_CORRECTLY = 1;
        public static final int PREDICTED_SCORE_DIF_CORRECTLY= 2;
        public static final int PREDICTED_WINNER_CORRECTLY = 3;
        public static final int PREDICTED_WRONG = 4;
        public static final int PREDICTED_NOT_STARTED = 5;
        public static final int PREDICTED_STARTED = 6;
        public static final int NOT_PREDICTED_STARTED = 7;
        public static final int NOT_PREDICTED_FINISHED = 8;
        public static final int NOT_PREDICTED_NOT_STARTED = 9;
        public static final int PREDICTED_CANCELED = 10;
        public static final int PREDICTED_UNKNOWN = 11;
        public static final int NOT_PREDICTED_CANCELED = 12;
        public static final int NOT_PREDICTED_UNKNOWN = 13;

        public static boolean finished(int status){
            return status > 0 && (status <= PREDICTED_WRONG || status == NOT_PREDICTED_FINISHED);
        }

        public static boolean predicted(int status){
            return status > 0 &&
                    ( status <= PREDICTED_STARTED || status == PREDICTED_CANCELED || status == PREDICTED_UNKNOWN);
        }

        public static boolean unknown(int status){
            return status > 0 && (status == PREDICTED_UNKNOWN || status == NOT_PREDICTED_UNKNOWN);
        }

    }

    public static class Status {
        public static final int NOT_STARTED = 1;
        public static final int IN_PROGRESS = 2;
        public static final int FINISHED = 3;
        public static final int CANCELED = 4;
        public static final int UNKNOWN = 5;
    }

    public static class Prediction {
        public static final int SCORE = 1;
        public static final int WINNER_ONLY = 2;
    }
}
