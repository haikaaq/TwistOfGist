package ru.pick;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;

public class Levels {
    public static final Level[] LEVELS = {
        // Уровень 0
        new Level(
            "bggame.png",
            "enemyes.png",
            5,
            0,
            1450,
            200,
            9000,
            "level0",
            Level.SCREEN,
            false,
            false,
            false,
            false,
            false,
            false,
            false

        ),
        //1
        new Level(
            "bggame.png",
            "enemyes.png",
            10,
            1,
            1000,
            200,
            10,
            "level1",
            Level.SCREEN,
            false,
            false,
            true, true,
            false,
            false,
            false

        ),

        // Уровень 2
        new Level(
            "bggame.png",
            "enemyes.png",
            0,
            10,
            1000,
            200,
            0,
            "level2",
            Level.SCREEN,
            false,
            false,
            true, true,
            false,
            false,
            false


        ),

        // Уровень 3
        new Level(
            "bggame.png",
            "enemyes.png",
            10,
            5,
            1600,
            300,
            500,
            "level3",
            Level.ACCELEROMETER,
            false,
            false,
            true, true,
            false,
            false,
            false

        ),
        //4
        new Level(
            "bggame.png",
            "enemyes.png",
            6,
            4,
            1900,
            250,
            0,
            "level4",
            Level.SCREEN,
            false,
            true,
            false, true,
            false,
            false,
            false

        ),
        //5
        new Level(
            "bggame.png",
            "nothing.png",
            16,
            0,
            1000,
            150,
            0,
            "level5",
            Level.SCREEN,
            false,
            false,
            true, true,
            false,
            false,
            false

        ),
        //6
        new Level(
            "bggame.png",
            "enemyes.png",
            100,
            0,
            1400,
            200,
            0,
            "level6",
            Level.JOYSTIK_RIGHT,
            false,
            false,
            true, true,
            false,
            false,
            false


        ),
        //7
        new Level(
            "bggame.png",
            "enemyes.png",
            30,
            4,
            1400,
            200,
            0,
            "level7",
            Level.SCREEN,
            false,
            false,
            true,
            true,
            false,
            false,
            true


        ),
        //8
        new Level(
            "bggame.png",
            "enemyes.png",
            47,
            5,
            900,
            120,
            0,
            "level8",
            Level.SCREEN,
            true,
            false,
            true, false,
            true,
            false,
            false


        ),
        //9
        new Level(
            "bggame.png",
            "enemyes.png",
            20,
            5,
            2100,
            300,
            0,
            "level9",
            Level.SCREEN,
            false,
            false,
            true, true,
            false,
            true,
            false

        ),

        //10
        new Level(
            "bggame.png",
            "enemyes.png",
            9,
            0,
            3000,
            2000,
            0,
            "level10",
            Level.SCREEN,
            true,
            false,
            false, false,
            false,
            false,
            false

        )


    };

    public static class Level {
        Texture imgEnemyes;
        public final String backgroundPath;
        public final String enemyPath;
        public final int enemiesMax;
        public final int bossCount;
        public long timeSpawnInterval;
        public final long timeShotsInterval;
        public final long timeFirstSpawnEnemy;
        public final String numLevel;
        public static final int SCREEN = 0, JOYSTIK = 1, JOYSTIK_LEFT = 2, JOYSTIK_RIGHT = 3, ACCELEROMETER = 4;
        public final int controls;
        public boolean isRexlexLevel;
        public final boolean isKeyboard;
        public final boolean isBoss;
        public boolean isShots;
        public final boolean isSettingLevel;
        public final boolean isTapLevel;
        public final boolean isAccelerometrLevel;
        public final int MaxLevel = 10;


        public Level(String backgroundPathg, String enemyPath, int enemies, int bosses, long spawnEnemy, long spawnShots, long timeFirstSpawnEnemy, String numLevel, int controls, boolean isReflexLevel, boolean isKeyboard, boolean isBoss, boolean isShots, boolean isSettingLevel, boolean isTapLevel, boolean isAccelerometrLevel) {
            this.backgroundPath = backgroundPathg;
            this.enemiesMax = enemies;
            this.bossCount = bosses;
            this.timeSpawnInterval = spawnEnemy;
            this.timeShotsInterval = spawnShots;
            this.timeFirstSpawnEnemy = timeFirstSpawnEnemy;
            this.numLevel = numLevel;
            this.controls = controls;
            this.enemyPath = enemyPath;
            this.isRexlexLevel = isReflexLevel;
            this.isKeyboard = isKeyboard;
            this.isBoss = isBoss;
            this.isShots = isShots;
            this.isSettingLevel = isSettingLevel;
            this.isTapLevel = isTapLevel;
            this.isAccelerometrLevel = isAccelerometrLevel;
            imgEnemyes = new Texture(enemyPath);
        }


    }
}
