package com.omega_r.rounding_imageview;

import android.app.SharedElementCallback;

public class RoundingImageTransitionUtil {
    public static SharedElementCallback getCallback(RoundingImageTransitionValues values) {
        return new BaseSharedElementCallback(values);
    }
}
