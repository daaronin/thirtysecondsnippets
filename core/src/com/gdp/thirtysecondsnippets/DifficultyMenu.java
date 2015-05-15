/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gdp.thirtysecondsnippets;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Json;
import java.util.ArrayList;

/**
 *
 * @author George McDaid
 */
public class DifficultyMenu implements Screen{
    
    private Game tss;
    private Stage stage = new Stage();
    private Table table = new Table();
    
    static final int LEISURELY_DIFFICULTY = 7;
    static final int BRISK_DIFFICULTY = 5;
    static final int BREAKNECK_DIFFICULTY = 3;
    
    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("buttons.pack"));

    private Skin skin = new Skin(Gdx.files.internal("skin.json"), atlas);

    public DifficultyMenu(Game tss){
        this.tss = tss;
    }
    
    @Override
    public void show() {
        MusicDB db = new MusicDB();
        ArrayList<Genre> genres = db.getGenres();
        
        Json json = new Json();
        json.addClassTag("genre", Genre.class); // This may not be needed. I don't know how json deals with String
        FileHandle handle = Gdx.files.external("genre_list");
        if (handle.exists()) {
            handle.delete();
        }
        json.toJson(genres, handle);
        
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
                    GenreMenu menu = new GenreMenu(tss, LEISURELY_DIFFICULTY);
                    tss.setScreen(menu);
                }
        });
        brisk.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    //Same way we moved here from the Splash Screen
                    //We set it to new Splash because we got no other screens
                    //otherwise you put the screen there where you want to go
                    GenreMenu menu = new GenreMenu(tss, BRISK_DIFFICULTY);
                    tss.setScreen(menu);
                }
        });
        breakneck.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    //Same way we moved here from the Splash Screen
                    //We set it to new Splash because we got no other screens
                    //otherwise you put the screen there where you want to go
                    GenreMenu menu = new GenreMenu(tss, BREAKNECK_DIFFICULTY);
                    tss.setScreen(menu);
                }
        });
        
        TextButton back = new TextButton("<", skin.get("back", TextButton.TextButtonStyle.class));
        
        back.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    MainMenu game = new MainMenu(tss);
                    tss.setScreen(game);
                }
        });
        
        
        table.add(title).top().center().width(Value.percentWidth(.9f)).height(Value.percentHeight(.45f)).padTop(10).colspan(3);
        table.row();
        table.add(leisurely).height(80).width(340).expandY().colspan(3);
        table.row();
        table.add(brisk).height(80).width(340).expandY().colspan(3);
        table.row();
        table.add(breakneck).height(80).width(340).expandY().colspan(3);
        table.row();
        
        table.add(back).height(Value.percentHeight(.30f)).width(Value.percentHeight(.30f)).left().padLeft(Value.percentWidth(.5f)).padBottom(Value.percentHeight(.3f));
        
        table.setBackground(skin.getDrawable("bg_blur"));
        table.setFillParent(true);
        //table.debug();
        
        stage.addActor(table);
        
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
        skin.dispose();
        stage.dispose();        
    }

}