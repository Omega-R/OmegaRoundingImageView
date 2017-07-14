package com.omega_r.rounding_imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.widget.ImageView;

public class RoundingImageView extends ImageView {

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 2;

    private final RectF mDrawableRect = new RectF();

    Matrix mTempMatrix = new Matrix();

    private final Paint mBitmapPaint = new Paint();

    private Bitmap mBitmap;
    private BitmapShader mBitmapShader;
    private BitmapShader mBitmapFitXYShader;

    private ColorFilter mColorFilter;

    private boolean mReady;
    private boolean mSetupPending;

    private float mImageOffsetX;
    private float mImageOffsetY;

    public void setImageOffsetY(float offsetY) {
        mImageOffsetY = offsetY;
        setup();
    }

    public void setImageOffsetX(float offsetX) {
        mImageOffsetX = offsetX;
        setup();
    }

    // Changes

    // Common use cases
    public enum RoundingProgress {

        // No rounding
        MIN(0f),

        // Perfect rounding
        MAX(1f);

        RoundingProgress(float progressValue) {
            mProgressValue = progressValue;
        }

        /**
         * Rounding value for this enum.
         *
         * @return the assigned rounding value
         */
        public float progressValue() {
            return mProgressValue;
        }

        // Rounding value for this enum.
        private float mProgressValue;
    }

    // Amount of rounding to apply
    private float mRoundingProgress;

    // Used while drawing to the canvas
    private float mRoundedRadius;

    // Exposed property that is animated by ObjectAnimator
    public static final Property<View, Float> ROUNDING_PROGRESS_PROPERTY
            = new Property<View, Float>(Float.class, "roundingProgress") {
        @Override
        public Float get(View object) {
            return null;
        }

        @Override
        public void set(View object, Float value) {
            if (object instanceof RoundingImageView) {

                ((RoundingImageView) object).setRoundingProgress(value);
            }
        }
    };

    public RoundingImageView(Context context) {
        super(context);
        mRoundingProgress = RoundingProgress.MAX.progressValue();
        init();
    }

    public RoundingImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundingImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundingImageView, defStyle, 0);
        mRoundingProgress = constrain(a.getFloat(R.styleable.RoundingImageView_rounding,
                RoundingProgress.MAX.progressValue()), RoundingProgress.MIN.progressValue(),
                RoundingProgress.MAX.progressValue());

        mImageOffsetX = a.getFloat(R.styleable.RoundingImageView_image_offset_x, 0.5f);
        mImageOffsetY = a.getFloat(R.styleable.RoundingImageView_image_offset_y, 0.5f);

        a.recycle();
        init();
    }

    private void init() {
        mReady = true;
        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        if (adjustViewBounds) {
            throw new IllegalArgumentException("adjustViewBounds not supported.");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap == null) {
            return;
        }

        updateShaderMatrix();

        mDrawableRect.set(calculateBounds());

        mBitmapPaint.setShader(mBitmapShader);

        canvas.drawRoundRect(mDrawableRect, mRoundedRadius, mRoundedRadius, mBitmapPaint);
    }

    private void updateShaderMatrix() {
        if (getScaleType() == ScaleType.FIT_XY && getImageMatrix().isIdentity()) {
            Rect bounds = getDrawable().getBounds();

            float bitmapWidth = mBitmap.getWidth();
            float bitmapHeight = mBitmap.getHeight();
            float clearViewWidth = bounds.width();
            float clearViewHeight = bounds.height();

            mTempMatrix.reset();
            mTempMatrix.setScale(clearViewWidth / bitmapWidth, clearViewHeight / bitmapHeight);
        } else {
            mTempMatrix = getImageMatrix();
        }

        mBitmapShader.setLocalMatrix(mTempMatrix);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        setup();
    }

    public float getImageOffsetX() {
        return mImageOffsetX;
    }

    public float getImageOffsetY() {
        return mImageOffsetY;
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        super.setPaddingRelative(start, top, end, bottom);
        setup();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        initializeBitmap();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        initializeBitmap();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        initializeBitmap();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        initializeBitmap();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if (cf == mColorFilter) {
            return;
        }

        mColorFilter = cf;
        applyColorFilter();
        invalidate();
    }

    @Override
    public ColorFilter getColorFilter() {
        return mColorFilter;
    }

    private void applyColorFilter() {
        if (mBitmapPaint != null) {
            mBitmapPaint.setColorFilter(mColorFilter);
        }
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
                    return null;
                }

                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initializeBitmap() {
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }


    private void setup() {
        if (!mReady) {
            mSetupPending = true;
            return;
        }

        if (getWidth() == 0 && getHeight() == 0) {
            return;
        }

        if (mBitmap == null) {
            invalidate();
            return;
        }

        mRoundedRadius = getWidth() / 2f * mRoundingProgress;
        mBitmapPaint.setAntiAlias(true);

        initBitmapShadersIfNeeded();

        applyColorFilter();
        invalidate();
    }

    private void initBitmapShadersIfNeeded() {
        int clearViewWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int clearViewHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        if (mBitmapShader == null) {
            mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        }

        if (mBitmapFitXYShader == null && clearViewWidth > 0 && clearViewHeight > 0) {
            Bitmap fillBitmap = Bitmap.createScaledBitmap(mBitmap, clearViewWidth, clearViewHeight, false);
            mBitmapFitXYShader = new BitmapShader(fillBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        }
    }

    private RectF calculateBitmapBounds() {
        RectF rect = new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        mTempMatrix.mapRect(rect);
        return rect;
    }

    private RectF calculateBounds() {
        int bitmapTransformedHeight;
        int drawingSpaceHeight;
        int bitmapTransformedWidth;
        int drawingSpaceWidth;

        drawingSpaceHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        drawingSpaceWidth = getWidth() - getPaddingLeft() - getPaddingRight();

        RectF bitmapBounds = calculateBitmapBounds();

        bitmapTransformedWidth = (int) bitmapBounds.width();
        bitmapTransformedHeight = (int) bitmapBounds.height();

        float width = Math.min(drawingSpaceWidth, bitmapTransformedWidth);
        float height = Math.min(drawingSpaceHeight, bitmapTransformedHeight);

        float left = getPaddingLeft();
        float top = getPaddingTop();

        if (drawingSpaceWidth - bitmapTransformedWidth > 0) {
            left += bitmapBounds.left;
        }

        if (drawingSpaceHeight - bitmapTransformedHeight > 0) {
            top += bitmapBounds.top;
        }

        return new RectF(left, top, left + width, top + height);
    }


    /********************* Changes *********************/

    /**
     * Governs the rounding aspect of this ImageView.
     * Value of `0f` signifies no rounding at all.
     * Value of `1f` signifies perfectly rounded corners.
     * Values in-between are interpolated to provide the
     * required rounding.
     *
     * @param roundingProgress rounding value to apply; constrained in range [0f,1f]
     */
    public void setRoundingProgress(float roundingProgress) {
        // constrain value in range [0f,1f]
        roundingProgress = constrain(roundingProgress, RoundingProgress.MIN.progressValue(),
                RoundingProgress.MAX.progressValue());

        if (mRoundingProgress == roundingProgress) {
            // no change
            return;
        }

        // apply changes
        mRoundingProgress = roundingProgress;
        setup();
    }

    /**
     * Returns the current amount of rounding.
     *
     * @return current rounding amount
     */
    public float getRoundingProgress() {
        return mRoundingProgress;
    }

    /**
     * Constrains the given `amount` within `low` & `high`.
     *
     * @param amount the amount to work with
     * @param low    lower bound for `amount`
     * @param high   upper bound for `amount`
     * @return constrained `amount`
     */
    private float constrain(float amount, float low, float high) {
        return amount < low ? low : (amount > high ? high : amount);
    }
}
