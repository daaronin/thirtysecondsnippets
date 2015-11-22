package com.gdp.thirtysecondsnippets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.Random;

/**
 * Created by Dan on 11/22/2015.
 */
public class LevelController {

    TextureAtlas atlas0 = new TextureAtlas(Gdx.files.internal("game-assets/GameAssets0.txt"));
    TextureAtlas atlas1 = new TextureAtlas(Gdx.files.internal("game-assets/GameAssets1.txt"));
    TextureAtlas atlas2 = new TextureAtlas(Gdx.files.internal("game-assets/GameAssets2.txt"));
    TextureAtlas atlas3 = new TextureAtlas(Gdx.files.internal("game-assets/GameAssets3.txt"));
    TextureAtlas atlas4 = new TextureAtlas(Gdx.files.internal("game-assets/GameAssets4.txt"));
    TextureAtlas atlas5 = new TextureAtlas(Gdx.files.internal("game-assets/GameAssets5.txt"));
    TextureAtlas atlas6 = new TextureAtlas(Gdx.files.internal("game-assets/GameAssets6.txt"));
    TextureAtlas atlas7 = new TextureAtlas(Gdx.files.internal("game-assets/GameAssets7.txt"));

    private Skin skin = new Skin(Gdx.files.internal("skin2.json"), atlas0);
    int level;

    Sprite needleGreen, needleBlue, needleYellow, emptyTree,
            scissor1, scissor2, scissor3, scissor4, scissor5, scissor6,
            star1, star2, star3, star4, star5,
            shortthreadlet, threadlet, tail,
            woodsBackground,woodsBack,woodsFront,woodsClouds,
            background,shadowBackground,threadedBackground,colorBackground;

    public LevelController(int levelType){
        skin.addRegions(atlas1);
        skin.addRegions(atlas2);
        skin.addRegions(atlas3);
        skin.addRegions(atlas4);
        skin.addRegions(atlas5);
        skin.addRegions(atlas6);
        skin.addRegions(atlas7);
        level = levelType;
        setThread();
        setLevel();
    }

    public void setThread(){
        Random rand = new Random();
        int texType = rand.nextInt(3);
        if (level == 2){
            texType = 3;
        } else if (level == 3){
            texType = 4;
        } else if (level == 4 || level == 5){
            texType = 5;
        }else if (level == 6){
            texType = 6;
        } else if (level == 7){
            texType = 7;
        }
        switch (texType){
            case 0:
                shortthreadlet = skin.getSprite("shortthreadhighcontrast_alt");
                threadlet = skin.getSprite("shortthreadhighcontrast2_alt");
                break;
            case 1:
                shortthreadlet = skin.getSprite("shortthreadhighcontrast_purp");
                threadlet = skin.getSprite("shortthreadhighcontrast2_purp");
                break;
            case 2:
                shortthreadlet = skin.getSprite("shortthreadhighcontrast");
                threadlet = skin.getSprite("shortthreadhighcontrast2");
                break;
            case 3:
                shortthreadlet = skin.getSprite("shortthreadhighcontrast_wood");
                threadlet = skin.getSprite("shortthreadhighcontrast2_wood");
                break;
            case 4:
                shortthreadlet = skin.getSprite("shortthreadhighcontrast_snake");
                threadlet = skin.getSprite("shortthreadhighcontrast2_snake2");
                tail = skin.getSprite("shortthreadhighcontrast_snaketail");
                break;
            case 5:
                shortthreadlet = skin.getSprite("shortthreadhighcontrast_staff2");
                threadlet = skin.getSprite("shortthreadhighcontrast2_staff2");
                break;
            case 6:
                shortthreadlet = skin.getSprite("shortthreadhighcontrast_fish");
                threadlet = skin.getSprite("shortthreadhighcontrast_fish");
                break;
            case 7:
                shortthreadlet = skin.getSprite("thread_8bit2");
                threadlet = skin.getSprite("thread_8bit");
                break;
            default:
                break;
        }
    }

    public void setLevel(){
        background = skin.getSprite("tallback");
        threadedBackground = skin.getSprite("threadstall");
        colorBackground = skin.getSprite("rainbow");
        shadowBackground = skin.getSprite("shadowmap3");

        woodsBackground = skin.getSprite("woodssmallbackground");
        woodsBack = skin.getSprite("woodsback");
        woodsFront = skin.getSprite("woodsfront");
        woodsClouds = skin.getSprite("woodsclouds");


        if (level == 1){
            needleGreen = skin.getSprite("needlegreenoutline");
            needleBlue = skin.getSprite("needleblueoutline");
            needleYellow = skin.getSprite("needleyellowoutline");

            scissor1 = skin.getSprite("scissor1");
            scissor2 = skin.getSprite("scissor2");
            scissor3 = skin.getSprite("scissor3");
            scissor4 = skin.getSprite("scissor4");
            scissor5 = skin.getSprite("scissor5");
            scissor6 = skin.getSprite("scissor6");

            star1 = skin.getSprite("star1");
            star2 = skin.getSprite("star2");
            star3 = skin.getSprite("star3");
            star4 = skin.getSprite("star4");
            star5 = skin.getSprite("star5");
        } else if (level == 2){
            woodsBackground = skin.getSprite("woodssmallbackground");
            woodsBack = skin.getSprite("woodsback");
            woodsFront = skin.getSprite("woodsfront");
            woodsClouds = skin.getSprite("woodsclouds");

            needleGreen = skin.getSprite("woodspinetreebird");
            needleBlue = skin.getSprite("woodspinetreebird");
            needleYellow = skin.getSprite("woodspinetreebird");
            emptyTree = skin.getSprite("woodspinetree");

            scissor1 = skin.getSprite("saw1");
            scissor2 = skin.getSprite("saw2");
            scissor3 = skin.getSprite("saw3");
            scissor4 = skin.getSprite("saw4");
            scissor5 = skin.getSprite("saw5");
            scissor6 = skin.getSprite("saw6");

            star1 = skin.getSprite("wood1");
            star2 = skin.getSprite("wood2alt");
            star3 = skin.getSprite("wood3alt");
            star4 = skin.getSprite("wood4");
            star5 = skin.getSprite("wood5");
        } else if (level == 3){
            woodsBackground = skin.getSprite("desertsmallbackground");
            woodsBack = skin.getSprite("desertback");
            woodsFront = skin.getSprite("desertfront");
            woodsClouds = skin.getSprite("desertsky");

            needleGreen = skin.getSprite("skeletonhand");
            needleBlue = skin.getSprite("skeletonhand2");
            needleYellow = skin.getSprite("skeletonhand3");

            scissor1 = skin.getSprite("sword1");
            scissor2 = skin.getSprite("sword2");
            scissor3 = skin.getSprite("sword3");
            scissor4 = skin.getSprite("sword4");
            scissor5 = skin.getSprite("sword5");
            scissor6 = skin.getSprite("sword6");

            star1 = skin.getSprite("wood1");
            star2 = skin.getSprite("wood2alt");
            star3 = skin.getSprite("wood3alt");
            star4 = skin.getSprite("wood4");
            star5 = skin.getSprite("wood4");
        } else if (level == 4){
            woodsBackground = skin.getSprite("clubbackgroundsmall");
            woodsBack = skin.getSprite("clubback");
            woodsFront = skin.getSprite("clubback");
            woodsClouds = skin.getSprite("desertsky");

            needleGreen = skin.getSprite("microphone");
            needleBlue = skin.getSprite("microphone");
            needleYellow = skin.getSprite("microphone");

            scissor1 = skin.getSprite("trombone1");
            scissor2 = skin.getSprite("trombone2");
            scissor3 = skin.getSprite("trombone3");
            scissor4 = skin.getSprite("trombone4");
            scissor5 = skin.getSprite("trombone5");
            scissor6 = skin.getSprite("trombone6");

            star1 = skin.getSprite("wood1");
            star2 = skin.getSprite("wood2alt");
            star3 = skin.getSprite("wood3alt");
            star4 = skin.getSprite("wood4");
            star5 = skin.getSprite("wood4");
        }else if (level == 5){
            woodsBackground = skin.getSprite("clubbackgroundsmall2");
            woodsBack = skin.getSprite("clubback");
            woodsFront = skin.getSprite("clubback");
            woodsClouds = skin.getSprite("desertsky");

            needleGreen = skin.getSprite("microphone");
            needleBlue = skin.getSprite("microphone");
            needleYellow = skin.getSprite("microphone");

            scissor1 = skin.getSprite("trombone1");
            scissor2 = skin.getSprite("trombone2");
            scissor3 = skin.getSprite("trombone3");
            scissor4 = skin.getSprite("trombone4");
            scissor5 = skin.getSprite("trombone5");
            scissor6 = skin.getSprite("trombone6");

            star1 = skin.getSprite("wood1");
            star2 = skin.getSprite("wood2alt");
            star3 = skin.getSprite("wood3alt");
            star4 = skin.getSprite("wood4");
            star5 = skin.getSprite("wood4");
        } else if (level == 6){
            woodsBackground = skin.getSprite("oceanbackground");
            woodsBack = skin.getSprite("oceanmiddleground");
            woodsFront = skin.getSprite("oceanforeground");

            needleGreen = skin.getSprite("bubblestickpopped");
            needleBlue = skin.getSprite("bubblestickpopped");
            needleYellow = skin.getSprite("bubblestickpopped");
            emptyTree = skin.getSprite("bubblestick");

            scissor1 = skin.getSprite("spear1");
            scissor2 = skin.getSprite("spear2");
            scissor3 = skin.getSprite("spear3");
            scissor4 = skin.getSprite("spear4");
            scissor5 = skin.getSprite("spear5");
            scissor6 = skin.getSprite("spear6");

            star1 = skin.getSprite("star1");
            star2 = skin.getSprite("star2");
            star3 = skin.getSprite("star3");
            star4 = skin.getSprite("star4");
            star5 = skin.getSprite("star5");
        } else if (level == 7){
            woodsBackground = skin.getSprite("background8bit");
            woodsBack = skin.getSprite("back8bit");
            woodsFront = skin.getSprite("mid8bit");

            needleGreen = skin.getSprite("ring1");
            needleBlue = skin.getSprite("ring1");
            needleYellow = skin.getSprite("ring1");
            emptyTree = skin.getSprite("ring2");

            scissor1 = skin.getSprite("laser6");
            scissor2 = skin.getSprite("laser5");
            scissor3 = skin.getSprite("laser4");
            scissor4 = skin.getSprite("laser3");
            scissor5 = skin.getSprite("laser2");
            scissor6 = skin.getSprite("laser1");

            star1 = skin.getSprite("star1");
            star2 = skin.getSprite("star2");
            star3 = skin.getSprite("star3");
            star4 = skin.getSprite("star4");
            star5 = skin.getSprite("star5");
        }
    }

    public void dispose(){
        skin.dispose();
        atlas0.dispose();
        atlas1.dispose();
        atlas2.dispose();
        atlas3.dispose();
        atlas4.dispose();
        atlas5.dispose();
        atlas6.dispose();
        atlas7.dispose();
    }


}
