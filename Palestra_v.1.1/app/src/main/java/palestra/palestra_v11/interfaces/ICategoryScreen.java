package palestra.palestra_v11.interfaces;

import com.arellomobile.mvp.MvpView;

/**
 * Created by Dmitry on 29.07.2017.
 */

public interface ICategoryScreen extends MvpView, ILoading, IMessage {
    void showListCategory();
}
