package com.achmadhafizh.stocktake.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.achmadhafizh.stocktake.R;
import com.achmadhafizh.stocktake.model.Stocktake;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by achmad.hafizh on 10/27/2017.
 */

public class InquiryViewHolder extends RecyclerView.ViewHolder {
    public RelativeLayout viewForeground;

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
    }

    public void bind(Stocktake stocktake, Boolean indent) {
        if(indent) {
            bc1.setText(stocktake.getBc1().substring(5, 11));
            bc2.setText(String.valueOf(Integer.parseInt(stocktake.getBc2().substring(1, 12))));
            fixture.setText(stocktake.getFixture());
            qty.setText(String.valueOf(stocktake.getQty()) + " Pcs");
        } else {
            bc1.setText(stocktake.getBc1());
            bc2.setText(stocktake.getBc2());
            fixture.setText(stocktake.getFixture());
            qty.setText(String.valueOf(stocktake.getQty()) + " Pcs");
        }
    }
}
