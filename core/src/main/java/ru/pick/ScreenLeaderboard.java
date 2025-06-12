package ru.pick;

import static ru.pick.Main.SCR_HEIGHT;
import static ru.pick.Main.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    Texture imgPBT;
    private InputKeyboard keyboard;
    public static boolean Iskeyboard=true;
    Texture imgBackAtlas;
    TextureRegion[] imgBack = new TextureRegion[2];

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



        imgBackAtlas= new Texture("buttonsLeftRight.png");
        imgBG=new Texture("bgleadbd.png");
        imgPBT =new Texture("leaderboardBT.png");
        btnBack = new SpaceButton(10,1500,90,90,0);
        btnLead = new SpaceButton(font,"Leaderboard",SCR_HEIGHT-20);
        btnName = new SpaceButton(font,"ENTER YOUR NAME",SCR_HEIGHT/2+100);
        btnPlace =new SpaceButton(font,"You are in "+main.player.rank+" place",SCR_HEIGHT/4);

        keyboard = new InputKeyboard(font,SCR_WIDTH,SCR_HEIGHT*3/4,11);


        for (int e = 0; e < imgBack.length; e++) {

            imgBack[e] = new TextureRegion(imgBackAtlas, (e) * 200, 0, 200, 200);
        }



    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        changeButtons();
        leaderboard();
        getplase();
       // btnPlace.changeText("You are in "+main.player.rank+" place");

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
        batch.draw(imgBack[btnBack.type],btnBack.imgX,btnBack.imgY,btnBack.imgWidht,btnBack.imgHeight);


        if(!Iskeyboard) {
            font.draw(batch, "NAME" , 100, SCR_HEIGHT - 160 );
            font.draw(batch, "LEVEL" , 400, SCR_HEIGHT - 160 );
            font.draw(batch, "MONEY" , 645, SCR_HEIGHT - 160 );


            btnPlace.font.draw(batch,btnPlace.text,btnPlace.x,btnPlace.y);
            for (int i = 0; i < topPlayers.size(); i++) {

                Player p = topPlayers.get(i);
                batch.draw(imgPBT,60,SCR_HEIGHT - 260 - 68 * (i+1),SCR_WIDTH-120,70);
                font.draw(batch, (i + 1) + ". " + p.name, 100, SCR_HEIGHT - 260 - 68 * i);
                font.draw(batch, "" + p.level, 500, SCR_HEIGHT - 260 - 68 * i);
                if(p.money<100000){
                font.draw(batch, ( p.money+""), 700, SCR_HEIGHT - 260 - 68 * i);}
                else font.draw(batch, ( p.money/1000+"k"), 700, SCR_HEIGHT - 260 - 68 * i);


            }
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
        firebase.getTop10ByLevel(new FirebaseManager.SortedLeaderboardCallback()  {


            @Override
            public void onSuccess(List<Player> players) {

                // Сохраняем игроков в поле класса
                Gdx.app.postRunnable(() -> {
                    topPlayers = players;
                });



            }

            @Override
            public void onError(String message) {
                Gdx.app.error("Leaderboard", message);
            }
        },false);

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
    public void changeButtons (){
        FirebaseManager firebase = FirebaseService.create();
        if(firebase.isOnline()) btnPlace.changeText("You are in "+main.player.rank+"place");
        else btnPlace.changeText("You are offline");
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
