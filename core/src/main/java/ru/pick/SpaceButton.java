package ru.pick;

import static ru.pick.Main.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class SpaceButton {
    float x, y;
    BitmapFont font;
    String text;
    float width, height;
    float imgWidth, imgHeight;
    float minImgWidth, minImgHeight;
    float imgY, imgX;
    private boolean iscenter;
    public int phase;
    public boolean isPressed = false;
    public boolean isHover = false;
    private boolean isImageAndTextButton = false;
    private boolean isOnlyImageButton = false;
    public boolean setScreenButton;
    public boolean wrapEnabled;
    public int type;
    private int spin=1;
    private boolean wasTouched;
    public float imgWidhtCoefficient;
    public boolean isMove;


    public SpaceButton(BitmapFont font, String text, float x, float y) {
        this.font = font;
        this.text = text;
        this.x = x;
        this.y = y;
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        width = glyphLayout.width;
        height = glyphLayout.height;
        glyphLayout.setText(font, text);

    }


    public SpaceButton(BitmapFont font, String text, float y) {
        this.font = font;
        this.text = text;
        this.y = y;
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        width = glyphLayout.width;
        height = glyphLayout.height;
        this.x = SCR_WIDTH / 2 - width / 2;
        iscenter = true;
    }

    public SpaceButton(float x, float y, float widht, float height, int type) {
        isOnlyImageButton = true;
        this.type = type;
        this.imgWidth = widht;
        this.imgHeight = height;
        minImgWidth=imgWidth;
        minImgHeight=imgHeight;

        this.imgX = x;
        this.imgY = y;

    }

    public SpaceButton(BitmapFont font, String text, Texture img, float y, float imgWidhtCoefficient) {
        this.font = font;
        this.text = text;
        isImageAndTextButton = true;
        iscenter = true;
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        this.imgWidhtCoefficient = imgWidhtCoefficient;

        this.y = y;

        this.width = glyphLayout.width;
        this.x = SCR_WIDTH / 2 - width / 2;
        this.height = glyphLayout.height;
        this.imgWidth = width * imgWidhtCoefficient;
        this.imgHeight = height * 3.9f;

        this.imgY = y - imgHeight / 2 - height / 2;
        this.imgX = x + width / 2 - imgWidth / 2;

    }

    public SpaceButton(BitmapFont font, String text, float x, float y, float imgWidhtCoefficient) {
        this.font = font;
        this.text = text;
        isImageAndTextButton = true;
        this.imgWidhtCoefficient = imgWidhtCoefficient;

        this.y = y;
        this.x = x;
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        this.width = glyphLayout.width;

        this.height = glyphLayout.height;
        this.imgWidth = width * imgWidhtCoefficient;
        this.imgHeight = height * 3.9f;
        this.imgY = y - imgHeight / 2 - height / 2;
        this.imgX = x + width / 2 - imgWidth / 2;

    }


    boolean hit(float tx, float ty) {
        if (isImageAndTextButton||isOnlyImageButton)
            return imgX < tx && tx < imgX + imgWidth && ty > imgY && ty < imgY + imgHeight;


        else return x < tx && tx < x + width && ty < y && ty > y - height;
    }


    public void changeText(String text) {
        this.text = text;
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        width = glyphLayout.width;
        height = glyphLayout.height;
        if (iscenter) this.x = SCR_WIDTH / 2 - width / 2;
        if (isImageAndTextButton) {


            this.minImgWidth = width * imgWidhtCoefficient;
            this.minImgHeight = height * 3.9f;
            if(isMove){
               moveButton(4f,8f, 12);
            }
            else{
                imgHeight=minImgHeight;
                imgWidth=minImgWidth;
            }
            this.imgY = y - imgHeight / 2 - height / 2;
            this.imgX = x + width / 2 - imgWidth / 2;

        }


    }


    public void changeText(int money) {
        this.text = money < 1000 ? "" + money : money + "k";
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        width = glyphLayout.width;
    }


    public void changePhases() {
        phase = 0;
        if (isHover) phase = 1;
        if (isPressed) phase = 2;

    }

    public void ButtonsState(float x, float y) {
        isPressed = false;
        setScreenButton = false;
        isHover = false;


        if (hit(x, y)) {
            isHover = true;
        }
        if (wasTouched && (!Gdx.input.justTouched()) && hit(x, y)) {

            setScreenButton = true;
            wasTouched = false;

        }
        if (hit(x, y) && Gdx.input.justTouched()) {
            isPressed = true;
            wasTouched = true;

        }

    }
    public void moveButton(float vH,float vW,float coefficient){

        imgWidth +=vW*spin;
        imgHeight+=vH*spin;
        if(isOnlyImageButton)imgX-=vW/2f*spin;
        if((imgWidth-minImgWidth>=minImgWidth/coefficient)||(imgWidth<=minImgWidth)){
            spin=spin*-1;
        }


    }
}


