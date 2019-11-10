package ir.moovic.entertainment.app.main.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResHomePage {
    @SerializedName("rowConfig")        public RowConfig rowConfig;
    @SerializedName("bundles")          public List<ResBundle> bundles;

    public static class RowConfig {
        @SerializedName("ratioW")       public int ratioW;
        @SerializedName("ratioH")       public int ratioH;
        @SerializedName("span")         public int span;
    }

}
