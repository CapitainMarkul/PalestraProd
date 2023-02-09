package palestra.palestra_v11.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;

import palestra.palestra_v11.EventInfoRoot;
import palestra.palestra_v11.interfaces.IEventCard;

/**
 * Created by Dmitry on 30.07.2017.
 */
@InjectViewState
public class EventCardScreenPresenter extends MvpPresenter<IEventCard> {
    public EventCardScreenPresenter() {
        getViewState().showListEvent(createMass());
    }

    private ArrayList<EventInfoRoot> createMass(){
        ArrayList<EventInfoRoot> result = new ArrayList<>();
        ArrayList<EventInfoRoot.EventInfoChildren> resChild = new ArrayList<>();
        resChild.add(new EventInfoRoot.EventInfoChildren("5", "2", "02-04-2094", 12, 32));
        result.add(new EventInfoRoot("Привет, я тест", "Брифинг", "Приключения", "Мальчик",
                1, 2, 1, 2, 16, resChild));


        ArrayList<EventInfoRoot.EventInfoChildren> resChild2 = new ArrayList<>();
        resChild2.add(new EventInfoRoot.EventInfoChildren("5", "2", "02-04-2094", 12, 32));

        result.add(new EventInfoRoot("Привет, я тест 2 2 2", "Брифинг", "Музяка", "Мальчик4",
                1, 2, 1, 2, 16, resChild2));


        ArrayList<EventInfoRoot.EventInfoChildren> resChild3 = new ArrayList<>();
        resChild3.add(new EventInfoRoot.EventInfoChildren("5", "2", "02-04-2094", 12, 32));

        result.add(new EventInfoRoot("Привет, я тест 3 3 3", "Брифинг hsdfhs dkfhsiu", "Приключения", "Мальчик",
                1, 2, 1, 2, 16, resChild3));

        return  result;
    }
}
