package ru.pick;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class LocalLeaderBoard {
    private static final String PREFS_NAME = "LeaderboardCache";
    private static final String KEY_DATA = "leaderboard_data";

    public static void save(List<Player> players) {
        Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
        String json = new Gson().toJson(players);
        prefs.putString(KEY_DATA, json);
        prefs.flush();
    }

    public static List<Player> load() {
        Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
        String json = prefs.getString(KEY_DATA, "");

        if (json.isEmpty()) {

            return createDefaultLeaderboard();
        }

        Type type = new TypeToken<List<Player>>(){}.getType();
        return new Gson().fromJson(json, type);
    }

    private static List<Player> createDefaultLeaderboard() {
        List<Player> defaultPlayers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            defaultPlayers.add(new Player());
        }
        return defaultPlayers;
    }
}
