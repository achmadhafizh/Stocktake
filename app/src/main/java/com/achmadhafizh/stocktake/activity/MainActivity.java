package com.achmadhafizh.stocktake.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.achmadhafizh.stocktake.R;
import com.achmadhafizh.stocktake.adapter.BuilderManager;
import com.achmadhafizh.stocktake.fragment.ConsignmentFragment;
import com.achmadhafizh.stocktake.fragment.SettingsFragment;
import com.achmadhafizh.stocktake.helper.SessionManager;
import com.achmadhafizh.stocktake.utilities.CommonConstant;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.achmadhafizh.stocktake.utilities.CommonConstant.IS_DRAGGABLE;
import static com.nightonke.boommenu.ButtonEnum.Ham;

public class MainActivity extends AppCompatActivity {
    private static String TAG = MainActivity.class.getSimpleName();
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static SessionManager sessionManager;
    private static HashMap<String, Boolean> settingPrefs;
    private static Boolean isDraggable;
    private static Switch draggableSwitch;
    private Fragment fragment;
    private String fragmentTag;
    private FragmentTransaction fragmentTransaction;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.bmb)
    BoomMenuButton bmb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        sessionManager = new SessionManager(getApplicationContext());
        sharedPreferences = getSharedPreferences(CommonConstant.PREFS_NAME, Context.MODE_PRIVATE);

        assert bmb != null;
        bmb.setButtonEnum(Ham);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_4);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_4);
        bmb.setPieceCornerRadius(2);

        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            bmb.addBuilder(new HamButton.Builder()
                    .normalImageRes(BuilderManager.getImageResource())
                    .normalText(getResources().getString(BuilderManager.getTextResource()))
                    .subNormalText(getResources().getString(BuilderManager.getSubTextResource()))
                    .pieceColor(Color.WHITE)
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            //Toast.makeText(MainActivity.this, "No." + index + " boom-button is clicked!", Toast.LENGTH_SHORT).show();
                            switch(index){
                                case 0:
                                    break;
                                case 1:
                                    fragment = new ConsignmentFragment();
                                    fragmentTag = "ConsignmentFragment";
                                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                                    fragmentTransaction.replace(R.id.content, fragment, fragmentTag);
                                    fragmentTransaction.commitAllowingStateLoss();
                                    break;
                                case 2:
                                    break;
                                case 3:
                                    fragment = new SettingsFragment();
                                    fragmentTag = "SettingsFragment";
                                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                                    fragmentTransaction.replace(R.id.content, fragment, fragmentTag);
                                    fragmentTransaction.commitAllowingStateLoss();
                                    break;
                                default:
                                    return;
                            }
                        }
                    }));
        }

        draggableSwitch = (Switch) findViewById(R.id.draggable_switch);
        draggableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor = sharedPreferences.edit();
                editor.putBoolean(IS_DRAGGABLE, isChecked);
                editor.commit();

                bmb.setDraggable(isChecked);
            }
        });

        setDraggableSwitch();
    }

    public static void setDraggableSwitch() {
        settingPrefs = sessionManager.getSettingDetails();
        isDraggable = settingPrefs.get(IS_DRAGGABLE);
        draggableSwitch.setChecked(isDraggable);
        Log.d(TAG, "isDraggable : "+isDraggable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
