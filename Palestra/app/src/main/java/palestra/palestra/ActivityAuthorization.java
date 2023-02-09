package palestra.palestra;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.ksoap2.serialization.SoapObject;

public class ActivityAuthorization extends AppCompatActivity {

    private EditText editTextUserName;
    private EditText editTextPassword;
    private TextView tvRegistration;
    private TextView tvPostText;
    private ImageView buttonSingIn;
    private RelativeLayout content;

    private ConnectionToWCF connectionToWCF;
    private GetConnection getConnection;
    private CheckRegistrationUser checkRegistrationUser;
    private WorkingWithJSON workingWithJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow();
//            //w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//           // w.addFlags(WindowManager.ayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
//        else {
//            //TODO: сделать градиент, для версий API ниже 19!!!
//        }
        getWindow().setStatusBarColor(getResources().getColor(R.color.StatusBarAuthorization));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        getConnection = GetConnection.getInstance();
        connectionToWCF = ConnectionToWCF.getInstance();

        editTextUserName = (EditText)findViewById(R.id.user_name);
        editTextPassword = (EditText)findViewById(R.id.user_password);
        tvRegistration =(TextView) findViewById(R.id.tv_registration);
        tvPostText = (TextView)findViewById(R.id.post_text);
        buttonSingIn = (ImageView)findViewById(R.id.button_login);
        content = (RelativeLayout)findViewById(R.id.content);

       // content.setAlpha(0);
       // textInputLayoutUserName.setAlpha(0);
        //textInputLayoutPassword.setAlpha(0);
       // buttonSingIn.setAlpha(0);

        //Background Blur effect
//        Bitmap blurredBitmap = Bluring.blur( ActivityAuthorization.this,
//                BitmapFactory.decodeResource(this.getResources(), R.drawable.background) );
//        content.setBackground( new BitmapDrawable( getResources(), blurredBitmap ) );

        //animating interface
       // AnimatedAlpha(content,100,1200,0.5f);
       // AnimatedAlpha(textInputLayoutUserName,500, 400, 0.5f);
       // AnimatedAlpha(textInputLayoutPassword,900, 400, 0.5f);
       // AnimatedAlpha(buttonSingIn,1300, 400, 0.5f);

        tvRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAuthorization.this, ActivityRegistration.class);
                startActivity(intent);
            }
        });
    }

    public void onClickAuthorizationActivity(View button){
        getConnection = GetConnection.getInstance();
        //TODO: добавить проверки на правильность ввода информации
        String phoneNumber = CheckCorrectLogin(editTextUserName.getText().toString(), editTextPassword.getText().toString());
        if(phoneNumber != null) {
            if(getConnection.isOnline(this,content)) {
                checkRegistrationUser = new CheckRegistrationUser();
                checkRegistrationUser.execute(phoneNumber, editTextPassword.getText().toString());

               // GetAllCategores getAllCategores = new GetAllCategores();
               // getAllCategores.execute();
            }
        }
        else {
            Snackbar.make(content,"Проверьте введенную информацию",1000).show();
        }
    }

    class GetAllCategores extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            SoapTask request = new SoapTask(connectionToWCF.NAMESPACE, connectionToWCF.METHOD_SELECT_ALL_CATEGORES);
            SoapObject resultsRequestSOAP =
                    request.startSoap(connectionToWCF.URL, connectionToWCF.SOAP_ACTION(connectionToWCF.METHOD_SELECT_ALL_CATEGORES));

            if (resultsRequestSOAP != null) {
                if(resultsRequestSOAP.getProperty(0) != null){
                    String response = resultsRequestSOAP.getProperty(0).toString();
                    workingWithJSON = new WorkingWithJSON(response);
                    workingWithJSON.setEventsCategory();
                }
            }

            return null;
        }
    }



    private String CheckCorrectLogin(String phoneNumber, String password){
        if(phoneNumber.equals("") || password.equals(""))
            return null;
        if(!phoneNumber.startsWith("+7"))
            phoneNumber = "+7".concat(phoneNumber.substring(1));
        if(phoneNumber.length() != 12)
            return null;
        if(password.length() < 1)
            return null;

        return phoneNumber;
    }

//    private void cancelTask() {
//        if (checkRegistrationUser == null) return;
//        checkRegistrationUser.cancel(false);
//    }

    class CheckRegistrationUser extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvPostText.setText("Подождите...");
            //userName.setText("Начинаю искать...");
        }

        protected Boolean doInBackground(String... loginData) {
            SoapTask request = new SoapTask(connectionToWCF.NAMESPACE, connectionToWCF.METHOD_LOGIN);
            request.AddProperty("phoneNumber",new String(loginData[0]));
            request.AddProperty("password",new String(loginData[1]));
            SoapObject resultsRequestSOAP =
                    request.startSoap(connectionToWCF.URL,connectionToWCF.SOAP_ACTION(connectionToWCF.METHOD_LOGIN));

            Boolean flag = false;
            if (resultsRequestSOAP != null) {
                if(resultsRequestSOAP.getProperty(0) != null){
                    String response = resultsRequestSOAP.getProperty(0).toString();
                    workingWithJSON = new WorkingWithJSON(response);
                    workingWithJSON.setUserInformation();
                    flag = true;
                }
            }
            return flag;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result){
                Intent intent = new Intent(ActivityAuthorization.this, ActivityMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else {
                tvPostText.setText(R.string.login);
                Snackbar.make(content,"Пользователь не найден",1000).show();
            }
        }
    }

    /*public void onClickRelativeLayout(View view)
    {
        view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }

        *//*if(!view.hasFocusable()) {
            //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            view.setFocusable(true);
        }*//*
    }*/

    private void AnimatedAlpha(View view, int timeStartDelay, int timeDuration, float timeInterpolation){
        ViewCompat.animate(view)
                .alpha(1)
                .setStartDelay(timeStartDelay)
                .setDuration(timeDuration).setInterpolator(
                new DecelerateInterpolator(timeInterpolation)).start();
    }
    @Override
    protected void onStart() {
        super.onStart();

    }
}
