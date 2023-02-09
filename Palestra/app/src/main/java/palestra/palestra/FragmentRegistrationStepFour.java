package palestra.palestra;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by Dmitry on 29.03.2017.
 */

public class FragmentRegistrationStepFour extends Fragment {
    private EditText editTextPassword;
    private EditText editTextPasswordRepeat;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_registration_step_four, null);
        editTextPassword = (EditText)v.findViewById(R.id.user_first_password_registration);
        editTextPasswordRepeat = (EditText)v.findViewById((R.id.user_second_password_registration));
        return v;
    }

    public String getPassword(){
        return editTextPassword.getText().toString();
    }
    public String getPasswordRepeat(){return editTextPasswordRepeat.getText().toString();}


    public Boolean CheckPassword(){
        if (editTextPassword.getText().toString()
                .equals(editTextPasswordRepeat.getText().toString()))
            return true;
        return false;
    }
}
