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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 *
 * @author George and Dan
 */
public class FinishScreen implements Screen{

    Game tss;
    private Stage stage = new Stage(new StretchViewport(TSS.WIDTH, TSS.HEIGHT));
    private Table table = new Table();

    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("Menus.txt"));
    TextureAtlas backgrounds = new TextureAtlas(Gdx.files.internal("Backgrounds.txt"));

    private Skin skin = new Skin(Gdx.files.internal("skin.json"), atlas);

    Preferences prefs = Gdx.app.getPreferences("30SSSettings");

    String score = "Score: 0";
    Track track = null;
    Results results;
    String title = "title";
    int genreId = 0;
    String artist = "artist";
    int difficulty = 0;

    static final int LEISURELY_DIFFICULTY = 3;
    static final int BRISK_DIFFICULTY = 5;
    static final int BREAKNECK_DIFFICULTY = 7;

    public FinishScreen(Game tss, String score, Track track, Results results, String title, String artist, int difficulty){
        this.tss = tss;
        this.score = score;
        this.track = track;
        this.results = results;
        this.title = title;
        this.artist = artist;
        this.difficulty = difficulty;
    }

    public FinishScreen(Game tss, String score, int genre, Results results, String title, String artist, int difficulty){
        this.tss = tss;
        this.score = score;
        genreId = genre;
        this.results = results;
        this.title = title;
        this.artist = artist;
        this.difficulty = difficulty;
    }

    public FinishScreen(Game tss){
        this.tss = tss;
    }

    @Override
    public void show() {
        skin.addRegions(backgrounds);
        String fontColour = prefs.getString("fontcolor", "labelb");
        Label titleLabel = new Label(title, skin.get(fontColour, Label.LabelStyle.class));
        Label artistLabel = new Label(artist, skin.get(fontColour, Label.LabelStyle.class));

        Label scoreLabel = new Label(score, skin.get(fontColour, Label.LabelStyle.class));

        TextButton replay = new TextButton("Replay", skin.get("green", TextButton.TextButtonStyle.class));

        replay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                //Same way we moved here from the Splash Screen
                //We set it to new Splash because we got no other screens
                //otherwise you put the screen there where you want to go
                LoadTrackData load = new LoadTrackData(tss, genreId, difficulty);
                tss.setScreen(load);
            }
        });

        TextButton genres = new TextButton("New Game", skin.get("orange", TextButton.TextButtonStyle.class));

        genres.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                //Same way we moved here from the Splash Screen
                //We set it to new Splash because we got no other screens
                //otherwise you put the screen there where you want to go
                GenreMenu menu = new GenreMenu(tss, LEISURELY_DIFFICULTY);
                tss.setScreen(menu);
            }
        });
        table.add(titleLabel).top().center().height(Value.percentHeight(.15f, table)).colspan(2);
        table.row();
        table.add(artistLabel).top().center().height(Value.percentHeight(.15f, table)).colspan(2);
        table.row();
        table.add(scoreLabel).top().center().height(Value.percentHeight(.15f, table)).colspan(2);
        for(int i = 0;i<results.getResults().size();i++){
            table.row();
            Label infolabel = new Label((CharSequence) results.getResults().get(i), skin.get(fontColour, Label.LabelStyle.class));
            table.add(infolabel).top().center().height(Value.percentHeight(.15f, table)).colspan(2);
        }
        table.row();
        table.add(replay).size(450, 120).padBottom(10).padTop(30);
        table.add(genres).size(450,120).padBottom(10).padTop(30).padLeft(10);

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
        this.dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        atlas.dispose();
        backgrounds.dispose();
        skin.dispose();
        table.clear();
    }
    
}
