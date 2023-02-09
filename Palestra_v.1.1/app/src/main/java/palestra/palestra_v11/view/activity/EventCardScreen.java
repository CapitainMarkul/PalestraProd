package palestra.palestra_v11.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.ArrayList;

import palestra.palestra_v11.EventInfoRoot;
import palestra.palestra_v11.R;
import palestra.palestra_v11.adapter.RootAdapter;
import palestra.palestra_v11.interfaces.IEventCard;
import palestra.palestra_v11.presenter.EventCardScreenPresenter;

/**
 * Created by Dmitry on 30.07.2017.
 */

public class EventCardScreen extends MvpAppCompatActivity implements IEventCard {

    @InjectPresenter
    EventCardScreenPresenter mPresenter;

    private RecyclerView recyclerView;
    private RootAdapter rootAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Далее все ТЕСТ! Нужно создать свою Layout со своим Recycle  + MVP
        setContentView(R.layout.screen_category);
        recyclerView = (RecyclerView) findViewById(R.id.category_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(int message) {

    }

    @Override
    public void showListEvent(ArrayList<EventInfoRoot> listEvent) {
        rootAdapter = new RootAdapter(listEvent, getBaseContext());
        recyclerView.setAdapter(rootAdapter);
    }
}
