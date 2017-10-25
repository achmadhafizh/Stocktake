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

public class SessionManager {
    private String TAG = SessionManager.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Intent intent;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(CommonConstant.PREFS_NAME, CommonConstant.PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createLoginSession(String nik, String dob, String name, String store) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(CommonConstant.NIK, nik);
        editor.putString(CommonConstant.DOB, dob);
        editor.putString(CommonConstant.NAME, name);
        editor.putString(CommonConstant.STORE, store);

        editor.commit();

        Log.e(TAG, "createLoginSession Finish");
    }

    public boolean checkLogin() {
        if (!this.isLoggedIn()) {
            //intentTaskTo(LoginActivity.class);
            return true;
        }
        return false;
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(CommonConstant.NIK, sharedPreferences.getString(CommonConstant.NIK, null));
        user.put(CommonConstant.DOB, sharedPreferences.getString(CommonConstant.DOB, null));
        user.put(CommonConstant.NAME, sharedPreferences.getString(CommonConstant.NAME, null));
        user.put(CommonConstant.STORE, sharedPreferences.getString(CommonConstant.STORE, null));

        Log.e(TAG, "getUserDetails Finish");

        return user;
    }

    public HashMap<String, Boolean> getSettingDetails() {
        HashMap<String, Boolean> user = new HashMap<String, Boolean>();

        user.put(CommonConstant.IS_DRAGGABLE, sharedPreferences.getBoolean(CommonConstant.IS_DRAGGABLE, false));

        Log.e(TAG, "getSettingDetails Finish");

        return user;
    }

    public void clearUser() {
        editor.putBoolean(IS_LOGIN, false);
        editor.putString(CommonConstant.NIK, "");
        editor.putString(CommonConstant.DOB, "");
        editor.putString(CommonConstant.NAME, "");
        editor.putString(CommonConstant.STORE, "");

        editor.commit();

        Log.e(TAG, "clearUser Finish");
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    private void intentTaskTo(Class activity) {
        intent = new Intent(context, activity);
        context.startActivity(intent);
    }
}
