package com.example.dobble.release.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.dobble.R;
import com.example.dobble.databinding.RegistrationFragmentBinding;
import com.example.dobble.release.App;
import com.example.dobble.release.dto.RxStatusDto;
import com.example.dobble.release.extensions.ui.DatePickerFragment;
import com.example.dobble.release.extensions.ui.IValidatable;
import com.example.dobble.release.extensions.ui.IValidatableSubscriber;
import com.example.dobble.release.extensions.ui.IValidateEditText;
import com.example.dobble.release.extensions.ui.ValidatableRadioGroup;
import com.example.dobble.release.repositories.local.AppDatabase;
import com.example.dobble.release.repositories.remote.payload.body.RegisterUserBody;
import com.example.dobble.release.repositories.remote.payload.response.CheckAuthResponse;
import com.example.dobble.release.utils.IEmailValidator;
import com.example.dobble.release.vm.InitialViewModel;
import com.example.dobble.release.vm.RegistrationViewModel;

import java.net.HttpURLConnection;
import java.util.Date;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.example.dobble.release.Cfg.PREF_USER;
import static com.example.dobble.release.Cfg.REGISTER_MIN_CITY_LENGTH;
import static com.example.dobble.release.Cfg.REGISTER_MIN_FIRST_NAME_LENGTH;
import static com.example.dobble.release.Cfg.REGISTER_MIN_LAST_NAME_LENGTH;
import static com.example.dobble.release.Cfg.REGISTER_MIN_PASSWORD_LENGTH;
import static com.example.dobble.release.Cfg.REGISTER_MIN_STATE_LENGTH;
import static com.example.dobble.release.Cfg.USER_ID_KEY;

public class RegistrationFragment extends Fragment {
    @Inject
    RegistrationViewModel registrationVm;
    @Inject
    InitialViewModel initialVm;
    @Inject
    AppDatabase db;
    @Inject
    IEmailValidator emailValidator;

    private RegistrationFragmentBinding binding;
    private DatePickerData datePickerData = new DatePickerData();
    private NavController navController;

    private class DatePickerData implements IValidatableSubscriber, IValidatable {
        int year;
        int month;
        int day;
        boolean touched = false;
        IValidatableSubscriber subscriber;

        @Override
        public void notify(IValidatable validatable) {
            if (!(validatable instanceof DatePickerFragment)) {
                return;
            }

            DatePickerFragment datePickerFragment = (DatePickerFragment) validatable;
            year = datePickerFragment.getYear();
            month = datePickerFragment.getMonth();
            day = datePickerFragment.getDay();
            touched = true;

            if (subscriber != null) {
                subscriber.notify(this);
            }
        }

        @Override
        public void removeValidatable(IValidatable validatable) {
            if (subscriber != null) {
                subscriber.removeValidatable(this);
            }
        }

        @Override
        public IValidatableSubscriber addValidatable(IValidatable validatable) {
            return null;
        }

        @Override
        public boolean isValid() {
            return touched;
        }

        @Override
        public void subscribe(IValidatableSubscriber subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public void unsubscribe(IValidatableSubscriber subscriber) {
            this.subscriber = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        App
            .getAppComponent()
            .fragmentSubComponentBuilder()
            .with(this)
            .build()
            .inject(this);

        binding = RegistrationFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        navController = Navigation.findNavController(getActivity(),
            R.id.nav_host_fragment_container);
        init();
    }

    private void showDatePickerDialog(View view) {
        DatePickerFragment newFragment;
        if (datePickerData.touched) {
            newFragment = new DatePickerFragment(datePickerData.year, datePickerData.month,
                datePickerData.day, true);
        }
        else {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            newFragment = new DatePickerFragment(year, month, day, false);
        }
        newFragment.subscribe(datePickerData);
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    private void onSubmitBtnClick(View view) {
        String email = binding.inputEmail.getText().toString();
        String password = binding.inputPassword.getText().toString();
        String firstName = binding.inputFirstName.getText().toString();
        String lastName = binding.inputLastName.getText().toString();
        String city = binding.inputCity.getText().toString();
        String state = binding.inputState.getText().toString();
        String gender = binding.radioGroup.getSelectedValue() == ValidatableRadioGroup.SelectedValue.MALE
            ? "0" : "1";
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, datePickerData.year);
        calendar.set(Calendar.MONTH, datePickerData.month);
        calendar.set(Calendar.DAY_OF_MONTH, datePickerData.day);
        Date date = calendar.getTime();
        String dob = date.toString();
        RegisterUserBody registerUserBody = new RegisterUserBody(email, password, firstName, lastName, city, state, gender,
            dob);

        registrationVm.registerUser(registerUserBody);
    }

    private void init() {
        binding.dobButton.setOnClickListener(this::showDatePickerDialog);
        binding.submitButton.setOnClickListener(this::onSubmitBtnClick);
        Resources r = getResources();

        IValidateEditText emailCallback = (String text) -> emailValidator.isValid(text);
        String invalidEmailMsg = r.getString(R.string.register_error_invalid_email);
        TextView emailErrorView = binding.emailError;
        String emailExistsMsg = r.getString(R.string.register_error_email_exists);
        binding.inputEmail.init(emailCallback, emailErrorView, invalidEmailMsg, emailExistsMsg,
            binding.progress, registrationVm, this);

        IValidateEditText passwordCallback = (String text) -> text.length() >= REGISTER_MIN_PASSWORD_LENGTH;
        String invalidPasswordMsg = r.getString(R.string.register_error_invalid_password);
        TextView passwordErrorView = binding.passwordError;
        binding.inputPassword.init(passwordCallback, passwordErrorView, invalidPasswordMsg);

        IValidateEditText firstNameCallback = (String text) -> text.length() >= REGISTER_MIN_FIRST_NAME_LENGTH;
        String invalidFirstNameMsg = r.getString(R.string.register_error_invalid_first_name);
        TextView firstNameErrorView = binding.firstNameError;
        binding.inputFirstName.init(firstNameCallback, firstNameErrorView, invalidFirstNameMsg );

        IValidateEditText lastNameCallback = (String text) ->  text.length() >= REGISTER_MIN_LAST_NAME_LENGTH;
        String invalidLastNameMsg = r.getString(R.string.register_error_invalid_last_name);
        TextView lastNameErrorView = binding.lastNameError;
        binding.inputLastName.init(lastNameCallback, lastNameErrorView, invalidLastNameMsg);

        IValidateEditText cityCallback = (String text) -> text.length() >= REGISTER_MIN_CITY_LENGTH;
        String invalidCityMsg = r.getString(R.string.register_error_invalid_city);
        TextView cityErrorView = binding.cityError;
        binding.inputCity.init(cityCallback, cityErrorView, invalidCityMsg);

        IValidateEditText stateCallback = (String text) -> text.length() >= REGISTER_MIN_STATE_LENGTH;
        String invalidStateMsg = r.getString(R.string.register_error_invalid_state);
        TextView stateErrorView = binding.stateError;
        binding.inputState.init(stateCallback, stateErrorView, invalidStateMsg);

        binding.submitButton.init();
        binding.submitButton
            .addValidatable(binding.inputEmail)
            .addValidatable(binding.inputPassword)
            .addValidatable(binding.inputFirstName)
            .addValidatable(binding.inputLastName)
            .addValidatable(binding.inputCity)
            .addValidatable(binding.inputState)
            .addValidatable(datePickerData)
            .addValidatable(binding.radioGroup);

        registrationVm.registerUserLiveData().observe(getViewLifecycleOwner(), this::registerUserUpdate);
        initialVm.checkAuthLiveData().observe(getViewLifecycleOwner(), this::checkAuthUpdate);
    }

    private void registerUserUpdate(RxStatusDto<Response<Void>> response) {
        if (!response.isSuccess()) {
            return;
        }

        if (response.getData().code() == HttpURLConnection.HTTP_CREATED) {
            Observable.just(db)
                .subscribeOn(Schedulers.io())
                .subscribe(db -> {
                    db.clearAllTables();
                });

            initialVm.checkAuth();
        }
        else {
            // TODO: Signals an error_icon
        }
    }

    private void checkAuthUpdate(RxStatusDto<Response<CheckAuthResponse>> response) {
        if (!response.isSuccess()) {
            return;
        }

        if (response.getData().code() == HttpURLConnection.HTTP_OK) {
            SharedPreferences.Editor editor = getActivity().getApplicationContext()
                .getSharedPreferences(PREF_USER, Context.MODE_PRIVATE).edit();
            long userId = response.getData().body().getId();
            editor.putString(USER_ID_KEY, String.valueOf(userId));
            editor.apply();

            App.setUserId(userId);

            navController.navigate(R.id.contentFragment);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        initialVm.releaseResources();
        registrationVm.releaseResources();
    }
}
