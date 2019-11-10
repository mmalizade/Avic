package ir.moovic.entertainment.ad;

import ir.moovic.entertainment.R;
import ir.moovic.entertainment.app.config.App;
import ir.tapsell.sdk.Tapsell;
import ir.tapsell.sdk.TapsellConfiguration;
import ir.tapsell.sdk.bannerads.TapsellBannerType;

public class TapsellHelper {
    public static final String TAPSEL_APP_KEY = "qnlpjolmrmojlejpqjgigkhechgimshcnjjttqpinfdtfbdfdjbonnrnrkqlesotppseiq";

    public static final TapsellBannerType NEWS_PAGE_BANNER_TYPE = TapsellBannerType.BANNER_320x100;

    public static void init(App app) {
        Zone.TAPSELL_REWARDED_VIDEO = app.getString(R.string.tapsell_zone_id_rewarded_video);
        Zone.TAPSELL_STANDARD_BANNER = app.getString(R.string.tapsell_zone_id_standard_banner);
        Zone.TAPSELL_NATVIE_BANNER = app.getString(R.string.tapsell_zone_id_native_banner);
        Zone.TAPSELL_NATIVE_VIDEO = app.getString(R.string.tapsell_zone_id_native_video);
        Zone.TAPSELL_INTRESTITIAL_BANNER = app.getString(R.string.tapsell_zone_id_intrestitial_banner);
        Zone.TAPSELL_INTRESTITIAL_VIDEO = app.getString(R.string.tapsell_zone_id_intrestitial_video);
        TapsellConfiguration config = new TapsellConfiguration(app);
        config.setDebugMode(true);
        Tapsell.initialize(app, config, TAPSEL_APP_KEY);
    }


    public static class Zone {
        public static String TAPSELL_REWARDED_VIDEO = "";
        public static String TAPSELL_STANDARD_BANNER = "";
        public static String TAPSELL_NATVIE_BANNER = "";
        public static String TAPSELL_NATIVE_VIDEO = "";
        public static String TAPSELL_INTRESTITIAL_BANNER = "";
        public static String TAPSELL_INTRESTITIAL_VIDEO = "";
    }





}
