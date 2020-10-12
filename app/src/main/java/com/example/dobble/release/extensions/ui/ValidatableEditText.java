package com.example.dobble.release.extensions.ui;

import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;

import com.example.dobble.R;

import java.util.ArrayList;
import java.util.List;

public class ValidatableEditText extends AppCompatEditText implements IValidatable {

    private static final int[] STATE_VALID = {R.attr.state_valid};
    private static final int ADDITIONAL_STATE_CNT = 1;

    protected boolean wasUnfocusedOrValid = false;
    protected boolean isValid = false;
    protected String errorMsg;
    protected TextView errorTextView;
    protected IValidateEditText callback;
    protected boolean backgroundChanged = false;
    protected boolean initialized = false;
    protected List<IValidatableSubscriber> subscribers = new ArrayList<>();

    public ValidatableEditText(Context context) {
        super(context);
    }

    public ValidatableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ValidatableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(IValidateEditText callback, TextView errorView, String errorMsg) {
        this.callback = callback;
        this.errorTextView = errorView;
        this.errorMsg = errorMsg;

        initialized = true;
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + ADDITIONAL_STATE_CNT);

        if (isValid) {
            mergeDrawableStates(drawableState, STATE_VALID);
        }
        return drawableState;
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        if (!initialized) {
            return;
        }

        Editable editable = getText();
        String typedText;
        if (editable == null) {
            typedText = "";
        }
        else {
            typedText = editable.toString();
        }

        validate(typedText);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);

        if (!initialized) {
            return;
        }

        Editable editable = getText();
        String typedText;
        if (editable == null) {
            typedText = "";
        }
        else {
            typedText = editable.toString();
        }

        if (!focused && typedText.length() > 0) {
            wasUnfocusedOrValid = true;
        }

        validate(typedText);
    }

    private void validate(String text) {
        if (callback.validate(text)) {
            errorTextView.setText("");
            errorTextView.setVisibility(INVISIBLE);

            setValid(true);
            wasUnfocusedOrValid = true;
        }
        else {
            if (!wasUnfocusedOrValid) {
                return;
            }

            errorTextView.setText(errorMsg);
            errorTextView.setVisibility(VISIBLE);

            setValid(false);
        }
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public void subscribe(IValidatableSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void unsubscribe(IValidatableSubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    private void setValid(boolean valid) {
        if (!backgroundChanged) {
            setBackgroundResource(R.drawable.validatable_edit_text_selector);
            backgroundChanged = true;
        }

        isValid = valid;
        refreshDrawableState();

        for(IValidatableSubscriber subscriber  : subscribers){
            subscriber.notify(this);
        }
    }
}
