
package ru.pick;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class FirebaseDesktop implements FirebaseManager {
    private static final String DB_URL = "https://twistofgist-default-rtdb.europe-west1.firebasedatabase.app/";
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    public boolean online;

    @Override
    public void savePlayer(Player player) {
        String firebaseId = loadFirebaseIdFromPrefs();
        player.firebaseId = firebaseId;
        String json = gson.toJson(player);

        Request request;
        if (firebaseId == null) {
            // Создаем новую запись
            request = new Request.Builder()
                    .url(DB_URL + ".json")
                    .post(RequestBody.create(json, MediaType.get("application/json")))
                    .build();
            } else {
            // Обновляем существующую запись
            request = new Request.Builder()
                .url(DB_URL + firebaseId + ".json")
                    .patch(RequestBody.create(json, MediaType.get("application/json")))
                    .build();
            }

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Gdx.app.error("Firebase", "Save failed: " + e.getMessage());
                online = false;
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        Gdx.app.error("Firebase", "Save error: " + response.code());
                        return;
                    }

                if (firebaseId == null && response.body() != null) {
                    JsonObject jsonResponse = gson.fromJson(response.body().charStream(), JsonObject.class);
                    String newId = jsonResponse.get("name").getAsString();
                    player.firebaseId = newId;
                    saveFirebaseIdToPrefs(newId);
                    Gdx.app.log("Firebase", "New player ID: " + newId);
                } else {
                    Gdx.app.log("Firebase", "Player updated");
                    }
                    online = true;
            }
        });
    }

    @Override
    public void getTop10ByLevel(SortedLeaderboardCallback callback) {
        Request request = new Request.Builder()
            .url(DB_URL + ".json?orderBy=\"level\"&limitToLast=10")
            .get()
            .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Gdx.app.postRunnable(() -> {
                    online = false;
                    callback.onError(e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    if (!response.isSuccessful()) {
                        throw new IOException("HTTP " + response.code());
                    }

                    String jsonData = response.body().string();
                    JsonObject data = gson.fromJson(jsonData, JsonObject.class);
                    List<Player> players = new ArrayList<>();

                    for (Map.Entry<String, JsonElement> entry : data.entrySet()) {
                        Player player = gson.fromJson(entry.getValue(), Player.class);
                        player.firebaseId = entry.getKey();
                        players.add(player);
                    }

                    players.sort((p1, p2) -> {
                        int levelCompare = Integer.compare(p2.level, p1.level);
                        return levelCompare != 0 ? levelCompare : Integer.compare(p2.money, p1.money);
                    });
                    Gdx.app.postRunnable(() -> {
                        online = true;
                        callback.onSuccess(players);
                    });
                } catch (Exception e) {
                    Gdx.app.postRunnable(() -> callback.onError(e.getMessage()));
                }
            }
        });
    }

    @Override
    public void getPlayerRank(Player targetPlayer, PlayerRankCallback callback) {
        String url = DB_URL + ".json?orderBy=\"level\"&limitToLast=10&print=pretty";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    JsonObject data = JsonParser.parseString(response.body().string()).getAsJsonObject();
                    List<Player> allPlayers = new ArrayList<>();

                    // Парсим всех игроков
                    for (Map.Entry<String, JsonElement> entry : data.entrySet()) {
                        Player player = new Gson().fromJson(entry.getValue(), Player.class);
                        player.firebaseId = entry.getKey();
                        allPlayers.add(player);
                    }

                    // Сортировка
                    allPlayers.sort((p1, p2) -> {
                        int levelCompare = Integer.compare(p2.level, p1.level);
                        return levelCompare != 0 ? levelCompare : Integer.compare(p2.money, p1.money);
                    });

                    // Поиск позиции
                    int rank = IntStream.range(0, allPlayers.size())
                        .filter(i -> allPlayers.get(i).firebaseId.equals(targetPlayer.firebaseId))
                        .findFirst()
                        .orElse(-1) + 1; // +1 чтобы сделать рейтинг от 1

                    if (rank > 0) {
                        callback.onSuccess(rank);
                    } else {
                        callback.onError("Player not found");
                    }
                } else {
                    callback.onError("HTTP error: " + response.code());
                }
            }
        });
    }

    // Остальные методы остаются без изменений
    public boolean isOnline() {
        return online;
    }

    private String loadFirebaseIdFromPrefs() {
        Preferences prefs = Gdx.app.getPreferences("игровые ресурсы");
        return prefs.getString("firebaseId", null);
    }

    private void saveFirebaseIdToPrefs(String firebaseId) {
        Preferences prefs = Gdx.app.getPreferences("игровые ресурсы");
        prefs.putString("firebaseId", firebaseId);
        prefs.flush();
    }
}
