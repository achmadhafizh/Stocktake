package com.achmadhafizh.stocktake.utilities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.achmadhafizh.stocktake.R;
import com.karumi.dexter.PermissionToken;

/**
 * @Created 6/12/2018.
 * @Author by Achmad Hafizh.
 * @Email achmadhafizhh@gmail.com.
 * @LinkedIn https://www.linkedin.com/in/achmad-hafizh-296361148/
 */
public class CallbackPermissionListener {
    private static final String TAG = CallbackPermissionListener.class.getSimpleName();
    private Context context;

    public CallbackPermissionListener(Context context) {
        this.context = context;
    }

    public void showPermissionGranted(String permission) {
        Log.d(TAG, permission + " Permissions Granted");
    }

    public void showPermissionDenied(String permission, boolean isPermanentlyDenied) {
        if (isPermanentlyDenied) {
            Log.d(TAG, permission + " Permissions Permanently Denied");
        } else {
            Log.d(TAG, permission + " Permissions Denied");
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void showPermissionRationaleOld(final PermissionToken token) {
        new AlertDialog.Builder(context).setTitle(R.string.permission_rationale_title)
                .setMessage(R.string.permission_rationale_message)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        token.cancelPermissionRequest();
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        token.continuePermissionRequest();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        token.cancelPermissionRequest();
                    }
                })
                .show();
    }

    public void showPermissionRationaleNew(final PermissionToken token) {
        token.continuePermissionRequest();
    }

}
