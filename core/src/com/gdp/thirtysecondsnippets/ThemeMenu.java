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
        
        prefs.putInteger("unlocked", user.getSong_played()/50);
        
        Texture clicked = new Texture("clicked.png");
        Texture hover = new Texture("hover.png");
        Texture selected = new Texture("selected.png");
        
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
            default:
                break;
        }
                
        
        basicPic.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    prefs.putInteger("theme", 1);
                    prefs.flush();
                    SettingsMenu menu = new SettingsMenu(tss);
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
                        SettingsMenu menu = new SettingsMenu(tss);
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
                        SettingsMenu menu = new SettingsMenu(tss);
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
                        SettingsMenu menu = new SettingsMenu(tss);
                        tss.setScreen(menu);
                    }
            });
        
        }
        
        table_root.setBackground(skin.get("bg_blur", Drawable.class));
        
        TextButton back = new TextButton("<", skin.get("back", TextButton.TextButtonStyle.class));
        
        back.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    SettingsMenu menu = new SettingsMenu(tss);
                    tss.setScreen(menu);
                }
        });
        
        Label basiclbl = new Label("Standard", skin.get("labelb", Label.LabelStyle.class));
        Label folklbl = new Label("Folked Up", skin.get("labelb", Label.LabelStyle.class));
        Label metallbl = new Label("Hardcore", skin.get("labelb", Label.LabelStyle.class));
        Label jazzlbl = new Label("Jazztastic", skin.get("labelb", Label.LabelStyle.class));
        
        ScrollPane pane = new ScrollPane(table);
        pane.setScrollingDisabled(false, true);
        table_root.add(pane).expand();
        table.add(basicPic).height(Value.percentHeight(.40f)).width(Value.percentWidth(.40f)).padLeft(Value.percentWidth(.10f)).padRight(Value.percentWidth(.10f));
        table.add(folkPic).height(Value.percentHeight(.40f)).width(Value.percentWidth(.40f)).padLeft(Value.percentWidth(.10f)).padRight(Value.percentWidth(.10f));
        table.add(metalPic).height(Value.percentHeight(.40f)).width(Value.percentWidth(.40f)).padLeft(Value.percentWidth(.10f)).padRight(Value.percentWidth(.10f));
        table.add(jazzPic).height(Value.percentHeight(.40f)).width(Value.percentWidth(.40f)).padLeft(Value.percentWidth(.10f)).padRight(Value.percentWidth(.10f));
        table.row();
        table.add(basiclbl).padLeft(Value.percentWidth(.10f)).padRight(Value.percentWidth(.10f));
        table.add(folklbl).padLeft(Value.percentWidth(.10f)).padRight(Value.percentWidth(.10f));
        table.add(metallbl).padLeft(Value.percentWidth(.10f)).padRight(Value.percentWidth(.10f));
        table.add(jazzlbl).padLeft(Value.percentWidth(.10f)).padRight(Value.percentWidth(.10f));
        pane.setWidget(table);
        table_root.row();
        table_root.add(back).height(Value.percentHeight(.30f)).width(Value.percentHeight(.30f)).left().padLeft(Value.percentHeight(.20f)).padBottom(Value.percentHeight(.20f));
       
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