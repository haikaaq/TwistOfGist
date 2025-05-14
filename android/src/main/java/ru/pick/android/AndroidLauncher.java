package ru.pick.android;

import android.content.pm.ActivityInfo;
import android.os.Bundle;


import java.util.Map;
import java.util.HashMap;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import ru.pick.Main;
import ru.pick.Player;
import ru.pick.SpaceButton;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        configuration.useImmersiveMode = true; // Recommended, but not required.
        initialize(new Main(), configuration);
    }
}
