package dk.ozgur.ubs;

import android.app.Application;

/**
 * Created by ozgur on 3/24/17.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Api.init(this);
    }
}
