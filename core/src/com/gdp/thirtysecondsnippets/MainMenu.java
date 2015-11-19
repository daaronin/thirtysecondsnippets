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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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

    int clicks;
    float blinkingTimer = 0;

    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("Menus.txt"));
    TextureAtlas backgrounds = new TextureAtlas(Gdx.files.internal("Backgrounds.txt"));

    private Skin skin = new Skin(Gdx.files.internal("skin.json"), atlas);

    Preferences prefs = Gdx.app.getPreferences("30SSSettings");

    TextButton play;

    Texture titleTex;

    public MainMenu(Game tss){
        this.tss = tss;
    }
    
    @Override
    public void show() {
        skin.addRegions(backgrounds);
        clicks = 0;

        titleTex = new Texture(Gdx.files.internal("title.png"));
        ImageButton title = new ImageButton(new Image(titleTex).getDrawable());
        play = new TextButton("Play", skin.get("blue", TextButtonStyle.class));
        //TextButton setting = new TextButton("*", skin.get("setting", TextButtonStyle.class));
        TextButton about = new TextButton("?", skin.get("about", TextButtonStyle.class));
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
        
        play.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    //Same way we moved here from the Splash Screen
                    //We set it to new Splash because we got no other screens
                    //otherwise you put the screen there where you want to go
                    //DifficultyMenu menu = new DifficultyMenu(tss);
                    //tss.setScreen(menu);
                    GenreMenu menu = new GenreMenu(tss, LEISURELY_DIFFICULTY);
                    tss.setScreen(menu);

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
        
        about.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    Gdx.net.openURI("http://www.30secondsnippets.com/");
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

        table.add(basket).top().left().width(Value.percentWidth(.18f, table)).height(Value.percentWidth(.135f, table)).padTop(10).padRight(7);
        table.add(title).top().center().width(Value.percentWidth(.5f, table)).height(Value.percentHeight(.35f, table)).padTop(20);
        table.add(patch).top().right().width(Value.percentWidth(.18f, table)).height(Value.percentWidth(.18f, table)).padTop(10).padLeft(7);
        table.row();
        table.add(play).height(Value.percentHeight(.15f, table)).width(Value.percentWidth(.375f, table)).expandY().colspan(3);
        table.row();
        table.add(setting).height(Value.percentHeight(.11f, table)).width(Value.percentWidth(.06f, table)).bottom().center().padBottom(Value.percentHeight(.12f, table)).colspan(3);
        //table.add(stats).height(Value.percentHeight(.3f)).width(Value.percentHeight(.3f)).bottom().center().padBottom(Value.percentHeight(.2f));
        //table.add(about).height(Value.percentHeight(.3f)).width(Value.percentHeight(.3f)).bottom().right().padBottom(Value.percentHeight(.2f)).padRight(Value.percentWidth(.2f));

        String backgroundStr = prefs.getString("background", "halloween_blur");
        table.setBackground(skin.getDrawable(backgroundStr));
        table.setFillParent(true);
        //table.debug();
        
        stage.addActor(table);
        
        Gdx.input.setInputProcessor(stage);
   }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        blinkingTimer += delta;
        if (blinkingTimer > 1.1){
            if (play.getBackground().equals(skin.getDrawable("blueup"))){
                play.setStyle(skin.get("green", TextButtonStyle.class));
            } else if (play.getBackground().equals( skin.getDrawable("greenup"))){
                play.setStyle(skin.get("orange", TextButtonStyle.class));
            } else if (play.getBackground().equals( skin.getDrawable("orangeup"))){
                play.setStyle(skin.get("blue", TextButtonStyle.class));
            }
            blinkingTimer = 0;
        }


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
        atlas.dispose();
        backgrounds.dispose();
        skin.dispose();
        stage.dispose();
        table.clear();
        titleTex.dispose();
    }

}