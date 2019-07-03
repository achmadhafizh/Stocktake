package com.achmadhafizh.stocktake.holder;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.achmadhafizh.stocktake.R;
import com.achmadhafizh.stocktake.adapter.InquiryAdapter;
import com.achmadhafizh.stocktake.fragment.DirectPurchaseFragment;
import com.achmadhafizh.stocktake.helper.DatabaseManager;
import com.achmadhafizh.stocktake.model.Stocktake;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by achmad.hafizh on 10/27/2017.
 */

public class InquiryViewHolder extends RecyclerView.ViewHolder {
    public RelativeLayout viewForeground;
    private DatabaseManager db;

    @BindView(R.id.fixture)
    TextView fixture;

    @BindView(R.id.bc1)
    TextView bc1;

    @BindView(R.id.bc2)
    TextView bc2;

    @BindView(R.id.qty)
    TextView qty;

    @BindView(R.id.view_background)
    RelativeLayout viewBackground;

    public InquiryViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);

        viewForeground = view.findViewById(R.id.view_foreground);
        db = new DatabaseManager(view.getContext());
    }

    public void bind(Stocktake stocktake, Boolean indent) {
        if(indent) {
            bc1.setText(stocktake.getBc1().substring(5, 11));
            bc2.setText(String.valueOf(Integer.parseInt(stocktake.getBc2().substring(1, 12))));
            fixture.setText(stocktake.getFixture());
            qty.setText(stocktake.getQty() + " Pcs");

            viewForeground.setOnClickListener(v -> {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getContext());
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.custom_dialog_input, null);

                dialogBuilder.setView(dialogView);
                dialogBuilder.setCancelable(false);

                final LinearLayout llScanner = dialogView.findViewById(R.id.ll_create_scanner_id);
                final LinearLayout llConfig = dialogView.findViewById(R.id.ll_config);
                final LinearLayout llQty = dialogView.findViewById(R.id.ll_qty);
                final LinearLayout llOneButton = dialogView.findViewById(R.id.ll_one_button);
                final LinearLayout llTwoButton = dialogView.findViewById(R.id.ll_two_button);
                final EditText etQty = dialogView.findViewById(R.id.et_qty);
                final Button btnYes = dialogView.findViewById(R.id.button_save);
                final Button btnNo = dialogView.findViewById(R.id.button_cancel);

                llScanner.setVisibility(View.GONE);
                llConfig.setVisibility(View.GONE);
                llQty.setVisibility(View.VISIBLE);
                llOneButton.setVisibility(View.GONE);
                llTwoButton.setVisibility(View.VISIBLE);
                btnYes.setText("Yes");
                btnNo.setText("No");

                final AlertDialog alertDialog = dialogBuilder.create();

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        alertDialog.dismiss();
                    }
                });

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        String qty = etQty.getText().toString().trim();

                        if(qty.isEmpty()) {
                            etQty.setError(v.getContext().getResources().getString(R.string.err_msg_qty1));
                        } else if (Integer.valueOf(qty) < 1) {
                            etQty.setError(v.getContext().getResources().getString(R.string.err_msg_qty2));
                        } else {
                            DirectPurchaseFragment.update_qty(stocktake.getId(), Integer.valueOf(qty));
                            alertDialog.dismiss();
                        }
                    }
                });

                alertDialog.show();
            });
        } else {
            bc1.setText(stocktake.getBc1());
            bc2.setText(stocktake.getBc2());
            fixture.setText(stocktake.getFixture());
            qty.setText(stocktake.getQty() + " Pcs");
        }
    }
}
