package ru.pick;

import java.util.List;

public interface FirebaseManager {
    void savePlayer(Player player);
    /*void getLeaderboard(LeaderboardCallback callback);

  interface LeaderboardCallback {
        void onSuccess(List<Player> players);
        void onError(String message);
    }*/
    boolean isOnline();
    void getPlayerRank(Player targetPlayer, PlayerRankCallback callback);

    interface PlayerRankCallback {
        void onSuccess(int rank); // Позиция в рейтинге (1 = первое место)
        void onError(String message);
    }
    void getTop10ByLevel(SortedLeaderboardCallback callback, boolean foeceOnline);

    interface SortedLeaderboardCallback {
        void onSuccess(List<Player> sortedPlayers); // Готовый отсортированный список
        void onError(String message);
    }

}
