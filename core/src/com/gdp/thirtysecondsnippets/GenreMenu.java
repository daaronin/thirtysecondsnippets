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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import java.util.ArrayList;

/**
 *
 * @author George McDaid
 */
public class GenreMenu implements Screen{
    
    private Stage stage = new Stage();
    private Table table = new Table();

    private Skin skin = new Skin(Gdx.files.internal("skin.json"),
        new TextureAtlas(Gdx.files.internal("buttons.pack")));

    
    
    @Override
    public void show() {
        MusicDB db = new MusicDB();
        ArrayList<Genre> genres = db.getGenres();
        
        for(int i = 0;i < genres.size();i++){
            if(i == ((genres.size() + genres.size()%2)/2)){
                table.row();
            }
            
            TextButton button = new TextButton(genres.get(i).getName(), skin);
            
            button.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    //Same way we moved here from the Splash Screen
                    //We set it to new Splash because we got no other screens
                    //otherwise you put the screen there where you want to go
                    System.out.println("Play");
                }
            });

            //The elements are displayed in the order you add them.
            //The first appear on top, the last at the bottom.
            table.add(button).size(350,120).padBottom(10).padTop(10).padRight(10).padLeft(10);
            
            
        }
        
        Table table_parent = new Table();
        ScrollPane pane = new ScrollPane(table);
        table_parent.add(pane);

        stage.addActor(table_parent);
        table_parent.setFillParent(true);
        

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
