package com.achmadhafizh.stocktake.utilities;

import android.content.Context;

import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

/**
 * @Created 6/12/2018.
 * @Author by Achmad Hafizh.
 * @Email achmadhafizhh@gmail.com.
 * @LinkedIn https://www.linkedin.com/in/achmad-hafizh-296361148/
 */
public class MultiplePermissionListener implements MultiplePermissionsListener {
    private Context context;
    private CallbackPermissionListener callbackPermissionListener;

    public MultiplePermissionListener(Context context) {
        this.context = context;
        callbackPermissionListener = new CallbackPermissionListener(context);
    }

    @Override
    public void onPermissionsChecked(MultiplePermissionsReport report) {
        for (PermissionGrantedResponse response : report.getGrantedPermissionResponses()) {
            callbackPermissionListener.showPermissionGranted(response.getPermissionName());
        }

        for (PermissionDeniedResponse response : report.getDeniedPermissionResponses()) {
            callbackPermissionListener.showPermissionDenied(response.getPermissionName(), response.isPermanentlyDenied());
        }
    }

    @Override
    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
        callbackPermissionListener.showPermissionRationaleNew(token);
    }
}
