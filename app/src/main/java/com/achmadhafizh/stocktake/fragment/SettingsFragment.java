package com.achmadhafizh.stocktake.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.achmadhafizh.stocktake.R;
import com.achmadhafizh.stocktake.activity.MainActivity;
import com.achmadhafizh.stocktake.helper.SessionManager;
import com.achmadhafizh.stocktake.utilities.CommonConstant;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.achmadhafizh.stocktake.utilities.CommonConstant.IS_DRAGGABLE;

public class SettingsFragment extends Fragment {
    private String TAG = SettingsFragment.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SessionManager sessionManager;
    private HashMap<String, Boolean> settingPrefs;
    private Boolean isDraggable;

    @BindView(R.id.draggable_switch)
    Switch draggableSwitch;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, null);

        ButterKnife.bind(this, v);

        sessionManager = new SessionManager(getActivity().getApplicationContext());
        sharedPreferences = getActivity().getSharedPreferences(CommonConstant.PREFS_NAME, Context.MODE_PRIVATE);

        settingPrefs = sessionManager.getSettingDetails();

        draggableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor = sharedPreferences.edit();
                editor.putBoolean(IS_DRAGGABLE, isChecked);
                editor.commit();

                MainActivity.setDraggableSwitch();

                Log.d(TAG, "isDraggable : "+isChecked);
            }
        });

        isDraggable = settingPrefs.get(IS_DRAGGABLE);
        draggableSwitch.setChecked(isDraggable);
        Log.d(TAG, "isDraggable : "+isDraggable);

        return v;
    }
}
