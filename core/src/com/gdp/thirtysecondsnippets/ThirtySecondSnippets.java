package com.gdp.thirtysecondsnippets;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThirtySecondSnippets implements InputProcessor, Screen {

    SpriteBatch batch;
    Texture threadlet, background, scissor1, scissor2, scissor3, scissor4, scissor5, scissor6, redlet, shortthreadlet;
    Sprite player_sprite;// scissors_sprite;
    World world;
    Body body;// scissors_body;
    int iterator = 6, lastiterator = 5;
    int timer = 0, timerpace = 15;
    float posX, posY;
    float scissorsX, scissorsY;
    float scissors_speed = -6f;
    float width, height;
    int screen_top_height = 5;
    float bgx;
    long lastTimeBg;
    
    static final short THREAD_BIT = 2;
    static final short HEAD_BIT = 4;
    static final short BLADE_BIT = 8;
    static final short SCISSOR_BIT = 16;
    static final short NEEDLE_BIT = 32;
    static final short NEEDLE_HOLE_BIT = 64;
    
    ArrayList<JointEdge> jointDeletionList = new ArrayList<JointEdge>();
    boolean jointDestroyable = true;
    
    ArrayList<Sprite> threadSprites = new ArrayList<Sprite>();
    ArrayList<Body> threadBodies = new ArrayList<Body>();
    
    ArrayList<Sprite> scissorSprites = new ArrayList<Sprite>();
    ArrayList<Body> scissorBodies = new ArrayList<Body>();
    
    ArrayList<Integer> queueToRemove = new ArrayList<Integer>();
    
    Box2DDebugRenderer debugRenderer;
    OrthographicCamera camera;
    Matrix4 debugMatrix;
    
    Vector2 mouseLoc;
    
    float torque = 0.0f;
    boolean drawSprite = true;
    boolean drawBoxes = false;
    
    final float PIXELS_TO_METERS = 100f;
    
    boolean dansTryingToGetWorkDone = true;
    private Game tss;

    public ThirtySecondSnippets(Game tss){
        this.tss = tss;
    }
    
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Buttons.LEFT) {
            posX = screenX - player_sprite.getWidth();
            posY = Gdx.graphics.getHeight() - screenY - player_sprite.getHeight() / 2;
            mouseLoc = new Vector2(posX, posY);

            if (posY > (body.getPosition().y* PIXELS_TO_METERS)){
                body.applyForceToCenter(new Vector2(0f,2f), true);                
            } else {
                body.applyForceToCenter(new Vector2(0f,-2f), true);
            }
            body.setTransform(body.getPosition().x, posY/PIXELS_TO_METERS, 0);
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
        scissor1.dispose();
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
        if (keycode == Input.Keys.F3){
            drawBoxes = !drawBoxes;
        }
        if (keycode == Input.Keys.W){
            createScissorsBody("down");
        }
        if (keycode == Input.Keys.S){
            createScissorsBody("up");
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

            if (posY > (body.getPosition().y* PIXELS_TO_METERS)){
                body.applyForceToCenter(new Vector2(0f,2f), true);                
            } else {
                body.applyForceToCenter(new Vector2(0f,-2f), true);
            }
            body.setTransform(body.getPosition().x, posY/PIXELS_TO_METERS, 0);
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
    
    public ArrayList<Sprite> createSprites(int length){
        ArrayList<Sprite> sprites = new ArrayList<Sprite>();
        float startingX = width/4-player_sprite.getWidth()/2;
        float startingY = height/2-player_sprite.getHeight()/2;
        for (int i = 0; i < length; i++){
            sprites.add(new Sprite(shortthreadlet));

            sprites.get(i).setPosition(startingX - sprites.get(i).getWidth(),startingY);
            startingX -= sprites.get(i).getWidth();
        }
        return sprites;
    }
    
    public Sprite createScissorsSprite(float posX, float posY, String orientation){
       Sprite newScissorSprite =  new Sprite(scissor6);
       if ("down".equals(orientation)){
            newScissorSprite.setPosition(posX - newScissorSprite.getWidth() / 2, posY);
            newScissorSprite.setRotation(0);
        } else if ("up".equals(orientation)){
            newScissorSprite.setPosition(posX - newScissorSprite.getWidth() / 2, 0 - newScissorSprite.getHeight() / 2);
            newScissorSprite.setRotation(180);
        } else {
            newScissorSprite.setPosition(posX - newScissorSprite.getWidth() / 2, posY - newScissorSprite.getHeight() / 2);
            newScissorSprite.setRotation(90);
        }
       return newScissorSprite;
    }
    
    public Body createScissorsBody(String orientation){
        float x = width * 2;
        float y = height;
        Sprite scissors_sprite;
        
        scissors_sprite = createScissorsSprite(x,y, orientation);
        
        scissorSprites.add(scissors_sprite);
        
        BodyDef scissors_bodyDef = new BodyDef();
        scissors_bodyDef.type = BodyDef.BodyType.KinematicBody;

        scissors_bodyDef.position.set((scissors_sprite.getX() + scissors_sprite.getWidth()/2) / PIXELS_TO_METERS, 
                (scissors_sprite.getY() + scissors_sprite.getHeight()/2) / PIXELS_TO_METERS);

        Body scissors_body = world.createBody(scissors_bodyDef);
        PolygonShape scissors_shape = new PolygonShape();

        scissors_shape.setAsBox(scissors_sprite.getWidth()/32 / PIXELS_TO_METERS,  
                scissors_sprite.getHeight() / 4 / PIXELS_TO_METERS);
        
        FixtureDef scissors_fixtureDef = new FixtureDef();
        scissors_fixtureDef.shape = scissors_shape;
        scissors_fixtureDef.density = .1f;
        scissors_fixtureDef.filter.categoryBits = BLADE_BIT;
        scissors_fixtureDef.filter.maskBits = THREAD_BIT;

        if ("down".equals(orientation)){
            scissors_body.setTransform(scissors_body.getTransform().getPosition(), scissors_body.getAngle());
        } else if ("up".equals(orientation)){
            scissors_body.setTransform(scissors_body.getTransform().getPosition(), 110);
        } else {
            scissors_body.setTransform(scissors_body.getTransform().getPosition(), 90);
        }
        
        scissors_body.createFixture(scissors_fixtureDef);
        scissorBodies.add(scissors_body);
        scissors_shape.dispose();
        
        return scissors_body;
    }
    
    public Texture animateScissor(){
        Texture tex;
        
        if (iterator == 6){
            if (timer == timerpace){
                lastiterator = 6;
                iterator = 5;
                timer = 0;
            } else {
                timer++;
            }
        } else if (iterator == 1){
            if (timer == timerpace){
                lastiterator = 1;
                iterator = 2;
                timer = 0;
            } else {
                timer++;
            }
        } else if (iterator > lastiterator){
            iterator++;
            lastiterator++;
        } else {
            iterator--;
            lastiterator--;
        }
        
        switch (iterator){
                case 1:
                    tex = scissor1;
                    break;
                case 2:
                    tex = scissor2;
                    break;
                case 3:
                    tex = scissor3;
                    break;
                case 4:
                    tex = scissor4;
                    break;
                case 5:
                    tex = scissor5;
                    break;
                default:
                    tex = scissor6;
                    break;
        }
        for (Sprite scissor_sprite : scissorSprites){
            scissor_sprite.setTexture(tex);
        }
        return tex;
    }
    
    public ArrayList<Body> createRope(ArrayList<Sprite> sprites){
        ArrayList<Body> segments = new ArrayList<Body>();
        RevoluteJoint[] joints = new RevoluteJoint[sprites.size()-1];
        RevoluteJoint[] secondaryjoints = new RevoluteJoint[sprites.size()-1];
        RevoluteJoint[] tertiaryjoints = new RevoluteJoint[sprites.size()-1];
        
        BodyDef segmentDef = new BodyDef();
        segmentDef.type = BodyType.DynamicBody;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprites.get(1).getWidth()/2 / PIXELS_TO_METERS, sprites.get(1).getHeight()
                       /2 / PIXELS_TO_METERS);
        
        for (int i = 0; i < sprites.size(); i++){
            segments.add(world.createBody(segmentDef));
            segmentDef.position.set((sprites.get(i).getX() + sprites.get(i).getWidth()/2) / 
                             PIXELS_TO_METERS, 
                (sprites.get(i).getY() + sprites.get(i).getHeight()/2) / PIXELS_TO_METERS);
            FixtureDef threadDef = new FixtureDef();
            threadDef.shape = shape;
            threadDef.density = .1f;
            threadDef.filter.categoryBits = THREAD_BIT;
            threadDef.filter.maskBits = SCISSOR_BIT | BLADE_BIT | NEEDLE_BIT;
            
            segments.get(i).createFixture(threadDef);
        }
        shape.dispose();
        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.collideConnected = false;
        jointDef.localAnchorA.x = -sprites.get(1).getWidth()/2/PIXELS_TO_METERS;
        jointDef.localAnchorB.x = sprites.get(1).getWidth()/2/PIXELS_TO_METERS;
        
        for (int i = 0; i < joints.length-1; i++){
            jointDef.bodyA = segments.get(i);
            jointDef.bodyB = segments.get(i + 1);
            joints[i] = (RevoluteJoint) world.createJoint(jointDef);
            secondaryjoints[i] = (RevoluteJoint) world.createJoint(jointDef);
            tertiaryjoints[i] = (RevoluteJoint) world.createJoint(jointDef);
        }
        
        jointDef.bodyA = body;        
        jointDef.bodyB = segments.get(0);

        joints[joints.length -1] = (RevoluteJoint) world.createJoint(jointDef);
        secondaryjoints[secondaryjoints.length - 1] = (RevoluteJoint) world.createJoint(jointDef);
        tertiaryjoints[tertiaryjoints.length - 1] = (RevoluteJoint) world.createJoint(jointDef);
        
        return segments;
    }

    @Override
    public void show() {
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
        System.out.println("Width: " + width + ", Height: " + height);
        
        camera = new OrthographicCamera(width,height);

        //camera.viewportWidth = width/PIXELS_TO_METERS;
        //camera.viewportHeight = height/PIXELS_TO_METERS;
        camera.position.set(width/2f, height/2f, 0);
       
        bgx = 800;
        
        batch = new SpriteBatch();
        shortthreadlet = new Texture("shortthread.png");
        threadlet = new Texture("thread.png");
        redlet = new Texture("redthread.png");
        background = new Texture("backgroundstars.jpg");
        scissor1 = new Texture("scissor1.png");
        scissor2 = new Texture("scissor2.png");
        scissor3 = new Texture("scissor3.png");
        scissor4 = new Texture("scissor4.png");
        scissor5 = new Texture("scissor5.png");
        scissor6 = new Texture("scissor6.png");
        player_sprite = new Sprite(shortthreadlet);

        player_sprite.setPosition(width/3-player_sprite.getWidth()/2,height/2-player_sprite.getHeight()/2);
   
        world = new World(new Vector2(-10f, 0), true);
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
        fixtureDef.filter.categoryBits = HEAD_BIT;
        fixtureDef.filter.maskBits = NEEDLE_HOLE_BIT;
        
        body.createFixture(fixtureDef);

        shape.dispose();
        /*----------------------------------------------------------------*/
        debugRenderer = new Box2DDebugRenderer();
        
        lastTimeBg = TimeUtils.nanoTime();

        Gdx.input.setInputProcessor(this);
        
        if(m != null){
            m.play();
        }
        threadSprites = createSprites(7);
        threadBodies = createRope(threadSprites);
        
        world.setContactListener(new ContactListener() {

            //Called when two box2d objects come into contact with one another
            //Each object is returned as a fixture
            @Override
            public void beginContact(Contact contact) {
                
                if (contact.getFixtureB().getFilterData().categoryBits == THREAD_BIT && jointDestroyable){
                    for (int i = 0; i < contact.getFixtureB().getBody().getJointList().size; i+=2){
                        if (!jointDeletionList.contains(contact.getFixtureB().getBody().getJointList().get(i))){
                            jointDeletionList.add(contact.getFixtureB().getBody().getJointList().get(i));
                        }
                    }
                    jointDestroyable = false;
                }
                if (contact.getFixtureA().getFilterData().categoryBits == THREAD_BIT && jointDestroyable){
                    for (int i = 0; i < contact.getFixtureA().getBody().getJointList().size; i+=2){
                        if (!jointDeletionList.contains(contact.getFixtureA().getBody().getJointList().get(i))){
                            jointDeletionList.add(contact.getFixtureA().getBody().getJointList().get(i));
                        }
                    }
                    jointDestroyable = false;
                }
                
                player_sprite.setTexture(redlet);
                for (Sprite threadSprite : threadSprites) {
                    threadSprite.setTexture(redlet);
                }
                
            }

            //Called when two box2d objects move away from each other after contact
            //Each object is returned as a fixture
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
    public void render(float f) {
        camera.update();
        world.step(1f/60f, 6, 2);
        
        body.applyTorque(torque,true);
        
        float ydir = posY - (body.getPosition().y* PIXELS_TO_METERS);
        
        if (ydir <= 5 && ydir >= -5){
            body.setLinearVelocity(0f, 0f);
        }
        
        player_sprite.setPosition((body.getPosition().x * PIXELS_TO_METERS) - player_sprite.getWidth()/2, 
                (body.getPosition().y * PIXELS_TO_METERS) -player_sprite.getHeight()/2);
        
        player_sprite.setRotation((float)Math.toDegrees(body.getAngle()));
        for (int i = 0; i < scissorSprites.size(); i++){
            scissorSprites.get(i).setPosition((scissorBodies.get(i).getPosition().x * PIXELS_TO_METERS) - scissorSprites.get(i).getWidth()/2 , 
                (scissorSprites.get(i).getY()));
        
            scissorSprites.get(i).setRotation((float)Math.toDegrees(scissorBodies.get(i).getAngle()));
        }
        for (int i = 0; i < threadSprites.size(); i++){
            threadSprites.get(i).setPosition((threadBodies.get(i).getPosition().x * PIXELS_TO_METERS) - threadSprites.get(i).getWidth()/2 , 
                (threadBodies.get(i).getPosition().y * PIXELS_TO_METERS) - threadSprites.get(i).getHeight()/2);
        
            threadSprites.get(i).setRotation((float)Math.toDegrees(threadBodies.get(i).getAngle()));
        }
        
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if (TimeUtils.nanoTime() - lastTimeBg > 50000000) {
            animateScissor();
        }
        
        if (TimeUtils.nanoTime() - lastTimeBg > 100000000) {
            bgx -= 50;
            for (int i = 0; i < scissorBodies.size(); i++){
                scissorBodies.get(i).setLinearVelocity(new Vector2(scissors_speed,0f));
                if (scissorSprites.get(i).getY() >= 0){
                    if (scissorSprites.get(i).getTexture() == scissor1 || scissorSprites.get(i).getTexture() == scissor2){
                        scissorBodies.get(i).setTransform(scissorBodies.get(i).getPosition().x, 6.2f, scissorBodies.get(i).getAngle());
                    } else {
                        scissorBodies.get(i).setTransform(scissorBodies.get(i).getPosition().x, 15, scissorBodies.get(i).getAngle());
                    }
                } else {
                    if (scissorSprites.get(i).getTexture() == scissor1 || scissorSprites.get(i).getTexture() == scissor2){
                        scissorBodies.get(i).setTransform(scissorBodies.get(i).getPosition().x, 1.4f, scissorBodies.get(i).getAngle());
                    } else {
                        scissorBodies.get(i).setTransform(scissorBodies.get(i).getPosition().x, 15, scissorBodies.get(i).getAngle());
                    }
                }

                if (scissorSprites.get(i).getX() <= -220) {
                   queueToRemove.add(i);
                }
            }
            if (queueToRemove.size() > 0){
                System.out.println("Queue Size: " + queueToRemove.size());
                for (Integer ref : queueToRemove){
                    scissorBodies.remove(scissorBodies.get(ref));
                    scissorSprites.remove(scissorSprites.get(ref));
                }
                queueToRemove.clear();
            }
            lastTimeBg = TimeUtils.nanoTime();
        }

        if (bgx == 0) {
            bgx = 800;
        }
        
        batch.setProjectionMatrix(camera.combined);
        debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS, 
                      PIXELS_TO_METERS, 0);
        
        if (jointDeletionList.size() > 0){
            for (JointEdge jointEdge : jointDeletionList) {
                Joint joint = jointEdge.joint;
                world.destroyJoint(joint);
                joint.setUserData(null);
                joint = null;    
            }
            jointDeletionList.clear();
            jointDestroyable = true;
        }
        
        batch.begin();
        if(drawSprite){
            batch.draw(background, bgx - 800, 0);
            batch.draw(background, bgx, 0);
        
            batch.draw(player_sprite, player_sprite.getX(), player_sprite.getY(),player_sprite.getOriginX(),
                       player_sprite.getOriginY(), player_sprite.getWidth(),player_sprite.getHeight(),
                       player_sprite.getScaleX(),player_sprite.getScaleY(),player_sprite.getRotation());
            
            for (Sprite scissors_sprite : scissorSprites){
                batch.draw(scissors_sprite, scissors_sprite.getX(), scissors_sprite.getY(),scissors_sprite.getOriginX(),
                           scissors_sprite.getOriginY(), scissors_sprite.getWidth(),scissors_sprite.getHeight(),
                           scissors_sprite.getScaleX(),scissors_sprite.getScaleY(),scissors_sprite.getRotation());
            }
            
            for (Sprite threadSprite : threadSprites) {
                batch.draw(threadSprite, threadSprite.getX(), threadSprite.getY(), threadSprite.getOriginX(), 
                        threadSprite.getOriginY(), threadSprite.getWidth(), threadSprite.getHeight(), 
                        threadSprite.getScaleX(), threadSprite.getScaleY(), threadSprite.getRotation());
            }
        }

        batch.end();
        if (drawBoxes){
            debugRenderer.render(world, debugMatrix);
        }
    }

    @Override
    public void hide() {
        
    }
}