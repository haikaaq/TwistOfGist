package ru.pick;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;


public class Main extends Game {

    public static final float SCR_WIDTH = 900;
    public static final float SCR_HEIGHT = 1600;


    public static final int SCREEN = 0, JOYSTIK = 1, JOYSTIK_LEFT = 2, JOYSTIK_RIGHT = 3, ACCELEROMETER = 4;
    public static int controls = SCREEN;
    public static final int GAME_OVER = 1, GAME = 0;
    public static int gameState = 1;
    public AssetManager manager;
    public SpriteBatch batch;
    public OrthographicCamera camera;
    public Vector3 touch;
    public BitmapFont font70;
    public BitmapFont font32;
    public boolean isPlayMove = true;
    public ScreenMenu screenMenu;
    public ScreenGame screenGame;
    public ScreenShop screenShop;
    public LoadingScreen screenLoading;
    public ScreenSettings screenSettings;
    public ScreenLeaderboard screenLeaderboard;
    public ScreenAbout screenAbout;
    public ScreenNumLevel screenNumLevel;
    public Music FonMusic;
    public int level;
    public int shipSkin;
    public int shotsShots;
    public int shotEven;
    public int shotsBoostCount;
    public int allmoney;
    public int basicSkinCoast = 300;
    public int basicBoostCoast = 100;
    public int basicShotCoast = 50;
    public int aboutLevel;
    public boolean isFonMusic = true;
    public boolean isAboutLevel = false;
    public boolean isActionSounds = true;
    public boolean isFirstRunning;
    public boolean isNewName = false;
    public boolean isFirstLeaderboard = true;
    public Player player;


    @Override
    public void create() {
        manager = new AssetManager();
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
        touch = new Vector3();
        player = new Player();
        font70 = new BitmapFont(Gdx.files.internal("701.fnt"));
        font32 = new BitmapFont(Gdx.files.internal("32.fnt"));
        FonMusic = Gdx.audio.newMusic(Gdx.files.internal("musicFon.mp3"));
        LanguageManager.loadBundles();
        screenLoading = new LoadingScreen(this);
        setScreen(screenLoading);


    }


    @Override
    public void dispose() {
        batch.dispose();
        font70.dispose();
        font32.dispose();
    }
}

