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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 *
 * @author George McDaid
 */
public class MainMenu implements Screen{
    
    private Game tss;
    private Stage stage = new Stage();
    private Table table = new Table();
    
    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("buttons.pack"));

    private Skin skin = new Skin(Gdx.files.internal("skin.json"), atlas);

    public MainMenu(Game tss){
        this.tss = tss;
    }
    
    @Override
    public void show() {
        ImageButton title = new ImageButton(skin.getDrawable("title"));
        TextButton play = new TextButton("Play", skin.get("blue", TextButtonStyle.class));
        TextButton setting = new TextButton("*", skin.get("setting", TextButtonStyle.class));
        TextButton about = new TextButton("?", skin.get("about", TextButtonStyle.class));
        
        play.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    //Same way we moved here from the Splash Screen
                    //We set it to new Splash because we got no other screens
                    //otherwise you put the screen there where you want to go
                    GenreMenu menu = new GenreMenu(tss);
                    tss.setScreen(menu);
                }
            });
        
        table.add(title).top().center().width(Value.percentWidth(.9f)).height(Value.percentHeight(.45f)).padTop(20).colspan(2);
        table.row();
        table.add(play).height(100).width(340).expandY().colspan(2);
        table.row();
        table.add(setting).height(Value.percentHeight(.28f)).width(Value.percentHeight(.28f)).bottom().left().padBottom(Value.percentHeight(.2f)).padLeft(Value.percentWidth(.2f));
        table.add(about).height(Value.percentHeight(.28f)).width(Value.percentHeight(.28f)).bottom().right().padBottom(Value.percentHeight(.2f)).padRight(Value.percentWidth(.2f));
        
        table.setBackground(skin.getDrawable("bg_blur"));
        table.setFillParent(true);
        table.debug();
        
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
    }

    @Override
    public void dispose() {
    }

}
