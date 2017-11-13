package com.achmadhafizh.stocktake.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.achmadhafizh.stocktake.R;
import com.achmadhafizh.stocktake.holder.InquiryViewHolder;
import com.achmadhafizh.stocktake.model.Stocktake;

import java.util.List;

/**
 * Created by achmad.hafizh on 10/27/2017.
 */

public class InquiryAdapter extends RecyclerView.Adapter<InquiryViewHolder> {
    private List<Stocktake> mStocktakeList;
    private Boolean indent;

    public InquiryAdapter(List<Stocktake> stocktakeList, Boolean indent) {
        this.mStocktakeList = stocktakeList;
        this.indent = indent;
    }

    @Override
    public void onBindViewHolder(InquiryViewHolder inquiryViewHolder, int i) {
        final Stocktake model = mStocktakeList.get(i);
        inquiryViewHolder.bind(model, indent);
    }

    @Override
    public InquiryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_list_item, viewGroup, false);
        return new InquiryViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mStocktakeList.size();
    }

    public Stocktake removeItem(int position) {
        final Stocktake model = mStocktakeList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void restoreItem(Stocktake item, int position) {
        mStocktakeList.add(position, item);
        notifyItemInserted(position);
    }
}
