package palestra.palestra;

/**
 * Created by Dmitry on 28.03.2017.
 */

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.ksoap2.serialization.SoapObject;

import java.util.List;

//import com.android.volley.toolbox.JsonObjectRequest;

public class FragmentCardsCategory extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Resources resources;
    private ConnectionToWCF connectionToWCF;
    private WorkingWithJSON workingWithJSON;
    private UserInformation userInformation;
    private GetConnection getConnection;
    private int d;
    private int d_2;

    //Volley Request Queue
   // private RequestQueue requestQueue;
    private SelectEvents selectEvents;
    private SelectEvents_2 selectEvents_2;

    //private int requestCount = 1;
    //private List<Event> events;
    private List<Event> events;

    private int requestCount = 1;
    private int requestCount_2 = 1;
    private final int SELECTION_COUNT = 10;

    private boolean creatorEvent = true;    // создатель события
    private boolean firstAsyncTask = true;
    private final int MAX_ASYNC_TASK = 1;
    private int countAsyncTask = 0;

    private boolean requestCountReset = true;
    private boolean firstRequest = true;
    //private boolean selectAllEventsNotCreator = false;   // обнулить счетчик начала поиска

    private TypedArray images;
    private int[] id_category;
    private ProgressBar progressBar;

    private View view;
    private ParamUpdateView paramUpdateView;

    private Snackbar snackbar;

//    private boolean wasAllEvents = true;   // исключить обновления в той же категории
//    private boolean wasFutureEvents = false;// исключить обновления в той же категории

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_cards, container, false);

        paramUpdateView = ParamUpdateView.getInstance();
        getConnection = GetConnection.getInstance();

        progressBar = (ProgressBar)view.findViewById(R.id.progressBarPl);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.blue_light), PorterDuff.Mode.MULTIPLY);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        d = 0;
        d_2 = 0;

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setRefreshing(true);

        paramUpdateView = ParamUpdateView.getInstance();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(getConnection.isOnline(getContext(), getView())) {
                    if (countAsyncTask < MAX_ASYNC_TASK) {
                        if (view != null) {
                            swipeRefreshLayout.setRefreshing(true);
                            ClearAllConstants();
                            if (paramUpdateView.getTYPE_EVENT() == paramUpdateView.getALL_EVENS()) {
                                selectEvents = new SelectEvents();
                                selectEvents.execute();
                            } else{
                                selectEvents = new SelectEvents();
                                selectEvents.execute();
                                selectEvents_2 = new SelectEvents_2();
                                selectEvents_2.execute();
                            }
                        }
                    }
                }
            }
        });
        //чистим предыдущие результаты, при переключении на другую вкладку
        //ClearAllConstants();

        //первый запрос
        if (countAsyncTask < MAX_ASYNC_TASK) {
            ClearAllConstants();
            // if(getConnection.isOnline(getContext(), getView())) {
            if (paramUpdateView.getTYPE_EVENT() == paramUpdateView.getALL_EVENS()) {
                selectEvents = new SelectEvents();
                selectEvents.execute();
            } else{
                selectEvents = new SelectEvents();
                selectEvents.execute();
                selectEvents_2 = new SelectEvents_2();
                selectEvents_2.execute();
            }
        }
           // }

        resources = getResources();
        //id_category = resources.getIntArray(R.array.id_category);
        //images = getResources().obtainTypedArray(R.array.category_random_picture);

        //здесь
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // используем linear layout manager
        mLayoutManager = new WrapContentLinearLayoutManage(getActivity(), LinearLayoutManager.VERTICAL, false);    //отличие
        mRecyclerView.setLayoutManager(mLayoutManager);

        progressBar.setVisibility(View.INVISIBLE);

//TODO:доработать, зависания при подгрузке
//        if(events == null) {
//            try {
//                selectEvents.get();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//        }
        //Listener
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //Ifscrolled at last then
                if (isLastItemDisplaying(mRecyclerView)) {
                    if (countAsyncTask < MAX_ASYNC_TASK) {
                        if(getConnection.isOnline(getContext(), getView())) {
                            if (paramUpdateView.getTYPE_EVENT() == paramUpdateView.getALL_EVENS()) {
                                selectEvents = new SelectEvents();
                                selectEvents.execute();
                            } else{
                                selectEvents = new SelectEvents();
                                selectEvents.execute();
                                selectEvents_2 = new SelectEvents_2();
                                selectEvents_2.execute();
                            }
                        }
                    }
                    //mAdapter = new AdapterRecycler(events, images, id_category);

//                    mAdapter.notifyDataSetChanged();

                    //recyclerView.setAdapter(new AdapterRecycler(events, images, id_category));
                    //recyclerView.invalidate();
                    //list.remove(position);
//                    recycler.removeViewAt(position);
//                    mAdapter.notifyItemRemoved(position);
//                    mAdapter.notifyItemRangeChanged(position, list.size());

                   // mAdapter.notifyDataSetChanged();
                }
            }
        });


//        // создаем адаптер
//        mAdapter = new AdapterRecycler(events, images, id_category, getContext());
//        //mAdapter = new AdapterRecycler(category, user_names, name_events, brief_events, images, id_category);
//        mRecyclerView.setAdapter(mAdapter);

        //RequestQueue
        return view;
    }

    //TODO:разобраться с гонкой потоков
    class SelectEvents extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            countAsyncTask++;
            if(getView() != null){

                swipeRefreshLayout.setRefreshing(true);
            }
        }
        protected Void doInBackground(Void... params) {
            connectionToWCF = ConnectionToWCF.getInstance();
            paramUpdateView = ParamUpdateView.getInstance();
            userInformation = UserInformation.getInstance();

            SoapTask request = null;
            SoapObject resultsRequestSOAP = null;

            String currentMethod = "";


            if (paramUpdateView.getTYPE_EVENT() == paramUpdateView.getALL_EVENS()){
                request = new SoapTask(connectionToWCF.NAMESPACE, connectionToWCF.METHOD_SELECT_ALL_EVENTS);
                request.AddProperty("startID", requestCount);
                request.AddProperty("top", SELECTION_COUNT);
                currentMethod = connectionToWCF.METHOD_SELECT_ALL_EVENTS;
            } else if (paramUpdateView.getTYPE_EVENT() == paramUpdateView.getMY_FUTURE_EVENS()){

                request = new SoapTask(connectionToWCF.NAMESPACE, connectionToWCF.METHOD_SELECT_ALL_EVENTS_CREATOR);
                request.AddProperty("startID", requestCount);
                request.AddProperty("userID", String.valueOf(userInformation.getUserID()));
                request.AddProperty("top", SELECTION_COUNT);
                currentMethod = connectionToWCF.METHOD_SELECT_ALL_EVENTS_CREATOR;

            }
            try{
                resultsRequestSOAP =
                        request.startSoap(connectionToWCF.URL,connectionToWCF.SOAP_ACTION(currentMethod));
            }
            catch (Exception e ){
                //Log.e("ex", e.toString());
            }

            if (resultsRequestSOAP != null) {
                if (resultsRequestSOAP.getProperty(0) != null) {

                    String response = resultsRequestSOAP.getProperty(0).toString();
                    workingWithJSON = new WorkingWithJSON(response);

                    if (events == null) {
                        events = workingWithJSON.getAllEvent();
                    } else {
                        List<Event> temp = workingWithJSON.getAllEvent();

                        for (Event event : temp) {
                            events.add(event);
                        }
                    }
                    requestCount = workingWithJSON.getIndexNextPage() + 1;
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            countAsyncTask--;

            if(d < 1 && events != null) {
                // создаем адаптер
                mAdapter = new AdapterRecycler(events, id_category, getContext());
                //mAdapter = new AdapterRecycler(category, user_names, name_events, brief_events, images, id_category);
                mRecyclerView.setAdapter(mAdapter);
            }
            d++;

            if(swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);
        if(mAdapter != null)
            mAdapter.notifyDataSetChanged();
        }
    }

    //TODO:разобраться с гонкой потоков
    class SelectEvents_2 extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            countAsyncTask++;
            //Log.e("PreCountThread", String.valueOf(countAsyncTask));
            if(getView() != null){

                swipeRefreshLayout.setRefreshing(true);
            }
        }
        protected Void doInBackground(Void... params) {
            connectionToWCF = ConnectionToWCF.getInstance();
            paramUpdateView = ParamUpdateView.getInstance();
            userInformation = UserInformation.getInstance();

            SoapTask request_2 = null;
            SoapObject resultsRequestSOAP_2 = null;

            String currentMethod_2 = "";

            if (paramUpdateView.getTYPE_EVENT() == paramUpdateView.getMY_FUTURE_EVENS()){
                request_2 = new SoapTask(connectionToWCF.NAMESPACE, connectionToWCF.METHOD_SELECT_ALL_EVENTS_NOT_CREATOR);
                request_2.AddProperty("startID", requestCount_2);
                request_2.AddProperty("userID", userInformation.getUserID());
                request_2.AddProperty("top", SELECTION_COUNT);
                currentMethod_2 = connectionToWCF.METHOD_SELECT_ALL_EVENTS_NOT_CREATOR;
            }
            else if(paramUpdateView.getTYPE_EVENT() == paramUpdateView.getMY_PAST_EVENS()){
                //TODO:заглушка TYPE_PAST
                return null;
            }

            try{
                resultsRequestSOAP_2 =
                        request_2.startSoap(connectionToWCF.URL,connectionToWCF.SOAP_ACTION(currentMethod_2));
            }
            catch (Exception e ){
                //Log.e("ex", e.toString());
            }

            if (resultsRequestSOAP_2 != null) {
                if (resultsRequestSOAP_2.getProperty(0) != null) {

                    String response = resultsRequestSOAP_2.getProperty(0).toString();
                    workingWithJSON = new WorkingWithJSON(response);

                    if (events == null) {
                        events = workingWithJSON.getAllEvent();
                    } else {
                        List<Event> temp = workingWithJSON.getAllEvent();

                        for (Event event : temp) {
                            events.add(event);
                        }
                    }
                    requestCount_2 = workingWithJSON.getIndexNextPage() + 1;
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            countAsyncTask--;

            if(d_2 < 1 && events != null) {
                // создаем адаптер
                mAdapter = new AdapterRecycler(events, id_category, getContext());
                //mAdapter = new AdapterRecycler(category, user_names, name_events, brief_events, images, id_category);
                mRecyclerView.setAdapter(mAdapter);
            }
            d_2++;

            if(swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);

            if(mAdapter != null)
                mAdapter.notifyDataSetChanged();
        }
    }

    //This method would check that the recyclerview scroll has reached the bottom or not
    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if(recyclerView.getAdapter() != null) {
            if (recyclerView.getAdapter().getItemCount() != 0) {

                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int qwe = recyclerView.getAdapter().getItemCount();
                if (lastVisibleItemPosition != RecyclerView.NO_POSITION
                        && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                    return true;
//            else if (lastVisibleItemPosition != RecyclerView.NO_POSITION
//                    && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount()
//                    && recyclerView.getAdapter().getItemCount() > 3)
//                return true;
            }
        }
        return false;
    }
    private void ClearAllConstants(){
        if (events != null){
            events = null;
            d = 0;
            d_2 = 0;
            requestCount = 1;
            requestCount_2 = 1;
            //events.clear();
        }
    }
}
