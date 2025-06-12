package ru.pick.android;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;


import java.util.Map;
import java.util.HashMap;
import android.os.Bundle;
import android.widget.Toast;




import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

import ru.pick.FirebaseManager;
import ru.pick.Main;
import ru.pick.OrientationHelper;
import ru.pick.Player;
import ru.pick.SpaceButton;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        configuration.useImmersiveMode = true;


        FirebaseApp.initializeApp(this);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG);

        OrientationHelper.setListener(new OrientationHelper.OrientationListener() {

            @Override
            public void lockCurrent() {
                runOnUiThread(() -> {
                    int orientation = getResources().getConfiguration().orientation;
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    } else {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }
                });
            }


            @Override
            public void unlock() {
                runOnUiThread(() ->
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR));
            }
        });
        OrientationHelper.setProvider(new  OrientationHelper.OrientationProvider() {
            @Override
            public OrientationHelper.ScreenOrientation getOrientation() {
                int orientation = getResources().getConfiguration().orientation;
                return orientation == Configuration.ORIENTATION_LANDSCAPE ?
                    OrientationHelper.ScreenOrientation.LANDSCAPE :
                    OrientationHelper.ScreenOrientation.PORTRAIT;
            }
        });
        initialize(new Main(), configuration);
       //

    }
    /// не используется автоповорт из AndroidManifest т.к в игре присутствуют уровни с акселерометром и во время их прохождения необходимо отключать автоповорот
}
