package ru.pick;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class FirstRunManager {
    private static final String FIRST_RUN_KEY = "isFirstRun";

    public static boolean isFirstRun() {
        Preferences prefs = Gdx.app.getPreferences("игровые ресурсы");
        // Если ключа нет - возвращаем true (первый запуск)
        return prefs.getBoolean(FIRST_RUN_KEY, true);
    }

    public static void setFirstRunCompleted() {
        Preferences prefs = Gdx.app.getPreferences("игровые ресурсы");
        prefs.putBoolean(FIRST_RUN_KEY, false);
        prefs.flush();
    }
}
