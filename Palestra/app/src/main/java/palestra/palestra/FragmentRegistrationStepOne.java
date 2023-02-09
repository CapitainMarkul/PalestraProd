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

public class FragmentRegistrationStepOne extends Fragment {
    private EditText editTextFirstName;
    private EditText editTextLastName;

    View view = null;

    public FragmentRegistrationStepOne(){
        this.setRetainInstance(true);
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
    {

// Проверяем, создано ли представление фрагмента
        if(view == null) {
            // Если представления нет, создаем его
            view = inflater.inflate(R.layout.fragment_registration_step_one, container, false);
            editTextFirstName = (EditText)view.findViewById(R.id.user_first_name_registration);
            editTextLastName = (EditText)view.findViewById(R.id.user_second_name_registration);
        }
        else {
            // Если представление есть, удаляем его из разметки,
            // иначе возникнет ошибка при его добавлении
            //((ViewGroup) view.getParent()).removeView(view);
        }
            //view = inflater.inflate(R.layout.fragment_registration_step_one, null);

        return view;
    }

    public Boolean CheckCorrectUserName(){
        if (editTextFirstName.getText().length() != 0 &&
                editTextLastName.getText().length()!= 0)
            return true;
        return false;
    }

    public String getFirstUserName(){
        return editTextFirstName.getText().toString();
    }
    public String getLastUserName(){
        return editTextLastName.getText().toString();
    }

//    public void setFirstUserName(String firstName){
//        editTextFirstName.setText(firstName);
//    }
//    public void setLastUserName(String lastName){
//        editTextLastName.setText(lastName);
//    }
}
