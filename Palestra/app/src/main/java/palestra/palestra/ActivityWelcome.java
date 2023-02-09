package palestra.palestra;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by Dmitry on 28.03.2017.
 */

public class ActivityWelcome extends Activity {

    private ImageView button_login;
    private FrameLayout frameLayout;

    private FragmentTransaction transaction;
    private Fragment fragmentsWelcomeSliderFirst;
    private Fragment fragmentsWelcomeSliderSecond;
    private Fragment fragmentsWelcomeSliderThrid;
    private Fragment fragments_null;

    private Fragment fragmentsWelcomSliderFour;

    private PrefManager prefManager;
    private LinearLayout dotsLayout;
    private TextView[] dots;

    private final int FRAGMENT_COUNT = 3;
    private int POSITION_CURRENT_FRAGMENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow();
//            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//            // w.addFlags(WindowManager.ayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
//        else {
//            //TODO: сделать градиент, для версий API ниже 19!!!
//        }
        getWindow().setStatusBarColor(getResources().getColor(R.color.StatusBarAuthorization));
        super.onCreate(savedInstanceState);

        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

        setContentView(R.layout.activity_welcome);

        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        button_login = (ImageView)findViewById(R.id.button_login);

        fragmentsWelcomeSliderFirst = new FragmentWelcomeSliderFirst();
        fragmentsWelcomeSliderSecond = new FragmentWelcomeSliderSecond();
        fragmentsWelcomeSliderThrid = new FragmentWelcomeSliderThrid();

        fragments_null = new FragmentNull();
//        fragmentsRegistrationStepThree = new FragmentRegistrationStepThree();
//        fragmentsRegistrationStepFour = new FragmentRegistrationStepFour();
        frameLayout = (FrameLayout)findViewById(R.id.contentWelcomeSlider);

        transaction = getFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.contentWelcomeSlider, fragmentsWelcomeSliderFirst);
        transaction.addToBackStack(null);
        transaction.commit();

        // adding bottom dots
        addBottomDots(POSITION_CURRENT_FRAGMENT);


        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        frameLayout.setOnTouchListener(new OnSwipeTouchListener(ActivityWelcome.this) {
            @Override
            public void onSwipeLeft() {
                transaction = getFragmentManager().beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                if (fragmentsWelcomeSliderFirst.isVisible()) {
                    POSITION_CURRENT_FRAGMENT = 1;
                    transaction.replace(R.id.contentWelcomeSlider, fragmentsWelcomeSliderSecond);
                } else if(fragmentsWelcomeSliderSecond.isVisible()){
                    POSITION_CURRENT_FRAGMENT = 2;
                    transaction.replace(R.id.contentWelcomeSlider, fragmentsWelcomeSliderThrid);
                }
                transaction.addToBackStack(null);
                transaction.commit();
//                else if (fragmentsRegistrationStepTwo.isVisible()) {
//                    transaction.replace(R.id.content, fragmentsRegistrationStepThree);
//                    transaction.commit();
//                } else if (fragmentsRegistrationStepThree.isVisible()) {
//                    transaction.replace(R.id.content, fragmentsRegistrationStepFour);
//                    transaction.commit();
//                } else if (fragmentsRegistrationStepFour.isVisible()) {
//                    Intent intent = new Intent(ActivityRegistration.this, ActivityMain.class);
//                    startActivity(intent);
//                }
                addBottomDots(POSITION_CURRENT_FRAGMENT);
            }
            @Override
            public void onSwipeRight(){
                transaction = getFragmentManager().beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                if (fragmentsWelcomeSliderSecond.isVisible()) {
                    POSITION_CURRENT_FRAGMENT = 0;
                    transaction.replace(R.id.contentWelcomeSlider, fragmentsWelcomeSliderFirst);
                    transaction.commit();
                } else if(fragmentsWelcomeSliderThrid.isVisible()){
                    POSITION_CURRENT_FRAGMENT = 1;
                    transaction.replace(R.id.contentWelcomeSlider, fragmentsWelcomeSliderSecond);
                    transaction.commit();
                }
                addBottomDots(POSITION_CURRENT_FRAGMENT);
            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[FRAGMENT_COUNT];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#11044;") + " ");
            dots[i].setTextSize(getResources().getDimensionPixelSize(R.dimen.welcome_screen_dot_size_inactive));
            dots[i].setTextColor(getResources().getColor(R.color.dot_inactive));
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[currentPage].setTextColor(getResources().getColor(R.color.dot_active));
            dots[currentPage].setTextSize(getResources().getDimensionPixelSize(R.dimen.welcome_screen_dot_size_active));
        }
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(ActivityWelcome.this, ActivityAuthorization.class));
        finish();
    }
}
