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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 *
 * @author George
 */
public class SplashScreen implements Screen{
    Game tss;
    private Stage stage = new Stage(new StretchViewport(TSS.WIDTH, TSS.HEIGHT));
    private Table table = new Table();

    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("Menus.txt"));

    private Skin skin = new Skin(Gdx.files.internal("skin.json"), atlas);

    Preferences prefs = Gdx.app.getPreferences("30SSSettings");

    boolean skipping = false;

    public SplashScreen(Game tss){
        this.tss = tss;
    }

    @Override
    public void show() {

        //Drawable white = skin.getDrawable("whiteblock");
        //Image whiteImg = new Image(white);

        Drawable octoTex = skin.getDrawable("octopieinkhappyclear");
        Image octo = new Image(octoTex);

        table.add(octo).center().width(Value.percentHeight(.86f, table)).height(Value.percentHeight(.97f, table)).padTop(10);
        table.row();

        //TODO fix background scaling
        table.setFillParent(true);
        //table.debug();

        stage.addActor(table);

        stage.addAction(Actions.sequence(Actions.alpha(0)
                , Actions.fadeIn(1f), Actions.delay(1f),Actions.fadeOut(.2f), Actions.delay(.1f), Actions.run(new Runnable() {
            @Override
            public void run() {
                if (skipping){
                    ThirtySecondSnippets game = new ThirtySecondSnippets(tss, 4, 3);
                    tss.setScreen(game);
                } else {
                    MainMenu menu = new MainMenu(tss);
                    tss.setScreen(menu);
                }
            }
        })));

        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, .5f);
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
        atlas.dispose();
        skin.dispose();
        stage.dispose();        
    }
}
