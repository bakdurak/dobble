package com.example.dobble.release.extensions.ui;

import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;

import com.example.dobble.R;
import com.example.dobble.release.ui.fragments.RegistrationFragment;
import com.example.dobble.release.vm.RegistrationViewModel;
import com.example.dobble.release.utils.Fsm;
import com.example.dobble.release.utils.ITransition;

import java.util.ArrayList;
import java.util.List;

public class EmailValidatableEditText extends AppCompatEditText implements IValidatable {

    private static final int[] STATE_VALID = {R.attr.state_valid};
    private static final int ADDITIONAL_STATE_CNT = 1;
    private static final Class<? extends IEmailState> INITIAL_STATE = IdleState.class;

    private ProgressBar spinner;
    private RegistrationViewModel vm;
    protected boolean wasUnfocused = false;
    protected boolean wasValid = false;
    protected boolean isValid = false;
    protected String errorMsg;
    protected String emailExistsMsg;
    protected TextView errorView;
    protected IValidateEditText callback;
    protected boolean backgroundChanged = false;
    protected boolean initialized = false;
    protected boolean checkedAfterUnfocused = false;
    protected Fsm<IEmailState> fsm;
    protected List<IValidatableSubscriber> subscribers = new ArrayList<>();

    private interface IEmailState {
        Class<? extends IEmailState> consume(OnTypeValidTransition transition);
        Class<? extends IEmailState> consume(OnTypeInvalidTransition transition);
        Class<? extends IEmailState> consume(TimerExpiredTransition transition);
        Class<? extends IEmailState> consume(ResponseTransition transition);
    }

    private class OnTypeValidTransition implements ITransition<IEmailState> {
        private String email;

        public OnTypeValidTransition(String email) {
            this.email = email;
        }

        @Override
        public Class<? extends IEmailState> accept(IEmailState visitor) {
            return visitor.consume(this);
        }

        public String getEmail() {
            return email;
        }
    }

    private class OnTypeInvalidTransition implements ITransition<IEmailState> {
        @Override
        public Class<? extends IEmailState> accept(IEmailState visitor) {
            return visitor.consume(this);
        }
    }

    private class TimerExpiredTransition implements ITransition<IEmailState> {
        @Override
        public Class<? extends IEmailState> accept(IEmailState visitor) {
            return visitor.consume(this);
        }
    }

    private class ResponseTransition implements ITransition<IEmailState> {
        RegistrationViewModel.EmailCheckResponse response;

        public ResponseTransition(RegistrationViewModel.EmailCheckResponse response) {
            this.response = response;
        }

        @Override
        public Class<? extends IEmailState> accept(IEmailState visitor) {
            return visitor.consume(this);
        }

        public RegistrationViewModel.EmailCheckResponse getResponse() {
            return response;
        }
    }

    private class IdleState implements IEmailState {

        @Override
        public Class<? extends IEmailState> consume(OnTypeValidTransition transition) {
            vm.checkEmail(transition.getEmail());
            return TimerPendingState.class;
        }

        @Override
        public Class<? extends IEmailState> consume(OnTypeInvalidTransition transition) {
            if (!wasUnfocused || !wasValid) {
                return IdleState.class;
            }

            errorView.setText(errorMsg);
            errorView.setVisibility(VISIBLE);

            setValid(false);

            return IdleState.class;
        }

        @Override
        public Class<? extends IEmailState> consume(TimerExpiredTransition transition) {
            return null;
        }

        @Override
        public Class<? extends IEmailState> consume(ResponseTransition transition) {
            return null;
        }
    }

    private class TimerPendingState implements IEmailState {

        @Override
        public Class<? extends IEmailState> consume(OnTypeValidTransition transition) {
            vm.checkEmail(transition.getEmail());
            return TimerPendingState.class;
        }

        @Override
        public Class<? extends IEmailState> consume(OnTypeInvalidTransition transition) {
            boolean canceled = vm.cancelCheckEmailTimer();

            errorView.setText(errorMsg);
            errorView.setVisibility(VISIBLE);

            setValid(false);

            if (canceled) {
                return IdleState.class;
            }
            else {
                return TimerPendingState.class;
            }
        }

        @Override
        public Class<? extends IEmailState> consume(TimerExpiredTransition transition) {
            disable();

            return ResponsePendingState.class;
        }

        @Override
        public Class<? extends IEmailState> consume(ResponseTransition transition) {
            return null;
        }
    }

    private class ResponsePendingState implements IEmailState {

        @Override
        public Class<? extends IEmailState> consume(OnTypeValidTransition transition) {
            return null;
        }

        @Override
        public Class<? extends IEmailState> consume(OnTypeInvalidTransition transition) {
            return null;
        }

        @Override
        public Class<? extends IEmailState> consume(TimerExpiredTransition transition) {
            return null;
        }

        @Override
        public Class<? extends IEmailState> consume(ResponseTransition transition) {
            errorView.setVisibility(VISIBLE);
            switch (transition.getResponse()) {
                case DOES_NOT_EXIST:
                    errorView.setText("");
                    errorView.setVisibility(INVISIBLE);
                    setValid(true);
                    break;
                case EXISTS:
                    errorView.setText(emailExistsMsg);
                    setValid(false);
                    break;
                case TIMEOUT_EXPIRED:
                    errorView.setText("Server overloaded or something else happened");
                    setValid(false);
                    break;
                case SERVER_ERROR:
                    errorView.setText("Server error_icon");
                    setValid(false);
                    break;
                default:
                    break;
            }
            enable();

            return IdleState.class;
        }
    }

    public EmailValidatableEditText(Context context) {
        super(context);
    }

    public EmailValidatableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmailValidatableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(IValidateEditText callback, TextView errorView, String errorMsg,
                     String emailExistsMsg, ProgressBar spinner, RegistrationViewModel vm,
                     RegistrationFragment activity) {
        this.callback = callback;
        this.errorView = errorView;
        this.errorMsg = errorMsg;
        this.spinner = spinner;
        this.emailExistsMsg = emailExistsMsg;
        this.vm = vm;

        vm.checkEmailLiveData().observe(activity, r -> {
            fsm.advance(new ResponseTransition(r.getData()));
        });
        
        vm.timerLiveData().observe(activity, expired -> {
            if (expired) {
                fsm.advance(new TimerExpiredTransition());
            }
        });

        ArrayList<IEmailState> states = new ArrayList<IEmailState>() {{
            add(new IdleState());
            add(new TimerPendingState());
            add(new ResponsePendingState());
        }};
        fsm = new Fsm<>(INITIAL_STATE, states);

        initialized = true;
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace
            + ADDITIONAL_STATE_CNT);

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
            wasUnfocused = true;
        }

        if (isEnabled() && wasValid && !checkedAfterUnfocused) {
            validate(typedText);
            checkedAfterUnfocused = true;
        }
    }

    private void validate(String text) {
        if (callback.validate(text)) {
            fsm.advance(new OnTypeValidTransition(text));
        } else {
            fsm.advance(new OnTypeInvalidTransition());
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

        if (valid) {
            wasValid = true;
        }

        isValid = valid;
        refreshDrawableState();

        for(IValidatableSubscriber subscriber  : subscribers){
            subscriber.notify(this);
        }
    }

    private void disable() {
        spinner.setVisibility(VISIBLE);
        setEnabled(false);
        setAlpha(0.2f);
    }

    private void enable() {
        spinner.setVisibility(INVISIBLE);
        setEnabled(true);
        setAlpha(1);
    }
}
