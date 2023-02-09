package palestra.palestra;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

/**
 * Created by Dmitry on 29.03.2017.
 */

public class FragmentRegistrationStepCity extends Fragment {

    private AutoCompleteTextView autoCompleteTextView;
    private static String TAG = FragmentRegistrationStepCity.class.getSimpleName();

    private boolean clickOnAutoComplete = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_registration_step_city, null);

        autoCompleteTextView = (AutoCompleteTextView) v.findViewById(R.id.autocompleteText);
        autoCompleteTextView.setAdapter(
                new PlacesAutoCompleteAdapter(getActivity(), R.layout.auto_complete_list_item));

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get data associated with the specified position
                // in the list (AdapterView)
                clickOnAutoComplete = true;

                String description = (String) parent.getItemAtPosition(position);
                //Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
            }
        });
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clickOnAutoComplete = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                final String value = s.toString();
//
//                // Remove all callbacks and messages
//                mThreadHandler.removeCallbacksAndMessages(null);
//
//                // Now add a new one
//                mThreadHandler.postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        // Background thread
//
//                        mAdapter.resultList = mAdapter.mPlaceAPI.autocomplete(value);
//
//                        // Footer
//                        if (mAdapter.resultList.size() > 0)
//                            mAdapter.resultList.add("footer");
//
//                        // Post to Main Thread
//                        mThreadHandler.sendEmptyMessage(1);
//                    }
//                }, 500);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                doAfterTextChanged();
//            }
//        });

        return v;
    }

    public String getCity() {
        return autoCompleteTextView.getText().toString();
    }

    public boolean getClickOnAuto() {
        return clickOnAutoComplete;
    }
}
