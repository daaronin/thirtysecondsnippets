/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gdp.thirtysecondsnippets;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 *
 * @author George and Dan
 */
public class MainMenu implements Screen{
    
    private Game tss;
    private Stage stage = new Stage(new StretchViewport(TSS.WIDTH, TSS.HEIGHT));
    private Table table = new Table();

    static final int LEISURELY_DIFFICULTY = 3;
    static final int BRISK_DIFFICULTY = 5;
    static final int BREAKNECK_DIFFICULTY = 7;

    /**
     * Controls the Main Menu Style
     * 1: Large rectangle play button
     * 2: Large circle play button
     * 3: Old style menu
     */
    int buttonOption = 2;

    int clicks;
    float blinkingTimer = 0;

    TextureAtlas menu0 = new TextureAtlas(Gdx.files.internal("menu/Menus0.txt"));
    TextureAtlas menu1 = new TextureAtlas(Gdx.files.internal("menu/Menus1.txt"));
    TextureAtlas menu2 = new TextureAtlas(Gdx.files.internal("menu/Menus2.txt"));
    TextureAtlas menu3 = new TextureAtlas(Gdx.files.internal("menu/Menus3.txt"));

    private Skin skin = new Skin(menu0);

    Preferences prefs = Gdx.app.getPreferences("30SSSettings");

    Texture play, playdown;
    ImageButton playbutton;

    Texture titleTex, background;

    public MainMenu(Game tss){
        this.tss = tss;
    }
    
    @Override
    public void show() {
        skin.addRegions(menu1);
        skin.addRegions(menu2);
        skin.addRegions(menu3);
        skin.load(Gdx.files.internal("skin.json"));
        skin.getFont("font").getData().setScale(0.9f,0.9f);
        clicks = 0;

        titleTex = new Texture(Gdx.files.internal("title2.png"));
        ImageButton title = new ImageButton(new Image(titleTex).getDrawable());
        //play = new TextButton("PLAY", skin.get("blue", TextButtonStyle.class));
        if (buttonOption == 1 || buttonOption == 3) {
            play = new Texture(Gdx.files.internal("newplay.png"));
            SpriteDrawable ply = new SpriteDrawable(new Sprite(play));

            playdown = new Texture(Gdx.files.internal("newplaydown.png"));
            SpriteDrawable plydown = new SpriteDrawable(new Sprite(playdown));

            ImageButton.ImageButtonStyle style3 = new ImageButton.ImageButtonStyle();
            style3.up = ply;
            style3.down = plydown;
//        style3.imageUp = metal;
//        style3.imageOver = hover;
//        style3.imageChecked = selected;
//        style3.imageDown = clicked;
            playbutton = new ImageButton(style3);
        } else if (buttonOption == 2){
            play = new Texture(Gdx.files.internal("roundplaybuttonalt.png"));
            SpriteDrawable ply = new SpriteDrawable(new Sprite(play));

            playdown = new Texture(Gdx.files.internal("roundplaybuttonaltdown.png"));
            SpriteDrawable plydown = new SpriteDrawable(new Sprite(playdown));

            ImageButton.ImageButtonStyle style3 = new ImageButton.ImageButtonStyle();
            style3.up = ply;
            style3.down = plydown;
//        style3.imageUp = metal;
//        style3.imageOver = hover;
//        style3.imageChecked = selected;
//        style3.imageDown = clicked;
            playbutton = new ImageButton(style3);
        }

        //TextButton setting = new TextButton("*", skin.get("setting", TextButtonStyle.class));
        TextButton stats = new TextButton("%", skin.get("back", TextButtonStyle.class));
        
        TextButton patch = new TextButton("", skin.get("patch", TextButtonStyle.class));
        TextButton basket = new TextButton("", skin.get("basket", TextButtonStyle.class));
        
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        if (prefs.getString("background").equals("halloween_blur") || prefs.getString("background").equals("ocean_blur")
                || prefs.getString("background").equals("woods_blur")){
            style.up = skin.getDrawable("sq_blue_up");
            style.down = skin.getDrawable("sq_blue_down");
        } else {
            style.up = skin.getDrawable("sq_green_up");
            style.down = skin.getDrawable("sq_green_down");
        }
        style.imageUp = skin.getDrawable("settingsicon");
        ImageButton setting = new ImageButton(style);
        
        playbutton.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    //Same way we moved here from the Splash Screen
                    //We set it to new Splash because we got no other screens
                    //otherwise you put the screen there where you want to go
                    //DifficultyMenu menu = new DifficultyMenu(tss);
                    //tss.setScreen(menu);
                    stage.addAction(Actions.sequence( Actions.fadeOut(.5f), Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            GenreMenu menu = new GenreMenu(tss, LEISURELY_DIFFICULTY);
                            tss.setScreen(menu);
                        }
                    })));


                }
        });

        title.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                clicks++;
                if (clicks >= 5){
                    prefs.putInteger("unlocked", 10);
                    prefs.putString("background", "halloween_blur");
                    prefs.putString("fontcolor", "labelw");
                    prefs.putInteger("theme", 7);
                    prefs.flush();
                }
            }
        });
        
        setting.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    SettingsMenu menu = new SettingsMenu(tss);
                    tss.setScreen(menu);
                }
            });
        
        stats.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    StatsScreen menu = new StatsScreen(tss);
                    tss.setScreen(menu);
                }
            });
        
        patch.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    Gdx.net.openURI("http://www.30secondsnippets.com/");
                }
            });
        
        basket.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                ThemeMenu menu = new ThemeMenu(tss);
                tss.setScreen(menu);
            }
        });

        //table.add(basket).bottom().left().width(Value.percentWidth(.18f, table)).height(Value.percentWidth(.135f, table)).padTop(10).padRight(7); //Acts as a spacer
        table.add(title).top().center().width(Value.percentWidth(.5f, table)).height(Value.percentHeight(.35f, table)).padTop(20).colspan(12);
        //table.add(setting).height(Value.percentHeight(.11f, table)).width(Value.percentHeight(.11f, table)).bottom().right().padBottom(20).colspan(3);

        //table.add(basket).top().right().width(Value.percentWidth(.18f, table)).height(Value.percentWidth(.18f, table)).padTop(10).padLeft(7); //Acts as a spacer, PATCH LOG GOES HERE
        table.row();
        //table.add(play).height(Value.percentHeight(.15f, table)).width(Value.percentWidth(.375f, table)).expandY().colspan(3);
        if (buttonOption == 1) {
            table.add(playbutton).height(Value.percentHeight(.341f, table)).width(Value.percentHeight(1.229f, table)).expandY().center().colspan(12);
        } else if (buttonOption == 2){
            table.add(playbutton).height(Value.percentHeight(.6f, table)).width(Value.percentHeight(.6f, table)).center().colspan(12);
        } else if (buttonOption == 3){
            table.add(playbutton).height(Value.percentHeight(.244f, table)).width(Value.percentHeight(.878f, table)).expandY().center().colspan(12);
            table.row();
            table.add(basket).bottom().left().width(Value.percentWidth(.18f, table)).height(Value.percentWidth(.135f, table)).padBottom(10).padRight(7).colspan(9);
            table.add(setting).height(Value.percentHeight(.11f, table)).width(Value.percentHeight(.11f, table)).bottom().right().padBottom(20).colspan(3);
        }
        table.row();
//        table.add(basket).bottom().left().width(Value.percentWidth(.18f, table)).height(Value.percentWidth(.135f, table)).padBottom(10).padRight(7);
//        table.add(setting).height(Value.percentHeight(.11f, table)).width(Value.percentHeight(.11f, table)).bottom().right().padBottom(20).colspan(3);
        //table.add(stats).height(Value.percentHeight(.3f)).width(Value.percentHeight(.3f)).bottom().center().padBottom(Value.percentHeight(.2f));
        //table.add(about).height(Value.percentHeight(.3f)).width(Value.percentHeight(.3f)).bottom().right().padBottom(Value.percentHeight(.2f)).padRight(Value.percentWidth(.2f));

        String backgroundStr = prefs.getString("background", "halloween_blur");
        String url = "backgrounds/" + backgroundStr + ".jpg";
        background = new Texture(Gdx.files.internal(url));
        SpriteDrawable backspr = new SpriteDrawable(new Sprite(background));
        table.setBackground(backspr);
        table.setFillParent(true);
        //table.debug();
        stage.addActor(table);

//        title.addAction(Actions.sequence(Actions.moveBy(0, 2000, 3f)
//                , Actions.delay(1f), Actions.run(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        })));

        stage.addAction(Actions.sequence(Actions.alpha(0)
                , Actions.fadeIn(.5f), Actions.run(new Runnable() {
            @Override
            public void run() {

            }
        })));

        Gdx.input.setInputProcessor(stage);
   }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

//        blinkingTimer += delta;
//        if (blinkingTimer > 1.1){
//            if (play.getBackground().equals(skin.getDrawable("blueup"))){
//                play.setStyle(skin.get("green", TextButtonStyle.class));
//            } else if (play.getBackground().equals( skin.getDrawable("greenup"))){
//                play.setStyle(skin.get("orange", TextButtonStyle.class));
//            } else if (play.getBackground().equals( skin.getDrawable("orangeup"))){
//                play.setStyle(skin.get("blue", TextButtonStyle.class));
//            }
//            blinkingTimer = 0;
//        }


        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        menu0.dispose();
        menu1.dispose();
        menu2.dispose();
        menu3.dispose();
        background.dispose();
        skin.dispose();
        play.dispose();
        playdown.dispose();
        stage.dispose();
        table.clear();
        titleTex.dispose();
    }

}