package ir.moovic.entertainment.app.main.models;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class ResBundle {
    @SerializedName("title")            public String title;
    @SerializedName("imageUrl")         public String imageUrl;
    @SerializedName("activity")         public String activity;
    @SerializedName("params")           public JsonObject params;
    @SerializedName("span")             public int span;
    @SerializedName("ratioW")           public int ratioW;
    @SerializedName("ratioH")           public int ratioH;

    public String aspectRatio(){
        return ratioW + "/" + ratioH;
    }
}
