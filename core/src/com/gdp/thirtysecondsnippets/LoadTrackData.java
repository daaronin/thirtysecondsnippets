/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gdp.thirtysecondsnippets;

import analysis.SnippetAnalysis;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.TimeUtils;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author George
 */
public class LoadTrackData  implements Screen{
    private final Game tss;
    private int genreId;
    private Table table = new Table();
    private Stage stage = new Stage();
    
    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("buttons.pack"));

    private Skin skin = new Skin(Gdx.files.internal("skin.json"), atlas);
    
    OrthographicCamera camera;
    
    Matrix4 debugMatrix;
    
    final float PIXELS_TO_METERS = 100f;
    
    SpriteBatch batch;
    Texture yarn, cat, background, threadedBackground, 
            colorBackground, shadowBackground;
    
    Sprite cat_sprite;
    Sprite yarn_sprite;
    World world;
    
    float width, height;
    int screen_top_height = 5;
    float bgx, bgcolorx;
    long lastTimeBg;
    
    float BACKGROUND_SPEED = 28.8f;    
    
    float SCROLLING_FOREGROUND_SPEED = 130/60f*-3f;
    
    boolean up = true;
    
    BitmapFont font;
    
    public LoadTrackData(Game tss){
        this.tss = tss;
    }
    
    public LoadTrackData(Game tss, int genreId){
        this.tss = tss;
        this.genreId = genreId;
    }

    @Override
    public void show() {
        BACKGROUND_SPEED = 4.8f;
        world = new World(new Vector2(-10f, 0), true);
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        
        camera = new OrthographicCamera(width,height);
        
        camera = new OrthographicCamera(width,height);
        
        camera.position.set(width/2f, height/2f, 0);
       
        bgx = 144;
        bgcolorx = 2016;
        
        //BACKGROUND_SPEED = tempo/60f*3f;
        SCROLLING_FOREGROUND_SPEED = 13/60f*-3f;
        
        batch = new SpriteBatch();
        
        background = new Texture("tallback.png");
        threadedBackground = new Texture("threadstall.png");
        colorBackground = new Texture("rainbow.png");
        shadowBackground = new Texture("shadowmap3.png");
        
        cat = new Texture("cat.png");
        cat_sprite = new Sprite(cat);
        
        yarn = new Texture("yarn_ball.png");
        yarn_sprite = new Sprite(yarn);

        cat_sprite.setPosition(width/3-cat_sprite.getWidth()/2,height/2-cat_sprite.getHeight()/2);
        yarn_sprite.setPosition(width/3-cat_sprite.getWidth()/2+1.5f*cat_sprite.getWidth(),height/2-cat_sprite.getHeight()/2);
        
        font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"),Gdx.files.internal("fonts/font.png"),false);
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream is = null;
                try {
                    // do something important here, asynchronously to the rendering threa
                    loadGenreList();
                    MusicDB db = new MusicDB();
                    final Track t = db.getTrackByGenreID(genreId);
                    t.setGenreId(genreId);
                    String filename = "music.mp3";
                    is = new URL(t.getPreview_url()).openStream();
                    
                    BufferedInputStream stream = new BufferedInputStream(is);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    FileHandle handle = Gdx.files.external(filename);
                    if (handle.exists()) {
                        handle.delete();
                    }
                    int current = 0;
                    while ((current = stream.read()) != -1) {
                        bytes.write(current);
                    }
                    FileOutputStream fos = new FileOutputStream(handle.file());
                    bytes.writeTo(fos);

                    fos.flush();
                    fos.close();
                    
                     final Music m = Gdx.audio.newMusic(handle);
                    
                    final SnippetAnalysis analysis = new SnippetAnalysis(handle, 1024);
                    analysis.doAnalysis();
                    
                    // post a Runnable to the rendering thread that processes the result
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            ThirtySecondSnippets game = new ThirtySecondSnippets(tss, t, analysis, m);
                            tss.setScreen(game);
                        }
                    });
                } catch (MalformedURLException ex) {
                    Logger.getLogger(LoadTrackData.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(LoadTrackData.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        is.close();
                    } catch (IOException ex) {
                        Logger.getLogger(LoadTrackData.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
             }
      }).start();
        
//        Label musicvol = new Label("Loading... ", skin.get("labelb", Label.LabelStyle.class));
//        table.add(musicvol).center().expand();
//        
//        table.setBackground(skin.getDrawable("bg_blur"));
//        table.setFillParent(true);
//        //table.debug();
//        
//        stage.addActor(table);
//        
//        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        camera.update();
        world.step(1f/60f, 6, 2);
        
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if (TimeUtils.nanoTime() - lastTimeBg > 100000000) {
            bgx -= BACKGROUND_SPEED;
            bgcolorx -= BACKGROUND_SPEED;
        }
        
        if (bgx <= 0) {
            bgx = 144;
        }
        
        if (bgcolorx <= 0){
            bgcolorx = 2016;
        }
        
        batch.setProjectionMatrix(camera.combined);
        debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS, 
                      PIXELS_TO_METERS, 0);
        
        
         batch.begin();
        batch.draw(background, bgx + 144*8, 0);
        batch.draw(background, bgx + 144*7, 0);
        batch.draw(background, bgx + 144*6, 0);
        batch.draw(background, bgx + 144*5, 0);            
        batch.draw(background, bgx + 144*4, 0);
        batch.draw(background, bgx + 144*3, 0);
        batch.draw(background, bgx + 144*2, 0);
        batch.draw(background, bgx + 144, 0);
        batch.draw(background, bgx, 0);
        batch.draw(background, bgx - 144, 0);

        batch.draw(colorBackground, bgcolorx, 0);
        batch.draw(colorBackground, bgcolorx - 2016, 0);

        batch.draw(threadedBackground, bgx + 144*8, 0);
        batch.draw(threadedBackground, bgx + 144*7, 0);
        batch.draw(threadedBackground, bgx + 144*6, 0);
        batch.draw(threadedBackground, bgx + 144*5, 0);            
        batch.draw(threadedBackground, bgx + 144*4, 0);
        batch.draw(threadedBackground, bgx + 144*3, 0);
        batch.draw(threadedBackground, bgx + 144*2, 0);
        batch.draw(threadedBackground, bgx + 144, 0);
        batch.draw(threadedBackground, bgx, 0);
        batch.draw(threadedBackground, bgx - 144, 0);

        batch.draw(shadowBackground, bgcolorx, 0);
        batch.draw(shadowBackground, bgcolorx - 2016, 0);

        batch.draw(cat_sprite, cat_sprite.getX(), cat_sprite.getY(),cat_sprite.getOriginX(),
                   cat_sprite.getOriginY(), cat_sprite.getWidth(),cat_sprite.getHeight(),
                   cat_sprite.getScaleX(),cat_sprite.getScaleY(),cat_sprite.getRotation());
        
               
        cat_sprite.setPosition(cat_sprite.getX(), cat_sprite.getY());
        
        if(up){
            cat_sprite.setPosition(cat_sprite.getX(), cat_sprite.getY()+2);
            yarn_sprite.setPosition(yarn_sprite.getX(), yarn_sprite.getY()+2);
        }else{
            cat_sprite.setPosition(cat_sprite.getX(), cat_sprite.getY()-2);
            yarn_sprite.setPosition(yarn_sprite.getX(), yarn_sprite.getY()-2);
        }
        
        if(cat_sprite.getY() >= .49*height){
            up = false;
        }else if(cat_sprite.getY() <= .07*height){
            up = true;
        }
        
        batch.draw(yarn_sprite, yarn_sprite.getX(), yarn_sprite.getY(),yarn_sprite.getOriginX(),
                   yarn_sprite.getOriginY(), yarn_sprite.getWidth(),yarn_sprite.getHeight(),
                   yarn_sprite.getScaleX(),yarn_sprite.getScaleY(),yarn_sprite.getRotation());
        
        font.draw(batch, "Collecting the yarn...", 350, 670);
        
        batch.end();
        
        
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
    
    public void loadGenreList(){
        MusicDB db = new MusicDB();
        ArrayList<Genre> genres = db.getGenres();
        
        Json json = new Json();
        json.addClassTag("genre", Genre.class); // This may not be needed. I don't know how json deals with String
        FileHandle handle = Gdx.files.external("genre_list");
        if (handle.exists()) {
            handle.delete();
        }
        json.toJson(genres, Gdx.files.external("genre_list"));
    }
    
}
