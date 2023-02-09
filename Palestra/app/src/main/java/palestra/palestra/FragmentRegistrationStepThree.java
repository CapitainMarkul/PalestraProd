package palestra.palestra;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Dmitry on 29.03.2017.
 */

public class FragmentRegistrationStepThree extends Fragment {

    private TextView textViewDateBirthday;
    Calendar dateAndTime= Calendar.getInstance();
    private View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_registration_step_three, null);
        textViewDateBirthday = (TextView) v.findViewById(R.id.center_layout);

        textViewDateBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(),
                        R.style.Datepicker, d,
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });
        return v;
    }

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            if(year > dateAndTime.get(Calendar.YEAR)){
                ErrorDate();
                return;
            } else {
                if(year == dateAndTime.get(Calendar.YEAR)) {
                    if (monthOfYear > dateAndTime.get(Calendar.MONTH)) {
                        ErrorDate();
                        return;
                    } else {
                        if(monthOfYear == dateAndTime.get(Calendar.MONTH)) {
                            if (dayOfMonth > dateAndTime.get(Calendar.DAY_OF_MONTH)) {
                                ErrorDate();
                                return;
                            }
                        }
                    }
                }
            }
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };
    private void ErrorDate(){
        RelativeLayout content_create_new_event = (RelativeLayout) getActivity().getWindow().findViewById(R.id.content_registration);
        Snackbar.make(content_create_new_event, "Неверная дата рождения", 1000).show();
    }
    // установка начальных даты и времени
    private void setInitialDateTime() {
        textViewDateBirthday.setText(DateUtils.formatDateTime(getActivity(),
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    public String getBirthday(){ return textViewDateBirthday.getText().toString(); }
}
