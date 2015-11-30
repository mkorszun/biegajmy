package com.biegajmy.general;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import com.biegajmy.utils.SystemUtils;

public abstract class GenericFragment extends Fragment implements View.OnTouchListener {

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnTouchListener(this);
    }
    
    @Override public boolean onTouch(View v, MotionEvent event) {
        SystemUtils.hideKeyboard(getActivity());
        return false;
    }
}
