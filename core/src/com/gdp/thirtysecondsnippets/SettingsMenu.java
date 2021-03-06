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
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 *
 * @author George McDaid
 */
public class SettingsMenu implements Screen{
    
    Game tss;
    private Stage stage = new Stage(new StretchViewport(TSS.WIDTH, TSS.HEIGHT));
    private Table table = new Table();
    
    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("buttons.pack"));

    private Skin skin = new Skin(Gdx.files.internal("skin.json"), atlas);

    Preferences prefs = Gdx.app.getPreferences("30SSSettings");
    
    public SettingsMenu(Game tss){
        this.tss = tss;
    }

    @Override
    public void show() {
        ImageButton title = new ImageButton(skin.getDrawable("settings"));
        final Slider musicvolslider = new Slider(0f, 20f, 1f, false, skin.get("slider", SliderStyle.class));
        
        musicvolslider.setValue(prefs.getFloat("musicvol", 10));
        
        musicvolslider.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    prefs.putFloat("musicvol", musicvolslider.getValue());
                    prefs.flush();
                }
        });
        
        SliderStyle style = skin.get("slider", SliderStyle.class);
        style.knobAfter = skin.getDrawable("slider_after");
        final Slider soundfxslider = new Slider(0f, 20f, 1f, false, style);
        soundfxslider.setValue(prefs.getFloat("soundsfxvol", 8));
        
        soundfxslider.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    prefs.putFloat("soundsfxvol", soundfxslider.getValue());
                    prefs.flush();
                }
        });
        
        final CheckBox guidecheck = new CheckBox("", skin.get("strobecheck", CheckBoxStyle.class));
        
        boolean checked2 = prefs.getBoolean("guide", false);
        guidecheck.setChecked(checked2);
        
        guidecheck.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    prefs.putBoolean("guide", guidecheck.isChecked());
                    prefs.flush();
                }
        });
        
        Label musicvol = new Label("Music Volume:", skin.get("labelb", LabelStyle.class));
        Label soundfxvol = new Label("Sound FX Volume:", skin.get("labelb", LabelStyle.class));
//        Label strobelabel = new Label("Strobe?", skin.get("labelb", LabelStyle.class));
        Label guideelabel = new Label("Guide Thread?", skin.get("labelb", LabelStyle.class));
        
        TextButton back = new TextButton("<", skin.get("back", TextButton.TextButtonStyle.class));
        
        back.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    MainMenu game = new MainMenu(tss);
                    tss.setScreen(game);
                }
        });
        
        TextButton themeselect = new TextButton("Theme Select", skin.get("blue", TextButton.TextButtonStyle.class));
        themeselect.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    //Same way we moved here from the Splash Screen
                    //We set it to new Splash because we got no other screens
                    //otherwise you put the screen there where you want to go
                    ThemeMenu menu = new ThemeMenu(tss);
                    tss.setScreen(menu);
                }
        });
                
        table.add(title).top().padTop(2).colspan(2).width(Value.percentWidth(.5f, table)).height(Value.percentHeight(.15f, table)).padBottom(40);
        table.row();
        table.add(musicvol).right();//label
        table.add(musicvolslider).center().width(Value.percentWidth(5f)).height(Value.percentHeight(.5f)).padLeft(20);
        table.row();
        table.add(soundfxvol).padTop(40).right();//label
        table.add(soundfxslider).center().width(Value.percentWidth(5f)).height(Value.percentHeight(.5f)).padLeft(20).padTop(45);
        table.row();
        table.add(guideelabel).padBottom(20).padTop(20).right();//label
        table.add(guidecheck).center().width(Value.percentWidth(1f)).height(Value.percentHeight(1f)).padLeft(20).padTop(15).padBottom(20);
        table.row();
        //table.add(themeselect).padTop(40).padBottom(20).center().colspan(2).width(Value.percentWidth(.8f)).height(Value.percentHeight(.4f));//label
        table.row();
        table.add(back).height(Value.percentHeight(.4f)).width(Value.percentHeight(.4f)).left().bottom().padLeft(Value.percentHeight(.20f)).padTop(Value.percentHeight(.40f)).padBottom(Value.percentHeight(.20f));
        
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