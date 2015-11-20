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
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 *
 * @author George and Dan
 */
public class ThemeMenu implements Screen{
    
    private Game tss;
    
    ImageButton basicPic;
    ImageButton folkPic;
    ImageButton metalPic;
    ImageButton jazzPic;
    ImageButton bubblePic;

    private Stage stage = new Stage(new StretchViewport(TSS.WIDTH, TSS.HEIGHT));
    private Table table = new Table();

    TextureAtlas menu0 = new TextureAtlas(Gdx.files.internal("menu/Menus0.txt"));
    TextureAtlas menu1 = new TextureAtlas(Gdx.files.internal("menu/Menus1.txt"));
    TextureAtlas menu2 = new TextureAtlas(Gdx.files.internal("menu/Menus2.txt"));
    TextureAtlas menu3 = new TextureAtlas(Gdx.files.internal("menu/Menus3.txt"));

    private Skin skin = new Skin(menu0);
    
    Preferences prefs = Gdx.app.getPreferences("30SSSettings");

    TextButton back;

    Texture background, cli, hov, sel, basic, basicLocked, bubble,
            bubbleLocked, folk, folkLocked, jazz, jazzLocked, metal, metalLocked;

    public ThemeMenu(Game tss){
        this.tss = tss;
    }
    
    @Override
    public void show() {
        skin.addRegions(menu1);
        skin.addRegions(menu2);
        skin.addRegions(menu3);
        skin.load(Gdx.files.internal("skin.json"));

        cli = new Texture(Gdx.files.internal("themes/clicked.png"));
        skin.add("clicked", cli );
        hov = new Texture(Gdx.files.internal("themes/hover.png"));
        skin.add("hover", hov);
        sel = new Texture(Gdx.files.internal("themes/selected.png"));
        skin.add("selected", sel);
        basic = new Texture(Gdx.files.internal("themes/levelbasic.jpg"));
        skin.add("levelbasic", basic);
        basicLocked = new Texture(Gdx.files.internal("themes/levelbasiclocked.jpg"));
        skin.add("levelbasiclocked", basicLocked);
        bubble = new Texture(Gdx.files.internal("themes/levelbubble.jpg"));
        skin.add("levelbubble", bubble);
        bubbleLocked = new Texture(Gdx.files.internal("themes/levelbubblelocked.jpg"));
        skin.add("levelbubblelocked", bubbleLocked);
        folk = new Texture(Gdx.files.internal("themes/levelfolk.jpg"));
        skin.add("levelfolk", folk);
        folkLocked = new Texture(Gdx.files.internal("themes/levelfolklocked.jpg"));
        skin.add("levelfolklocked", folkLocked);
        jazz = new Texture(Gdx.files.internal("themes/leveljazz2.jpg"));
        skin.add("leveljazz2", jazz);
        jazzLocked = new Texture(Gdx.files.internal("themes/leveljazz2locked.jpg"));
        skin.add("leveljazz2locked", jazzLocked);
        metal = new Texture(Gdx.files.internal("themes/levelmetal.jpg"));
        skin.add("levelmetal", metal);
        metalLocked = new Texture(Gdx.files.internal("themes/levelmetallocked.jpg"));
        skin.add("levelmetallocked", metalLocked);



        String fontColour = prefs.getString("fontcolor", "labelw");
        Table table_root = new Table();
        MusicDB db = new MusicDB();
        final User user = db.getUserByID(Installation.id());

        if (prefs.getInteger("unlocked") < 5) {
            prefs.putInteger("unlocked", user.getSong_played() / 10);
        }

        Drawable clicked = skin.getDrawable("clicked");
        Drawable hover = skin.getDrawable("hover");
        Drawable selected = skin.getDrawable("selected");
        
        Label basiclbl = new Label("Standard", skin.get(fontColour, Label.LabelStyle.class));
        Label folklbl = new Label("Folked Up", skin.get(fontColour, Label.LabelStyle.class));
        Label metallbl = new Label("Hardcore", skin.get(fontColour, Label.LabelStyle.class));
        Label jazzlbl = new Label("Jazztastic", skin.get(fontColour, Label.LabelStyle.class));
        Label bubblelbl = new Label("Pop Art", skin.get(fontColour, Label.LabelStyle.class));
        
        Drawable basic;
        
        basic = skin.getDrawable("levelbasic");
        
        ImageButtonStyle style = new ImageButtonStyle();
        style.up = basic;
        style.down = basic;
        style.imageUp = basic;
        style.imageOver = hover;
        style.imageChecked = selected;
        style.imageDown = clicked;
        basicPic = new ImageButton(style);

        Drawable folk;
        if (prefs.getInteger("unlocked", 1) > 1){
            folk = skin.getDrawable("levelfolk");
        } else {
            folk = skin.getDrawable("levelfolklocked");
            folklbl.setText("*Play 20 Songs*");
        }
        
        ImageButtonStyle style2 = new ImageButtonStyle();
        style2.up = folk;
        style2.down = folk;
        style2.imageUp = folk;
        style2.imageOver = hover;
        style2.imageChecked = selected;
        style2.imageDown = clicked;
        folkPic = new ImageButton(style2);
        folkPic.setDisabled(true);

        Drawable metal;

        if (prefs.getInteger("unlocked", 1)  > 2){
            metal = skin.getDrawable("levelmetal");
        } else {
            metal = skin.getDrawable("levelmetallocked");
            metallbl.setText("*Play 30 Songs*");
        }
        
        ImageButtonStyle style3 = new ImageButtonStyle();
        style3.up = metal;
        style3.down = metal;
        style3.imageUp = metal;
        style3.imageOver = hover;
        style3.imageChecked = selected;
        style3.imageDown = clicked;
        metalPic = new ImageButton(style3);
        metalPic.setDisabled(true);
        
        Drawable jazz;
        
        if (prefs.getInteger("unlocked", 1) > 3){
            jazz = skin.getDrawable("leveljazz2");
        } else {
            jazz = skin.getDrawable("leveljazz2locked");
            jazzlbl.setText("*Play 40 Songs*");
        }
        
        ImageButtonStyle style4 = new ImageButtonStyle();
        style4.up = jazz;
        style4.down = jazz;
        style4.imageUp = jazz;
        style4.imageOver = hover;
        style4.imageChecked = selected;
        style4.imageDown = clicked;
        jazzPic = new ImageButton(style4);
        jazzPic.setDisabled(true);
        
        Drawable bubble;
        
        if (prefs.getInteger("unlocked", 1) > 4){
            bubble = skin.getDrawable("levelbubble");
        } else {
            bubble = skin.getDrawable("levelbubblelocked");
            bubblelbl.setText("*Play 50 Songs*");
        }
        
        ImageButtonStyle style5 = new ImageButtonStyle();
        style5.up = bubble;
        style5.down = bubble;
        style5.imageUp = bubble;
        style5.imageOver = hover;
        style5.imageChecked = selected;
        style5.imageDown = clicked;
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
                basicPic.setChecked(true);
                break;
        }
                
        
        basicPic.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    prefs.putString("background", "bg_blur");
                    prefs.putString("fontcolor", "labelb");
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
                        prefs.putString("background", "woods_blur");
                        prefs.putString("fontcolor",  "labelb");
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
                        prefs.putString("background", "metal_blur");
                        prefs.putString("fontcolor", "labelw");
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
                        prefs.putString("background", "cave_blur");
                        prefs.putString("fontcolor", "labelw");
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
                        prefs.putString("background", "ocean_blur");
                        prefs.putString("fontcolor",  "labelw");
                        prefs.putInteger("theme", 6);
                        prefs.flush();
                        MainMenu menu = new MainMenu(tss);
                        tss.setScreen(menu);
                    }
            });
        
        }
        
        TextButton statselect = new TextButton("Stats", skin.get("blue", TextButton.TextButtonStyle.class));
        statselect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                //Same way we moved here from the Splash Screen
                //We set it to new Splash because we got no other screens
                //otherwise you put the screen there where you want to go
                StatsScreen menu = new StatsScreen(tss);
                tss.setScreen(menu);
            }
        });

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
        cli.dispose();
        hov.dispose();
        sel.dispose();
        basic.dispose();
        basicLocked.dispose();
        bubble.dispose();
        bubbleLocked.dispose();
        folk.dispose();
        folkLocked.dispose();
        jazz.dispose();
        jazzLocked.dispose();
        metal.dispose();
        metalLocked.dispose();
        stage.dispose();
        skin.dispose();
        table.clear();
    }
    
}