package palestra.palestra;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.ksoap2.serialization.SoapObject;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AdapterViewPagerTabs adapter;
    private UserInformation userInformation;
    private ConnectionToWCF connectionToWCF;
    private WorkingWithJSON workingWithJSON;

    private SelectCountEvents selectCountEvents;
    private int countAsyncTask = 0;

    private NavigationView navigationView;

    private TextView userName;
    private TextView tv_online;
    private ImageView avatarUser;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton floatingActionButton;

    private ParamUpdateView paramUpdateView;

    private int[] tabIcons={
            R.drawable.ic_card,
            R.drawable.ic_map
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        paramUpdateView = ParamUpdateView.getInstance();

//Загрузка вкладок
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //toggle.setDrawerIndicatorEnabled(false);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View navHeaderMain = navigationView.inflateHeaderView(R.layout.nav_header_main);
        userName = (TextView) navHeaderMain.findViewById(R.id.navigation_user_name);
        tv_online = (TextView) navHeaderMain.findViewById(R.id.textView);
        avatarUser = (ImageView)navHeaderMain.findViewById(R.id.avatar_current_user);
        navigationView.setNavigationItemSelectedListener(this);

        userInformation = UserInformation.getInstance();
        Picasso.with(this)
                .load("http://lowelly525-001-site1.itempurl.com/images/150px/face_"
                + userInformation.getAvatarID() + ".png")
                //.memoryPolicy(MemoryPolicy.NO_CACHE)
                //.networkPolicy(NetworkPolicy.NO_CACHE)
                .into(avatarUser);
        userName.setText(userInformation.getUserLastName() + " " + userInformation.getUserFirstName());

        if(userInformation.getUserCategory() == 1){
            tv_online.setText("Администратор");
        }
        selectCountEvents = new SelectCountEvents();
        selectCountEvents.execute();

//КОНЕЦ

//Загрузка кнопки
        floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(ActivityMain.this, ActivityCreateNewEvent.class);
                startActivity(intent);
            }
        });
//КОНЕЦ
    }

//    class InfoUser extends AsyncTask<Integer, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            userName.setText("Начинаю искать...");
//        }
//
//        protected String doInBackground(Integer... idUser) {
//                SoapTask request = new SoapTask(NAMESPACE, METHOD_NAME);
//
//                PropertyInfo pa = new PropertyInfo();
//                pa.setName("idUser");
//                pa.setValue(new Integer(USER_ID).toString());
//
//                request.addProperty(pa);
//
//                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//                envelope.dotNet = true;
//
//                envelope.setOutputSoapObject(request);
//                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
//                androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
//
//                try {
//                    androidHttpTransport.call(SOAP_ACTION, envelope);
//                }
//                catch (Exception e) {
//
//                }
//
//                SoapTask resultsRequestSOAP = (SoapTask) envelope.bodyIn;
//            if(resultsRequestSOAP!=null) {
//                String response =  resultsRequestSOAP.getProperty(0).toString();
//                workingWithJSON = new WorkingWithJSON(response);
//                return workingWithJSON.getUserName();
//            }
//            return "В поисках Вашего имени...";
//        }
//
////        @Override
////        protected void onProgressUpdate(String... values) {
////            super.onProgressUpdate(values);
////        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            if(result!=null)
//                userName.setText(result);
//        }
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if(id == R.id.menu_all_events){
            paramUpdateView.setTYPE_EVENT("ALL_EVENTS");
        } else if (id == R.id.menu_future_events) {
            paramUpdateView.setTYPE_EVENT("MY_FUTURE_EVENTS");
        }
//        else if (id == R.id.menu_past_events) {
//            paramUpdateView.setTYPE_EVENT("MY_PAST_EVENTS");
//        }
//        else if (id == R.id.menu_setting) {
//
//        }
        else if (id == R.id.menu_info_about_user) {
            UserInformation userInformation = UserInformation.getInstance();
            Intent intent = new Intent(ActivityMain.this, ActivityInfoAboutUser.class);
            intent.putExtra(ActivityInfoAboutUser.USER_ID, userInformation.getUserID());
            intent.putExtra(ActivityInfoAboutUser.AVATAR_ID, userInformation.getAvatarID());
            intent.putExtra(ActivityInfoAboutUser.CREATOR_NAME, userInformation.getUserFirstName()
                    +" "+ userInformation.getUserLastName());
            intent.putExtra(ActivityInfoAboutUser.CITY, userInformation.getCity());
            intent.putExtra(ActivityInfoAboutUser.AGE, userInformation.getAge());
            startActivity(intent);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else if(id == R.id.menu_exit){
            Intent intent = new Intent(ActivityMain.this, ActivityAuthorization.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }

        SelectCountEvents selectCountEvents = new SelectCountEvents();
        selectCountEvents.execute();

        setupViewPager(viewPager);
        setupTabIcons();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class SelectCountEvents extends AsyncTask<Void,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            countAsyncTask++;
        }

        @Override
        protected String doInBackground(Void... params) {
            connectionToWCF = ConnectionToWCF.getInstance();
            userInformation = UserInformation.getInstance();

            SoapTask request = new SoapTask(connectionToWCF.NAMESPACE, connectionToWCF.METHOD_COUNT_EVENTS_IN_USER);
            request.AddProperty("userID", userInformation.getUserID());

            SoapObject resultsRequestSOAP =
                    request.startSoap(connectionToWCF.URL,connectionToWCF.SOAP_ACTION(connectionToWCF.METHOD_COUNT_EVENTS_IN_USER));


            if (resultsRequestSOAP != null) {
                if(resultsRequestSOAP.getProperty(0) != null){
                    String response = resultsRequestSOAP.getProperty(0).toString();
                    workingWithJSON = new WorkingWithJSON(response);
                    return workingWithJSON.getCountEventInUser();
                }
            }
            return "0";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            countAsyncTask--;
            navigationView.getMenu().findItem(R.id.menu_future_events).setTitle(
                    getResources().getString(R.string.events_future) + "  (" + result + ")");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (countAsyncTask == 0) {
            SelectCountEvents selectCountEvents = new SelectCountEvents();
            selectCountEvents.execute();
        }
       // Log.e("e",String.valueOf(countAsyncTask));
    }

    private void setupTabIcons() {
//        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
//        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(0).setText("Карточки");
        tabLayout.getTabAt(1).setText("Карта");

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new AdapterViewPagerTabs(getSupportFragmentManager());
        adapter.addFragment(new FragmentCardsCategory(), "CARD");
        adapter.addFragment(new FragmentMaps(), "MAP");
        viewPager.setAdapter(adapter);
    }
}
