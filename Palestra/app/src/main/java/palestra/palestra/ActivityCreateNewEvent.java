package palestra.palestra;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

/**
 * Created by Dmitry on 28.03.2017.
 */

public class ActivityCreateNewEvent extends AppCompatActivity implements
        OnMapReadyCallback,
        //GoogleMap.InfoWindowAdapter,
        GoogleMap.OnCameraMoveListener{

    private EditText et_event_name;
    private EditText et_event_brief;
    private EditText et_age_limit;
    private Spinner spinner_category;

    private RelativeLayout content_create_new_event;

    private TextView tv_set_date;
    private TextView tv_create_event;
    private TextView tv_geo_message;
    private TextView tv_user_name;
    private TextView place;

    private ImageView bt_create_event;
    private ImageView bt_back;

    private String address = "";
    private boolean currentDay = false;

    private GoogleMap googleMap;
    private Marker marker;

    Calendar dateAndTime= Calendar.getInstance();

    private ConnectionToWCF connectionToWCF;
    private UserInformation userInformation;
    private HttpGetAddress httpGetAddress;
    private GetConnection getConnection;
    private EventCategores eventCategores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow();
//           // w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//            // w.addFlags(WindowManager.ayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        getWindow().setStatusBarColor(getResources().getColor(R.color.blue_dark));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event);

        content_create_new_event = (RelativeLayout)findViewById(R.id.content_create_new_event);

//Create Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        et_event_name = (EditText)findViewById(R.id.et_event_name);
        et_event_brief = (EditText)findViewById(R.id.et_event_description);
        tv_set_date = (TextView)findViewById(R.id.tv_set_date);
        tv_create_event = (TextView)findViewById(R.id.tv_create_event);
        tv_geo_message = (TextView)findViewById(R.id.tv_geo_message);
        et_age_limit = (EditText)findViewById(R.id.et_age_limit);
        spinner_category = (Spinner)findViewById(R.id.spinner_category);
        bt_create_event = (ImageView)findViewById(R.id.button_create_event);
        bt_back = (ImageView)findViewById(R.id.button_back_create_event);
        spinner_category = (Spinner)findViewById(R.id.spinner_category);

       // tv_user_name = (TextView) findViewById(R.id.info_window_user_name);
       // place = (TextView) findViewById(R.id.info_window_place);

        setInitialDateTime();

        // адаптер

        String[] arrayList = getResources().getStringArray(R.array.category);
        Arrays.sort(arrayList);
       // eventCategores = EventCategores.getInstance();
//        String[] arrayList = eventCategores.getString();

        ArrayAdapter<String> adapterForSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_category.setAdapter(adapterForSpinner); //TODO:добавить обработчик нажатий

        userInformation = UserInformation.getInstance();
        httpGetAddress = HttpGetAddress.getInstance();
        getConnection = GetConnection.getInstance();

        tv_set_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setInitialDateTime();
                setDate(tv_set_date);
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bt_create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getConnection.isOnline(getBaseContext(), content_create_new_event)) {
                    if (CheckInfo()) {
                        CreateEvent createEvent = new CreateEvent();
                        createEvent.execute(
                                et_event_name.getText().toString(),
                                et_event_brief.getText().toString(),
                                tv_set_date.getText().toString(),
                                et_age_limit.getText().toString(),
                                String.valueOf(getIdCategory(spinner_category.getSelectedItem().toString())),
                                String.valueOf(userInformation.getUserID()),
                                String.valueOf(marker.getPosition().latitude),
                                String.valueOf(marker.getPosition().longitude));
                        try {
                            createEvent.get();

                            finish();
                        } catch (InterruptedException e) {
                            Snackbar.make(content_create_new_event, "Повторите попытку", 1000).show();
                            return;
                        } catch (ExecutionException e) {
                            Snackbar.make(content_create_new_event, "Повторите попытку", 1000).show();
                            return;
                        }
                    } else {
                        Snackbar.make(content_create_new_event, "Проверьте введенную информацию", 1000).show();
                        tv_create_event.setText("Создать событие  +");
                    }
                }
            }
        });
    }

    private int getIdCategory(String nameCategory){
        switch (nameCategory){
            case "Искусство":{
                return 2;
            }
            case "Кулинария":{
                return 3;
            }
            case "Литература":{
                return 4;
            }
            case "Развлечение":{
                return 5;
            }
            case "Спорт":{
                return 6;
            }
            case "Танцы":{
                return 7;
            }
            case "Мастеркласс":{
                return 8;
            }
            case "Фокусы":{
                return 9;
            }
            case "Автомобили":{
                return 10;
            }
            case "Праздники":{
                return 11;
            }
            case "Путешествия":{
                return 12;
            }
            case "Книги":{
                return 13;
            }
            case "Образование":{
                return 14;
            }
            case "Знакомства":{
                return 15;
            }
            case "Выставка":{
                return 16;
            }
            case "Помощь":{
                return 17;
            }
            case "Праздник":{
                return 18;
            }
        }
        return 2;
    }

    private String DateRel(String date){
        String day;
        String month;
        String year;
        String hour;

        int index = date.indexOf(' ');
        day = date.substring(0, index);

        date = date.substring(index+1);
        month = CheckMonth(date.substring(0, date.indexOf(' ')));

        index = date.indexOf(' ');
        date = date.substring(index+1);
        year = date.substring(0, date.indexOf(' '));

        index = date.indexOf(' ');
        date = date.substring(index+1);
        index = date.indexOf(' ');
        date = date.substring(index+1);
        hour = date;
        //"2011.01.12 14:34:00"
        date = year+"."+month+"."+day+" "+hour+":00";

        return  date;
    }

    private String CheckMonth(String month){
        switch (month){
            case "января":{
                return "01";
            }
            case "февраля":{
                return "02";
            }
            case "марта":{
                return "03";
            }
            case "апреля":{
                return "04";
            }
            case "мая":{
                return "05";
            }
            case "июня":{
                return "06";
            }
            case "июля":{
                return "07";
            }
            case "августа":{
                return "08";
            }
            case "сентября":{
                return "09";
            }
            case "октября":{
                return "10";
            }
            case "ноября":{
                return "11";
            }
            case "декабря":{
                return "12";
            }
            default:{
                return "01";
            }
        }
    }

    class CreateEvent extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tv_create_event.setText("Подождите...");
            //userName.setText("Начинаю искать...");
        }

        @Override
        protected Void doInBackground(String... eventsInfo) {
            connectionToWCF = ConnectionToWCF.getInstance();

            SoapTask request = new SoapTask(connectionToWCF.NAMESPACE, connectionToWCF.METHOD_ADD_NEW_EVENT);
            request.AddProperty("title",new String(eventsInfo[0]));
            request.AddProperty("briefInfo",new String(eventsInfo[1]));
            request.AddProperty("dateOf", new String(DateRel(eventsInfo[2])));
            request.AddProperty("ageLimit", Integer.parseInt(eventsInfo[3]));
            request.AddProperty("eventCategory", Integer.parseInt(eventsInfo[4]));
            request.AddProperty("idCreator", Integer.parseInt(eventsInfo[5]));
            request.AddProperty("latitude", new String(eventsInfo[6]));
            request.AddProperty("longitude", new String(eventsInfo[7]));

           // String s = request.toString();
            try {
                request.startSoap(connectionToWCF.URL, connectionToWCF.SOAP_ACTION(connectionToWCF.METHOD_ADD_NEW_EVENT));
            }
            catch (Exception e)
            {
               // Log.e("e",e.toString());
            }
                return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            tv_create_event.setText("Успешно...");
        }
    }

    // отображаем диалоговое окно для выбора даты
    public void setDate(View v) {
        new DatePickerDialog(ActivityCreateNewEvent.this,
                R.style.Datepicker, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // отображаем диалоговое окно для выбора времени
    public void setTime(View v) {
        new TimePickerDialog(ActivityCreateNewEvent.this,
                R.style.Datepicker, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }
    // установка начальных даты и времени
    private void setInitialDateTime() {
        tv_set_date.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_SHOW_TIME));
    }

    // установка обработчика выбора времени
    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar c = Calendar.getInstance();
            int nowHour = c.get(Calendar.HOUR_OF_DAY);
            int nowMinute = c.get(Calendar.MINUTE);

            if(currentDay) {
                if (hourOfDay < nowHour) {
                    ErrorDate();
                    setDefaultDate();
                    return;
                } else if (hourOfDay == nowHour) {
                    if (minute < nowMinute) {
                        ErrorDate();
                        setDefaultDate();
                        return;
                    }
                }
            }
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };

    private void setDefaultDate(){
        Calendar c = Calendar.getInstance();
        tv_set_date.setText(DateUtils.formatDateTime(this,
                c.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_SHOW_TIME));
    }


    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar c = Calendar.getInstance();
            int nowDay = c.get(Calendar.DAY_OF_MONTH);
            int nowMonth = c.get(Calendar.MONTH);
            int nowYear = c.get(Calendar.YEAR);

            if (year < nowYear) {
                ErrorDate();
                setDefaultDate();
                return;
            } else if (year == nowYear) {
                if (monthOfYear < nowMonth) {
                    ErrorDate();
                    setDefaultDate();
                    return;
                } else if (monthOfYear == nowMonth){
                    if (dayOfMonth < nowDay) {
                        ErrorDate();
                        setDefaultDate();
                        return;
                    }
                }
            }
            if (year == nowYear
                    && monthOfYear == nowMonth
                    && dayOfMonth == nowDay) {
                currentDay = true;
            } else {
                currentDay = false;
            }

            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
            setTime(tv_set_date);
        }
    };

    private void ErrorDate(){
        Snackbar.make(content_create_new_event, "Неверная дата события", 1000).show();
    }
    private void ErrorDateFuture(){
        Snackbar.make(content_create_new_event, "Ограничение планирования на 30 дней вперед", 1000).show();
    }

    private boolean CheckInfo(){
        if(!et_event_name.getText().toString().equals("") &&
                !et_event_brief.getText().toString().equals("") &&
                !tv_set_date.getText().toString().equals("Указать дату") &&
                !tv_geo_message.getText().toString().equals("Место встречи...")){
            return true;
        }
        return false;
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        googleMap = map;
        //googleMap.setInfoWindowAdapter(this);
        googleMap.setOnCameraMoveListener(this);
        //googleMap.setPadding(10,10,0,100);
        //googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);99

        //defaultMarker - Kostroma
        LatLng defaultPosition = new LatLng(57.767439, 40.925939);
        marker = googleMap.addMarker(new MarkerOptions()
                .position(defaultPosition)
                .draggable(false)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                .alpha(0.8f));
        //marker.showInfoWindow();
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultPosition,  7f));
    }
    private int COUNT_REQUEST_GEOCODE = 0;
    @Override
    public void onCameraMove() {
        marker.setPosition(googleMap.getCameraPosition().target);
        if(COUNT_REQUEST_GEOCODE < 2) {
            Geocode geocode = new Geocode();
            geocode.execute(String.valueOf(marker.getPosition().latitude),
                    String.valueOf(marker.getPosition().longitude));
            COUNT_REQUEST_GEOCODE++;
        }

    }

    private class Geocode extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String response;
            WorkingWithJSON workingWithJSON;
            try {
                response = httpGetAddress.SendRequest(
                        httpGetAddress.URL + params[0]+","+params[1]+ httpGetAddress.LANGUAGE_REQUEST +httpGetAddress.KEY_API);
                workingWithJSON = new WorkingWithJSON(response);

                address = workingWithJSON.getAddress();

                return address;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String address) {
            super.onPostExecute(address);
            COUNT_REQUEST_GEOCODE--;
            if (address != null && !address.equals("null")) {
                tv_geo_message.setText(address);
                //marker.showInfoWindow();
            }
            else {
                tv_geo_message.setText("Неопределенное место");
            }
        }
    }

//    @Override
//    public View getInfoWindow(Marker marker) {
//        ContextThemeWrapper wrapper = new ContextThemeWrapper(getApplicationContext(), R.style.AppTheme);
//        LayoutInflater inflater = (LayoutInflater) wrapper.getSystemService(LAYOUT_INFLATER_SERVICE);
//        View layout = inflater.inflate(R.layout.custon_infowindow, null);
//
//        tv_user_name = (TextView) layout.findViewById(R.id.info_window_user_name);
//        place = (TextView) layout.findViewById(R.id.info_window_place);
//
//        tv_user_name.setText(userInformation.getUserLastName() + userInformation.getUserFirstName());
//        if (!address.equals("null")) {
//            place.setText(address);
//        }
//        else {
//            place.setText(httpGetAddress.SEARCH_ADDRESS);
//        }
//
//        return layout;
//    }
//
//    @Override
//    public View getInfoContents(Marker marker) {
//        return null;
//    }

//    public String getLatLongByURL(String requestURL) {
//        URL url;
//        String response = "";
//        try {
//            url = new URL(requestURL);
//
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setReadTimeout(15000);
//            conn.setConnectTimeout(15000);
//            conn.setRequestMethod("GET");
//            conn.setDoInput(true);
//            conn.setRequestProperty("Content-Type",
//                    "application/x-www-form-urlencoded");
//            conn.setDoOutput(true);
//            int responseCode = conn.getResponseCode();
//
//            if (responseCode == HttpsURLConnection.HTTP_OK) {
//                String line;
//                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                while ((line = br.readLine()) != null) {
//                    response += line;
//                }
//            } else {
//                response = "";
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return response;
//    }
}

