package palestra.palestra;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

/**
 * Created by Dmitry on 31.03.2017.
 */

public class ActivityDetailCategory extends AppCompatActivity implements
        GoogleMap.OnInfoWindowClickListener,
        OnMapReadyCallback,
        GoogleMap.InfoWindowAdapter {

    public static final String EVENT_ID = "eventID";
    public static final String TITLE = "title";
    //public static final String USER_NAME = "user_name";
    public static final String BRIEF_INFO = "briefInfo";
    public static final String DATE_OF = "dateOf";
    public static final String AGE_LIMIT = "ageLimit";
    public static final String EVENT_CATEGORY = "eventCategory";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String COUNT_REVIEW = "count_review";
    public static final String COUNT_PARTICIPANT = "count_participant";
    public static final String CREATOR_NAME = "creator_name";
    public static final String PARTICIPANT = "participant";
    public static final String BACKGROUND_ID = "background_id";
    public static final String CREATOR_ID = "creator_id";
    public static final String AVATAR_ID = "avatar_id";
    public static final String USER_CATEGORY = "user_category";

    private GoogleMap googleMap;
    private Marker marker;

    private LinearLayout content_detail_category;
    private TextView tv_title;
    private TextView tv_brief_information;
    private TextView tv_date_time;
    private TextView tv_count_review;
    private TextView tv_count_participant;
    private TextView tv_will_go;
    private TextView tv_user_name;
    private TextView place;
    private ImageView bt_will_go;
    private ImageView bt_back;
    private ImageView imageView_background;
    private ImageView avatar_mini;
    private ImageView iv_trash;

    private int eventID;
    private int userCategory;
    private int avatarID;
    private int ageLimit;
    private int eventCategory;
    private String creatorName;
    private double latitude;
    private double longitude;

    private String address = "";
    private TypedArray images;
    private ConnectionToWCF connectionToWCF;
    private SoapTask soapTask;
    private UserInformation userInformation;
    private HttpGetAddress httpGetAddress;
    private GetConnection getConnection;
    private DeletedEvents deletedEvents;

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow();
//            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//            // w.addFlags(WindowManager.ayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        //getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
//        getWindow().getDecorView().setSystemUiVisibility(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().addFlags(        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_category);

        httpGetAddress = HttpGetAddress.getInstance();
        userInformation = UserInformation.getInstance();
        getConnection = GetConnection.getInstance();
        deletedEvents = DeletedEvents.getInstance();
        //errorMessage = new ErrorMessage(this);

        //images = getResources().obtainTypedArray(R.array.category_random_picture);

        content_detail_category = (LinearLayout) findViewById(R.id.content_datail_category);

        tv_title = (TextView) findViewById(R.id.item_detail_tv_title);
        tv_brief_information = (TextView) findViewById(R.id.item_detail_tv_brief_information);
        tv_date_time = (TextView) findViewById(R.id.item_detail_tv_date_time);
        tv_count_review = (TextView) findViewById(R.id.item_detail_tv_count_review);
        tv_count_participant = (TextView) findViewById(R.id.item_detail_tv_count_participant);
        tv_will_go = (TextView) findViewById(R.id.tv_will_go);
        bt_will_go = (ImageView) findViewById(R.id.button_will_go);
        bt_back = (ImageView) findViewById(R.id.button_back);

        iv_trash = (ImageView) findViewById(R.id.iv_trash);
        iv_trash.getLayoutParams().width = 0;
        iv_trash.getLayoutParams().height = 0;
        iv_trash.setEnabled(false);

        tv_title.setText(getIntent().getStringExtra(TITLE));
        tv_brief_information.setText(getIntent().getStringExtra(BRIEF_INFO));
        tv_date_time.setText(getIntent().getStringExtra(DATE_OF));
        tv_count_review.setText(String.valueOf(getIntent().getIntExtra(COUNT_REVIEW, 0)));
        tv_count_participant.setText(String.valueOf(getIntent().getIntExtra(COUNT_PARTICIPANT, 0)));

        eventID = getIntent().getIntExtra(EVENT_ID, 1);
        avatarID = getIntent().getIntExtra(AVATAR_ID,2);
        ageLimit = getIntent().getIntExtra(AGE_LIMIT, 6);
        creatorName = getIntent().getStringExtra(CREATOR_NAME);
        latitude = Double.parseDouble(getIntent().getStringExtra(LATITUDE));
        longitude = Double.parseDouble(getIntent().getStringExtra(LONGITUDE));

        userCategory = getIntent().getIntExtra(USER_CATEGORY,3);

        if(userCategory == 1){
            iv_trash.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
            iv_trash.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
            iv_trash.setEnabled(true);
        }

        if(deletedEvents.isEventDelete(eventID)){
            tv_will_go.setText("Событие отменено");
            bt_will_go.setEnabled(false);
            iv_trash.setEnabled(false);
        }
        else {
            if (getIntent().getStringExtra(PARTICIPANT).toString().equals("true") &&
                    (userInformation.getUserLastName() + " " + userInformation.getUserFirstName()).equals(creatorName)) {
                tv_will_go.setText(getResources().getString(R.string.will_go_creator));
                iv_trash.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
                iv_trash.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
                iv_trash.setEnabled(true);
                bt_will_go.setEnabled(false);
            } else if (getIntent().getStringExtra(PARTICIPANT).toString().equals("true")) {
                tv_will_go.setText(getResources().getString(R.string.will_go_yes));
            }
        }

//        int imageId = getIntent().getIntExtra(BACKGROUND_ID, 0);
//        //иначе, дефолтная картинка
//        if (imageId != 0) {
//            imageView_background = (ImageView) findViewById(R.id.detail_background);
//            imageView_background.setBackgroundResource(imageId);
//        }
        eventCategory = getIntent().getIntExtra(EVENT_CATEGORY, 0);
        imageView_background = (ImageView) findViewById(R.id.detail_background);
//        imageView_background.setBackgroundResource(
//                images.getResourceId(getPositionImage(eventCategory), 0));


//        byte[] byteArray = getIntent().getByteArrayExtra(BACKGROUND_ID);
//        Bitmap imageBackground = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//
//        imageView_background.setImageBitmap(imageBackground);

        //update countReview
        UpdateCountReview updateCountReview = new UpdateCountReview();
        updateCountReview.execute();

        //Create Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapViewDetailInfo);
        mapFragment.getMapAsync(this);
//        if(null == googleMap) {
//            Toast.makeText(getApplicationContext(),
//                    "Error creating map",Toast.LENGTH_SHORT).show();
//        }

        iv_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tv_will_go.getText().toString().equals("Событие отменено")) {
                    openTrashDialog();
                }
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt_will_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getConnection.isOnline(getBaseContext(), content_detail_category)) {
                    WillGoYes willGoYes = new WillGoYes();
                    WillGoNo willGoNo = new WillGoNo();

                    if (tv_will_go.getText().toString() == getResources().getString(R.string.will_go)) {
                        tv_will_go.setText(getResources().getString(R.string.will_go_yes));
                        if (willGoNo.getStatus().toString() != "RUNNING") {
                            willGoYes.execute();
                        }
                    } else {
                        tv_will_go.setText(getResources().getString(R.string.will_go));
                        if (willGoYes.getStatus().toString() != "RUNNING") {
                            willGoNo.execute();
                        }
                    }
                }
            }
        });
    }





    private void openTrashDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                ActivityDetailCategory.this,R.style.YourDialogStyle);


        quitDialog
                .setTitle("Удалить событие: Вы уверены?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tv_will_go.setText("Событие отменено");
                            bt_will_go.setEnabled(false);
                            iv_trash.setEnabled(false);
                            deletedEvents.addEventId(eventID);

                            //вызов класса смены статуса
                            CancelEvent cancelEvent = new CancelEvent();
                            cancelEvent.execute();

                            finish();
                        }
                    })
                .setNegativeButton("Нет",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(getResources().getDrawable(R.drawable.ic_waste_bin_red));

        quitDialog.show();
    }

    class CancelEvent extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            connectionToWCF = ConnectionToWCF.getInstance();
            soapTask = new SoapTask(connectionToWCF.NAMESPACE, connectionToWCF.METHOD_DELETE_EVENT);
            soapTask.AddProperty("eventID", eventID);
            try {
                soapTask.startSoap(connectionToWCF.URL, connectionToWCF.SOAP_ACTION(connectionToWCF.METHOD_DELETE_EVENT));
            } catch (Exception e) {
                return null;
            }
            return null;
        }
    }

    class WillGoYes extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            connectionToWCF = ConnectionToWCF.getInstance();
            userInformation = UserInformation.getInstance();
            soapTask = new SoapTask(connectionToWCF.NAMESPACE, connectionToWCF.METHOD_WILL_GO_YES);
            soapTask.AddProperty("eventID", eventID);
            soapTask.AddProperty("userID", userInformation.getUserID());
            soapTask.AddProperty("creator", 0);
            try {
                soapTask.startSoap(connectionToWCF.URL, connectionToWCF.SOAP_ACTION(connectionToWCF.METHOD_WILL_GO_YES));
            } catch (Exception e) {
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            SetUpCountParticipant();
        }
    }

    class WillGoNo extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            connectionToWCF = ConnectionToWCF.getInstance();
            userInformation = UserInformation.getInstance();
            soapTask = new SoapTask(connectionToWCF.NAMESPACE, connectionToWCF.METHOD_WILL_GO_NO);
            soapTask.AddProperty("eventID", eventID);
            soapTask.AddProperty("userID", userInformation.getUserID());
            try {
                soapTask.startSoap(connectionToWCF.URL, connectionToWCF.SOAP_ACTION(connectionToWCF.METHOD_WILL_GO_NO));
            } catch (Exception e) {
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            SetDownCountParticipant();
        }
    }

    class UpdateCountReview extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Picasso.with(getBaseContext())
                    .load("http://lowelly525-001-site1.itempurl.com/images/background/"+
                            eventCategory+".png")
                    //.memoryPolicy(MemoryPolicy.NO_CACHE)
                    //.networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(imageView_background);
        }

        @Override
        protected Void doInBackground(Void... params) {
            connectionToWCF = ConnectionToWCF.getInstance();
            soapTask = new SoapTask(connectionToWCF.NAMESPACE, connectionToWCF.METHOD_UPDATE_COUNT_REVIEW);
            soapTask.AddProperty("eventID", eventID);
            soapTask.startSoap(connectionToWCF.URL, connectionToWCF.SOAP_ACTION(connectionToWCF.METHOD_UPDATE_COUNT_REVIEW));
            return null;
        }
    }

    private void SetUpCountParticipant() {
        tv_count_participant.setText(
                String.valueOf(
                        Integer.parseInt(
                                tv_count_participant.getText().toString()) + 1));
    }

    private void SetDownCountParticipant() {
        tv_count_participant.setText(
                String.valueOf(
                        Integer.parseInt(
                                tv_count_participant.getText().toString()) - 1));
    }

    @Override
    public View getInfoWindow(Marker marker) {
        ContextThemeWrapper wrapper = new ContextThemeWrapper(getApplicationContext(), R.style.AppTheme);
        LayoutInflater inflater = (LayoutInflater) wrapper.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custon_infowindow, null);

        tv_user_name = (TextView) layout.findViewById(R.id.info_window_user_name);
        place = (TextView) layout.findViewById(R.id.info_window_place);
        avatar_mini = (ImageView) layout.findViewById(R.id.avatar_mini);
        Picasso.with(this)
                .load("http://lowelly525-001-site1.itempurl.com/images/150px/face_"+avatarID+".png")
                //.memoryPolicy(MemoryPolicy.NO_CACHE)
                //.networkPolicy(NetworkPolicy.NO_CACHE)
                .into(avatar_mini);

        tv_user_name.setText(creatorName);
        if (!address.equals("null")) {
            place.setText(address);
        } else {
            place.setText(httpGetAddress.SEARCH_ADDRESS);
        }
        return layout;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }


    private class Geocode extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String response;
            WorkingWithJSON workingWithJSON;
            try {
                response = httpGetAddress.SendRequest(
                        httpGetAddress.URL + params[0] + "," + params[1] + httpGetAddress.LANGUAGE_REQUEST + httpGetAddress.KEY_API);
                workingWithJSON = new WorkingWithJSON(response);

                address = workingWithJSON.getAddress();

                return address;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!address.equals("null")) {
                marker.showInfoWindow();
            } else {
                address = "Неопределенное место";
            }
        }
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        googleMap = map;
        googleMap.setPadding(10, 10, 0, 10);
        googleMap.setInfoWindowAdapter(this);
        //googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        //defaultMarker
        LatLng eventPosition = new LatLng(latitude, longitude);
        marker = googleMap.addMarker(new MarkerOptions()
                .position(eventPosition)
                .title(tv_title.getText().toString())
                .snippet(tv_brief_information.getText().toString())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        marker.showInfoWindow();
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map)));
        googleMap.setOnInfoWindowClickListener(this);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventPosition, 15f));


        Geocode geocode = new Geocode();
        geocode.execute(String.valueOf(marker.getPosition().latitude),
                String.valueOf(marker.getPosition().longitude));
//        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                latitude = latLng.latitude;
//                if (marker == null){
//                marker = googleMap.addMarker(new MarkerOptions()
//                        .position(latLng)
//                        .title(tv_title.getText().toString())
//                        .draggable(true));
//                }
//                else {
//                    marker.remove();
//                    marker = googleMap.addMarker(new MarkerOptions()
//                            .position(latLng)
//                            .title(tv_title.getText().toString())
//                            .draggable(true));
//                }
//            }
//        });
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        int creatorID = getIntent().getIntExtra(CREATOR_ID, 0);
        int avatarID = getIntent().getIntExtra(AVATAR_ID,2);
        String creatorName = getIntent().getStringExtra(CREATOR_NAME);

        if(creatorID != 0) {
            Intent intent = new Intent(ActivityDetailCategory.this, ActivityInfoAboutUser.class);
            intent.putExtra(ActivityInfoAboutUser.USER_ID, creatorID);
            intent.putExtra(ActivityInfoAboutUser.AVATAR_ID,avatarID);
            intent.putExtra(ActivityInfoAboutUser.CREATOR_NAME, creatorName);
            startActivity(intent);
        } else
            Snackbar.make(content_detail_category, "Что-то пошло не так", 1000).show();

        //Snackbar.make(content_detail_category, "Future: Open info about user", 1000).show();
        //TODO: сделать открытие профиля пользователя
    }


    // Возвращает номер изображения в массиве, соответствующий категории события
    //заранее предопределено, номер категории - ее изображение
    public int getPositionImage(int id_category) {
        switch (id_category) {
            case 1:
                return 0;   //Запрещенная категория
            case 2:
                return 1;   //Искусство
            case 3:
                return 2;   //Кулинария
            case 4:
                return 3;   //Литература
            case 5:
                return 4;   //Развлечение
            case 6:
                return 5;   //Спорт
            case 7:
                return 6;   //Танцы
            case 8:
                return 7;   //Танцы
            case 9:
                return 8;   //Танцы
            case 10:
                return 9;   //Танцы
            case 11:
                return 10;   //Танцы
            case 12:
                return 11;   //Танцы

            //TODO:добавить соответствия Категория-Изображение
            default:
                return 0;   //default image
        }
    }
}






//        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//
//        // Set Collapsing Toolbar layout to the screen
////        CollapsingToolbarLayout collapsingToolbar =
////                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        // Set title of Detail page
//        // collapsingToolbar.setTitle(getString(R.string.item_title));
//

//
//        Resources resources = getResources();
//        String[] category = resources.getStringArray(R.array.category);
////        collapsingToolbar.setTitle(category[postion % category.length]);
//
////        String[] placeDetails = resources.getStringArray(R.array.place_details);
////        TextView placeDetail = (TextView) findViewById(R.id.place_detail);
////        placeDetail.setText(placeDetails[postion % placeDetails.length]);
//
////        String[] placeLocations = resources.getStringArray(R.array.place_locations);
////        TextView placeLocation =  (TextView) findViewById(R.id.place_location);
////        placeLocation.setText(placeLocations[postion % placeLocations.length]);
//
//        TypedArray categoryPictures = resources.obtainTypedArray(R.array.category_picture);
//        ImageView categoryPicutre = (ImageView) findViewById(R.id.image);
//        categoryPicutre.setImageDrawable(categoryPictures.getDrawable(postion % categoryPictures.length()));
//
//        categoryPictures.recycle();
