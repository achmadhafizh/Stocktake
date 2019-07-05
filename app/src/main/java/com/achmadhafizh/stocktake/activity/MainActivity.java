package com.achmadhafizh.stocktake.activity;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.achmadhafizh.stocktake.R;
import com.achmadhafizh.stocktake.fragment.ConsignmentFragment;
import com.achmadhafizh.stocktake.fragment.ConsignmentInquiryFragment;
import com.achmadhafizh.stocktake.fragment.DirectPurchaseFragment;
import com.achmadhafizh.stocktake.fragment.DirectPurchaseInquiryFragment;
import com.achmadhafizh.stocktake.fragment.SettingsFragment;
import com.achmadhafizh.stocktake.helper.BuilderManager;
import com.achmadhafizh.stocktake.utilities.MultiplePermissionListener;
import com.achmadhafizh.stocktake.utilities.PermissionErrorListener;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.jetradar.multibackstack.BackStackActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.achmadhafizh.stocktake.utilities.CommonConstant.MAIN_TAB_ID;
import static com.achmadhafizh.stocktake.utilities.CommonConstant.STATE_CURRENT_TAB_ID;
import static com.nightonke.boommenu.ButtonEnum.Ham;

public class MainActivity extends BackStackActivity implements BottomNavigationBar.OnTabSelectedListener {
    private static String TAG = MainActivity.class.getSimpleName();
    private Fragment curFragment;
    private int curTabId;
    private ActionBar mActionBar;
    private LayoutInflater mInflater;
    private View actionBarView;
    private BoomMenuButton rightBmb;
    private MultiplePermissionsListener allPermissionsListener;
    private PermissionRequestErrorListener errorListener;
    public static TextView mTitleTextView;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.bottom_navigation)
    BottomNavigationBar bottomNavBar;

    @BindView(android.R.id.content)
    View contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        requestMultiplePermission();
        setUpTopNavBar();
        setUpBottomNavBar();

        if (savedInstanceState == null) {
            bottomNavBar.selectTab(MAIN_TAB_ID, false);
            showFragment(rootTabFragment(MAIN_TAB_ID));
        }
    }

    private void requestMultiplePermission() {
        createPermissionListeners();

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(allPermissionsListener)
                .withErrorListener(errorListener)
                .check();
    }

    private void createPermissionListeners() {
        MultiplePermissionsListener feedbackViewMultiplePermissionListener = new MultiplePermissionListener(this);

        allPermissionsListener = new CompositeMultiplePermissionsListener(feedbackViewMultiplePermissionListener,
                SnackbarOnAnyDeniedMultiplePermissionsListener.Builder.with(contentView, R.string.permission_rationale_message)
                        .withOpenSettingsButton(R.string.permission_rationale_positive_button)
                        .build());

        errorListener = new PermissionErrorListener();
    }

    private void setUpTopNavBar() {
        setSupportActionBar(toolbar);

        mActionBar = getSupportActionBar();
        assert mActionBar != null;
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mInflater = LayoutInflater.from(this);

        actionBarView = mInflater.inflate(R.layout.custom_actionbar, null);
        mTitleTextView = (TextView) actionBarView.findViewById(R.id.title_text);
        mTitleTextView.setText("ActionBar");
        mActionBar.setCustomView(actionBarView);
        mActionBar.setDisplayShowCustomEnabled(true);
        ((Toolbar) actionBarView.getParent()).setContentInsetsAbsolute(0, 0);

        rightBmb = (BoomMenuButton) actionBarView.findViewById(R.id.action_bar_right_bmb);
        rightBmb.setButtonEnum(Ham);
        rightBmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_2);
        rightBmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_2);

        for (int i = 0; i < rightBmb.getPiecePlaceEnum().pieceNumber(); i++) {
            rightBmb.addBuilder(new HamButton.Builder()
                    .normalImageRes(BuilderManager.getImageResource())
                    .normalText(getResources().getString(BuilderManager.getTextResource()))
                    .subNormalText(getResources().getString(BuilderManager.getSubTextResource()))
                    .pieceColor(Color.WHITE)
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            showFragment(rootButtonFragment(index));
                        }
                    }));
        }
    }

    private void setUpBottomNavBar() {
        bottomNavBar
                .addItem(new BottomNavigationItem(R.drawable.ic_settings_24dp, R.string.settings))
                .addItem(new BottomNavigationItem(R.drawable.ic_info_24dp, R.string.indent))
                .addItem(new BottomNavigationItem(R.drawable.ic_info_outline_24dp, R.string.consignment))
                .initialise();
        bottomNavBar.setTabSelectedListener(this);
    }

    @NonNull
    private Fragment rootTabFragment(int tabId) {
        switch (tabId) {
            case 0:
                return SettingsFragment.newInstance(getString(R.string.settings));
            case 1:
                return DirectPurchaseFragment.newInstance(getString(R.string.indent));
            case 2:
                return ConsignmentFragment.newInstance(getString(R.string.consignment));
            default:
                throw new IllegalArgumentException();
        }
    }

    @NonNull
    private Fragment rootButtonFragment(int btnId) {
        switch (btnId) {
            case 0:
                return DirectPurchaseInquiryFragment.newInstance(getString(R.string.menu1));
            case 1:
                return ConsignmentInquiryFragment.newInstance(getString(R.string.menu2));
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        curFragment = getSupportFragmentManager().findFragmentById(R.id.content);
        curTabId = savedInstanceState.getInt(STATE_CURRENT_TAB_ID);
        bottomNavBar.selectTab(curTabId, false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_CURRENT_TAB_ID, curTabId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        Pair<Integer, Fragment> pair = popFragmentFromBackStack();
        if (pair != null) {
            backTo(pair.first, pair.second);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onTabSelected(int position) {
        if (curFragment != null) {
            pushFragmentToBackStack(curTabId, curFragment);
        }
        curTabId = position;
        Fragment fragment = popFragmentFromBackStack(curTabId);
        if (fragment == null) {
            fragment = rootTabFragment(curTabId);
        }
        replaceFragment(fragment);
    }

    @Override
    public void onTabReselected(int position) {
        backToRoot();
    }

    @Override
    public void onTabUnselected(int position) {
    }

    public void showFragment(@NonNull Fragment fragment) {
        showFragment(fragment, true);
    }

    public void showFragment(@NonNull Fragment fragment, boolean addToBackStack) {
        if (curFragment != null && addToBackStack) {
            pushFragmentToBackStack(curTabId, curFragment);
        }
        replaceFragment(fragment);
    }

    private void backTo(int tabId, @NonNull Fragment fragment) {
        if (tabId != curTabId) {
            curTabId = tabId;
            bottomNavBar.selectTab(curTabId, false);
        }
        replaceFragment(fragment);
        getSupportFragmentManager().executePendingTransactions();
    }

    private void backToRoot() {
        if (isRootTabFragment(curFragment, curTabId)) {
            return;
        }
        resetBackStackToRoot(curTabId);
        Fragment rootFragment = popFragmentFromBackStack(curTabId);
        assert rootFragment != null;
        backTo(curTabId, rootFragment);
    }

    private boolean isRootTabFragment(@NonNull Fragment fragment, int tabId) {
        return fragment.getClass() == rootTabFragment(tabId).getClass();
    }

    private void replaceFragment(@NonNull Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tr = fm.beginTransaction();
        tr.replace(R.id.content, fragment);
        tr.commitAllowingStateLoss();
        curFragment = fragment;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume : running");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause : running");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart : running");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop : running");
    }
}
