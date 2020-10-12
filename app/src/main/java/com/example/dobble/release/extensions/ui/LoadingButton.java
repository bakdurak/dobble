package com.example.dobble.release.extensions.ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.appcompat.widget.AppCompatButton;

import com.example.dobble.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoadingButton extends AppCompatButton {
    @FunctionalInterface
    public interface CallOnClick {
        boolean exec();
    }

    private static final int LOADING_SPINNER = R.drawable.loading_spinner;
    private static final int DEFAULT_SUCCESS_ICON = R.drawable.success;
    private static final int DEFAULT_ERROR_ICON = R.drawable.error;
    private static final int MAX_LEVEL = 10_000;
    private static final int RESET_BACK_ICON_TIMER = 3;

    private int idleIcon;
    private boolean initialized = false;
    private Integer successIcon = DEFAULT_SUCCESS_ICON;
    private Integer errorIcon = DEFAULT_ERROR_ICON;
    private Drawable spinnerDrawable;
    private ObjectAnimator animator;
    private CallOnClick callback;

    public LoadingButton(Context context) {
        super(context);
    }

    public LoadingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(int idleIcon, Integer successIcon, Integer errorIcon, CallOnClick callback) {
        if (initialized) {
            return;
        }
        
        spinnerDrawable = getContext().getResources().getDrawable(LOADING_SPINNER);
        animator = ObjectAnimator.ofInt(spinnerDrawable, "level", 0, MAX_LEVEL);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ObjectAnimator.INFINITE);

        setOnClickListener(this::onClickHandler);

        this.idleIcon = idleIcon;
        if (successIcon != null) {
            this.successIcon = successIcon;
        }
        if (errorIcon != null) {
            this.errorIcon = errorIcon;
        }
        this.callback = callback;

        initialized = true;
    }

    private void onClickHandler(View v) {
        if (callback != null) {
            if (!callback.exec()) {
                return;
            }
        }

        setEnabled(false);

        setCompoundDrawablesWithIntrinsicBounds(spinnerDrawable, null , null, null);
        animator.start();
    }

    public void onError() {
        setEnabled(true);
        setCompoundDrawablesWithIntrinsicBounds(errorIcon, 0 , 0, 0);
        setUpTimer();
    }

    public void onSuccess() {
        setEnabled(true);
        setCompoundDrawablesWithIntrinsicBounds(successIcon, 0 , 0, 0);
        setUpTimer();
    }

    private void setUpTimer() {
        Single.timer(RESET_BACK_ICON_TIMER, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(t -> setCompoundDrawablesWithIntrinsicBounds(idleIcon, 0 , 0, 0));
    }
}
