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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 *
 * @author George and Dan
 */
public class StatsScreen implements Screen{
    Game tss;
    private Stage stage = new Stage(new StretchViewport(TSS.WIDTH, TSS.HEIGHT));
    private Table table = new Table();

    TextureAtlas menu0 = new TextureAtlas(Gdx.files.internal("menu/Menus0.txt"));
    TextureAtlas menu1 = new TextureAtlas(Gdx.files.internal("menu/Menus1.txt"));
    TextureAtlas menu2 = new TextureAtlas(Gdx.files.internal("menu/Menus2.txt"));
    TextureAtlas menu3 = new TextureAtlas(Gdx.files.internal("menu/Menus3.txt"));

    private Skin skin = new Skin( menu0);

    Preferences prefs = Gdx.app.getPreferences("30SSSettings");

    TextButton back;

    Texture background;

    public StatsScreen(Game tss){
        this.tss = tss;
    }

    @Override
    public void show() {
        skin.addRegions(menu1);
        skin.addRegions(menu2);
        skin.addRegions(menu3);
        skin.load(Gdx.files.internal("skin.json"));
        skin.getFont("font").getData().setScale(0.8f,0.8f);
        skin.getFont("bfont").getData().setScale(0.8f,0.8f);

        String fontColour = prefs.getString("fontcolor", "labelw");
        MusicDB db = new MusicDB();
        final User user = db.getUserByID(Installation.id());
        //User user = new User();
        //user.insertTestData();
        
        DecimalFormat df = new DecimalFormat();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setGroupingSeparator(',');

        df.setDecimalFormatSymbols(dfs);
        
        Label statsLabel = new Label("Stats", skin.get(fontColour, Label.LabelStyle.class));
       
        Label threaded  = new Label("Needles Threaded", skin.get(fontColour, Label.LabelStyle.class));
        Label threaded_val  = new Label(df.format(user.getNeedles_thread()), skin.get(fontColour, Label.LabelStyle.class));
        
        Label thread_cut  = new Label("Thread Cut", skin.get(fontColour, Label.LabelStyle.class));
        Label thread_cut_val  = new Label(df.format((int) (user.getThread_cut()/2.0))+" m", skin.get(fontColour, Label.LabelStyle.class));
        
        Label songs_played  = new Label("Songs Played", skin.get(fontColour, Label.LabelStyle.class));
        Label songs_played_val  = new Label(df.format(user.getSong_played()), skin.get(fontColour, Label.LabelStyle.class));
        
        Label beats  = new Label("Beats Felt ", skin.get(fontColour, Label.LabelStyle.class));
        Label beats_val  = new Label(df.format(user.getBeats()), skin.get(fontColour, Label.LabelStyle.class));
        
        Label total_needles  = new Label("Needles Seen", skin.get(fontColour, Label.LabelStyle.class));
        Label total_needles_val  = new Label(df.format(user.getTotal_needles()), skin.get(fontColour, Label.LabelStyle.class));
        
        Label thread_rate  = new Label("Thread Rate", skin.get(fontColour, Label.LabelStyle.class));
        System.out.println((int)(((double)user.getNeedles_thread()/(double)user.getTotal_needles())));
        Label thread_rate_val  = new Label(Integer.toString((int)(((double)user.getNeedles_thread()/(double)user.getTotal_needles())*100))+"%", skin.get(fontColour, Label.LabelStyle.class));

        if (prefs.getString("background").equals("halloween_blur") || prefs.getString("background").equals("ocean_blur")
                || prefs.getString("background").equals("woods_blur")){
            back = new TextButton("<", skin.get("setting", TextButton.TextButtonStyle.class));
        } else {
            back = new TextButton("<", skin.get("back", TextButton.TextButtonStyle.class));
        }

        back.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    ThemeMenu game = new ThemeMenu(tss);
                    tss.setScreen(game);
                }
        });        
                
        table.add(statsLabel).top().padTop(2).colspan(2).height(Value.percentHeight(.15f, table));
        table.row();

        table.add(threaded).right();
        table.add(threaded_val).padLeft(35).left();
        table.row();

        table.add(thread_cut).right();
        table.add(thread_cut_val).padLeft(35).left();
        table.row();

        table.add(songs_played).right();
        table.add(songs_played_val).padLeft(35).left();
        table.row();

        table.add(beats).right();
        table.add(beats_val).padLeft(35).left();
        table.row();
        
        table.add(total_needles).right();
        table.add(total_needles_val).padLeft(35).left();
        table.row();

        table.add(thread_rate).right();
        table.add(thread_rate_val).padLeft(35).left();
        table.row();
        
        table.add(back).height(Value.percentHeight(.40f)).width(Value.percentHeight(.40f)).padTop(20).left().colspan(3);

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
        skin.dispose();
        background.dispose();
        stage.dispose();        
    }
}
