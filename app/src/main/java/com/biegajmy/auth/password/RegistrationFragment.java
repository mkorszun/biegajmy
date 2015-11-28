package com.biegajmy.auth.password;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.biegajmy.R;
import com.biegajmy.model.NewUser;
import com.biegajmy.user.UserBackendService_;
import com.biegajmy.user.UserEventBus;
import com.biegajmy.validators.PasswordValidator;
import com.biegajmy.validators.TextFormValidator;
import com.squareup.otto.Subscribe;
import java.util.HashMap;
import java.util.Map;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

@EFragment(R.layout.fragment_registration_password) public class RegistrationFragment extends Fragment {

    private Map<TextView, Integer> fields;
    @Bean protected TextFormValidator formValidator;
    @Bean protected PasswordValidator passwordValidator;

    @ViewById(R.id.email) protected EditText email;
    @ViewById(R.id.password) protected EditText password1;
    @ViewById(R.id.repassword) protected EditText password2;

    @StringRes(R.string.email_already_used) protected String EMAIL_ALREADY_USED;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserEventBus.getInstance().register(this);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        UserEventBus.getInstance().unregister(this);
    }

    @AfterViews public void setup() {
        fields = fields();
    }

    @Click(R.id.register_button) public void register() {
        if (formValidator.validate(fields) && passwordValidator.validate(password1, password2)) {
            NewUser user = new NewUser();
            user.email = email.getText().toString();
            user.password = password1.getText().toString();
            UserBackendService_.intent(getActivity()).register(user).start();
        }
    }

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @Subscribe public void event(UserEventBus.RegistrationOK event) {
        new MaterialDialog.Builder(getActivity()).content(R.string.registration_finish)
            .positiveText(R.string.ok)
            .show();
        getActivity().onBackPressed();
    }

    @Subscribe public void event(UserEventBus.RegistrationNOK event) {
        Toast.makeText(getActivity(), R.string.registration_failed, Toast.LENGTH_LONG).show();
        switch (event.reason) {
            case USER_EXISTS:
                email.setError(EMAIL_ALREADY_USED);
                break;
        }
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private Map<TextView, Integer> fields() {
        Map<TextView, Integer> fields = new HashMap<>();
        fields.put(email, R.string.field_empty_error);
        fields.put(password1, R.string.field_empty_error);
        fields.put(password2, R.string.field_empty_error);
        return fields;
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
