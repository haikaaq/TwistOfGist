package ru.pick;

import com.badlogic.gdx.graphics.Texture;

public class Levels {
    public static final Level[] LEVELS = {
        // Уровень 0
        new Level(
            "bggame.png",
            "enemyes.png",
            15,
            0,
            1450,
            200,
            9000,
            1234234567,
            "level0",
            Level.SCREEN,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            true,
            false,
            30000,
            10000

        ),
        //1
        new Level(
            "bggame.png",
            "enemyes.png",
            25,
            1,
            1000,
            200,
            10,
            20000,
            "level1",
            Level.SCREEN,
            false,
            false,
            true, true,
            false,
            false,
            false,
            false,
            false,
            12000,
            11000

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
            15000,
            "level2",
            Level.JOYSTIK_RIGHT,
            false,
            false,
            true, true,
            false,
            false,
            false,
            false,
            false,
            999999999,
            10000


        ),

        // Уровень 3
        new Level(
            "bggame.png",
            "enemyes.png",
            30,
            5,
            1600,
            300,
            500,
            25000,
            "level3",
            Level.ACCELEROMETER,
            false,
            false,
            true, true,
            false,
            false,
            false,
            false,
            true,
            20000,
            10000

        ),
        //4
        new Level(
            "bggame.png",
            "enemyes.png",
            30,
            6,
            1900,
            250,
            0,
            60000,
            "level4",
            Level.SCREEN,
            false,
            true,
            false, true,
            false,
            false,
            false,
            false,
            false,
            999999999,
            12000

        ),
        //5
        new Level(
            "bggame.png",
            "nothing.png",
            25,
            0,
            1000,
            150,
            0,
            17000,
            "level5",
            Level.SCREEN,
            false,
            false,
            true, true,
            false,
            false,
            false,
            false,
            false,
            11000,
            10000

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
            16000,
            "level6",
            Level.JOYSTIK_LEFT,
            false,
            false,
            true, true,
            false,
            false,
            false,
            false,
            true,
            14000,
            10000


        ),
        //7
        new Level(
            "bggame.png",
            "enemyes.png",
            30,
            5,
            1400,
            200,
            0,
            99999999,
            "level7",
            Level.SCREEN,
            false,
            false,
            true,
            true,
            false,
            false,
            true,
            false,
            false,
            23000,
            10000


        ),
        //8
        new Level(
            "bggame.png",
            "enemyes.png",
            20,
            0,
            2800,
            120,
            0,
            3000,
            "level8",
            Level.SCREEN,
            true,
            false,
            false, false,
            true,
            false,
            false,
            false,
            false,
            9500,
            999999999


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
            15000,
            "level9",
            Level.JOYSTIK_LEFT,
            false,
            false,
            true, true,
            false,
            true,
            false,
            false,
            false,
            10000,
            10000

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
            99999999,
            "level10",
            Level.SCREEN,
            true,
            false,
            false, false,
            false,
            false,
            false,
            false,
            false,
            999999999,
            10000

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
        public boolean isReflexLevel;
        public final boolean isNums;
        public final boolean isBoss;
        public boolean isShots;
        public final boolean isSettingLevel;
        public final long timeRocketInterwal;
        ;
        public final boolean isZeroLevel;
        public final boolean isTapLevel;
        public final boolean isAccelerometrLevel;
        public final boolean isStoneLevel;
        public final int MaxLevel = 10;
        public final long timeShieldInterval;
        public final long timeBoostInterval;

        public Level(String backgroundPathg, String enemyPath, int enemies, int bosses, long spawnEnemy,
                     long spawnShots, long timeFirstSpawnEnemy, long timeRocketInterwal, String numLevel, int controls, boolean isReflexLevel,
                     boolean isNums, boolean isBoss, boolean isShots, boolean isSettingLevel, boolean isTapLevel,
                     boolean isAccelerometrLevel, boolean isZeroLevel, boolean isStoneLevel, long timeShieldInterval,
                     long timeBoostInterval
        ) {
            this.backgroundPath = backgroundPathg;
            this.enemiesMax = enemies;
            this.bossCount = bosses;
            this.timeSpawnInterval = spawnEnemy;
            this.timeShotsInterval = spawnShots;
            this.timeFirstSpawnEnemy = timeFirstSpawnEnemy;
            this.timeRocketInterwal = timeRocketInterwal;
            this.numLevel = numLevel;
            this.controls = controls;
            this.enemyPath = enemyPath;
            this.isReflexLevel = isReflexLevel;
            this.isNums = isNums;
            this.isBoss = isBoss;
            this.isShots = isShots;
            this.isSettingLevel = isSettingLevel;
            this.isTapLevel = isTapLevel;
            this.isZeroLevel = isZeroLevel;
            this.isStoneLevel = isStoneLevel;
            this.timeShieldInterval = timeShieldInterval;
            this.timeBoostInterval = timeBoostInterval;

            this.isAccelerometrLevel = isAccelerometrLevel;
            imgEnemyes = new Texture(enemyPath);

        }


    }
}
