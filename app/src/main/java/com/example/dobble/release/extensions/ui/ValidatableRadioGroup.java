package com.example.dobble.release.extensions.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;

import com.example.dobble.R;

import java.util.ArrayList;
import java.util.List;

public class ValidatableRadioGroup extends RadioGroup implements IValidatable {
    private boolean isValid = false;
    private List<IValidatableSubscriber> subscribers = new ArrayList<>();
    private SelectedValue selectedValue = null;

    public enum SelectedValue {MALE, FEMALE}
    
    public ValidatableRadioGroup(Context context) {
        super(context);
        init();
    }

    public ValidatableRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.setOnCheckedChangeListener(this::onCheckListener);
    }

    private void onCheckListener(RadioGroup group, int checkedId) {
        if (checkedId == R.id.radio_male) {
            selectedValue = SelectedValue.MALE;
        }
        else if (checkedId == R.id.radio_female) {
            selectedValue = SelectedValue.FEMALE;
        }

        isValid = true;
        for(IValidatableSubscriber subscriber : subscribers) {
            subscriber.notify(this);
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

    public SelectedValue getSelectedValue() {
        return selectedValue;
    }
}
