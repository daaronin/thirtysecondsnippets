package com.gdp.thirtysecondsnippets;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThirtySecondSnippets extends ApplicationAdapter implements InputProcessor {

    SpriteBatch batch;
    Texture threadlet, background, scissors;
    Sprite player_sprite, scissors_sprite;
    World world;
    Body body;
    Body bodyEdgeScreen;
    float posX, posY;
    float scissorsX, scissorsY;
    float width, height;
    float bgx;
    long lastTimeBg;
    Box2DDebugRenderer debugRenderer;
    OrthographicCamera camera;
    Matrix4 debugMatrix;
    
    Vector2 mouseLoc;
    
    float torque = 0.0f;
    boolean drawSprite = true;
    
    final float PIXELS_TO_METERS = 100f;

    @Override
    public void create() {
        Music m = null;
        try {
            MusicDB db = new MusicDB();
            Track track = db.getTrackByGenre("pop");
            System.out.println(track.getArtist() + " | " + track.getName());
            
            String filename = "music.mp3";
            InputStream is = new URL(track.getPreview_url()).openStream();
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

            m = Gdx.audio.newMusic(handle);
            System.out.println(bytes.size());
        } catch (MalformedURLException ex) {
            Logger.getLogger(ThirtySecondSnippets.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ThirtySecondSnippets.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        bgx = 800;
        
        batch = new SpriteBatch();
        threadlet = new Texture("smallcat.png");
        background = new Texture("backgroundstars.jpg");
        scissors = new Texture("scissors.png");
        player_sprite = new Sprite(threadlet);
        scissors_sprite = new Sprite(scissors);

        player_sprite.setPosition(width/4-player_sprite.getWidth()/2,height/2-player_sprite.getHeight()/2);

        
        scissorsX = width * .65f - player_sprite.getWidth() / 2;
        scissorsY = height * .75f - player_sprite.getHeight() / 2;
        //player_sprite.setPosition(posX, posY);
        scissors_sprite.setPosition(scissorsX, scissorsY);
        
        world = new World(new Vector2(0, 0), true);
        
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set((player_sprite.getX() + player_sprite.getWidth()/2) / 
                             PIXELS_TO_METERS, 
                (player_sprite.getY() + player_sprite.getHeight()/2) / PIXELS_TO_METERS);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();

        shape.setAsBox(player_sprite.getWidth()/2 / PIXELS_TO_METERS, player_sprite.getHeight()
                       /2 / PIXELS_TO_METERS);
        
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = .1f;

        body.createFixture(fixtureDef);

        shape.dispose();
        
        BodyDef bodyDef2 = new BodyDef();
        bodyDef2.type = BodyDef.BodyType.StaticBody;
        float w = Gdx.graphics.getWidth()/PIXELS_TO_METERS;
        // Set the height to just 50 pixels above the bottom of the screen so we can see the edge in the
        // debug renderer
        float h = Gdx.graphics.getHeight()/PIXELS_TO_METERS - 150/PIXELS_TO_METERS;
        bodyDef2.position.set(w,
                h+h);
        //bodyDef2.position.set(0,0);
        FixtureDef fixtureDef2 = new FixtureDef();

        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(-w,-h/2,w,-h/2);
        fixtureDef2.shape = edgeShape;

        bodyEdgeScreen = world.createBody(bodyDef2);
        bodyEdgeScreen.createFixture(fixtureDef2);
        edgeShape.dispose();
        
        BodyDef bodyDef3 = new BodyDef();
        bodyDef2.type = BodyDef.BodyType.StaticBody;
        w = Gdx.graphics.getWidth()/PIXELS_TO_METERS;
        // Set the height to just 50 pixels above the bottom of the screen so we can see the edge in the
        // debug renderer
        h = Gdx.graphics.getHeight()/PIXELS_TO_METERS - 500/PIXELS_TO_METERS;
        bodyDef3.position.set(w,
                h+h);
        //bodyDef2.position.set(0,0);
        FixtureDef fixtureDef3 = new FixtureDef();

        EdgeShape edgeShape2 = new EdgeShape();
        edgeShape2.set(-w,-h/2,w,-h/2);
        fixtureDef3.shape = edgeShape2;

        bodyEdgeScreen = world.createBody(bodyDef3);
        bodyEdgeScreen.createFixture(fixtureDef3);
        edgeShape.dispose();

        debugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(Gdx.graphics.getWidth()/PIXELS_TO_METERS,Gdx.graphics.
                 getHeight()/PIXELS_TO_METERS);
        
        camera.viewportWidth = width/PIXELS_TO_METERS;
        camera.viewportHeight = height/PIXELS_TO_METERS;
        camera.position.set(width/PIXELS_TO_METERS/2f, height/PIXELS_TO_METERS/2f, 0);
        
        lastTimeBg = TimeUtils.nanoTime();

        Gdx.input.setInputProcessor(this);
        
        if(m != null){
            m.play();
        }

    }

    @Override
    public void render() {
        camera.update();
        world.step(1f/60f, 6, 2);
        
        body.applyTorque(torque,true);
        
        float ydir = posY - (body.getPosition().y* PIXELS_TO_METERS);
        float xdir = posX -(body.getPosition().x* PIXELS_TO_METERS);
        //System.out.println(ydir + " : " + xdir);
        //body.applyForceToCenter(new Vector2(0,ydir/10), true);
        //body.applyForceToCenter(new Vector2(xdir/10,0), true);
        
        if (ydir <= 5 && ydir >= -5)
            body.setLinearVelocity(0f, 0f);
        
        player_sprite.setPosition((body.getPosition().x * PIXELS_TO_METERS) - player_sprite.
                           getWidth()/2 , 
                (body.getPosition().y * PIXELS_TO_METERS) -player_sprite.getHeight()/2 )
                 ;
        
        player_sprite.setRotation((float)Math.toDegrees(body.getAngle()));
        
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if (TimeUtils.nanoTime() - lastTimeBg > 100000000) {
            bgx -= 50;
            scissors_sprite.translateX(-20f);
            lastTimeBg = TimeUtils.nanoTime();
        }

        if (bgx == 0) {
            bgx = 800;
        }
        if (scissors_sprite.getX() <= -220) {
            scissors_sprite.setX(650);
        }
        
        batch.setProjectionMatrix(camera.combined);
        debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS, 
                      PIXELS_TO_METERS, 0);
        
        batch.begin();
        if(drawSprite){
            batch.draw(background, bgx - 800, 0);
            batch.draw(background, bgx, 0);

            scissors_sprite.draw(batch);
        
            batch.draw(player_sprite, player_sprite.getX(), player_sprite.getY(),player_sprite.getOriginX(),
                       player_sprite.getOriginY(),
                player_sprite.getWidth(),player_sprite.getHeight(),player_sprite.getScaleX(),player_sprite.
                                getScaleY(),player_sprite.getRotation());
        }

        batch.end();
        debugRenderer.render(world, debugMatrix);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Buttons.LEFT) {
            posX = screenX - player_sprite.getWidth();
            posY = Gdx.graphics.getHeight() - screenY - player_sprite.getHeight() / 2;
            mouseLoc = new Vector2(posX, posY);
            float ud = (body.getPosition().y* PIXELS_TO_METERS);
            float lr = (body.getPosition().x* PIXELS_TO_METERS);
            System.out.println("Y - mouse : sprite " + posY +" : "+ ud);
            System.out.println("X - mouse : sprite " + posX +" : "+ lr);

            if (posY > (body.getPosition().y* PIXELS_TO_METERS)){
                body.applyForceToCenter(new Vector2(0f,2f), true);                
            } else {
                body.applyForceToCenter(new Vector2(0f,-2f), true);
            }
            /*if(checkPlayerX(screenX)){
                if (posX> (body.getPosition().x* PIXELS_TO_METERS)){
                  body.applyForceToCenter(new Vector2(2f, 0), true);                
                } else {
                  body.applyForceToCenter(new Vector2(-2f,0), true);
                }
            }*/
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.position.set(width/2f, height/2f, 0);
    }

    @Override
    public void pause() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resume() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dispose() {
        
        threadlet.dispose();
        background.dispose();
        scissors.dispose();
        world.dispose();

        System.out.println("Good day kind sir.");
    }

    @Override
    public boolean keyDown(int keycode) {
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            torque += 0.01f;
        }
        if (keycode == Keys.RIGHT) {
            torque -= 0.01f;
        }
        if (keycode == Keys.UP) {
            torque = 0f;
        }
        if(keycode == Input.Keys.ESCAPE)
            drawSprite = !drawSprite;
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (pointer == Buttons.LEFT) {
            posX = screenX - player_sprite.getWidth();
            posY = Gdx.graphics.getHeight() - screenY - player_sprite.getHeight() / 2;
            float ud = (body.getPosition().y* PIXELS_TO_METERS);
            float lr = (body.getPosition().x* PIXELS_TO_METERS);
            System.out.println("Y - mouse : sprite " + posY +" : "+ ud);
            System.out.println("X - mouse : sprite " + posX +" : "+ lr);
            if (posY > (body.getPosition().y* PIXELS_TO_METERS)){
                body.applyForceToCenter(new Vector2(0f,2f), true);                
            } else {
                body.applyForceToCenter(new Vector2(0f,-2f), true);
            }
            /*\if(checkPlayerX(screenX)){
                if (posX  - 200> (body.getPosition().x* PIXELS_TO_METERS)){
                  body.applyForceToCenter(new Vector2(2f, 0), true);                
                } else {
                    body.applyForceToCenter(new Vector2(-2f,0), true);
                }
            }*/
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean checkPlayerX(int screenX) {
        float mouseX = screenX - player_sprite.getWidth();
        if (mouseX <= Gdx.graphics.getWidth() / 6) {
            return true;
        }
        return false;
    }
}
