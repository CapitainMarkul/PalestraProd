package palestra.palestra;

/**
 * Created by Dmitry on 28.03.2017.
 */

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.ksoap2.serialization.SoapObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class FragmentMaps extends Fragment implements
        GoogleMap.OnInfoWindowClickListener,
        OnMapReadyCallback,
        GoogleMap.InfoWindowAdapter,
        GoogleMap.OnCameraMoveListener{

    private GoogleMap googleMap;
    private MapView mapView;

    private ConnectionToWCF connectionToWCF;
    private WorkingWithJSON workingWithJSON;
    private ParamUpdateView paramUpdateView;
    private UserInformation userInformation;
    private HttpGetAddress httpGetAddress;

    private boolean firstRequest = false;

    private Marker marker;
    private int countTask = 1;
    private int countTask_2 = 1;
    private int positionEvent = 0;
    private int positionEvent_2 = 0;

    private List<Event> eventsMap;
    private List<Event> eventsMap_2;
    private int requestCountMap = 1;
    private int requestCountMap_2 = 1;
    private final int SELECTION_COUNT = 10;
    private final int REQUEST_IN_COUNT = 60;
    private int currentCountRequest = 60;

    private String participant = "false";
    private String address = "";
    private GetConnection getConnection;

    private TextView tv_user_name;
    private TextView place;
    private TextView category_name;
    private TextView title_event;

    private ImageView avatar_mini;

    private boolean creatorEvent = true;    // создатель события

    public FragmentMaps(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        httpGetAddress = HttpGetAddress.getInstance();
        getConnection = GetConnection.getInstance();

        if(eventsMap != null) {
            requestCountMap = 1;
            requestCountMap_2 = 1;
            positionEvent = 0;
            creatorEvent = true;
            eventsMap.clear();
        }

        //Create Map
        mapView = (MapView) view.findViewById(R.id.mapViewAllEvents);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        ContextThemeWrapper wrapper = new ContextThemeWrapper(getActivity().getApplicationContext(), R.style.AppTheme);
        LayoutInflater inflater = (LayoutInflater) wrapper.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custon_infowindow_detail, null);

        title_event = (TextView)layout.findViewById(R.id.info_title_event);
        category_name = (TextView)layout.findViewById(R.id.info_category_name);
        tv_user_name = (TextView) layout.findViewById(R.id.info_window_user_name);
        place = (TextView)layout.findViewById(R.id.info_window_place);
        avatar_mini = (ImageView)layout.findViewById(R.id.avatar_mini_detail);

        if (!address.equals("null")) {
            place.setText(address);
        }
        else {
            place.setText(httpGetAddress.SEARCH_ADDRESS);
        }

        if (eventsMap != null) {
            for (int i = 0; i < eventsMap.size(); i++) {
                if (marker.getPosition().latitude == Double.parseDouble(eventsMap.get(i).getLatitude())
                        && marker.getPosition().longitude == Double.parseDouble(eventsMap.get(i).getLongitude())) {
                    Picasso.with(getContext())
                            .load("http://lowelly525-001-site1.itempurl.com/images/150px/face_"+eventsMap.get(i).getAvatarID()+".png")
                            //.memoryPolicy(MemoryPolicy.NO_CACHE)
                            //.networkPolicy(NetworkPolicy.NO_CACHE)
                            .into(avatar_mini);
                    tv_user_name.setText(eventsMap.get(i).getNameCreator());
                    title_event.setText(eventsMap.get(i).getTitle());
                    category_name.setText("Категория: " + eventsMap.get(i).getEventCategoryName());
                    break;
                }
            }
        }
        return layout;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        googleMap = map;
        googleMap.setPadding(10,10,200,200);
        googleMap.setInfoWindowAdapter(this);
        googleMap.setOnCameraMoveListener(this);
        //googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

//        //defaultMarker
//        LatLng eventPosition = new LatLng(49, 50);
//        marker = googleMap.addMarker(new MarkerOptions()
//                .position(eventPosition));
//                //.title("sdfsdf"));
////                .snippet(tv_brief_information.getText().toString()));
//        marker.showInfoWindow();
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map)));
        googleMap.setOnInfoWindowClickListener(this);
        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventPosition,  7f));

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker currentMarker) {
                marker = currentMarker;

                Geocode geocode = new Geocode();
                geocode.execute(String.valueOf(marker.getPosition().latitude),
                        String.valueOf(marker.getPosition().longitude));

                return false;
            }
        });
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        for (int i = 0; eventsMap.size() > i; i++){
            if(marker.getPosition().latitude == Double.parseDouble(eventsMap.get(i).getLatitude()) &&
                    marker.getPosition().longitude == Double.parseDouble(eventsMap.get(i).getLongitude())){

                Intent intent = new Intent(getActivity(), ActivityDetailCategory.class);

                GetParticipant getParticipant = new GetParticipant();
                Boolean startTask = false;
                if(paramUpdateView.getTYPE_EVENT() == "MY_FUTURE_EVENTS") {
                    intent.putExtra(ActivityDetailCategory.PARTICIPANT, "true");
                }
                else {
                    getParticipant.execute(userInformation.getUserID(),eventsMap.get(i).getEventID());
                    startTask = true;
                }

                intent.putExtra(ActivityDetailCategory.EVENT_ID, (eventsMap.get(i).getEventID()));
                intent.putExtra(ActivityDetailCategory.TITLE, (eventsMap.get(i).getTitle()));
                intent.putExtra(ActivityDetailCategory.BRIEF_INFO, (eventsMap.get(i).getBriefInfo()));
                intent.putExtra(ActivityDetailCategory.DATE_OF, (eventsMap.get(i).getDate()));
                intent.putExtra(ActivityDetailCategory.AGE_LIMIT, (eventsMap.get(i).getAgeLimit()));
                intent.putExtra(ActivityDetailCategory.LATITUDE, (eventsMap.get(i).getLatitude()));
                intent.putExtra(ActivityDetailCategory.LONGITUDE, (eventsMap.get(i).getLongitude()));
                //intent.putExtra(ActivityDetailCategory.USER_NAME, (events[getAdapterPosition()].getUserName()));
                //intent.putExtra(ActivityDetailCategory.EVENT_CATEGORY, (events[getAdapterPosition()].getEventCategory()));

                intent.putExtra(ActivityDetailCategory.COUNT_REVIEW, (eventsMap.get(i).getCountReview()));
                intent.putExtra(ActivityDetailCategory.COUNT_PARTICIPANT, (eventsMap.get(i).getCountParticipant()));
                intent.putExtra(ActivityDetailCategory.CREATOR_NAME,(eventsMap.get(i).getNameCreator()));

                intent.putExtra(ActivityDetailCategory.EVENT_CATEGORY, (eventsMap.get(i).getEventCategoryID()));
                intent.putExtra(ActivityDetailCategory.CREATOR_ID, (eventsMap.get(i).getIdCreator()));
                intent.putExtra(ActivityDetailCategory.AVATAR_ID, (eventsMap.get(i).getAvatarID()));

                if (startTask)
                {
                    try {
                        getParticipant.get();
                        intent.putExtra(ActivityDetailCategory.PARTICIPANT, participant);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }

                startActivity(intent);
            }
        }
    }

    private class Geocode extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String response;
            WorkingWithJSON workingWithJSON;
            try {
                response = httpGetAddress.SendRequest(
                        httpGetAddress.URL + params[0]+","+params[1]+ httpGetAddress.LANGUAGE_REQUEST + httpGetAddress.KEY_API);
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
            }
            else {
                address = "Неопределенное место";
            }
        }
    }

    class GetParticipant extends AsyncTask<Integer,Void,Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            ConnectionToWCF connectionToWCF = ConnectionToWCF.getInstance();
            WorkingWithJSON workingWithJSON;

            SoapTask request = new SoapTask(connectionToWCF.NAMESPACE, connectionToWCF.METHOD_CHECK_PARTICIPANT);
            request.AddProperty("userID", params[0]);
            request.AddProperty("eventID", params[1]);
            SoapObject resultsRequestSOAP =
                    request.startSoap(connectionToWCF.URL, connectionToWCF.SOAP_ACTION(connectionToWCF.METHOD_CHECK_PARTICIPANT));

            if (resultsRequestSOAP != null) {
                if (resultsRequestSOAP.getProperty(0) != null) {
                    String response = resultsRequestSOAP.getProperty(0).toString();
                    workingWithJSON = new WorkingWithJSON(response);
                    participant = workingWithJSON.getParticipant();
                }
            }
            return null;
        }
    }

    @Override
    public void onCameraMove() {
        paramUpdateView = ParamUpdateView.getInstance();

        if(paramUpdateView.getTYPE_EVENT() != paramUpdateView.getMY_FUTURE_EVENS()) {
            if (currentCountRequest == REQUEST_IN_COUNT && countTask == 1) {
                SelectAllEvents selectAllEvents = new SelectAllEvents();
                selectAllEvents.execute();
                currentCountRequest = 0;
            } else if (currentCountRequest > REQUEST_IN_COUNT)
                currentCountRequest = 0;
            else
                currentCountRequest++;
            return;
        } else {
            if (currentCountRequest == REQUEST_IN_COUNT && countTask_2 == 1) {
                SelectAllEvents_2 selectAllEvents_2 = new SelectAllEvents_2();
                selectAllEvents_2.execute();
                //currentCountRequest = 0;
            }
            if (currentCountRequest == REQUEST_IN_COUNT && countTask == 1) {
                SelectAllEvents selectAllEvents = new SelectAllEvents();
                selectAllEvents.execute();
                currentCountRequest = 0;
            } else if (currentCountRequest > REQUEST_IN_COUNT)
                currentCountRequest = 0;
            else
                currentCountRequest++;
            return;
        }
    }

    class SelectAllEvents extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            countTask++;
//            i++;
//            Log.e("I=",String.valueOf(i));
        }

        @Override
        protected Void doInBackground(Void... params) {
            connectionToWCF = ConnectionToWCF.getInstance();
            paramUpdateView = ParamUpdateView.getInstance();
            userInformation = UserInformation.getInstance();

            SoapTask request = null;
            SoapObject resultsRequestSOAP = null;
            String currentMethod = "";

            if (paramUpdateView.getTYPE_EVENT() == paramUpdateView.getALL_EVENS()) {
                request = new SoapTask(connectionToWCF.NAMESPACE, connectionToWCF.METHOD_SELECT_ALL_EVENTS);
                request.AddProperty("startID", requestCountMap);
                request.AddProperty("top", SELECTION_COUNT);
                currentMethod = connectionToWCF.METHOD_SELECT_ALL_EVENTS;
            } else if (paramUpdateView.getTYPE_EVENT() == paramUpdateView.getMY_FUTURE_EVENS()) {
                request = new SoapTask(connectionToWCF.NAMESPACE, connectionToWCF.METHOD_SELECT_ALL_EVENTS_CREATOR);
                request.AddProperty("startID", requestCountMap);
                request.AddProperty("userID", String.valueOf(userInformation.getUserID()));
                request.AddProperty("top", SELECTION_COUNT);
                currentMethod = connectionToWCF.METHOD_SELECT_ALL_EVENTS_CREATOR;
            } else if (paramUpdateView.getTYPE_EVENT() == paramUpdateView.getMY_PAST_EVENS()) {
                //TODO:заглушка TYPE_PAST
                return null;
            }

            try {
                resultsRequestSOAP =
                        request.startSoap(connectionToWCF.URL, connectionToWCF.SOAP_ACTION(currentMethod));
            } catch (Exception e) {
                // Log.e("ex", e.toString());
            }

            if (resultsRequestSOAP != null) {
                if (resultsRequestSOAP.getProperty(0) != null) {
                    String response = resultsRequestSOAP.getProperty(0).toString();
                    workingWithJSON = new WorkingWithJSON(response);

                    if (eventsMap == null) {
                        eventsMap = workingWithJSON.getAllEvent();
                    } else {
                        List<Event> temp = workingWithJSON.getAllEvent();

                        for (Event event : temp) {
                            eventsMap.add(event);
                        }
                    }
                    requestCountMap = workingWithJSON.getIndexNextPage() + 1;
                }
//                if (eventsMap != null) {
//                    Log.e("CardE=", String.valueOf(eventsMap.size()));
//                }
            } else {
                if (!getConnection.isShow()) {
                    getConnection.isOnlineMap(getContext(), getView());
                }
                return null;
            }

            if (eventsMap != null &&
                    eventsMap.size() <= 1 &&
                    creatorEvent &&
                    paramUpdateView.getTYPE_EVENT() == paramUpdateView.getMY_FUTURE_EVENS()) {
                creatorEvent = false;
                requestCountMap = 1;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            countTask--;

            if (eventsMap != null) {
                for (int i = positionEvent; eventsMap.size() > i; i++) {
                    AddMarker(Double.parseDouble(eventsMap.get(i).getLatitude()),
                            Double.parseDouble(eventsMap.get(i).getLongitude()));
                    positionEvent++;
                }
            }
        }
    }

        class SelectAllEvents_2 extends AsyncTask<Void,Void,Void> {

            @Override
            protected void onPreExecute() {
                countTask_2++;
//            i++;
//            Log.e("I=",String.valueOf(i));
            }

            @Override
            protected Void doInBackground(Void... params) {
                connectionToWCF = ConnectionToWCF.getInstance();
                paramUpdateView = ParamUpdateView.getInstance();
                userInformation = UserInformation.getInstance();

                SoapTask request = null;
                SoapObject resultsRequestSOAP = null;
                String currentMethod = "";

                if (paramUpdateView.getTYPE_EVENT() == paramUpdateView.getMY_FUTURE_EVENS()) {
                    request = new SoapTask(connectionToWCF.NAMESPACE, connectionToWCF.METHOD_SELECT_ALL_EVENTS_NOT_CREATOR);
                    request.AddProperty("startID", requestCountMap_2);
                    request.AddProperty("userID", String.valueOf(userInformation.getUserID()));
                    request.AddProperty("top", SELECTION_COUNT);
                    currentMethod = connectionToWCF.METHOD_SELECT_ALL_EVENTS_NOT_CREATOR;
                }

                try {
                    resultsRequestSOAP =
                            request.startSoap(connectionToWCF.URL, connectionToWCF.SOAP_ACTION(currentMethod));
                } catch (Exception e) {
                    // Log.e("ex", e.toString());
                }

                if (resultsRequestSOAP != null) {
                    if (resultsRequestSOAP.getProperty(0) != null) {
                        String response = resultsRequestSOAP.getProperty(0).toString();
                        workingWithJSON = new WorkingWithJSON(response);

                        if (eventsMap == null) {
                            eventsMap = workingWithJSON.getAllEvent();
                        } else {
                            List<Event> temp = workingWithJSON.getAllEvent();

                            for (Event event : temp) {
                                eventsMap.add(event);
                            }
                        }
                        requestCountMap_2 = workingWithJSON.getIndexNextPage() + 1;
                    }
//                if (eventsMap != null) {
//                    Log.e("CardE=", String.valueOf(eventsMap.size()));
//                }
                } else {
                    if (!getConnection.isShow()) {
                        getConnection.isOnlineMap(getContext(), getView());
                    }
                    return null;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                countTask_2--;

                if (eventsMap != null) {
                    for (int i = positionEvent; eventsMap.size() > i; i++) {
                        AddMarker(Double.parseDouble(eventsMap.get(i).getLatitude()),
                                Double.parseDouble(eventsMap.get(i).getLongitude()));
                        positionEvent++;
                    }
                }
            }
        }


        private void AddMarker(double latitude, double longitude){
            LatLng latLng = new LatLng(latitude, longitude);
            // Changing marker icon
            marker = googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                    .alpha(0.8f));
        }
    }

