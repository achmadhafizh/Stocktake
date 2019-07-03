package com.achmadhafizh.stocktake.utilities;

/**
 * Created by Achmad Hafizh on 8/28/2017.
 */

public class CommonConstant {
    public static final String MAIN_URL = "http://apps.metroindonesia.com:8088/";
    public static String API_URL = MAIN_URL + "v1/";
    public static final String URL_DATA_STOCKTAKE_CS = API_URL + "stocktake/sync_cs";
    public static final String URL_DATA_STOCKTAKE_DP = API_URL + "stocktake/sync_dp";
    public static final String URL_DATA_CONFIG = API_URL + "stocktake/config";

    public static final String STATE_CURRENT_TAB_ID = "current_tab_id";
    public static final int MAIN_TAB_ID = 0;

    public static final String PREFS_NAME = "settingsPrefs";
    public static final int PRIVATE_MODE = 0;

    public static final String IS_LOGIN = "islogin";
    public static final String ARG_ITEM = "item";

    public static String SCANNER_ID = "scanner_id";
    public static String STORE_NO = "store_no";
    public static String STORE_NAME = "store_name";
    public static String PASSWORD = "password";
    public static String TIME_STAMP = "time_stamp";

    public static String DIRECTORY_STOCKTAKE_CS = "Stocktake/CS/";
    public static String DIRECTORY_STOCKTAKE_DP = "Stocktake/DP/";
    public static String FILE_NAME = "stdata";
}
