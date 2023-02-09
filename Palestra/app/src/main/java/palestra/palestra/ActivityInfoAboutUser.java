package palestra.palestra;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

/**
 * Created by Dmitry on 05.06.2017.
 */

public class ActivityInfoAboutUser extends AppCompatActivity {
    private TextView tv_user_name;
    private TextView tv_user_city;
    private TextView tv_count_create_event;
    private TextView tv_count_create_event_all;
    private TextView tv_count_all;
    private TextView tv_count_participant_all;
    private TextView tv_count_participant;

    private SwipeRefreshLayout swipeRefreshLayout;

    private ImageView button_back_create_event;
    private ImageView iv_avatar;
    private RelativeLayout container_user_info;
    private LinearLayout lin_all_info;

    private ProgressBar progressBar;

    private ConnectionToWCF connectionToWCF;
    private WorkingWithJSON workingWithJSON;

    public static final String USER_ID = "USER_ID";
    public static final String CREATOR_NAME = "creator_name";
    public static final String AVATAR_ID = "avatar_id";
    public static final String CITY = "city";
    public static final String AGE = "age";
    public static final String ACTIVITY_NAME = "activity_name";

    private final int COUNT_ALL = 0;
    private final int COUNT_ALL_CREATED_EVENTS = 1;
    private final int COUNT_ACTIVE_EVENTS = 2;
    private final int COUNT_ALL_PARTICIPANT = 3;
    private final int COUNT_ACTIVE_PARTICIPANT = 4;
    private final int COUNT_CITY = 5;
    private final int COUNT_AGE = 6;


    private int user_id;
    private int avatar_id;
    private String city;
    private String user_name;
    private int age;
    private String activityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_about_user);

        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_user_city = (TextView) findViewById(R.id.tv_user_city);
        tv_count_create_event = (TextView) findViewById(R.id.tv_count_create_event);
        tv_count_create_event_all = (TextView) findViewById(R.id.tv_count_create_event_all);
        tv_count_participant = (TextView) findViewById(R.id.tv_count_participant);
        tv_count_participant_all = (TextView) findViewById(R.id.tv_count_participant_all);
        tv_count_all = (TextView) findViewById(R.id.tv_count_all);

        button_back_create_event = (ImageView) findViewById(R.id.button_back_create_event);
        iv_avatar = (ImageView) findViewById(R.id.avatar);

        container_user_info = (RelativeLayout) findViewById(R.id.rel_title);
        lin_all_info = (LinearLayout) findViewById(R.id.lin_all_info);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.blue_light), PorterDuff.Mode.MULTIPLY);

        user_id = getIntent().getIntExtra(USER_ID, 0);
        avatar_id = getIntent().getIntExtra(AVATAR_ID, 2);

        city = getIntent().getStringExtra(CITY);
        age = getIntent().getIntExtra(AGE,0);

        user_name = getIntent().getStringExtra(CREATOR_NAME);
        //activityName = getIntent().getStringExtra(ACTIVITY_NAME);

        if(city != null){
            tv_user_name.setText(user_name  + ", " + YearOld(age));
            tv_user_city.setText(city);

        }else{
            tv_user_name.setText(user_name);
        }



        connectionToWCF = ConnectionToWCF.getInstance();

        Picasso.with(this)
                .load("http://lowelly525-001-site1.itempurl.com/images/150px/face_" + avatar_id + ".png")
                //.memoryPolicy(MemoryPolicy.NO_CACHE)
                //.networkPolicy(NetworkPolicy.NO_CACHE)
                .into(iv_avatar);

        //iv_avatar.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshInfo();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        RefreshInfo();

        button_back_create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void RefreshInfo() {
        GetConnection getConnection = GetConnection.getInstance();
        progressBar.setVisibility(View.VISIBLE);

        if (getConnection.isOnline(getBaseContext())) {
            CountCreateEvent countCreateEvent = new CountCreateEvent();
            countCreateEvent.execute(user_id);
        } else {
            progressBar.setVisibility(View.INVISIBLE);

            Snackbar snackbar = Snackbar
                    .make(container_user_info, "Отсутствует соединение", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Повторить", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            RefreshInfo();
                        }
                    });
            snackbar.setActionTextColor(Color.CYAN);
            snackbar.show();
        }
    }

    private class CountCreateEvent extends AsyncTask<Integer, Void, ArrayList<Object>> {
        protected ArrayList<Object> doInBackground(Integer... Data) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SoapTask request = new SoapTask(connectionToWCF.NAMESPACE, connectionToWCF.METHOD_COUNT_INFO_USER);
            request.AddProperty("userID", new Integer(Data[0]));
            SoapObject resultsRequestSOAP =
                    request.startSoap(connectionToWCF.URL, connectionToWCF.SOAP_ACTION(connectionToWCF.METHOD_COUNT_INFO_USER));

            if (resultsRequestSOAP != null) {
                if (resultsRequestSOAP.getProperty(0) != null) {
                    String response = resultsRequestSOAP.getProperty(0).toString();
                    workingWithJSON = new WorkingWithJSON(response);
                    return workingWithJSON.getCountInfoUser();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Object> listCount) {
            super.onPostExecute(listCount);

            progressBar.setEnabled(false);

            if(city == null) {
                if (listCount != null && listCount.size() > 0) {
                    tv_user_name.setText(user_name + ", " + YearOld((int) (listCount.get(COUNT_AGE))));
                    tv_user_city.setText(String.valueOf(listCount.get(COUNT_CITY)));
                } else {
                    RefreshInfo();
                }
            }
            if (listCount != null && listCount.size() > 0) {
                tv_count_all.setText(String.valueOf(listCount.get(COUNT_ALL)));
                tv_count_create_event_all.setText(String.valueOf(listCount.get(COUNT_ALL_CREATED_EVENTS)));
                tv_count_create_event.setText(String.valueOf(listCount.get(COUNT_ACTIVE_EVENTS)));
                tv_count_participant_all.setText(String.valueOf(listCount.get(COUNT_ALL_PARTICIPANT)));
                tv_count_participant.setText(String.valueOf(listCount.get(COUNT_ACTIVE_PARTICIPANT)));
            }else {
                RefreshInfo();
            }
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private String YearOld(int age) {
        String year = " год";
        String year_1 = " года";
        String years = " лет";
        String ageString = String.valueOf(age);

        int ostAge = age % 10;

        if (age >= 5 && age <= 20 || ostAge >= 5 && ostAge <= 20) {
            return ageString + years;
        } else if (age >= 2 && age <= 4 || ostAge >= 2 && ostAge <= 4) {
            return ageString + year_1;
        } else {
            return ageString + year;
        }
    }
}
