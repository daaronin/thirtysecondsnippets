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
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 *
 * @author George and Dan
 */
public class SettingsMenu implements Screen{
    
    Game tss;
    private Stage stage = new Stage(new StretchViewport(TSS.WIDTH, TSS.HEIGHT));
    private Table table = new Table();

    TextureAtlas menu0 = new TextureAtlas(Gdx.files.internal("menu/Menus0.txt"));
    TextureAtlas menu1 = new TextureAtlas(Gdx.files.internal("menu/Menus1.txt"));
    TextureAtlas menu2 = new TextureAtlas(Gdx.files.internal("menu/Menus2.txt"));
    TextureAtlas menu3 = new TextureAtlas(Gdx.files.internal("menu/Menus3.txt"));

    private Skin skin = new Skin(menu0);

    Preferences prefs = Gdx.app.getPreferences("30SSSettings");

    TextButton back;

    Texture background, settings;
    
    public SettingsMenu(Game tss){
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
        settings = new Texture(Gdx.files.internal("settings.png"));
        ImageButton title = new ImageButton(new SpriteDrawable(new Sprite(settings)));
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
        
        Label musicvol = new Label("Music Volume:", skin.get(fontColour, LabelStyle.class));
        Label soundfxvol = new Label("Sound FX Volume:", skin.get(fontColour, LabelStyle.class));
//        Label strobelabel = new Label("Strobe?", skin.get(fontColour, LabelStyle.class));
        Label guideelabel = new Label("Guide Thread?", skin.get(fontColour, LabelStyle.class));

        if (prefs.getString("background").equals("halloween_blur") || prefs.getString("background").equals("ocean_blur")
                || prefs.getString("background").equals("woods_blur")){
            back = new TextButton("<", skin.get("setting", TextButton.TextButtonStyle.class));
        } else {
            back = new TextButton("<", skin.get("back", TextButton.TextButtonStyle.class));
        }
        
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
        
        TextButton themeselect = new TextButton("Theme Select", skin.get("blue", TextButton.TextButtonStyle.class));
        themeselect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                //Same way we moved here from the Splash Screen
                //We set it to new Splash because we got no other screens
                //otherwise you put the screen there where you want to go
                ThemeMenu menu = new ThemeMenu(tss);
                tss.setScreen(menu);
                }
        });
                
        table.add(title).top().padTop(2).colspan(2).width(Value.percentWidth(.5f, table)).height(Value.percentHeight(.15f, table)).padBottom(20);
        table.row();
        table.add(musicvol).right();//label
        table.add(musicvolslider).center().width(Value.percentWidth(5f)).height(Value.percentHeight(.5f)).padLeft(20);
        table.row();
        table.add(soundfxvol).padTop(40).right();//label
        table.add(soundfxslider).center().width(Value.percentWidth(5f)).height(Value.percentHeight(.5f)).padLeft(20).padTop(45);
        table.row();
        table.add(guideelabel).padBottom(10).padTop(10).right();//label
        table.add(guidecheck).center().width(Value.percentWidth(1f)).height(Value.percentHeight(1f)).padLeft(20).padTop(20).padBottom(2);
        //table.add(themeselect).padTop(40).padBottom(20).center().colspan(2).width(Value.percentWidth(.8f)).height(Value.percentHeight(.4f));//label
        table.row();
        table.add(back).height(Value.percentHeight(.4f)).width(Value.percentHeight(.4f)).left().bottom().padLeft(Value.percentHeight(.20f)).padTop(Value.percentHeight(.40f)).padBottom(Value.percentHeight(.20f));

        String backgroundStr = prefs.getString("background", "halloween_blur");
        String url = "backgrounds/" + backgroundStr + ".jpg";
        background = new Texture(Gdx.files.internal(url));
        SpriteDrawable backspr = new SpriteDrawable(new Sprite(background));
        table.setBackground(backspr);
        table.setFillParent(true);
        //table.debug();
        
        stage.addActor(table);

        stage.addAction(Actions.sequence(Actions.alpha(0)
                , Actions.fadeIn(.5f), Actions.run(new Runnable() {
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
        background.dispose();
        settings.dispose();
        skin.dispose();
        stage.dispose();
        table.clear();
    }
    
}