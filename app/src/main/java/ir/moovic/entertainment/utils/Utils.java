package ir.moovic.entertainment.utils;


import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;

import androidx.annotation.AttrRes;
import androidx.core.content.FileProvider;
import ir.moovic.entertainment.R;

public class Utils {

    public static String formatPhoneNumber(String phone) {
        if (phone.length() < 10) {
            return phone;
        }
        int len = phone.length();
        return "0" +
                phone.substring(len - 10, len - 7) + " " +
                phone.substring(len - 7, len - 4) + " " +
                phone.substring(len - 4);
    }

    public static String getCurrentDate(boolean withMonthName) {
        JalaliCalendar.YearMonthDate jdate = new JalaliCalendar(TimeZone.getTimeZone("Asia/Tehran"))
                .getJalaliDate(new Date());
        return jdate.toString(withMonthName);
    }

    public static String convertToPersianDateString(String yyyy_mm_dd, boolean withMonthName) {
        Date date = parseDateGregorian(yyyy_mm_dd);
        if (date != null) {
            return new JalaliCalendar(TimeZone.getTimeZone("Asia/Tehran"))
                    .getJalaliDate(date).toString(withMonthName);
        }
        return "";
    }

    public static String persianDateString(long time, boolean monthName) {
        Date date = new Date(time);
        return new JalaliCalendar(TimeZone.getTimeZone("Asia/Tehran"))
                .getJalaliDate(date).toString(monthName);
    }

    public static String parseDatePersian(String yyyy_mm_dd, boolean monthName) {
        // date should be in 'yyyy-MM-DD' format
        if (TextUtils.isEmpty(yyyy_mm_dd)) return "";
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(yyyy_mm_dd);
            return new JalaliCalendar(TimeZone.getTimeZone("Asia/Tehran"))
                    .getJalaliDate(date).toString(monthName);
        } catch (ParseException e) {
            return "";
        }
    }

    public static Date parseDateGregorian(String yyyy_mm_dd) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(yyyy_mm_dd);
        } catch (ParseException e) {
            return null;
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager conman = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(conman == null) return true;
        NetworkInfo netinfo = conman.getActiveNetworkInfo();
        return (netinfo != null && netinfo.isConnectedOrConnecting());
    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }


    public static DisplayMetrics displayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    public static boolean hideInputMethod(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            return imm != null && imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        return false;
    }

    public static void showInputMethod(Context context) {
        InputMethodManager imm = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void sharePlainText(Context context, String subject, String shareBody, String chooserTitle) throws Exception{
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(sharingIntent, chooserTitle));
    }

    public static int dp2px(float dp, Context context) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static String getAppVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(
                    context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "1.0.0";
        }
    }

    public static long getAppVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(
                    context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 1L;
        }
    }

    public static String fileExtention(String url) {
        if (url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.contains("%")) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.contains("/")) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();
        }
    }

    public static void openFileByIntent(Context context, File file) {
        MimeTypeMap myMime = MimeTypeMap.getSingleton();
        Intent newIntent = new Intent(Intent.ACTION_VIEW);
        String mimeType = myMime.getMimeTypeFromExtension(fileExtention(file.getAbsolutePath()));
        newIntent.setDataAndType(Uri.fromFile(file), mimeType);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(newIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG).show();
        }
    }

    public static void openApkFile(Context context, File apkFile) {
        Uri apkURI = FileProvider.getUriForFile(
                context, context.getApplicationContext()
                        .getPackageName() + ".provider", apkFile);
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        install.setDataAndType(apkURI, "application/vnd.android.package-archive");
        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(install);
    }

    public static int deviceType() {
        return 11;
    }


    public static Map<String, String> splitQuery(String queryString) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new HashMap<>();
        String q = (queryString.lastIndexOf('?') > 0) ? queryString.substring(queryString.lastIndexOf('?') + 1) : queryString;
        final String[] pairs = q.split("&");
        for (String pair : pairs) {
            final int idx = pair.indexOf("=");
            final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
            final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
            query_pairs.put(key, value);
        }
        return query_pairs;
    }


    public static String udid(long userId) {
        int x = new Random().nextInt(1000000000);
        return String.format(Locale.getDefault(), "%x%s%x",
                userId, new Date().toString(), x);
    }

    public static Date getZeroTimeDate(Date fecha) {
        Date res = fecha;
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(fecha);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        res = calendar.getTime();

        return res;
    }

    public static String persianDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("Asia/Tehran"));
        c.setFirstDayOfWeek(Calendar.SATURDAY);
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        return JalaliCalendar.persian_days[dayOfWeek % 7];
    }

    public static String persianDate(Date date) {
        JalaliCalendar.YearMonthDate ymd = new JalaliCalendar(TimeZone.getTimeZone("Asia/Tehran")).getJalaliDate(date);
        return ymd.toString(true);
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static int getActionBarSize(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        int size = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return size;
    }

    public static String getDateFormatString(Date gameDate, boolean appendYear) {
        String s = Utils.persianDayOfWeek(gameDate) + " ";
        JalaliCalendar.YearMonthDate ymd = new JalaliCalendar(TimeZone.getTimeZone("Asia/Tehran")).getJalaliDate(gameDate);
        s += ymd.getDate() + " " + ymd.getMonthName();
        if (appendYear) {
            s += " " + ymd.getYear();
        }
        return s;
    }


    public static int gcdInt(int a, int b) {
        BigInteger b1 = BigInteger.valueOf(a);
        BigInteger b2 = BigInteger.valueOf(b);
        BigInteger gcd = b1.gcd(b2);
        return gcd.intValue();
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static boolean isRtl(Context context) {
        if (Build.VERSION.SDK_INT >= 17) {
            return context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
        } else {
            return false;
        }
    }


    public static String formatRating(float rate) {
        return formatRating(rate, false);
    }

    public static String formatRating(float rate, boolean nullAllowed) {
        if (nullAllowed && rate < 0) {
            return null;
        }
        if (rate == (int) rate)
            return String.format(Locale.US, "%d", (long) rate);
        else
            return String.format(Locale.US, "%.1f", rate).replace('.', '/');
    }

    public static String inputStreamToString(InputStream inputStream, String defaultValue) {
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
            return total.toString();
        } catch (IOException e) {
            return defaultValue;
        }
    }

    public static String formatMobileNumber(String mobile, boolean international) {
        if (mobile.length() > 9) {
            if (international) {
                return "989" + mobile.substring(mobile.length() - 9);
            } else {
                return "09" + mobile.substring(mobile.length() - 9);
            }
        }
        return mobile;
    }

    public static String formatMobileNumber(String mobile) {
        return formatMobileNumber(mobile, false);
    }

    public static int[] getColorResArrays(Context context, int arrayResId) {
        TypedArray ta = context.getResources().obtainTypedArray(arrayResId);
        if (ta.length() == 0) {
            return null;
        }
        int[] colors = new int[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            colors[i] = ta.getColor(i, 0);
        }
        ta.recycle();
        return colors;
    }

    public static int getColorFromColorArrays(Context context, int arrayResId, int index) {
        TypedArray ta = context.getResources().obtainTypedArray(arrayResId);
        if (ta.length() == 0) {
            return Color.WHITE;
        }
        int color = ta.getColor(index % ta.length(), Color.WHITE);
        ta.recycle();
        return color;
    }

    public static int decreaseRgbChannels(int color, float percent) {
        // reduce rgb channel values to produce box shadow effect
        int red = (Color.red(color));
        red -= (red * percent);
        red = red > 0 ? red : 0;

        int green = (Color.green(color));
        green -= (green * percent);
        green = green > 0 ? green : 0;

        int blue = (Color.blue(color));
        blue -= (blue * percent);
        blue = blue > 0 ? blue : 0;

        return Color.argb(Color.alpha(color), red, green, blue);
    }

    public static Bitmap takeScreenShot(Window window) {
        View view = window.getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        Rect frame = new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Point size = new Point();
        window.getWindowManager().getDefaultDisplay().getSize(size);
        int width = size.x;
        int height = size.y;
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    public static Bitmap fastblur(Bitmap sentBitmap, int radius) {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

    public static void blurBehindDialog(Dialog dialog, Window backWindow, int alpha0to255) {
        Bitmap map = takeScreenShot(backWindow);
        Bitmap fast = fastblur(map, 10);
        final Drawable draw = new BitmapDrawable(dialog.getContext().getResources(), fast);
        if (alpha0to255 >= 0 && alpha0to255 < 255) {
            draw.setAlpha(alpha0to255);
        }
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(draw);
        }
    }

    public static void dimBehindPopupWindow(PopupWindow popupWindow) {
        View container;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            container = (View) popupWindow.getContentView().getParent();
        } else {
            container = popupWindow.getContentView();
        }
        if (popupWindow.getBackground() != null) {
            container = (View) container.getParent();
        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND; // add a flag here instead of clear others
        p.dimAmount = 0.3f;
        wm.updateViewLayout(container, p);
    }

    public static String mediaDuration(int sec) {
        if(sec <= 0) return "00:00";
        int h = sec / (3600);
        int m = (sec % 3600) / 60;
        int s = (sec % 60);
        StringBuilder sb = new StringBuilder();
        if (h > 0) {
            sb.append(String.format(Locale.US, "%02d:", h));
        }
        sb.append(String.format(Locale.US, "%02d:%02d", m, s));
        return sb.toString();
    }

    public static int px2dp(int px, Context context) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

    public static int getSelectableItemBackgroundResourceId(Context context) {
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        typedArray.recycle();
        return backgroundResource;
    }

    public static void openInstagramPage(Context context, String instagramId) {
        if (TextUtils.isEmpty(instagramId)) return;
        try {
            context.startActivity(
                    new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/" + instagramId))
                            .setPackage("com.instagram.android")
            );
        } catch (ActivityNotFoundException e){
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/" + instagramId)));
        }
    }

    public static void openActivityInfoScreen(Context context, String packageName) {

        try {
            //Open the specific App Info page:
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + packageName));
            context.startActivity(intent);
        } catch ( ActivityNotFoundException e ) {
            //e.printStackTrace();
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            context.startActivity(intent);
        }
    }

    public static void openTelegramUserName(Context context, String tgUserName) {
        if (TextUtils.isEmpty(tgUserName)) return;
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=" + tgUserName)));
        } catch (Exception e){}
    }

    public static void openAparatChannel(Context context, String channel) {
        if (TextUtils.isEmpty(channel)) return;
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://aparat.com/" + channel)));
    }

    public static void openWebsite(Context context, String website) {
        if (TextUtils.isEmpty(website)) return;
        try{
            if(!website.startsWith("http")){
                website = "http://" + website;
            }
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(website)));
        } catch (Exception e){}
    }

    public static void openDialScreen(Context context, String phone) {
        if (TextUtils.isEmpty(phone)) return;
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + phone)));
        } catch (Exception e){}
    }

    public static void copyToClipboard(Context context, String label, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
    }

    public static String getFileNameFromUrl(String url) {
        if(TextUtils.isEmpty(url)) return "";
        int index = url.lastIndexOf('/');
        if(index < 0 || url.endsWith("/")) return url;
        return url.substring(index+1);
    }

    public static int colorFromAttributes(Context context, @AttrRes int attr){
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }

    public static boolean deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            return deleteDir(dir);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public static boolean checkAppInstalled(Context context, String packagename){
        try {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }



    public static String relativeTime(Context context, String time) {
        try {
            String[] suffixes = context.getResources().getStringArray(R.array.time_suffix);
            DateTimeFormatter dateDecoder = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZone(DateTimeZone.forID("Asia/Tehran"));
            DateTime post = dateDecoder.parseDateTime(time);
            DateTime now = new DateTime();
            Period period = new Period(post, now);
            PeriodFormatter formatter;
            if(period.getYears() != 0) {
                formatter = new PeriodFormatterBuilder().appendYears().appendSuffix(" ").appendSuffix(suffixes[0]).printZeroNever().toFormatter();
            }else if(period.getMonths() !=0) {
                formatter = new PeriodFormatterBuilder().appendMonths().appendSuffix(" ").appendSuffix(suffixes[1]).printZeroNever().toFormatter();
            }else if(period.getWeeks() !=0){
                formatter = new PeriodFormatterBuilder().appendWeeks().appendSuffix(" ").appendSuffix(suffixes[2]).printZeroNever().toFormatter();
            }else if(period.getDays()!=0) {
                int days = period.getDays();
                if(days == 1) {
                    return suffixes[7];
                } else if(days == 2) {
                    return suffixes[8];
                }
                formatter = new PeriodFormatterBuilder().appendDays().appendSuffix(" ").appendSuffix(suffixes[3]).printZeroNever().toFormatter();
            }else if(period.getHours() !=0){
                formatter = new PeriodFormatterBuilder().appendHours().appendSuffix(" ").appendSuffix(suffixes[4]).printZeroNever().toFormatter();
            }else if(period.getMinutes() !=0) {
                formatter = new PeriodFormatterBuilder().appendMinutes().appendSuffix(" ").appendSuffix(suffixes[5]).printZeroNever().toFormatter();
            }else{
//                formatter = new PeriodFormatterBuilder().appendSeconds().appendSuffix("s").printZeroNever().toFormatter();
                return suffixes[6];
            }
            return formatter.print(period);
        } catch (Exception e) {
            return "";
        }
    }


    public static Bundle bundleFromJsonObject(JsonObject json) {
        Bundle bundle = new Bundle();
        try {
            Set<String> keys = json.keySet();
            for (String key:keys) {
                JsonElement element = json.get(key);
                try {
                    bundle.putString(key, element.getAsString());
                    continue;
                } catch (Exception e){}
                try {
                    bundle.putInt(key, element.getAsInt());
                    continue;
                } catch (Exception e){}
                try {
                    bundle.putDouble(key, element.getAsDouble());
                    continue;
                } catch (Exception e){}
                try {
                    bundle.putBoolean(key, element.getAsBoolean());
                    continue;
                } catch (Exception e){}
            }
            return bundle;
        } catch (Exception ex){
        }
        return null;
    }

}

