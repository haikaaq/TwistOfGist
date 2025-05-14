package ru.pick;

import static ru.pick.Main.SCR_WIDTH;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class SpaceButton {
    float x, y;
    BitmapFont font;
    String text;
    float widht, height;
    private boolean iscenter;

    public SpaceButton(BitmapFont font, String text, float x, float y) {
        this.font = font;
        this.text = text;
        this.x = x;
        this.y = y;
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        widht = glyphLayout.width;
        height = glyphLayout.height;
    }


    public SpaceButton(BitmapFont font, String text, float y) {
        this.font = font;
        this.text = text;
        this.y = y;
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        widht = glyphLayout.width;
        height = glyphLayout.height;
        this.x = SCR_WIDTH/2-widht/2;
        iscenter=true;
    }


    boolean hit(float tx, float ty) {
        return x < tx && tx < x +widht && ty < y && ty > y - height;
    }

   public void changeText(String text)
      {this.text=text;
       GlyphLayout glyphLayout = new GlyphLayout(font, text);
       widht = glyphLayout.width;
       height = glyphLayout.height;
       if(iscenter)   this.x = SCR_WIDTH/2-widht/2;



   }
    public void changeText(int money)
    {this.text=money>0?""+money:"0";
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        widht = glyphLayout.width;

    }
}










