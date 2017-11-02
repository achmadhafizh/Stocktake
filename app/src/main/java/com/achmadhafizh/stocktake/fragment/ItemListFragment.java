package com.achmadhafizh.stocktake.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.achmadhafizh.stocktake.R;
import com.achmadhafizh.stocktake.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by achmad.hafizh on 10/25/2017.
 */

public class ItemListFragment extends ListFragment {
    private static final String ARG_SECTION = "section";

    private List<String> items;

    public ItemListFragment() {
    }

    public static ItemListFragment newInstance(String section) {
        ItemListFragment fragment = new ItemListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION, section);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String section = getArguments().getString(ARG_SECTION);
        items = createItemsForSection(section);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpActionBar();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, items);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ((MainActivity) getActivity()).showFragment(ItemFragment.newInstance(items.get(position)));
    }

    private List<String> createItemsForSection(String section) {
        int itemsNumber = 10;
        List<String> items = new ArrayList<>(itemsNumber);
        for (int i = 0; i < itemsNumber; i++) {
            items.add(section + " " + (i + 1));
        }
        return items;
    }

    @SuppressWarnings("ConstantConditions")
    private void setUpActionBar() {
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(R.string.app_name);
    }
}
