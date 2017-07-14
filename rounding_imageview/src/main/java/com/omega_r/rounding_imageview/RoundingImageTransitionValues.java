package com.omega_r.rounding_imageview;

public class RoundingImageTransitionValues {

    public static final String KEY_IMAGE_START_ROUNDING = "imageStartRounding";
    public static final String KEY_IMAGE_END_ROUNDING = "imageEndRounding";
    public static final String KEY_IMAGE_START_OFFSET = "imageStartOffset";
    public static final String KEY_IMAGE_END_OFFSET = "imageEndOffset";

    private float mStartOffsetY;
    private float mEndOffsetY;
    private float mStartRounding;
    private float mEndRounding;

    public RoundingImageTransitionValues(float startOffsetY, float endOffsetY, float startRounding, float endRounding) {
        mStartOffsetY = startOffsetY;
        mEndOffsetY = endOffsetY;
        mStartRounding = startRounding;
        mEndRounding = endRounding;
    }

    public float getStartOffsetY() {
        return mStartOffsetY;
    }

    public float getEndOffsetY() {
        return mEndOffsetY;
    }

    public float getStartRounding() {
        return mStartRounding;
    }

    public float getEndRounding() {
        return mEndRounding;
    }
}
