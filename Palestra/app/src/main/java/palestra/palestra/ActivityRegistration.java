package palestra.palestra;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.ksoap2.serialization.SoapObject;

import java.util.Random;

/**
 * Created by Dmitry on 28.03.2017.
 */

public class ActivityRegistration extends Activity {

    private static String FRAGMENT_REGISTRATION_ONE_NAME = "fragment_registration_one";

    private FragmentTransaction transaction;
    private FragmentRegistrationStepOne fragmentsRegistrationStepOne = null;
    private FragmentRegistrationStepTwo fragmentsRegistrationStepTwo = null;
    private FragmentRegistrationStepThree fragmentsRegistrationStepThree = null;
    private FragmentRegistrationStepFour fragmentsRegistrationStepFour = null;
    private FragmentRegistrationStepCity fragmentRegistrationStepCity = null;
    private FragmentRegistrationStepFive fragmentsRegistrationStepFive;

    private TextView buttonRules;
    private TextView textViewNextButton;
    //private ImageView buttonBack;
    private ImageView buttonNextStepRegistration;
    private RelativeLayout content_registration;

    private UserInformation userInformation;
    private WorkingWithJSON workingWithJSON;
    private WorkWithDate workWithDate;
    private GetConnection getConnection;

//    private EditText editTextFirstName;
//    private EditText editTextLastName;
//    private EditText editTextPhoneNumber;
//    private EditText editTextAge;
//    private EditText editTextFirstPassword;
//    private EditText editTextRepeatPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow();
//            //w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//            // w.addFlags(WindowManager.ayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        } else {
//            //TODO: ?????????????? ????????????????, ?????? ???????????? API ???????? 19!!!
//        }
        getWindow().setStatusBarColor(getResources().getColor(R.color.StatusBarAuthorization));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        userInformation = UserInformation.getInstance();
        getConnection = GetConnection.getInstance();

        textViewNextButton = (TextView) findViewById(R.id.text_view_next_button);
        buttonRules = (TextView) findViewById(R.id.button_rules);
       // buttonBack = (ImageView) findViewById(R.id.button_back);
        buttonNextStepRegistration =
                (ImageView) findViewById(R.id.button_next_step_registration);

        content_registration = (RelativeLayout)findViewById(R.id.content_registration);

        fragmentsRegistrationStepOne = new FragmentRegistrationStepOne();
        fragmentsRegistrationStepTwo = new FragmentRegistrationStepTwo();
        fragmentsRegistrationStepThree = new FragmentRegistrationStepThree();
        fragmentsRegistrationStepFour = new FragmentRegistrationStepFour();
        fragmentRegistrationStepCity = new FragmentRegistrationStepCity();
        //fragmentsRegistrationStepFive = new FragmentRegistrationStepFive();

        transaction = getFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.contentRegisterStep, fragmentsRegistrationStepOne, "fragmentsRegistrationStepOne");
        transaction.addToBackStack(null);
        transaction.commit();

        buttonRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(content_registration,"?????????? ??????????, ?????? ?????????????????????? ????????????",1000).show();
            }
        });

//        buttonBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeFragments(true);
//            }
//        });

        buttonNextStepRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragments(false);
            }
        });
    }

    //        @Override//TODO:?????????? ?????????????????? ?? ???????????? ????????????????????! ???? ?????????????????? ??????
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        if(keyCode == KeyEvent.KEYCODE_BACK){
//           changeFragments(true);
//            return true;
//        }
//        return false;
//    }
    @Override
    public void onBackPressed() {
        if (fragmentsRegistrationStepOne.isVisible()) {
            Intent intent = new Intent(ActivityRegistration.this, ActivityAuthorization.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        super.onBackPressed();
        textViewNextButton.setText(R.string.registration_next);
    }

    private void changeFragments(boolean backKey) {
        if (backKey) {
            transaction = getFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

            if (fragmentsRegistrationStepOne.isVisible()) {
                Intent intent = new Intent(ActivityRegistration.this, ActivityAuthorization.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
//            else if (fragmentsRegistrationStepTwo.isVisible()) {
//                fragmentsRegistrationStepOne = (FragmentRegistrationStepOne) getFragmentManager().findFragmentByTag("fragmentsRegistrationStepOne");
//                // ???????? ???????????????? ???? ????????????????, ?????????????? ?????????? ??????????????????
//                if(fragmentsRegistrationStepOne == null){
//                    fragmentsRegistrationStepOne = new FragmentRegistrationStepOne();
//                }
//                //transaction.replace(R.id.contentRegisterStep, fragmentsRegistrationStepOne);
//                //transaction.commit();
//            } else if (fragmentsRegistrationStepThree.isVisible()) {
//                transaction.replace(R.id.contentRegisterStep, fragmentsRegistrationStepTwo);
//                transaction.commit();
//            } else if (fragmentsRegistrationStepFour.isVisible()) {
//                transaction.replace(R.id.contentRegisterStep, fragmentsRegistrationStepThree);
//                transaction.commit();
//            }
//            else if (fragmentsRegistrationStepFive.isVisible()) {
//                transaction.replace(R.id.contentRegisterStep, fragmentsRegistrationStepFour);
//                transaction.commit();
//            }
        } else {
            transaction = getFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

            String message;

            if (fragmentsRegistrationStepOne.isVisible()) {
                userInformation.setUserLastName(fragmentsRegistrationStepOne.getLastUserName());
                userInformation.setUserFirstName(fragmentsRegistrationStepOne.getFirstUserName());
                message = CheckInputUserName();

                if (message == null) {
                    transaction.replace(R.id.contentRegisterStep, fragmentsRegistrationStepTwo, "fragmentsRegistrationStepTwo");
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else
                    Snackbar.make(content_registration,message,1000).show();

            } else if (fragmentsRegistrationStepTwo.isVisible()) {
                userInformation.setPhoneNumber(fragmentsRegistrationStepTwo.getUserPhone());

                message = CheckCorrectPhoneNumber();
                if (message == null) {
                    CheckRegistration checkRegistration = new CheckRegistration();
                    checkRegistration.execute(fragmentsRegistrationStepTwo.getUserPhone());

//                        transaction.replace(R.id.contentRegisterStep, fragmentsRegistrationStepThree, "fragmentsRegistrationStepThree");
//                        transaction.addToBackStack(null);
//                        transaction.commit();


                } else
                    Snackbar.make(content_registration, message, 1000).show();
            }

            else if (fragmentsRegistrationStepThree.isVisible()) {
                userInformation.setBirthday(fragmentsRegistrationStepThree.getBirthday());
                message = CheckCorrectBirthday();

                if (message == null) {
                    transaction.replace(R.id.contentRegisterStep, fragmentRegistrationStepCity, "fragmentsRegistrationStepCity");
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else
                    Snackbar.make(content_registration,message,1000).show();

            } else if (fragmentRegistrationStepCity.isVisible()) {
                if (fragmentRegistrationStepCity.getClickOnAuto()) {
                    userInformation.setCity(fragmentRegistrationStepCity.getCity());
                    message = CheckCorrectCity();

                    if (message == null) {
                        textViewNextButton.setText(R.string.registration_yes);
                        transaction.replace(R.id.contentRegisterStep, fragmentsRegistrationStepFour, "fragmentsRegistrationStepFour");
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else
                        Snackbar.make(content_registration, message, 1000).show();
                } else {
                    Snackbar.make(content_registration, "???????????????????? ?????????????? ?????????? ???? ???????????????????????? ??????????????????", 1000).show();
                }
            }else if (fragmentsRegistrationStepFour.isVisible()) {
                //if (fragmentsRegistrationStepFour.CheckPassword()) {

                    message = CheckCorrectPassword();

                    if(message == null){
                        if(getConnection.isOnline(this, content_registration)) {
                            userInformation.setPassword(fragmentsRegistrationStepFour.getPassword());
                            workWithDate = new WorkWithDate();

                            int userAge = workWithDate.getUserAge(userInformation.getBirthday());
                            String userBirthdaySQLFormat = workWithDate.getBirthdaySQLFormat(userInformation.getBirthday());

                            Random random = new Random();

                            int avatarID = random.nextInt(7) + 1;

                            PostRegistration postRegistration = new PostRegistration();
                            postRegistration.execute(userInformation.getPhoneNumber()
                                    , userInformation.getPassword()
                                    , userInformation.getUserFirstName()
                                    , userInformation.getUserLastName()
                                    , userBirthdaySQLFormat
                                    , avatarID
                                    , userInformation.getCity());

                            userInformation.setAge(userAge);
                            userInformation.setUserCategory(3);

                            userInformation.setAvatarID(avatarID);

                            Intent intent = new Intent(ActivityRegistration.this, ActivityMain.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }
                    else
                        Snackbar.make(content_registration,message,1000).show();
               // }
            }
        }
    }

    class PostRegistration extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... userInfo) {
            ConnectionToWCF connectionToWCF = ConnectionToWCF.getInstance();

            SoapTask request = new SoapTask(connectionToWCF.NAMESPACE, connectionToWCF.METHOD_REGISTRATION_USER);

            request.AddProperty("phoneNumber", userInfo[0].toString());
            request.AddProperty("password", userInfo[1].toString());
            request.AddProperty("firstName", userInfo[2].toString());
            request.AddProperty("lastName", userInfo[3].toString());
            request.AddProperty("birthday", userInfo[4].toString());
            request.AddProperty("avatarID", userInfo[5].toString());
            request.AddProperty("city", userInfo[6].toString());
//            SoapObject resultRequest =
            //request.startSoap(connectionToWCF.URL,connectionToWCF.SOAP_ACTION(connectionToWCF.METHOD_REGISTRATION_USER));

            SoapObject resultsRequestSOAP =
                    request.startSoap(connectionToWCF.URL, connectionToWCF.SOAP_ACTION(connectionToWCF.METHOD_REGISTRATION_USER));

            if (resultsRequestSOAP != null) {
                if (resultsRequestSOAP.getProperty(0) != null) {
                    String response = resultsRequestSOAP.getProperty(0).toString();
                    workingWithJSON = new WorkingWithJSON(response);
                    workingWithJSON.setUserInformation();
                }
            }
            return null;
        }
    }

    class CheckRegistration extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fragmentsRegistrationStepTwo.setVisibilityProgress(true);
        }

        @Override
        protected Boolean doInBackground(String... userInfo) {
            ConnectionToWCF connectionToWCF = ConnectionToWCF.getInstance();

            SoapTask request = new SoapTask(connectionToWCF.NAMESPACE, connectionToWCF.METHOD_CHECK_REGISTER);

            request.AddProperty("phoneNumber", userInfo[0].toString());

            SoapObject resultsRequestSOAP =
                    request.startSoap(connectionToWCF.URL, connectionToWCF.SOAP_ACTION(connectionToWCF.METHOD_CHECK_REGISTER));

            if (resultsRequestSOAP != null) {
                if (resultsRequestSOAP.getProperty(0) != null) {
                    String response = resultsRequestSOAP.getProperty(0).toString();
                    workingWithJSON = new WorkingWithJSON(response);
                    return workingWithJSON.getCheckResister();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean userInfo) {
            super.onPostExecute(userInfo);
            fragmentsRegistrationStepTwo.setVisibilityProgress(false);
            if(userInfo == null){
                //?????? ??????????
                transaction.replace(R.id.contentRegisterStep, fragmentsRegistrationStepThree, "fragmentsRegistrationStepThree");
                transaction.addToBackStack(null);
                transaction.commit();
            } else {
                Snackbar.make(content_registration,"?????????? ?????? ?????????????????????????????? ?? ??????????????",1000).show();
            }
        }
    }

    private String CheckInputUserName() {
        if (userInformation.getUserFirstName().equals(""))
            return "?????????????? ?????? ????????????????????????";
        else if (userInformation.getUserLastName().equals(""))
            return "?????????????? ?????????????? ????????????????????????";
        return null;
    }

    private String CheckCorrectPhoneNumber() {
        if (userInformation.getPhoneNumber().equals(""))
            return "?????????????? ?????????? ????????????????";
        if (!userInformation.getPhoneNumber().startsWith("+7"))
            userInformation.setPhoneNumber("+7".concat(userInformation.getPhoneNumber().substring(1)));
        if (userInformation.getPhoneNumber().length() != 12)
            return "?????????????????? ???????????????????????? ?????????????????????? ????????????";
        return null;
    }

    private String CheckCorrectBirthday() {
        if (userInformation.getBirthday().equals("")) {
            return "?????????????? ???????? ????????????????";
        } else if (userInformation.getBirthday().equals("??????????????...")) {
            return "?????????????? ???????? ????????????????";
        }
        return null;
    }

    private String CheckCorrectCity() {
        if (userInformation.getCity().equals("")) {
            return "?????????????? ??????????";
        } else if (userInformation.getBirthday().equals("???????????????? ????????????")) {
            return "?????????????? ??????????";
        }
        return null;
    }

    private String CheckCorrectPassword() {
        if(fragmentsRegistrationStepFour.getPassword().equals(""))
            return "?????????????? ????????????";
        if(fragmentsRegistrationStepFour.getPasswordRepeat().equals(""))
            return "?????????????????? ????????????";
        if (!fragmentsRegistrationStepFour.CheckPassword())
            return "???????????? ???? ??????????????????";
        return null;
    }
}


