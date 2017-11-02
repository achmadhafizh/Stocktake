package com.achmadhafizh.stocktake.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.achmadhafizh.stocktake.utilities.CommonConstant;

import java.util.HashMap;

import static com.achmadhafizh.stocktake.utilities.CommonConstant.IS_LOGIN;

/**
 * Created by Achmad Hafizh on 09/11/2016.
 */

public class SettingsManager {
    private String TAG = SettingsManager.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Intent intent;
    private Context context;

    public SettingsManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(CommonConstant.PREFS_NAME, CommonConstant.PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void saveSettings(String id) {
        editor.putString(CommonConstant.SCANNER_ID, id);
        editor.commit();

        Log.e(TAG, "saveSettings Finish");
    }

    public void saveSettings(String id, String store_no, String store_name, String password, String stamp) {
        editor.putString(CommonConstant.SCANNER_ID, id);
        editor.putString(CommonConstant.STORE_NO, store_no);
        editor.putString(CommonConstant.STORE_NAME, store_name);
        editor.putString(CommonConstant.PASSWORD, password);
        editor.putString(CommonConstant.TIME_STAMP, stamp);
        editor.commit();

        Log.e(TAG, "saveSettings Finish");
    }

    public HashMap<String, String> getSettings() {
        HashMap<String, String> settings = new HashMap<String, String>();

        settings.put(CommonConstant.SCANNER_ID, sharedPreferences.getString(CommonConstant.SCANNER_ID, null));
        settings.put(CommonConstant.STORE_NO, sharedPreferences.getString(CommonConstant.STORE_NO, null));
        settings.put(CommonConstant.STORE_NAME, sharedPreferences.getString(CommonConstant.STORE_NAME, null));
        settings.put(CommonConstant.PASSWORD, sharedPreferences.getString(CommonConstant.PASSWORD, null));
        settings.put(CommonConstant.TIME_STAMP, sharedPreferences.getString(CommonConstant.TIME_STAMP, null));

        Log.e(TAG, "getSettings Finish");

        return settings;
    }

    public void clearSettings() {
        editor.putBoolean(IS_LOGIN, false);
        editor.putString(CommonConstant.SCANNER_ID, "");
        editor.putString(CommonConstant.STORE_NO, "");
        editor.putString(CommonConstant.STORE_NAME, "");
        editor.putString(CommonConstant.PASSWORD, "");
        editor.putString(CommonConstant.TIME_STAMP, "");

        editor.commit();

        Log.e(TAG, "clearSettings Finish");
    }
}
