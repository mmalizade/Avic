package ir.moovic.entertainment.network;

import com.google.gson.annotations.SerializedName;

public class ErrorJson {

    @SerializedName("success")          public boolean success;
    @SerializedName("message")          public String message;
    @SerializedName("statusCode")       public int statusCode;
    @SerializedName("statusMessage")    public String statusMessage;
    @SerializedName("errorMessage")     public String errorMessage;

}
