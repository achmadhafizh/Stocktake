package com.achmadhafizh.stocktake.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.achmadhafizh.stocktake.R;
import com.achmadhafizh.stocktake.helper.DatabaseHandler;
import com.achmadhafizh.stocktake.model.Stocktake;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by achmad.hafizh on 10/23/2017.
 */

public class StocktakeAdapter extends RecyclerView.Adapter<StocktakeAdapter.MyViewHolder> {
    private Context context;
    private List<Stocktake> stocktakeList;
    private DatabaseHandler db;

    public class MyViewHolder extends RecyclerView.ViewHolder {
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

        @BindView(R.id.view_foreground)
        RelativeLayout viewForeground;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            bc1 = view.findViewById(R.id.bc1);
            bc2 = view.findViewById(R.id.bc2);
            fixture = view.findViewById(R.id.fixture);
            qty = view.findViewById(R.id.qty);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }


    public StocktakeAdapter(Context context, List<Stocktake> stocktakeList) {
        this.context = context;
        this.stocktakeList = stocktakeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Stocktake item = stocktakeList.get(position);
        holder.bc1.setText(item.getBc1());
        holder.bc2.setText(item.getBc2());
        holder.fixture.setText(item.getFixture());
        holder.qty.setText(String.valueOf(item.getQty()) + " Pcs");
    }

    @Override
    public int getItemCount() {
        return stocktakeList.size();
    }

    public void removeItem(int position) {
        stocktakeList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Stocktake item, int position) {
        stocktakeList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}
