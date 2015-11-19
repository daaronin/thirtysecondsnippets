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
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author George and Dan
 */
public class LoadTrackData  implements Screen{
    private final Game tss;
    private int genreId;
    private Table table = new Table();
    private Stage stage = new Stage(new StretchViewport(TSS.WIDTH, TSS.HEIGHT));
    
    int difficulty = 0;

    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("LoadingScreen.txt"));

    //private Skin atlas = new Skin(Gdx.files.internal("atlas.json"), atlas);
    
    OrthographicCamera camera;
    
    Matrix4 debugMatrix;
    
    final float PIXELS_TO_METERS = 100f;
    
    SpriteBatch batch;
    Sprite background, threadedBackground, colorBackground, shadowBackground,
            cat_sprite, yarn_sprite_o, yarn_sprite_p, yarn_sprite_g, yarn_sprite_y;

    ArrayList<Sprite> sprites;
    World world;
    
    float width, height;
    int screen_top_height = 5;
    float bgx, bgcolorx;
    long lastTimeBg;

    double timer = 0;
    double time = 3;

    String flavorText;
    
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
        
        background = atlas.createSprite("tallback");
        threadedBackground = atlas.createSprite("threadstall");
        colorBackground = atlas.createSprite("rainbow");
        shadowBackground = atlas.createSprite("shadowmap3");
        
        cat_sprite = atlas.createSprite("super_cat");
        
        yarn_sprite_p = atlas.createSprite("yarn_purple");
        sprites.add(yarn_sprite_p);
        
        yarn_sprite_o = atlas.createSprite("yarn_orange");
        sprites.add(yarn_sprite_o);
        
        yarn_sprite_y = atlas.createSprite("yarn_yellow");
        sprites.add(yarn_sprite_y);
        
        yarn_sprite_g = atlas.createSprite("yarn_green");
        sprites.add(yarn_sprite_g);
        
        Sprite yarn_sprite_3 = atlas.createSprite("yarn_green");
        sprites.add(yarn_sprite_3);
        
        Sprite yarn_sprite_2 = atlas.createSprite("yarn_yellow");
        sprites.add(yarn_sprite_2);
        
        Sprite yarn_sprite_1 = atlas.createSprite("yarn_purple");
        sprites.add(yarn_sprite_1);
        
        Sprite yarn_sprite_0 = atlas.createSprite("yarn_orange");
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


//        Label musicvol = new Label("Loading... ", atlas.get("labelb", Label.LabelStyle.class));
//        table.add(musicvol).center().expand();
//        
//        table.setBackground(atlas.getDrawable("bg_blur"));
//        table.setFillParent(true);
//        //table.debug();
//        
//        stage.addActor(table);
//        
//        Gdx.input.setInputProcessor(stage);

        Random rand = new Random();
        int numerator = rand.nextInt(5);
        switch (numerator){
            case 0:
                flavorText = "Collecting the yarn...";
                break;
            case 1:
                flavorText = "Weaving a tangled skein...";
                break;
            case 2:
                flavorText = "Developing sewing machine learning...";
                break;
            case 3:
                flavorText = "Collecting the yarn...";
                break;
            case 4:
                flavorText = "Collecting the yarn...";
                break;
            case 5:
                flavorText = "Collecting the yarn...";
                break;
            case 6:
                flavorText = "Collecting the yarn...";
                break;
            case 7:
                flavorText = "Collecting the yarn...";
                break;
            case 8:
                flavorText = "Collecting the yarn...";
                break;
            case 9:
                flavorText = "Collecting the yarn...";
                break;
            default:
                flavorText = "Collecting the yarn...";
                break;
        }

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

        timer+= delta;
        if (timer > time){
            ThirtySecondSnippets game = new ThirtySecondSnippets(tss, genreId, difficulty);
            tss.setScreen(game);
        }

        
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
        
        font.draw(batch, flavorText, 10,75);
        
        batch.end();
        
        
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
        batch.dispose();
        font.dispose();
        table.clear();
        atlas.dispose();
        world.dispose();
        stage.dispose();
        sprites.clear();
        font.dispose();
    }
}
