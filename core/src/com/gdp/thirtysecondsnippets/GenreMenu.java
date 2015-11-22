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
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.ArrayList;

/**
 *
 * @author George and Dan
 */
public class GenreMenu implements Screen{
    
    private Game tss;
    
    int difficulty = 0;

    private Stage stage = new Stage(new StretchViewport(TSS.WIDTH, TSS.HEIGHT));
    private Table table = new Table();

    TextureAtlas menu0 = new TextureAtlas(Gdx.files.internal("menu/Menus0.txt"));
    TextureAtlas menu1 = new TextureAtlas(Gdx.files.internal("menu/Menus1.txt"));
    TextureAtlas menu2 = new TextureAtlas(Gdx.files.internal("menu/Menus2.txt"));
    TextureAtlas menu3 = new TextureAtlas(Gdx.files.internal("menu/Menus3.txt"));

    private Skin skin = new Skin(menu0);

    Preferences prefs = Gdx.app.getPreferences("30SSSettings");

    TextButton back;
    TextButton basket;

    Texture background;

    public GenreMenu(Game tss, int difficulty){
        this.tss = tss;
        this.difficulty = difficulty;
    }
    
    @Override
    public void show() {
        skin.addRegions(menu1);
        skin.addRegions(menu2);
        skin.addRegions(menu3);
        skin.load(Gdx.files.internal("skin.json"));
        skin.getFont("font").getData().setScale(0.75f,0.75f);
        skin.getFont("bfont").getData().setScale(0.75f,0.75f);

        final ArrayList<Genre> genres = new ArrayList();

        genres.add(new Genre(0, "Whispered"));
        genres.add(new Genre(1, "Find a Way"));
        genres.add(new Genre(2, "Lost Woods"));
        genres.add(new Genre(3, "Handlebars"));
        genres.add(new Genre(4, "8-bit"));
        genres.add(new Genre(5, "Random"));

        final String[] colors = {"green", "orange", "blue"};
        
        for(int i = 0;i < genres.size();i++){
            final int current = i;
            if(i == 3 || i == 6){
                table.row();
            }
            int colours = 0;
            switch (i){
                case 0:
                    colours = 2;
                    break;
                case 1:
                    colours = 0;
                    break;
                case 2:
                    colours = 1;
                    break;
                case 3:
                    colours = 0;
                    break;
                case 4:
                    colours = 2;
                    break;
                case 5:
                    colours = 0;
                    break;
                case 6:
                    colours = 1;
                    break;
                case 7:
                    colours = 0;
                    break;
                case 8:
                    colours = 2;
                    break;
                default:
                    break;
            }
            TextButton button = new TextButton(genres.get(i).getName(), skin.get(colors[colours], TextButtonStyle.class));
            
            button.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    //Same way we moved here from the Splash Screen
                    //We set it to new Splash because we got no other screens
                    //otherwise you put the screen there where you want to go
                    stage.addAction(Actions.sequence( Actions.fadeOut(.5f), Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            LoadTrackData load = new LoadTrackData(tss, genres.get(current).getId(), difficulty);
                            tss.setScreen(load);
                        }
                    })));

                }
            });

            //The elements are displayed in the order you add them.
            //The first appear on top, the last at the bottom.
            table.add(button).size(350,120).padBottom(10).padTop(10).padRight(10).padLeft(10);
            
            
        }
        
        Table table_root = new Table();

        String backgroundStr = prefs.getString("background", "halloween_blur");
        String url = "backgrounds/" + backgroundStr + ".jpg";
        background = new Texture(Gdx.files.internal(url));
        SpriteDrawable backspr = new SpriteDrawable(new Sprite(background));
        table_root.setBackground(backspr);

        if (prefs.getString("background").equals("halloween_blur") || prefs.getString("background").equals("ocean_blur")
                || prefs.getString("background").equals("woods_blur")){
            back = new TextButton("<", skin.get("setting", TextButton.TextButtonStyle.class));
        } else {
            back = new TextButton("<", skin.get("back", TextButton.TextButtonStyle.class));
        }

        basket = new TextButton("", skin.get("basket", TextButtonStyle.class));

        back.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            stage.addAction(Actions.sequence( Actions.fadeOut(.5f), Actions.run(new Runnable() {
                @Override
                public void run() {
                    MainMenu game = new MainMenu(tss);
                    tss.setScreen(game);
                }
            })));
                }
        });

        basket.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                ThemeMenu menu = new ThemeMenu(tss);
                tss.setScreen(menu);
            }
        });
        
        ScrollPane pane = new ScrollPane(table);
        table_root.add(pane).expand();
        table_root.row();
        table_root.add(back).height(Value.percentHeight(.40f)).width(Value.percentHeight(.40f)).left().padLeft(Value.percentWidth(.2f)).padBottom(Value.percentHeight(.2f));
        //table_root.add(basket).height(Value.percentWidth(.27f)).width(Value.percentWidth(.36f)).right().padRight(Value.percentWidth(.4f)).padBottom(Value.percentHeight(.2f));
        //table_root.add(basket).left().width(Value.percentWidth(.18f, table)).height(Value.percentWidth(.135f, table)).padBottom(Value.percentHeight(.2f)).padLeft(Value.percentWidth(.2f));

        stage.addActor(table_root);

        table_root.setFillParent(true);

        stage.addAction(Actions.sequence(Actions.alpha(0)
                , Actions.fadeIn(.5f), Actions.run(new Runnable() {
            @Override
            public void run() {

            }
        })));


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
        stage.dispose();
        menu0.dispose();
        menu1.dispose();
        menu2.dispose();
        menu3.dispose();
        background.dispose();
        skin.dispose();
        table.clear();
    }
    
}