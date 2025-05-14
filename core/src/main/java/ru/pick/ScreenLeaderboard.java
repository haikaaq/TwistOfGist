package ru.pick;

import static ru.pick.Main.SCR_HEIGHT;
import static ru.pick.Main.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;


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
        keyboard = new InputKeyboard(font,SCR_WIDTH,SCR_HEIGHT*3/4,11);





    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
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
                Iskeyboard =false;
            }

        }

        if (Iskeyboard) keyboard.start();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        keyboard.draw(batch);

        btnLead.font.draw(batch,btnLead.text,btnLead.x,btnLead.y);
        btnBack.font.draw(batch,btnBack.text,btnBack.x,btnBack.y);
        if(!Iskeyboard) {
            font.draw(batch, "NAME" , 100, SCR_HEIGHT - 160 );
            font.draw(batch, "LEVEL" , 400, SCR_HEIGHT - 160 );
            font.draw(batch, "MONEY" , 645, SCR_HEIGHT - 160 );
            for (int i = 0; i < main.screenGame.players.length - 1; i++) {
                font.draw(batch, main.screenGame.players[i].name , 100, SCR_HEIGHT - 260 - 68 * i);
                font.draw(batch,""+ main.screenGame.players[i].level ,500, SCR_HEIGHT - 260 - 68 * i);
                font.draw(batch,""+ main.screenGame.players[i].money , 700, SCR_HEIGHT - 260 - 68 * i);
            }
        }
        else btnName.font.draw(batch,btnName.text,btnName.x,btnName.y);
        batch.end();
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
