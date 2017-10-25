package com.achmadhafizh.stocktake.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by achmad.hafizh on 10/25/2017.
 */

public class BaseFragment extends Fragment {
    public final <T extends BaseKey> T getKey() {
        return getArguments() != null ? getArguments().<T>getParcelable("KEY") : null;
    }
}