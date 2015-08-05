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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author George McDaid
 */
public class ThemeMenu implements Screen{
    
    private Game tss;
    
    int difficulty = 0;
    
    int selectedTheme = 1;
    
    ImageButton basicPic;
    ImageButton folkPic;
    ImageButton metalPic;
    ImageButton jazzPic;
    ImageButton bubblePic;
    
    private Stage stage = new Stage();
    private Table table = new Table();
    
    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("buttons.pack"));

    private Skin skin = new Skin(Gdx.files.internal("skin.json"), atlas);
    
    Preferences prefs = Gdx.app.getPreferences("30SSSettings");

    public ThemeMenu(Game tss){
        this.tss = tss;
    }
    
    @Override
    public void show() {
        Table table_root = new Table();
        MusicDB db = new MusicDB();
        final User user = db.getUserByID(Installation.id());
        
        prefs.putInteger("unlocked", user.getSong_played()/10);
        
        Texture clicked = new Texture("clicked.png");
        Texture hover = new Texture("hover.png");
        Texture selected = new Texture("selected.png");
        
        Label basiclbl = new Label("Standard", skin.get("labelb", Label.LabelStyle.class));
        Label folklbl = new Label("Folked Up", skin.get("labelb", Label.LabelStyle.class));
        Label metallbl = new Label("Hardcore", skin.get("labelb", Label.LabelStyle.class));
        Label jazzlbl = new Label("Jazztastic", skin.get("labelb", Label.LabelStyle.class));
        Label bubblelbl = new Label("Pop Art", skin.get("labelb", Label.LabelStyle.class));
        
        Texture basic;
        
        basic = new Texture("levelbasic.png");
        
        ImageButtonStyle style = new ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(basic));
        style.down = new SpriteDrawable(new Sprite(basic));
        style.imageUp = new SpriteDrawable(new Sprite(basic));
        style.imageOver = new SpriteDrawable(new Sprite(hover));
        style.imageChecked = new SpriteDrawable(new Sprite(selected));
        style.imageDown = new SpriteDrawable(new Sprite(clicked));
        basicPic = new ImageButton(style);
        
        Texture folk;
        if (prefs.getInteger("unlocked", 1) > 1){
            folk = new Texture("levelfolk.png");
        } else {
            folk = new Texture("levelfolklocked.jpg");
            folklbl.setText("*Play 20 Songs*");
        }
        
        ImageButtonStyle style2 = new ImageButtonStyle();
        style2.up = new SpriteDrawable(new Sprite(folk));
        style2.down = new SpriteDrawable(new Sprite(folk));
        style2.imageUp = new SpriteDrawable(new Sprite(folk));
        style2.imageOver = new SpriteDrawable(new Sprite(hover));
        style2.imageChecked = new SpriteDrawable(new Sprite(selected));
        style2.imageDown = new SpriteDrawable(new Sprite(clicked));
        folkPic = new ImageButton(style2);
        folkPic.setDisabled(true);
        
        Texture metal;
        
        if (prefs.getInteger("unlocked", 1)  > 2){
            metal = new Texture("levelmetal.png");
        } else {
            metal = new Texture("levelmetallocked.jpg");
            metallbl.setText("*Play 30 Songs*");
        }
        
        ImageButtonStyle style3 = new ImageButtonStyle();
        style3.up = new SpriteDrawable(new Sprite(metal));
        style3.down = new SpriteDrawable(new Sprite(metal));
        style3.imageUp = new SpriteDrawable(new Sprite(metal));
        style3.imageOver = new SpriteDrawable(new Sprite(hover));
        style3.imageChecked = new SpriteDrawable(new Sprite(selected));
        style3.imageDown = new SpriteDrawable(new Sprite(clicked));
        metalPic = new ImageButton(style3);
        metalPic.setDisabled(true);
        
        Texture jazz;
        
        if (prefs.getInteger("unlocked", 1) > 3){
            jazz = new Texture("leveljazz2.png");
        } else {
            jazz = new Texture("leveljazz2locked.jpg");
            jazzlbl.setText("*Play 40 Songs*");
        }
        
        ImageButtonStyle style4 = new ImageButtonStyle();
        style4.up = new SpriteDrawable(new Sprite(jazz));
        style4.down = new SpriteDrawable(new Sprite(jazz));
        style4.imageUp = new SpriteDrawable(new Sprite(jazz));
        style4.imageOver = new SpriteDrawable(new Sprite(hover));
        style4.imageChecked = new SpriteDrawable(new Sprite(selected));
        style4.imageDown = new SpriteDrawable(new Sprite(clicked));
        jazzPic = new ImageButton(style4);
        jazzPic.setDisabled(true);
        
        Texture bubble;
        
        if (prefs.getInteger("unlocked", 1) > 4){
            bubble = new Texture("levelbubble.png");
        } else {
            bubble = new Texture("levelbubblelocked.jpg");
            bubblelbl.setText("*Play 50 Songs*");
        }
        
        ImageButtonStyle style5 = new ImageButtonStyle();
        style5.up = new SpriteDrawable(new Sprite(bubble));
        style5.down = new SpriteDrawable(new Sprite(bubble));
        style5.imageUp = new SpriteDrawable(new Sprite(bubble));
        style5.imageOver = new SpriteDrawable(new Sprite(hover));
        style5.imageChecked = new SpriteDrawable(new Sprite(selected));
        style5.imageDown = new SpriteDrawable(new Sprite(clicked));
        bubblePic = new ImageButton(style5);
        bubblePic.setDisabled(true);
        
        switch(prefs.getInteger("theme", 1)){
            case 1:
                basicPic.setChecked(true);
                break;
            case 2:
                folkPic.setChecked(true);
                break;
            case 3:
                metalPic.setChecked(true);
                break;
            case 5:
                jazzPic.setChecked(true);
                break;
            case 6:
                bubblePic.setChecked(true);
            default:
                break;
        }
                
        
        basicPic.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    prefs.putInteger("theme", 1);
                    prefs.flush();
                    MainMenu menu = new MainMenu(tss);
                    tss.setScreen(menu);
                    
                }
        });
        
        if (prefs.getInteger("unlocked", 1) > 1){
            folkPic.setDisabled(false);
            folkPic.addListener(new ChangeListener(){
                    @Override
                    public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                        prefs.putInteger("theme", 2);
                        prefs.flush();
                        MainMenu menu = new MainMenu(tss);
                        tss.setScreen(menu);
                    }
            });
        
        }
        if (prefs.getInteger("unlocked", 1) > 2){
            metalPic.setDisabled(false);
            metalPic.addListener(new ChangeListener(){
                    @Override
                    public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                        prefs.putInteger("theme", 3);
                        prefs.flush();
                        MainMenu menu = new MainMenu(tss);
                        tss.setScreen(menu);
                    }
            });
        
        }
        if (prefs.getInteger("unlocked", 1) > 3) {
            jazzPic.setDisabled(false);
            jazzPic.addListener(new ChangeListener(){
                    @Override
                    public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                        prefs.putInteger("theme", 5);
                        prefs.flush();
                        MainMenu menu = new MainMenu(tss);
                        tss.setScreen(menu);
                    }
            });
        
        }
        if (prefs.getInteger("unlocked", 1) > 4) {
            bubblePic.setDisabled(false);
            bubblePic.addListener(new ChangeListener(){
                    @Override
                    public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                        prefs.putInteger("theme", 6);
                        prefs.flush();
                        MainMenu menu = new MainMenu(tss);
                        tss.setScreen(menu);
                    }
            });
        
        }
        
        TextButton statselect = new TextButton("Stats", skin.get("blue", TextButton.TextButtonStyle.class));
        statselect.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    //Same way we moved here from the Splash Screen
                    //We set it to new Splash because we got no other screens
                    //otherwise you put the screen there where you want to go
                    StatsScreen menu = new StatsScreen(tss);
                    tss.setScreen(menu);
                }
        });
        
        table_root.setBackground(skin.get("bg_blur", Drawable.class));
        
        TextButton back = new TextButton("<", skin.get("back", TextButton.TextButtonStyle.class));
        
        back.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    MainMenu menu = new MainMenu(tss);
                    tss.setScreen(menu);
                }
        });
        

        
        ScrollPane pane = new ScrollPane(table);
        pane.setScrollingDisabled(false, true);
        table_root.add(pane).expand().colspan(3);
        table.add(basicPic).height(Value.percentHeight(.40f)).width(Value.percentWidth(.40f)).padLeft(Value.percentWidth(.10f)).padRight(Value.percentWidth(.10f));
        table.add(folkPic).height(Value.percentHeight(.40f)).width(Value.percentWidth(.40f)).padLeft(Value.percentWidth(.10f)).padRight(Value.percentWidth(.10f));
        table.add(metalPic).height(Value.percentHeight(.40f)).width(Value.percentWidth(.40f)).padLeft(Value.percentWidth(.10f)).padRight(Value.percentWidth(.10f));
        table.add(jazzPic).height(Value.percentHeight(.40f)).width(Value.percentWidth(.40f)).padLeft(Value.percentWidth(.10f)).padRight(Value.percentWidth(.10f));
        table.add(bubblePic).height(Value.percentHeight(.40f)).width(Value.percentWidth(.40f)).padLeft(Value.percentWidth(.10f)).padRight(Value.percentWidth(.10f));
        table.row();
        table.add(basiclbl).padLeft(Value.percentWidth(.10f)).padRight(Value.percentWidth(.10f));
        table.add(folklbl).padLeft(Value.percentWidth(.10f)).padRight(Value.percentWidth(.10f));
        table.add(metallbl).padLeft(Value.percentWidth(.10f)).padRight(Value.percentWidth(.10f));
        table.add(jazzlbl).padLeft(Value.percentWidth(.10f)).padRight(Value.percentWidth(.10f));
        table.add(bubblelbl).padLeft(Value.percentWidth(.10f)).padRight(Value.percentWidth(.10f));
        pane.setWidget(table);
        table_root.row();
        table_root.add(back).height(Value.percentHeight(.40f)).width(Value.percentHeight(.40f)).center().padLeft(Value.percentHeight(.20f)).padBottom(Value.percentHeight(.20f)).colspan(1);
        table_root.add(statselect).padRight(Value.percentHeight(.20f)).padBottom(Value.percentHeight(.20f)).center().colspan(1).width(Value.percentWidth(.5f)).height(Value.percentHeight(.4f)).center();//label
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