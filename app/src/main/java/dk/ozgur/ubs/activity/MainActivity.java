package dk.ozgur.ubs.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.ControllerChangeHandler;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;

import java.util.HashMap;

import dk.ozgur.ubs.controller.BaseController;
import dk.ozgur.ubs.controller.GradesHistoryController;
import dk.ozgur.ubs.controller.HomeController;
import dk.ozgur.ubs.controller.WeeklyScheduleController;
import dk.ozgur.ubs.controller.GradesController;
import dk.ozgur.ubs.R;
import dk.ozgur.ubs.User;

public class MainActivity extends BaseActivity {

    private Router router;
    private FrameLayout mContentFrame;
    private ProgressBar mProgressBar;
    private BottomNavigationView mBottomNavigationView;
    private static HashMap<Class, Integer> bottomMenuHashMap = new HashMap<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {

            mContentFrame.removeAllViews();

            switch (item.getItemId()) {

                case R.id.MenuItemWeeklySchedule:
                    openController(new WeeklyScheduleController());
                    return true;

                case R.id.MenuItemHome:
                    openController(new HomeController());
                    return true;

                case R.id.MenuItemGrades:
                    openController(new GradesController());
                    return true;
            }

            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomMenuHashMap.put(WeeklyScheduleController.class, 0);
        bottomMenuHashMap.put(HomeController.class, 1);

        bottomMenuHashMap.put(GradesController.class, 2);
        bottomMenuHashMap.put(GradesHistoryController.class, 2);

        mContentFrame = (FrameLayout) findViewById(R.id.Container);
        mProgressBar = (ProgressBar) findViewById(R.id.ProgressBar);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.BottomNavigationView);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        checkLogin();

        router = Conductor.attachRouter(this, mContentFrame, savedInstanceState);

        // open default
        openController(new HomeController());

        // route change listener

        router.addChangeListener(new ControllerChangeHandler.ControllerChangeListener() {
            @Override
            public void onChangeStarted(@Nullable Controller to, @Nullable Controller from, boolean isPush, @NonNull ViewGroup container, @NonNull ControllerChangeHandler handler) {}

            @Override
            public void onChangeCompleted(@Nullable Controller to, @Nullable Controller from, boolean isPush, @NonNull ViewGroup container, @NonNull ControllerChangeHandler handler) {

                if(to == null) {
                    return;
                }

                updateBottomMenu(bottomMenuHashMap.get(to.getClass()));
            }
        });

    }

    private void checkLogin() {
        if(!new User(this).isLogged()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    public void openController(BaseController controller) {
        router.pushController(RouterTransaction.with(controller));
        updateBottomMenu(bottomMenuHashMap.get(controller.getClass()));
        logControllerOpen(controller.getControllerName());
    }

    private void updateBottomMenu(int pos) {
        Menu menu = mBottomNavigationView.getMenu();
        MenuItem item = menu.getItem(pos);
        item.setChecked(true);
    }

    @Override
    public void onBackPressed() {

        if(router.handleBack()) {
            // noop
        } else {
            super.onBackPressed();
        }
    }

    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }
}
