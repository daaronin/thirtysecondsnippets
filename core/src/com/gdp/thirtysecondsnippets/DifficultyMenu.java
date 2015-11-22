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
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.ArrayList;

/**
 *
 * @author George and Dan
 */
public class DifficultyMenu implements Screen{
    
    private Game tss;
    private Stage stage = new Stage(new StretchViewport(TSS.WIDTH, TSS.HEIGHT));
    private Table table = new Table();
    
    static final int LEISURELY_DIFFICULTY = 3;
    static final int BRISK_DIFFICULTY = 5;
    static final int BREAKNECK_DIFFICULTY = 7;

    TextureAtlas menu0 = new TextureAtlas(Gdx.files.internal("menu/Menus0.txt"));
    TextureAtlas menu1 = new TextureAtlas(Gdx.files.internal("menu/Menus1.txt"));
    TextureAtlas menu2 = new TextureAtlas(Gdx.files.internal("menu/Menus2.txt"));
    TextureAtlas menu3 = new TextureAtlas(Gdx.files.internal("menu/Menus3.txt"));

    private Skin skin = new Skin(menu0);

    Preferences prefs = Gdx.app.getPreferences("30SSSettings");

    TextButton back;

    Texture background;

    public DifficultyMenu(Game tss){
        this.tss = tss;
    }
    
    @Override
    public void show() {
        skin.addRegions(menu1);
        skin.addRegions(menu2);
        skin.addRegions(menu3);
        skin.load(Gdx.files.internal("skin.json"));
        skin.getFont("bfont").getData().setScale(0.8f,0.8f);
        skin.getFont("font").getData().setScale(0.8f,0.8f);

        ImageButton title = new ImageButton(skin.getDrawable("title"));
        TextButton leisurely = new TextButton("Leisurely", skin.get("blue", TextButtonStyle.class));
        TextButton brisk = new TextButton("Brisk", skin.get("green", TextButtonStyle.class));
        TextButton breakneck = new TextButton("Breakneck", skin.get("orange", TextButtonStyle.class));
        
        leisurely.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    //Same way we moved here from the Splash Screen
                    //We set it to new Splash because we got no other screens
                    //otherwise you put the screen there where you want to go
                    //GenreMenu menu = new GenreMenu(tss, LEISURELY_DIFFICULTY);
                    //tss.setScreen(menu);
                    LoadTrackData load = new LoadTrackData(tss, 0, LEISURELY_DIFFICULTY);
                    tss.setScreen(load);
                }
        });
        brisk.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    //Same way we moved here from the Splash Screen
                    //We set it to new Splash because we got no other screens
                    //otherwise you put the screen there where you want to go
                    //GenreMenu menu = new GenreMenu(tss, BRISK_DIFFICULTY);
                    //tss.setScreen(menu);
                    LoadTrackData load = new LoadTrackData(tss, 0, BRISK_DIFFICULTY);
                    tss.setScreen(load);
                }
        });
        breakneck.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    //Same way we moved here from the Splash Screen
                    //We set it to new Splash because we got no other screens
                    //otherwise you put the screen there where you want to go
                    //GenreMenu menu = new GenreMenu(tss, BREAKNECK_DIFFICULTY);
                    //tss.setScreen(menu);
                    LoadTrackData load = new LoadTrackData(tss, 0, BREAKNECK_DIFFICULTY);
                    tss.setScreen(load);
                }
        });

        if (prefs.getString("background").equals("halloween_blur") || prefs.getString("background").equals("ocean_blur")
                || prefs.getString("background").equals("woods_blur")){
            back = new TextButton("<", skin.get("setting", TextButton.TextButtonStyle.class));
        } else {
            back = new TextButton("<", skin.get("back", TextButton.TextButtonStyle.class));
        }
        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                MainMenu game = new MainMenu(tss);
                tss.setScreen(game);
                }
        });
        
        
        table.add(title).top().center().width(Value.percentWidth(.9f)).height(Value.percentHeight(.45f)).padTop(10).colspan(3);
        table.row();
        table.add(leisurely).height(Value.percentHeight(.35f)).width(Value.percentWidth(.45f)).padTop(10).expandY().colspan(3);
        table.row();
        table.add(brisk).height(Value.percentHeight(.35f)).width(Value.percentWidth(.45f)).padTop(10).expandY().colspan(3);
        table.row();
        table.add(breakneck).height(Value.percentHeight(.35f)).width(Value.percentWidth(.45f)).padTop(10).expandY().colspan(3);
        table.row();
        
        table.add(back).height(Value.percentHeight(.40f)).width(Value.percentHeight(.40f)).left().padLeft(Value.percentWidth(.2f)).padBottom(Value.percentHeight(.2f));

        String backgroundStr = prefs.getString("background", "halloween_blur");
        String url = "backgrounds/" + backgroundStr + ".jpg";
        background = new Texture(Gdx.files.internal(url));
        SpriteDrawable backspr = new SpriteDrawable(new Sprite(background));
        table.setBackground(backspr);
        table.setFillParent(true);
        //table.debug();
        
        stage.addActor(table);

        stage.addAction(Actions.sequence(Actions.alpha(0)
                , Actions.fadeIn(1f), Actions.delay(1f), Actions.run(new Runnable() {
            @Override
            public void run() {

            }
        })));


        Gdx.input.setInputProcessor(stage);
   }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
        stage.dispose();        
    }

}