/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gdp.thirtysecondsnippets;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author George McDaid
 */
public class GenreMenu implements Screen{
    
    private Game tss;
    
    private Stage stage = new Stage();
    private Table table = new Table();
    
    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("buttons.pack"));

    private Skin skin = new Skin(Gdx.files.internal("skin.json"), atlas);

    public GenreMenu(Game tss){
        this.tss = tss;
    }
    
    @Override
    public void show() {
        final MusicDB db = new MusicDB();
        final ArrayList<Genre> genres = db.getGenres();
        
        final String[] colors = {"green", "orange", "blue"};
        
        for(int i = 0;i < genres.size();i++){
            final int current = i;
            if(i == ((genres.size() + genres.size()%2)/2)){
                table.row();
            }
            
            TextButton button = new TextButton(genres.get(i).getName(), skin.get(colors[i%colors.length], TextButtonStyle.class));
            
            button.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    //Same way we moved here from the Splash Screen
                    //We set it to new Splash because we got no other screens
                    //otherwise you put the screen there where you want to go
                    LoadTrackData load = new LoadTrackData(tss, genres.get(current).getId());
                    tss.setScreen(load);
                }
            });

            //The elements are displayed in the order you add them.
            //The first appear on top, the last at the bottom.
            table.add(button).size(350,120).padBottom(10).padTop(10).padRight(10).padLeft(10);
            
            
        }
        
        Table table_root = new Table();
        table_root.setBackground(skin.get("bg", Drawable.class));
        
        TextButton back = new TextButton("<", skin.get("back", TextButton.TextButtonStyle.class));
        
        back.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    MainMenu game = new MainMenu(tss);
                    tss.setScreen(game);
                }
        });
        
        ScrollPane pane = new ScrollPane(table);
        table_root.add(pane).expand();
        table_root.row();
        table_root.add(back).height(Value.percentHeight(.30f)).width(Value.percentHeight(.30f)).left().padLeft(Value.percentWidth(.5f)).padBottom(Value.percentHeight(.3f));
       
        stage.addActor(table_root);
        
        table_root.setFillParent(true);
        //table_root.setDebug(true);

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
        stage.dispose();
        skin.dispose();

    }
    
}