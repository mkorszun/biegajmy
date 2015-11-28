package com.biegajmy.auth.password;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.biegajmy.R;
import com.biegajmy.auth.LoginActivity;
import com.biegajmy.user.UserBackendService_;
import com.biegajmy.user.UserEventBus;
import com.biegajmy.validators.TextFormValidator;
import com.squareup.otto.Subscribe;
import java.util.HashMap;
import java.util.Map;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_login_password) public class LoginFragment extends Fragment {

    private Map<TextView, Integer> fields;
    @Bean protected TextFormValidator formValidator;
    @ViewById(R.id.email) protected EditText email;
    @ViewById(R.id.password) protected EditText password;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @AfterViews public void setup() {
        fields = fields();
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserEventBus.getInstance().register(this);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        UserEventBus.getInstance().unregister(this);
    }

    @Click(R.id.login_button) public void login() {
        if (formValidator.validate(fields)) {
            String user = email.getText().toString();
            String pass = password.getText().toString();
            UserBackendService_.intent(getActivity()).login(user, pass).start();
        }
    }

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @Subscribe public void event(UserEventBus.LoginOK event) {
        UserBackendService_.intent(getActivity()).syncUser().start();
        getActivity().setResult(LoginActivity.AUTH_OK);
        getActivity().finish();
    }

    @Subscribe public void event(UserEventBus.LoginNOK event) {
        Toast.makeText(getActivity(), R.string.login_failed, Toast.LENGTH_LONG).show();
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private Map<TextView, Integer> fields() {
        Map<TextView, Integer> fields = new HashMap<>();
        fields.put(email, R.string.field_empty_error);
        fields.put(password, R.string.field_empty_error);
        return fields;
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
