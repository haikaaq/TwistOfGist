package ru.pick;

import static ru.pick.Main.SCR_HEIGHT;
import static ru.pick.Main.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;


public class ScreenLeaderboard implements Screen {
    private Main main;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Vector3 touch;
    private BitmapFont font;
    private BitmapFont font32;
    Texture imgBG;
    private InputKeyboard keyboard;
    public static boolean Iskeyboard=true;

    SpaceButton btnBack;
    SpaceButton btnLead;
    SpaceButton btnName;
    SpaceButton btnPlace;

    private List<Player> topPlayers = new ArrayList<>();


    public ScreenLeaderboard(Main main) {
        this.main = main;
        batch= main.batch;
        camera= main.camera;
        touch= main.touch;
        font=main.font70;
        font32=main.font32;



        imgBG=new Texture("bgleadbd.png");

        btnBack = new SpaceButton(font,"Back",30,SCR_HEIGHT-20);
        btnLead = new SpaceButton(font,"Leaderboard",SCR_HEIGHT-20);
        btnName = new SpaceButton(font,"ENTER YOUR NAME",SCR_HEIGHT/2+100);
        btnPlace =new SpaceButton(font,"You are in "+main.player.rank+" place",SCR_HEIGHT/4);

        keyboard = new InputKeyboard(font,SCR_WIDTH,SCR_HEIGHT*3/4,11);





    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        leaderboard();
        getplase();

        if(Gdx.input.justTouched()){


            touch.set(Gdx.input.getX(),Gdx.input.getY(),0);
            camera.unproject(touch);
            if(btnBack.hit(touch.x,touch.y)){
                if(btnBack.hit(touch.x,touch.y)){
                    main.setScreen(main.screenMenu);
                }

            }
            if (keyboard.touch(touch.x,touch.y)){
                main.player.name=keyboard.getText();
                main.player.money= main.Allmoney;
                main.player.level= main.level;

                Iskeyboard =false;
            }

        }

        if (Iskeyboard) keyboard.start();
        main.player.savePlayerToFirebase(main.player);


        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        keyboard.draw(batch);

        btnLead.font.draw(batch,btnLead.text,btnLead.x,btnLead.y);
        btnBack.font.draw(batch,btnBack.text,btnBack.x,btnBack.y);
        btnPlace.font.draw(batch,btnPlace.text,btnPlace.x,btnPlace.y);
        if(!Iskeyboard) {
            font.draw(batch, "NAME" , 100, SCR_HEIGHT - 160 );
            font.draw(batch, "LEVEL" , 400, SCR_HEIGHT - 160 );
            font.draw(batch, "MONEY" , 645, SCR_HEIGHT - 160 );

            btnPlace.changeText("You are in "+main.player.rank+" place");
            for (int i = 0; i < topPlayers.size(); i++) {

                Player p = topPlayers.get(i);
                font.draw(batch, (i+1) + ". " + p.name, 100, SCR_HEIGHT - 260 - 68 * i);
                font.draw(batch, "" + p.level, 500, SCR_HEIGHT - 260 - 68 * i);
                font.draw(batch, "" + p.money, 700, SCR_HEIGHT - 260 - 68 * i);
                System.out.println( (i+1) + ". " + p.name+"   "
                    + p.level+ " " + p.money);
            }
          /*  for (int i = 0; i < main.screenGame.players.length - 1; i++) {
                font.draw(batch, main.screenGame.players[i].name , 100, SCR_HEIGHT - 260 - 68 * i);
                font.draw(batch,""+ main.screenGame.players[i].level ,500, SCR_HEIGHT - 260 - 68 * i);
                font.draw(batch,""+ main.screenGame.players[i].money , 700, SCR_HEIGHT - 260 - 68 * i);
            }*/
        }
        else btnName.font.draw(batch,btnName.text,btnName.x,btnName.y);
        batch.end();
    }










    public void getplase(){
        FirebaseManager firebase = FirebaseService.create();

        firebase.getPlayerRank(main.player, new FirebaseManager.PlayerRankCallback() {
            @Override
            public void onSuccess(int rank) {
                // Просто выводим число в лог
                Gdx.app.log("RANK", "Ваша позиция: " + rank);

                // Или сохраняем в переменную
                main.player.rank = rank;
            }

            @Override
            public void onError(String message) {
                Gdx.app.log("RANK", "Ошибка: " + message);
            }
        });
    }

    public void leaderboard(){
        FirebaseManager firebase = FirebaseService.create();
        firebase.getTop10ByLevel(new FirebaseManager.SortedLeaderboardCallback() {


            @Override
            public void onSuccess(List<Player> players) {
                // Сохраняем игроков в поле класса
                Gdx.app.postRunnable(() -> {
                    topPlayers = players;
                });
                System.out.println("все хорошо");


            }

            @Override
            public void onError(String message) {
                Gdx.app.error("Leaderboard", message);
            }
        });
    }
    /* private void LoadTable() {
        Preferences prefs = Gdx.app.getPreferences("TableRecords");
        for (int i=0; i<players.length;i++){
            players[i].name= prefs.getString("name"+i,"Noname");
            players[i].level=prefs.getInteger("level"+i,0);
            players[i].money=prefs.getInteger("money"+i,0);
        }
    }
    private void saveTable() {


        Preferences prefs = Gdx.app.getPreferences("TableRecords");
        for (int i=0; i<players.length;i++){

            prefs.putString("name"+i, players[i].name);
            prefs.putInteger("level"+i, players[i].level);
            prefs.putInteger("money"+i,players[i].money);

        }
        prefs.flush();
    }

     */
    public void savePlayer(Player player){
        FirebaseManager firebase = FirebaseService.create();
        firebase.savePlayer(player);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }



  }
