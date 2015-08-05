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
    
    int difficulty = 0;
    
    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("buttons.pack"));

    private Skin skin = new Skin(Gdx.files.internal("skin.json"), atlas);
    
    OrthographicCamera camera;
    
    Matrix4 debugMatrix;
    
    final float PIXELS_TO_METERS = 100f;
    
    SpriteBatch batch;
    Texture yarn_p, yarn_y, yarn_g, yarn_o, cat, background, threadedBackground, 
            colorBackground, shadowBackground;
    
    Sprite cat_sprite;
    Sprite yarn_sprite_o, yarn_sprite_p, yarn_sprite_g, yarn_sprite_y;
    ArrayList<Sprite> sprites;
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
    
    public LoadTrackData(Game tss, int genreId, int difficulty){
        this.tss = tss;
        this.genreId = genreId;
        this.difficulty = difficulty;
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
        
        sprites = new ArrayList<Sprite>();
        
        //BACKGROUND_SPEED = tempo/60f*3f;
        SCROLLING_FOREGROUND_SPEED = 13/60f*-3f;
        
        batch = new SpriteBatch();
        
        background = new Texture("tallback.png");
        threadedBackground = new Texture("threadstall.png");
        colorBackground = new Texture("rainbow.png");
        shadowBackground = new Texture("shadowmap3.png");
        
        cat = new Texture("super_cat.png");
        cat_sprite = new Sprite(cat);
        
        yarn_p = new Texture("yarn_purple.png");
        yarn_sprite_p = new Sprite(yarn_p);
        sprites.add(yarn_sprite_p);
        
        yarn_o = new Texture("yarn_orange.png");
        yarn_sprite_o = new Sprite(yarn_o);
        sprites.add(yarn_sprite_o);
        
        yarn_y = new Texture("yarn_yellow.png");
        yarn_sprite_y = new Sprite(yarn_y);
        sprites.add(yarn_sprite_y);
        
        yarn_g = new Texture("yarn_green.png");
        yarn_sprite_g = new Sprite(yarn_g);
        sprites.add(yarn_sprite_g);
        
        Sprite yarn_sprite_3 = new Sprite(yarn_g);
        sprites.add(yarn_sprite_3);
        
        Sprite yarn_sprite_2 = new Sprite(yarn_y);
        sprites.add(yarn_sprite_2);
        
        Sprite yarn_sprite_1 = new Sprite(yarn_p);
        sprites.add(yarn_sprite_1);
        
        Sprite yarn_sprite_0 = new Sprite(yarn_o);
        sprites.add(yarn_sprite_0);
        
        

        cat_sprite.setPosition(width/2-cat_sprite.getWidth()/2,height/2-cat_sprite.getHeight()/2);
        
        sprites.get(0).setPosition(width/4-sprites.get(0).getWidth()/2,height/2-sprites.get(0).getHeight()/2);
        sprites.get(1).setPosition(width/1.5f-sprites.get(1).getWidth()/2,height/4-sprites.get(1).getHeight()/2);
        sprites.get(2).setPosition(width/4 + width/2 -sprites.get(2).getWidth()/2,height/2-sprites.get(2).getHeight()/2);
        sprites.get(3).setPosition(width/3-sprites.get(3).getWidth()/2,height/4 -sprites.get(3).getHeight()/2);
        
        sprites.get(4).setPosition(0-sprites.get(4).getWidth()/2,height/4-sprites.get(4).getHeight()/2);
        sprites.get(5).setPosition(width/4-sprites.get(5).getWidth()/2,0-sprites.get(5).getHeight()/2);
        sprites.get(6).setPosition(0 + width/2 -sprites.get(6).getWidth()/2,0-sprites.get(6).getHeight()/2);
        sprites.get(7).setPosition(width/4-sprites.get(7).getWidth()/2,0 + height/2 -sprites.get(7).getHeight()/2);
//        yarn_sprite_o.setPosition(width/5-cat_sprite.getWidth()/2+1.5f*cat_sprite.getWidth(),height/2-cat_sprite.getHeight()/2);
//        yarn_sprite_y.setPosition(width/4 + width/2 -cat_sprite.getWidth()/2+1.5f*cat_sprite.getWidth(),height/2-cat_sprite.getHeight()/2);
//        yarn_sprite_g.setPosition(width/5 + width/2 -cat_sprite.getWidth()/2+1.5f*cat_sprite.getWidth(),height/2-cat_sprite.getHeight()/2);
        
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
                    
                    //Fast
//                    t.setArtist("Miss Kittin");
//                    t.setName("Flashforward");
//                    t.setPreview_url("https://p.scdn.co/mp3-preview/8cef07b9eecad74b412470b1233fbd732bd0d3f7");
//                    t.setTempo(220);
                    
                    //Slow       
//                    t.setPreview_url("https://p.scdn.co/mp3-preview/fcc74d3ce6a2d4f5017a776a30dc3cb3715e85c2");
//                    t.setArtist("Skylar Grey");
//                    t.setName("Coming Home - Part II / Bonus Track");
//                    t.setTempo(110);
                    
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
                            ThirtySecondSnippets game = new ThirtySecondSnippets(tss, t, analysis, m, difficulty);
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
        batch.draw(background, bgx + 144*17, 0);
        batch.draw(background, bgx + 144*16, 0);
        batch.draw(background, bgx + 144*15, 0);
        batch.draw(background, bgx + 144*14, 0);
        batch.draw(background, bgx + 144*13, 0);
        batch.draw(background, bgx + 144*12, 0);
        batch.draw(background, bgx + 144*11, 0);
        batch.draw(background, bgx + 144*10, 0);
        batch.draw(background, bgx + 144*9, 0);
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

        batch.draw(background, bgx + 144*17, background.getHeight());
        batch.draw(background, bgx + 144*16, background.getHeight());
        batch.draw(background, bgx + 144*15, background.getHeight());
        batch.draw(background, bgx + 144*14, background.getHeight());
        batch.draw(background, bgx + 144*13, background.getHeight());
        batch.draw(background, bgx + 144*12, background.getHeight());
        batch.draw(background, bgx + 144*11, background.getHeight());
        batch.draw(background, bgx + 144*10, background.getHeight());
        batch.draw(background, bgx + 144*9, background.getHeight());
        batch.draw(background, bgx + 144*8, background.getHeight());
        batch.draw(background, bgx + 144*7, background.getHeight());
        batch.draw(background, bgx + 144*6, background.getHeight());
        batch.draw(background, bgx + 144*5, background.getHeight());            
        batch.draw(background, bgx + 144*4, background.getHeight());
        batch.draw(background, bgx + 144*3, background.getHeight());
        batch.draw(background, bgx + 144*2, background.getHeight());
        batch.draw(background, bgx + 144, background.getHeight());
        batch.draw(background, bgx, background.getHeight());
        batch.draw(background, bgx - 144, background.getHeight());

        batch.draw(colorBackground, bgcolorx, 0);
        batch.draw(colorBackground, bgcolorx, colorBackground.getHeight());

        batch.draw(colorBackground, bgcolorx - 2016, 0);
        batch.draw(colorBackground, bgcolorx - 2016, colorBackground.getHeight());

        batch.draw(threadedBackground, bgx + 144*17, 0);
        batch.draw(threadedBackground, bgx + 144*16, 0);
        batch.draw(threadedBackground, bgx + 144*15, 0);
        batch.draw(threadedBackground, bgx + 144*14, 0);
        batch.draw(threadedBackground, bgx + 144*13, 0);
        batch.draw(threadedBackground, bgx + 144*12, 0);
        batch.draw(threadedBackground, bgx + 144*11, 0);
        batch.draw(threadedBackground, bgx + 144*10, 0);
        batch.draw(threadedBackground, bgx + 144*9, 0);
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

        batch.draw(threadedBackground, bgx + 144*17, threadedBackground.getHeight());
        batch.draw(threadedBackground, bgx + 144*16, threadedBackground.getHeight());
        batch.draw(threadedBackground, bgx + 144*15, threadedBackground.getHeight());
        batch.draw(threadedBackground, bgx + 144*14, threadedBackground.getHeight());
        batch.draw(threadedBackground, bgx + 144*13, threadedBackground.getHeight());
        batch.draw(threadedBackground, bgx + 144*12, threadedBackground.getHeight());
        batch.draw(threadedBackground, bgx + 144*11, threadedBackground.getHeight());
        batch.draw(threadedBackground, bgx + 144*10, threadedBackground.getHeight());
        batch.draw(threadedBackground, bgx + 144*9, threadedBackground.getHeight());
        batch.draw(threadedBackground, bgx + 144*8, threadedBackground.getHeight());
        batch.draw(threadedBackground, bgx + 144*7, threadedBackground.getHeight());
        batch.draw(threadedBackground, bgx + 144*6, threadedBackground.getHeight());
        batch.draw(threadedBackground, bgx + 144*5, threadedBackground.getHeight());            
        batch.draw(threadedBackground, bgx + 144*4, threadedBackground.getHeight());
        batch.draw(threadedBackground, bgx + 144*3, threadedBackground.getHeight());
        batch.draw(threadedBackground, bgx + 144*2, threadedBackground.getHeight());
        batch.draw(threadedBackground, bgx + 144, threadedBackground.getHeight());
        batch.draw(threadedBackground, bgx, threadedBackground.getHeight());
        batch.draw(threadedBackground, bgx - 144, threadedBackground.getHeight());

        batch.draw(shadowBackground, bgcolorx, 0);
        batch.draw(shadowBackground, bgcolorx - 2016, 0);

        batch.draw(shadowBackground, bgcolorx, shadowBackground.getHeight());
        batch.draw(shadowBackground, bgcolorx - 2016, shadowBackground.getHeight());

        batch.draw(cat_sprite, cat_sprite.getX(), cat_sprite.getY(),cat_sprite.getOriginX(),
                   cat_sprite.getOriginY(), cat_sprite.getWidth(),cat_sprite.getHeight(),
                   cat_sprite.getScaleX(),cat_sprite.getScaleY(),cat_sprite.getRotation());
        
               
        cat_sprite.setPosition(cat_sprite.getX(), cat_sprite.getY());
        
        if(up){
            cat_sprite.setPosition(cat_sprite.getX(), cat_sprite.getY()+1);
            //yarn_sprite.setPosition(yarn_sprite.getX(), yarn_sprite.getY()+2);
        }else{
            cat_sprite.setPosition(cat_sprite.getX(), cat_sprite.getY()-1);
            //yarn_sprite.setPosition(yarn_sprite.getX(), yarn_sprite.getY()-2);
        }
        
        if(cat_sprite.getY() >= .49*height){
            up = false;
        }else if(cat_sprite.getY() <= .45*height){
            up = true;
        }
        
        for (Sprite sprite: sprites){
            float x = sprite.getX() + sprite.getWidth()/2;
            float y = sprite.getY() + sprite.getHeight()/2;
            
            if (x == width/2 && y == height/2){
                sprite.setPosition(-1500, -1500);
            }else if (x > width/2 && y > height/2){
                sprite.setPosition(sprite.getX()-1, sprite.getY()-1);
            } else if (x > width/2 && y <= height/2){
                sprite.setPosition(sprite.getX()-1, sprite.getY()+1);                
            } else if (x <= width/2 && y > height/2){
                sprite.setPosition(sprite.getX()+1, sprite.getY()-1);                
            } else if (x <= width/2 && y <= height/2){
                sprite.setPosition(sprite.getX()+1, sprite.getY()+1);                
            }
            
            batch.draw(sprite, sprite.getX(), sprite.getY(),sprite.getOriginX(),
                   sprite.getOriginY(), sprite.getWidth(),sprite.getHeight(),
                   sprite.getScaleX(),sprite.getScaleY(),sprite.getRotation());
        }
//        batch.draw(yarn_sprite_o, yarn_sprite_o.getX(), yarn_sprite_o.getY(),yarn_sprite_o.getOriginX(),
//                   yarn_sprite_o.getOriginY(), yarn_sprite_o.getWidth(),yarn_sprite_o.getHeight(),
//                   yarn_sprite_o.getScaleX(),yarn_sprite_o.getScaleY(),yarn_sprite_o.getRotation());
//        
//        batch.draw(yarn_sprite_y, yarn_sprite_y.getX(), yarn_sprite_y.getY(),yarn_sprite_y.getOriginX(),
//                   yarn_sprite_y.getOriginY(), yarn_sprite_y.getWidth(),yarn_sprite_y.getHeight(),
//                   yarn_sprite_y.getScaleX(),yarn_sprite_y.getScaleY(),yarn_sprite_y.getRotation());
//        
//        batch.draw(yarn_sprite_g, yarn_sprite_g.getX(), yarn_sprite_g.getY(),yarn_sprite_g.getOriginX(),
//                   yarn_sprite_g.getOriginY(), yarn_sprite_g.getWidth(),yarn_sprite_g.getHeight(),
//                   yarn_sprite_g.getScaleX(),yarn_sprite_g.getScaleY(),yarn_sprite_g.getRotation());
//        
//        batch.draw(yarn_sprite_p, yarn_sprite_p.getX(), yarn_sprite_p.getY(),yarn_sprite_p.getOriginX(),
//                   yarn_sprite_p.getOriginY(), yarn_sprite_p.getWidth(),yarn_sprite_p.getHeight(),
//                   yarn_sprite_p.getScaleX(),yarn_sprite_p.getScaleY(),yarn_sprite_p.getRotation());
        
        font.draw(batch, "Collecting the yarn...", 10,75);
        
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
        dispose();
    }

    @Override
    public void dispose() {
        atlas.dispose();
        skin.dispose();
        stage.dispose();        
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
