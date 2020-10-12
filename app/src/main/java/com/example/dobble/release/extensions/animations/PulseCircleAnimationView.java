package com.example.dobble.release.extensions.animations;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

public class PulseCircleAnimationView extends View {
    private static final int ANIMATION_DURATION = 1000;

    private float radius;
    private int x;
    private int y;
    private Paint paint = new Paint();
    private ObjectAnimator growAnimator;

    public PulseCircleAnimationView(Context context) {
        super(context);
        init();
    }

    public PulseCircleAnimationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PulseCircleAnimationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PulseCircleAnimationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        paint.setColor(Color.parseColor("#86AFD3"));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        x = getWidth() / 2;
        y = getHeight() / 2;

        float wf = (float) w;
        growAnimator = ObjectAnimator.ofFloat(this, "radius", wf / 8, wf / 2.5f);
        growAnimator.setDuration(ANIMATION_DURATION);
        growAnimator.setInterpolator(new DecelerateInterpolator());
        growAnimator.setRepeatMode(ValueAnimator.REVERSE);
        growAnimator.setRepeatCount(ValueAnimator.INFINITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(x, y, radius, paint);
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        invalidate();
    }

    public void startAnimation() {
        growAnimator.start();
    }

    public void cancelAnimation() {
        growAnimator.cancel();
        radius = 0;
        invalidate();
    }

    public boolean isAnimating() {
        return growAnimator.isRunning();
    }
}
