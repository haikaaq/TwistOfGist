package ru.pick;

import com.badlogic.gdx.Gdx;
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

    @Override
    public void savePlayer(Player player) {
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();
        String json = gson.toJson(player); // Автоматическая конвертация объекта в JSON

        try {
            if (player.firebaseId == null) {
                // Создаем новую запись (POST)
                Request request = new Request.Builder()
                    .url(DB_URL + ".json")
                    .post(RequestBody.create(json, MediaType.get("application/json")))
                    .build();

                Response response = client.newCall(request).execute();

                if (response.isSuccessful() && response.body() != null) {
                    // Парсим ответ через Gson
                    JsonObject jsonResponse = gson.fromJson(response.body().charStream(), JsonObject.class);
                    player.firebaseId = jsonResponse.get("name").getAsString();
                    Gdx.app.log("Firebase", "Создана новая запись с ID: " + player.firebaseId);
                }
            } else {
                // Обновляем существующую запись (PATCH для частичного обновления)
                Request request = new Request.Builder()
                    .url(DB_URL  + player.firebaseId + ".json")
                    .patch(RequestBody.create(json, MediaType.get("application/json")))
                    .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    Gdx.app.log("Firebase", "Данные игрока обновлены");
                }
            }
        } catch (Exception e) {
            Gdx.app.error("Firebase", "Ошибка: " + e.getMessage());
        }
    }


   // @Override
   // public void getLeaderboard(FirebaseManager.LeaderboardCallback callback) {


          /*  OkHttpClient client = new OkHttpClient();
            String url = DB_URL + ".json?orderBy=\"level\"&limitToLast=10";

            Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onError(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String json = response.body().string();
                        JsonObject data = new Gson().fromJson(json, JsonObject.class);
                        List<Player> players = new ArrayList<>();

                        for (Map.Entry<String, JsonElement> entry : data.entrySet()) {
                            Player player = new Gson().fromJson(entry.getValue(), Player.class);
                            player.firebaseId = entry.getKey();
                            players.add(player);
                        }

                        // Сортировка DESC (REST API возвращает ASC)
                        players.sort((p1, p2) -> Integer.compare(p2.level, p1.level));
                        callback.onSuccess(players);
                    } else {
                        callback.onError("Ошибка: " + response.code());
                    }
                }
            });*/
     //   }
    @Override
    public void getTop10ByLevel(SortedLeaderboardCallback callback, boolean forceOnline) {

        String url = DB_URL + ".json?orderBy=\"level\"&limitToLast=10&print=pretty";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onError("network error"+e.getMessage());
                System.out.println("oshibka");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    if (!response.isSuccessful()) {
                        String errorBody = response.body() != null ? response.body().string() : "null";
                        callback.onError("HTTP " + response.code() + ": " + errorBody);
                        return;
                    }

                    assert response.body() != null;
                    String json = response.body().string();
                    Gdx.app.log("Firebase", "Raw JSON: " + json); // Логируем ответ

                    JsonObject data = JsonParser.parseString(json).getAsJsonObject();
                    List<Player> players = new ArrayList<>();

                    for (Map.Entry<String, JsonElement> entry : data.entrySet()) {
                        Player player = new Gson().fromJson(entry.getValue(), Player.class);
                        player.firebaseId = entry.getKey();
                        players.add(player);
                    }

                    // Сортировка
                    players.sort((p1, p2) -> {
                        int levelCompare = Integer.compare(p2.level, p1.level);
                        return levelCompare != 0 ? levelCompare : Integer.compare(p2.money, p1.money);
                    });

                    callback.onSuccess(players);
                } catch (Exception e) {
                    callback.onError("Parsing error: " + e.getMessage());
                }
            }
        });
    }
    public boolean isOnline() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("ping -c 1 google.com");
        } catch (IOException e) {
            return false;
        }

        try {
            int exitValue = process.waitFor();
            return exitValue == 0;
        } catch (InterruptedException e) {
            return false;
        }


     // Встроенный метод LibGDX
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

}

