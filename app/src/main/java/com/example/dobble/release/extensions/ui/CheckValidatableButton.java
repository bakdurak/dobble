package com.example.dobble.release.extensions.ui;


import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import java.util.HashMap;
import java.util.Map;

public class CheckValidatableButton extends AppCompatButton implements IValidatableSubscriber {
    private Map<IValidatable, Boolean> validatables = new HashMap<>();
    private int validatablesCnt;
    private int validItemCnt = 0;

    public CheckValidatableButton(Context context) {
        super(context);
    }

    public CheckValidatableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckValidatableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init() {
        disable();
    }

    @Override
    public void notify(IValidatable validatable) {
        Boolean validPrev = validatables.get(validatable);
        boolean validCur = validatable.isValid();

        if (validPrev == null) {
            return;
        }

        if(validPrev && !validCur) {
            --validItemCnt;
            validPrev = false;
        }
        if(!validPrev && validCur) {
            ++validItemCnt;
            validPrev = true;
        }
        validatables.put(validatable, validPrev);

        if (validItemCnt == validatablesCnt) {
            enable();
        }
        else {
            disable();
        }
    }

    @Override
    public void removeValidatable(IValidatable validatable) {
        Boolean valid = validatables.remove(validatable);
        if (valid) {
            --validItemCnt;
        }
        --validatablesCnt;
        validatable.unsubscribe(this);
    }

    @Override
    public CheckValidatableButton addValidatable(IValidatable validatable) {
        validatables.put(validatable, false);
        ++validatablesCnt;
        validatable.subscribe(this);

        return this;
    }

    private void disable() {
        setEnabled(false);
        setAlpha(0.2f);
    }

    private void enable() {
        setEnabled(true);
        setAlpha(1);
    }
}
