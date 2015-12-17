package com.biegajmy.auth.password;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.biegajmy.R;
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

@EFragment(R.layout.fragment_reset_password) public class ResetFragment extends Fragment {

    private Map<TextView, Integer> fields;
    @ViewById(R.id.email) protected EditText email;
    @Bean protected TextFormValidator formValidator;

    //********************************************************************************************//
    // Callbacks
    //********************************************************************************************//

    @AfterViews public void setup() {
        this.fields = fields();
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserEventBus.getInstance().register(this);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        UserEventBus.getInstance().unregister(this);
    }

    @Click(R.id.reset_button) public void reset() {
        if (formValidator.validate(fields)) {
            UserBackendService_.intent(getActivity()).resetPassword(email.getText().toString()).start();
        }
    }

    //********************************************************************************************//
    // Events
    //********************************************************************************************//

    @Subscribe public void event(UserEventBus.PasswordResetOK event) {
        Toast.makeText(getActivity(), R.string.password_reset_ok, Toast.LENGTH_LONG).show();
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Subscribe public void event(UserEventBus.PasswordResetNOK event) {
        switch (event.reason) {
            case USER_NOT_FOUND:
                Toast.makeText(getActivity(), R.string.password_reset_user_not_found_error, Toast.LENGTH_LONG).show();
                break;
            case UNKNOWN:
                Toast.makeText(getActivity(), R.string.login_unknown_error, Toast.LENGTH_LONG).show();
                break;
        }
    }

    //********************************************************************************************//
    // Helpers
    //********************************************************************************************//

    private Map<TextView, Integer> fields() {
        Map<TextView, Integer> fields = new HashMap<>();
        fields.put(email, R.string.field_empty_error);
        return fields;
    }

    //********************************************************************************************//
    //********************************************************************************************//
}
