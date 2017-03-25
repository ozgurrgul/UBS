package dk.ozgur.ubs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by ozgur on 3/24/17.
 */

public class BaseActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    public void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

    public void toast(int resId) {
        toast(getResources().getString(resId));
    }

    public void logControllerOpen(String controllerName) {
        Bundle bundle = new Bundle();
        bundle.putString("name", controllerName);
        mFirebaseAnalytics.logEvent("controller_open", bundle);
    }

}
