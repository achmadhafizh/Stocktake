package com.achmadhafizh.stocktake.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.achmadhafizh.stocktake.R;

/**
 * Created by Achmad Hafizh on 25/10/2016.
 */
public class CustomProgressDialog {
    private ProgressDialog dialog;
    private Context context;
    private String mess;
    boolean cancelable = false;
    boolean is_showing = false;

    public CustomProgressDialog(Context context, String message, boolean cancel) {
        this.context = context;
        this.mess = message;
        this.cancelable = cancel;

        dialog = new ProgressDialog(context, R.style.MyProgressBar) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                getWindow().setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                setContentView(R.layout.custom_loader);
            }
        };
        dialog.setIndeterminate(true);
        dialog.setCancelable(this.cancelable);

    }

    public void showDialog() {
        is_showing = true;
        dialog.show();
    }

    public void hideDialog() {
        try {
            is_showing = false;
            dialog.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isShowing() {
        return is_showing;
    }
}
