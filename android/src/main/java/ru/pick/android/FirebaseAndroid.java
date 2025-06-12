package ru.pick.android;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.badlogic.gdx.Gdx;
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
import ru.pick.LocalLeaderBoard;
import ru.pick.Player;

public class FirebaseAndroid implements FirebaseManager {
    private final Context context;
    private final DatabaseReference database;

    public FirebaseAndroid(Context context) {
        this.context = context;

        this.database = FirebaseDatabase.getInstance().getReference();}





    @Override
    public void savePlayer(Player player) {

        Gdx.app.log("FirebaseAndroid", "Saving player: " + player.name);
        //   FirebaseDatabase database = FirebaseDatabase.getInstance();

      //  DatabaseReference ref = database.getReference();

        // Если у игрока нет ID - создаем новую запись
        if (player.firebaseId == null) {
            DatabaseReference newRef = database.push();
            player.firebaseId = newRef.getKey();
            newRef.setValue(player).addOnSuccessListener(aVoid -> {
                    Gdx.app.log("Firebase", "Player saved! ID: " + player.firebaseId);
                })
                .addOnFailureListener(e -> {
                    Gdx.app.error("Firebase", "Save failed: " + e.getMessage());
                });
        }
        // Если ID есть - обновляем существующую
        else {
            database.child(player.firebaseId).setValue(player).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Gdx.app.log("Firebase", "Player updated");
                } else {
                    Gdx.app.error("Firebase", "Update failed", task.getException());
                }
            });
        }
    }


    @Override
    public void getTop10ByLevel(SortedLeaderboardCallback callback,boolean forceOnline) {
        if (!forceOnline && !isOnline()) {
            List<Player> cached = LocalLeaderBoard.load();
            callback.onSuccess(cached);
            return;
        }

        FirebaseDatabase.getInstance()
            .getReference()
            .orderByChild("level")
            .limitToLast(10)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NotNull DataSnapshot snapshot) {
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
                    LocalLeaderBoard.save(players); // Кешируем
                    callback.onSuccess(players);

                }

                @Override
                public void onCancelled(@NotNull DatabaseError error) {
                    List<Player> cached = LocalLeaderBoard.load();
                    callback.onSuccess(cached);

                }
            });
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

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
                        callback.onError("Player not found in leaderboard");
                    }
                }

                @Override
                public void onCancelled(@NotNull DatabaseError error) {
                    callback.onError(error.getMessage());
                }
            });
    }
}
