package ru.pick.android;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.pick.FirebaseDesktop;
import ru.pick.FirebaseManager;

import ru.pick.Player;

public class FirebaseAndroid implements FirebaseManager {
    private final Context context;
    private final DatabaseReference database;
    public boolean online;
    public FirebaseAndroid(Context context) {
        this.context = context;
        this.database = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void savePlayer(Player player) {
        Gdx.app.log("FirebaseAndroid", "Saving player: " + player.name);
        player.firebaseId = loadFirebaseIdFromPrefs();

        // Если у игрока нет ID - создаем новую запись
        if (player.firebaseId == null) {
            DatabaseReference newRef = database.push();
            player.firebaseId = newRef.getKey();
            saveFirebaseIdToPrefs(player.firebaseId);

            newRef.setValue(player).addOnSuccessListener(aVoid -> {
                    Gdx.app.log("Firebase", "Player saved! ID: " + player.firebaseId);
                })
                .addOnFailureListener(e -> {
                    online = false;
                });
        }
        // Если ID есть - обновляем существующую
        else {
            database.child(player.firebaseId).setValue(player).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    online=true;
                    Gdx.app.log("Firebase", "Player updated");
                } else {
                    online = false;
                }
            });
        }
    }

    @Override
    public void getTop10ByLevel(SortedLeaderboardCallback callback) {
        FirebaseDatabase.getInstance()
            .getReference()
            .orderByChild("level")
            .limitToLast(10)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NotNull DataSnapshot snapshot) {
                    online=true;
                    List<Player> players = new ArrayList<>();

                    for (DataSnapshot child : snapshot.getChildren()) {
                        Player player = child.getValue(Player.class);
                        assert player != null;
                        player.firebaseId = child.getKey();
                        players.add(player);
                        Gdx.app.log("Firebase", "Loaded player: " + player.name); // Логируем
                    }
                    players.sort( (p1, p2) -> {
                        if (p1.level != p2.level) {
                            return Integer.compare(p2.level, p1.level); // Уровень ↓
                        } else {
                            return Integer.compare(p2.money, p1.money); // Деньги ↓
                        }
                    });
                    callback.onSuccess(players);
                }

                @Override
                public void onCancelled(@NotNull DatabaseError error) {
                    online = false;
                }
            });
    }
    @Override
    public void getPlayerRank(Player targetPlayer, PlayerRankCallback callback) {
        FirebaseDatabase.getInstance()
            .getReference()
            .orderByChild("level")
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NotNull DataSnapshot snapshot) {
                    List<Player> allPlayers = new ArrayList<>();

                    // Собираем всех игроков
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Player player = child.getValue(Player.class);
                        if (player != null) {
                            player.firebaseId = child.getKey();
                            allPlayers.add(player);
                        }
                    }

                    // Сортируем как в топе
                    allPlayers.sort((p1, p2) -> {
                        int levelCompare = Integer.compare(p2.level, p1.level);
                        return levelCompare != 0 ? levelCompare : Integer.compare(p2.money, p1.money);
                    });

                    // Находим позицию игрока
                    int rank = -1;
                    for (int i = 0; i < allPlayers.size(); i++) {
                        if (allPlayers.get(i).firebaseId.equals(targetPlayer.firebaseId)) {
                            rank = i + 1; // Позиция в рейтинге (начинается с 1)
                            break;
                        }
                    }

                    if (rank != -1) {
                        callback.onSuccess(rank);
                    } else {
                        online = false;
                        // callback.onError("Player not found in leaderboard");
                    }
                }
                @Override
                public void onCancelled(@NotNull DatabaseError error) {
                    online = false;
                }
            });
    }
    public boolean isOnline(){
        return online;
    }

    public String loadFirebaseIdFromPrefs() {
        Preferences prefs = Gdx.app.getPreferences("игровые ресурсы");
        return prefs.getString("firebaseId", null); // null - значение по умолчанию, если ID не найден
    }

    private void saveFirebaseIdToPrefs(String firebaseId) {
        Preferences prefs = Gdx.app.getPreferences("игровые ресурсы");
        prefs.putString("firebaseId", firebaseId);
        prefs.flush();
    }
}
