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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;

/**
 *
 * @author George McDaid
 */
public class SettingsMenu implements Screen{
    
    Game tss;
    private Stage stage = new Stage();
    private Table table = new Table();
    
    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("buttons.pack"));

    private Skin skin = new Skin(Gdx.files.internal("skin.json"), atlas);

    
    public SettingsMenu(Game tss){
        this.tss = tss;
    }

    @Override
    public void show() {
        ImageButton title = new ImageButton(skin.getDrawable("settings"));
        Slider slide = new Slider(0f, 20f, 1f, false, skin.get("slider", SliderStyle.class));
        
        SliderStyle style = skin.get("slider", SliderStyle.class);
        style.knobAfter = skin.getDrawable("slider_after");
        Slider slide2 = new Slider(0f, 20f, 1f, false, style);
        
        CheckBox strobecheck = new CheckBox("", skin.get("strobecheck", CheckBoxStyle.class));
        
        Label musicvol = new Label("Music Volume ", skin.get("labelb", LabelStyle.class));
        Label soundfxvol = new Label("Sound FX Volume ", skin.get("labelb", LabelStyle.class));
        Label strobelabel = new Label("Strobe?", skin.get("labelb", LabelStyle.class));
        
        table.add(title).top().padTop(20).colspan(2).width(Value.percentWidth(.5f, table)).height(Value.percentHeight(.3f, table)).expand();
        table.row();
        table.add(musicvol);//label
        table.add(slide).center().width(Value.percentWidth(5f)).height(Value.percentHeight(.5f)).padLeft(20);
        table.row();
        table.add(soundfxvol).padTop(40);//label
        table.add(slide2).center().width(Value.percentWidth(5f)).height(Value.percentHeight(.5f)).padLeft(20).padTop(45);
        table.row();
        table.add(strobelabel).padTop(40).padBottom(40);//label
        table.add(strobecheck).center().width(Value.percentWidth(1f)).height(Value.percentHeight(1f)).padLeft(20).padTop(45).padBottom(40);
        
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
