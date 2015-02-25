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
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
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
    Texture threadlet, background, scissors, redlet, shortthreadlet;
    Sprite player_sprite, scissors_sprite;
    World world;
    Body body, scissors_body;
    Body bodyEdgeScreen;
    Body[] threadBodies;
    Sprite[] threadSprites;
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
    
    boolean dansTryingToGetWorkDone = true;
    
    @Override
    public void create() {
        //Gets track from Spotify
        Music m = null;
        if (!dansTryingToGetWorkDone){
            try {
                MusicDB db = new MusicDB();
                Track track = db.getTrackByGenre("rock");
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
        }
        
        //gets height and width
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        
        camera = new OrthographicCamera(Gdx.graphics.getWidth()/PIXELS_TO_METERS,Gdx.graphics.
         getHeight()/PIXELS_TO_METERS);

        camera.viewportWidth = width/PIXELS_TO_METERS;
        camera.viewportHeight = height/PIXELS_TO_METERS;
        camera.position.set(width/PIXELS_TO_METERS/2f, height/PIXELS_TO_METERS/2f, 0);
       
        bgx = 800;
        
        batch = new SpriteBatch();
        shortthreadlet = new Texture("shortthread.png");
        threadlet = new Texture("thread.png");
        redlet = new Texture("redthread.png");
        background = new Texture("backgroundstars.jpg");
        scissors = new Texture("scissors.png");
        player_sprite = new Sprite(shortthreadlet);
        scissors_sprite = new Sprite(scissors);

        player_sprite.setPosition(width/3-player_sprite.getWidth()/2,height/2-player_sprite.getHeight()/2);

        
        scissorsX = width * .65f - scissors_sprite.getWidth() / 2;
        scissorsY = height - scissors_sprite.getHeight() / 2;
        //player_sprite.setPosition(posX, posY);
        scissors_sprite.setPosition(scissorsX, scissorsY);
        
        world = new World(new Vector2(-10f, 0), true);
        /*----------------------------------------------------------------*/
        //MouseJointDef mouseJointDef = new MouseJointDef();
        //mouseJointDef.bodyA = 
        //mouseJointDef.maxForce = 50000;
        //mouseJointDef.collideConnected = true;
        
        //MouseJointAdapter mouseJointAdapter = new MouseJointAdapter(mouseJointDef, true, camera);
        
        /*----------------------------------------------------------------*/
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;

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
        /*----------------------------------------------------------------*/
        /*BodyDef bodyDef2 = new BodyDef();
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
        edgeShape.dispose(); */
        /*----------------------------------------------------------------*/        
        /*BodyDef bodyDef3 = new BodyDef();
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
        edgeShape.dispose();*/
        /*----------------------------------------------------------------*/
        BodyDef scissors_bodyDef = new BodyDef();
        scissors_bodyDef.type = BodyDef.BodyType.KinematicBody;

        scissors_bodyDef.position.set((scissors_sprite.getX() + scissors_sprite.getWidth()/2) / 
                             PIXELS_TO_METERS, 
                (scissors_sprite.getY() + scissors_sprite.getHeight()/2) / PIXELS_TO_METERS);

        scissors_body = world.createBody(scissors_bodyDef);

        PolygonShape scissors_shape = new PolygonShape();

        scissors_shape.setAsBox(scissors_sprite.getWidth()/2 / PIXELS_TO_METERS, scissors_sprite.getHeight()
                       /2 / PIXELS_TO_METERS);
        
        FixtureDef scissors_fixtureDef = new FixtureDef();
        scissors_fixtureDef.shape = shape;
        scissors_fixtureDef.density = .1f;

        scissors_body.createFixture(scissors_fixtureDef);

        scissors_shape.dispose();
        /*----------------------------------------------------------------*/        
        
        debugRenderer = new Box2DDebugRenderer();
        
        lastTimeBg = TimeUtils.nanoTime();

        Gdx.input.setInputProcessor(this);
        
        if(m != null){
            m.play();
        }
        threadSprites = createSprites(10);
        threadBodies = createRope(threadSprites);
        
        world.setContactListener(new ContactListener() {

                @Override
            public void beginContact(Contact contact) {
                 player_sprite.setTexture(redlet);
                for (Sprite threadSprite : threadSprites) {
                    threadSprite.setTexture(redlet);
                }
            }

            @Override
            public void endContact(Contact contact) {
                player_sprite.setTexture(shortthreadlet);
                for (Sprite threadSprite : threadSprites) {
                    threadSprite.setTexture(shortthreadlet);
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                System.out.println("Presolve.");
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
                System.out.println("postsolve");
            }
    });

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
        
        scissors_sprite.setPosition((scissors_body.getPosition().x * PIXELS_TO_METERS) - scissors_sprite.
                           getWidth()/2 , 
                (scissors_body.getPosition().y * PIXELS_TO_METERS) -scissors_sprite.getHeight()/2 )
                 ;
        
        scissors_sprite.setRotation((float)Math.toDegrees(scissors_body.getAngle()));
        
        for (int i = 0; i < threadSprites.length; i++){
            threadSprites[i].setPosition((threadBodies[i].getPosition().x * PIXELS_TO_METERS) - threadSprites[i].
                           getWidth()/2 , 
                (threadBodies[i].getPosition().y * PIXELS_TO_METERS) -threadSprites[i].getHeight()/2 )
                 ;
        
            threadSprites[i].setRotation((float)Math.toDegrees(threadBodies[i].getAngle()));
        }
        
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if (TimeUtils.nanoTime() - lastTimeBg > 100000000) {
            bgx -= 50;
            scissors_body.setLinearVelocity(new Vector2(-5f,0f));
            lastTimeBg = TimeUtils.nanoTime();
        }

        if (bgx == 0) {
            bgx = 800;
        }
        if (scissors_sprite.getX() <= -220) {
           scissors_body.setTransform(6, 5, 0); 
        }
        
        batch.setProjectionMatrix(camera.combined);
        debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS, 
                      PIXELS_TO_METERS, 0);
        
        batch.begin();
        if(drawSprite){
            batch.draw(background, bgx - 800, 0);
            batch.draw(background, bgx, 0);
        
            batch.draw(player_sprite, player_sprite.getX(), player_sprite.getY(),player_sprite.getOriginX(),
                       player_sprite.getOriginY(), player_sprite.getWidth(),player_sprite.getHeight(),
                       player_sprite.getScaleX(),player_sprite.getScaleY(),player_sprite.getRotation());
            
            batch.draw(scissors_sprite, scissors_sprite.getX(), scissors_sprite.getY(),scissors_sprite.getOriginX(),
                       scissors_sprite.getOriginY(), scissors_sprite.getWidth(),scissors_sprite.getHeight(),
                       scissors_sprite.getScaleX(),scissors_sprite.getScaleY(),scissors_sprite.getRotation());
            
            for (Sprite threadSprite : threadSprites) {
                batch.draw(threadSprite, threadSprite.getX(), threadSprite.getY(), threadSprite.getOriginX(), 
                        threadSprite.getOriginY(), threadSprite.getWidth(), threadSprite.getHeight(), 
                        threadSprite.getScaleX(), threadSprite.getScaleY(), threadSprite.getRotation());
            }
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
            //System.out.println("Y - mouse : sprite " + posY +" : "+ ud);
            //System.out.println("X - mouse : sprite " + posX +" : "+ lr);

            if (posY > (body.getPosition().y* PIXELS_TO_METERS)){
                body.applyForceToCenter(new Vector2(0f,2f), true);                
            } else {
                body.applyForceToCenter(new Vector2(0f,-2f), true);
            }
            body.setTransform(body.getPosition().x, posY/PIXELS_TO_METERS, 0);
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
        if(keycode == Input.Keys.ESCAPE){
            drawSprite = !drawSprite;
        }
        if (keycode == Input.Keys.R){
            
            body.setTransform(1.5f,2, 0);
            body.setLinearVelocity(new Vector2(0f,0f));
            body.setAngularVelocity(0);
        }
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
            //System.out.println("Y - mouse : sprite " + posY +" : "+ ud);
            //System.out.println("X - mouse : sprite " + posX +" : "+ lr);
            if (posY > (body.getPosition().y* PIXELS_TO_METERS)){
                body.applyForceToCenter(new Vector2(0f,2f), true);                
            } else {
                body.applyForceToCenter(new Vector2(0f,-2f), true);
            }
            body.setTransform(body.getPosition().x, posY/PIXELS_TO_METERS, 0);
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
    
    public Sprite[] createSprites(int length){
        Sprite[] sprites = new Sprite[length];
        float startingX = width/4-player_sprite.getWidth()/2;
        float startingY = height/2-player_sprite.getHeight()/2;
        for (int i = 0; i < sprites.length; i++){
            sprites[i] = new Sprite(shortthreadlet);

            sprites[i].setPosition(startingX - sprites[i].getWidth(),startingY);
            startingX -= sprites[i].getWidth();
        }
        return sprites;
    }
    
    public Body[] createRope(Sprite[] sprites){
        Body[] segments = new Body[sprites.length];
        RevoluteJoint[] joints = new RevoluteJoint[sprites.length-1];
        RevoluteJoint[] secondaryjoints = new RevoluteJoint[sprites.length-1];
        RevoluteJoint[] tertiaryjoints = new RevoluteJoint[sprites.length-1];
        BodyDef segmentDef = new BodyDef();
        segmentDef.type = BodyType.DynamicBody;
        
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprites[1].getWidth()/2 / PIXELS_TO_METERS, sprites[1].getHeight()
                       /2 / PIXELS_TO_METERS);
        
        for (int i = 0; i < segments.length; i++){
            segments[i] = world.createBody(segmentDef);
            segmentDef.position.set((sprites[i].getX() + sprites[i].getWidth()/2) / 
                             PIXELS_TO_METERS, 
                (sprites[i].getY() + sprites[i].getHeight()/2) / PIXELS_TO_METERS);
            segments[i].createFixture(shape, 2);
        }
        
        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.collideConnected = false;
        jointDef.localAnchorA.x = -sprites[1].getWidth()/2/PIXELS_TO_METERS;
        jointDef.localAnchorB.x = sprites[1].getWidth()/2/PIXELS_TO_METERS;
        
        for (int i = 0; i < joints.length-1; i++){
            jointDef.bodyA = segments[i];
            jointDef.bodyB = segments[i+1];
            joints[i] = (RevoluteJoint) world.createJoint(jointDef);
            secondaryjoints[i] = (RevoluteJoint) world.createJoint(jointDef);
            tertiaryjoints[i] = (RevoluteJoint) world.createJoint(jointDef);
        }
        
        jointDef.bodyA = body;        
        jointDef.bodyB = segments[0];

        joints[joints.length -1] = (RevoluteJoint) world.createJoint(jointDef);
        secondaryjoints[secondaryjoints.length - 1] = (RevoluteJoint) world.createJoint(jointDef);
        tertiaryjoints[tertiaryjoints.length - 1] = (RevoluteJoint) world.createJoint(jointDef);
        
        return segments;
    }
}
