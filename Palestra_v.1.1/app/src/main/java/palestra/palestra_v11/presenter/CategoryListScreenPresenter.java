package palestra.palestra_v11.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import palestra.palestra_v11.R;
import palestra.palestra_v11.interfaces.ICategoryScreen;

/**
 * Created by Dmitry on 27.07.2017.
 */
@InjectViewState
public class CategoryListScreenPresenter extends MvpPresenter<ICategoryScreen> {
    public CategoryListScreenPresenter() {
        getViewState().showMessage(R.string.app_name);
    }

    public void init() {

    }
}
