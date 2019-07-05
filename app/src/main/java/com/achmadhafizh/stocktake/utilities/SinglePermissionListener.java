package com.achmadhafizh.stocktake.utilities;

import android.content.Context;

import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

/**
 * @Created 6/12/2018.
 * @Author by Achmad Hafizh.
 * @Email achmadhafizhh@gmail.com.
 * @LinkedIn https://www.linkedin.com/in/achmad-hafizh-296361148/
 */
public class SinglePermissionListener implements PermissionListener {
    private Context context;
    private CallbackPermissionListener callbackPermissionListener;

    public SinglePermissionListener(Context context) {
        this.context = context;
        callbackPermissionListener = new CallbackPermissionListener(context);
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {
        callbackPermissionListener.showPermissionGranted(response.getPermissionName());
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse response) {
        callbackPermissionListener.showPermissionDenied(response.getPermissionName(), response.isPermanentlyDenied());
    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
        callbackPermissionListener.showPermissionRationaleNew(token);
    }

}