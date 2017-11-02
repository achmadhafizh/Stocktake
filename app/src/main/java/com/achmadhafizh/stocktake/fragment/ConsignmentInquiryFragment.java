package com.achmadhafizh.stocktake.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.achmadhafizh.stocktake.R;
import com.achmadhafizh.stocktake.activity.MainActivity;
import com.achmadhafizh.stocktake.adapter.FilteredAdapter;
import com.achmadhafizh.stocktake.adapter.RecyclerItemTouchHelper;
import com.achmadhafizh.stocktake.helper.DatabaseManager;
import com.achmadhafizh.stocktake.holder.InquiryViewHolder;
import com.achmadhafizh.stocktake.model.Stocktake;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.achmadhafizh.stocktake.utilities.CommonConstant.ARG_ITEM;

public class ConsignmentInquiryFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private static final String TAG = ConsignmentInquiryFragment.class.getSimpleName();
    private DatabaseManager db;
    private ArrayList<Stocktake> mStockTakeList;
    private FilteredAdapter mAdapter;
    private String item;

    @BindView(R.id.rl_outer)
    RelativeLayout relativeLayout;

    @BindView(R.id.searchView)
    SearchView searchView;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.tv_qty_footer)
    TextView tv_qty_footer;

    public ConsignmentInquiryFragment() {}

    public static ConsignmentInquiryFragment newInstance(String section) {
        ConsignmentInquiryFragment fragment = new ConsignmentInquiryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ITEM, section);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = getArguments().getString(ARG_ITEM);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_consignment_inquiry, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        setUpActionBar();

        db = new DatabaseManager(getActivity());

        search(searchView);
        searchView.setQueryHint("Search Fixture Here");

        mStockTakeList = new ArrayList<>();
        mAdapter = new FilteredAdapter(mStockTakeList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        prepareListData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("ConstantConditions")
    private void setUpActionBar() {
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(item);
        MainActivity.mTitleTextView.setText(item);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof InquiryViewHolder) {
            // get the removed item name to display it in snack bar
            final Integer id = mStockTakeList.get(viewHolder.getAdapterPosition()).getId();
            final String type = mStockTakeList.get(viewHolder.getAdapterPosition()).getType();
            final String fixture = mStockTakeList.get(viewHolder.getAdapterPosition()).getFixture();
            final String bc1 = mStockTakeList.get(viewHolder.getAdapterPosition()).getBc1();
            final String bc2 = mStockTakeList.get(viewHolder.getAdapterPosition()).getBc2();
            final Integer qty = mStockTakeList.get(viewHolder.getAdapterPosition()).getQty();
            final String nik = mStockTakeList.get(viewHolder.getAdapterPosition()).getNik();
            final String scannerID = mStockTakeList.get(viewHolder.getAdapterPosition()).getScanner();
            final Integer flag = mStockTakeList.get(viewHolder.getAdapterPosition()).getFlag();

            // backup of removed item for undo purpose
            final Stocktake deletedItem = mStockTakeList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            //remove from database
            Log.d(TAG, "Deleting ..");
            db.deleteStocktake(new Stocktake(id));

            // resummarize qty
            Integer total = getNumberFromString(tv_qty_footer.getText().toString().trim()) - qty;
            tv_qty_footer.setText(total  + " Pcs");

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar.make(relativeLayout, bc1 + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                    Log.d(TAG, "Inserting ..");
                    db.addStocktake(new Stocktake(type, fixture, bc1, bc2, qty, nik, scannerID, flag));

                    // resummarize qty
                    Integer total = getNumberFromString(tv_qty_footer.getText().toString().trim()) + qty;
                    tv_qty_footer.setText(total  + " Pcs");
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    private int getNumberFromString(String word) {
        String regex = "[^0-9]+";
        Integer number = Integer.parseInt(word.toString().trim().replaceAll(regex,""));
        return number;
    }

    private void prepareListData() {
        Log.d(TAG, "Reading all Stocktake CS");
        List<Stocktake> items = db.getAllStocktakeCS();
        int total = 0;

        for (Stocktake stk : items) {
            total = total + stk.getQty();
            String log = "Id: " + stk.getId() + ", Type: " + stk.getType() + ", Fixture: " + stk.getFixture() +
                    ", Barcode 1: " + stk.getBc1() + ", Barcode 2: " + stk.getBc2() + ", Qty: " + stk.getQty() +
                    ", Nik: " + stk.getNik() + " ,Scanner: " + stk.getScanner() + " ,Flag: " + stk.getFlag();
            Log.d("All CS Records ", log);
        }

        tv_qty_footer.setText(total + " Pcs");
        mStockTakeList.clear();
        mStockTakeList.addAll(items);

        mAdapter.notifyDataSetChanged();
    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

}
