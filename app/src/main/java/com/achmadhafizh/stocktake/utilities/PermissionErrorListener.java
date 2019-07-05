package com.achmadhafizh.stocktake.utilities;

import android.util.Log;

import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequestErrorListener;

/**
 * @Created 6/12/2018.
 * @Author by Achmad Hafizh.
 * @Email achmadhafizhh@gmail.com.
 * @LinkedIn https://www.linkedin.com/in/achmad-hafizh-296361148/
 */
public class PermissionErrorListener implements PermissionRequestErrorListener {
    private static final String TAG = PermissionErrorListener.class.getSimpleName();

    @Override public void onError(DexterError error) {
        Log.e(TAG, "There was an error: " + error.toString());
    }
}