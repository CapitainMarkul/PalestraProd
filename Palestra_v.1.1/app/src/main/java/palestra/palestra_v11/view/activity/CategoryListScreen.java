package palestra.palestra_v11.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.widget.LinearLayout;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import palestra.palestra_v11.R;
import palestra.palestra_v11.interfaces.ICategoryScreen;
import palestra.palestra_v11.presenter.CategoryListScreenPresenter;

/**
 * Created by Dmitry on 26.07.2017.
 */

public class CategoryListScreen extends MvpAppCompatActivity implements ICategoryScreen {
    @InjectPresenter
    CategoryListScreenPresenter mPresenter;

    private LinearLayout rootContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_category);

        rootContainer = (LinearLayout) findViewById(R.id.category_screen_root_container);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(int message) {
        Snackbar.make(rootContainer, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showListCategory() {

    }
}
