package ir.moovic.entertainment.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.UUID;

public class DeviceUuidHelper {
    protected static final String PREFS_FILE = "device_id";
    protected static final String KEY_DEV_ID = "device_id";

    public static synchronized String UUID(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        String uuid = sharedPrefs.getString(KEY_DEV_ID, null);
        if (TextUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            sharedPrefs.edit().putString(KEY_DEV_ID, uuid).apply();
        }
        return uuid;
    }

}