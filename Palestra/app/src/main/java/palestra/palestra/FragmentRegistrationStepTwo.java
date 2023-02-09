package palestra.palestra;

import android.app.Fragment;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

/**
 * Created by Dmitry on 29.03.2017.
 */

public class FragmentRegistrationStepTwo extends Fragment {
    private EditText editTextPhoneNumber;
    private ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_registration_step_two, null);
        editTextPhoneNumber = (EditText)v.findViewById(R.id.user_phone_registration);
        progressBar = (ProgressBar)v.findViewById(R.id.progressBarPhoneCheck);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.hint), PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.INVISIBLE);

        editTextPhoneNumber.setText("+7");//TODO:доработать +7
//        editTextPhoneNumber.addTextChangedListener(new TextWatcher(){
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                    return;
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//        });
        return v;
    }
    public void setVisibilityProgress(boolean visibility){
        if(visibility)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.INVISIBLE);
    }
    public String getUserPhone(){
        return editTextPhoneNumber.getText().toString();
    }
}
