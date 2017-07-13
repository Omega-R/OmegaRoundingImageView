package com.omega_r.rounding_imageview_sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

public abstract class BaseActivity extends BaseTransitionActivity {

    protected abstract @StringRes int getTitleId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getTitleId());
    }
}