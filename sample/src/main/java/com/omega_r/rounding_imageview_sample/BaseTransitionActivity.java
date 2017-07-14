package com.omega_r.rounding_imageview_sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseTransitionActivity extends AppCompatActivity {

    private final String KEY_TRANSITION_ARGUMENTS = "transitionArguments";

    protected abstract Bundle getTransitionsArguments();
    protected abstract void updateTransitionsArguments(Bundle bundle);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Bundle bundle = extras.getParcelable(KEY_TRANSITION_ARGUMENTS);
            updateTransitionsArguments(bundle);
        }
    }

    @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {
        intent.putExtra(KEY_TRANSITION_ARGUMENTS, getTransitionsArguments());
        super.startActivity(intent, options);
    }
}
