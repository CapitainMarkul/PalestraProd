package palestra.palestra_v11.interfaces;

import com.arellomobile.mvp.MvpView;

import java.util.ArrayList;

import palestra.palestra_v11.EventInfoRoot;

/**
 * Created by Dmitry on 30.07.2017.
 */

public interface IEventCard extends MvpView, ILoading, IMessage  {
    void showListEvent(ArrayList<EventInfoRoot> listEvent);
}
