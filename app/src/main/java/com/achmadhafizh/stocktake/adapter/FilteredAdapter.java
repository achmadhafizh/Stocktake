package com.achmadhafizh.stocktake.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.achmadhafizh.stocktake.R;
import com.achmadhafizh.stocktake.holder.InquiryViewHolder;
import com.achmadhafizh.stocktake.model.Stocktake;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by achmad.hafizh on 10/27/2017.
 */

public class FilteredAdapter extends RecyclerView.Adapter<InquiryViewHolder> implements Filterable {
    private ArrayList<Stocktake> mArrayList;
    private ArrayList<Stocktake> mFilteredList;
    private Boolean indent;

    public FilteredAdapter(ArrayList<Stocktake> arrayList, Boolean indent) {
        mArrayList = arrayList;
        mFilteredList = arrayList;
        this.indent = indent;
    }

    @Override
    public InquiryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_list_item, viewGroup, false);
        return new InquiryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InquiryViewHolder inquiryViewHolder, int i) {
        final Stocktake model = mFilteredList.get(i);
        inquiryViewHolder.bind(model, indent);
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredList = mArrayList;
                } else {
                    ArrayList<Stocktake> filteredList = new ArrayList<>();
                    for (Stocktake stocktake : mArrayList) {
                        if (stocktake.getFixture().toLowerCase().contains(charString) || stocktake.getFixture().toUpperCase().contains(charString)) {
                            filteredList.add(stocktake);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Stocktake>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void animateTo(List<Stocktake> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<Stocktake> newModels) {
        for (int i = mFilteredList.size() - 1; i >= 0; i--) {
            final Stocktake model = mFilteredList.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Stocktake> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Stocktake model = newModels.get(i);
            if (!mFilteredList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Stocktake> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Stocktake model = newModels.get(toPosition);
            final int fromPosition = mFilteredList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public Stocktake removeItem(int position) {
        final Stocktake model = mFilteredList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void restoreItem(Stocktake item, int position) {
        mFilteredList.add(position, item);
        notifyItemInserted(position);
    }


    public void addItem(int position, Stocktake model) {
        mFilteredList.add(position, model);
        notifyItemInserted(position);

    }

    public void moveItem(int fromPosition, int toPosition) {
        final Stocktake model = mFilteredList.remove(fromPosition);
        mFilteredList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
}
