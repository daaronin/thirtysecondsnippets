package com.gdp.thirtysecondsnippets;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 *
 * @author Dan
 */
public class ThirtySecondSnippets implements InputProcessor, Screen {



    Preferences prefs = Gdx.app.getPreferences("30SSSettings");

    boolean changeMasks = false;
    
    SpriteBatch batch;
    Sprite threadlet, background, scissor1, scissor2, scissor3, scissor4,
            scissor5, scissor6, shortthreadlet, threadedBackground,
            colorBackground, shadowBackground, needleYellow, needleGreen, 
            needleBlue, star1, star2, star3, star4, star5, woodsBack, woodsFront,
            woodsBackground, woodsClouds, emptyTree, tail;
    Sprite player_sprite;
    World world;
    Body body;
    int iterator = 6, lastiterator = 5;
    int timer = 0, timerpaceClosed = 20, timerpaceOpen = 4;
    float posX, posY;

    Music backgroundMusic;
    Sound beepA, beepC, beepE, beepG, scissorSound;

    int genreId = 0;

    float beatOffset = 0;
    float currentBeat = 0;
    
    int runtimeCounter = 0;
    int backgroundType = 2;

    float endtimer = 0;
    float endtime = 30;
    
    float width, height;
    float bgx, bgcolorx, bgcloudx;
    long lastTimeBg;
    long lastTimeTempo;
    
    BitmapFont font,blackfont;
    CharSequence lbl_score = "Score: ";
    CharSequence score_amount = "";
    CharSequence multiplier = "";
    CharSequence bonus = "";
    CharSequence hyperthreading = "";
    CharSequence songTitle = "";
    CharSequence songArtist = "";
    CharSequence titleDisplay = "";
    
    int score = 0;
    int needle_combo = 0;
    int needle_hit = 1;
    static final int GROWTH_SUPRESSOR = 0;
    static final int SCORE_CONSTANT = 1;
    boolean HYPERTHREADING_MODE = false;
    
    int tempo;
    int lastRand = 0;
    int topSpacer = 0;
    int bottomSpacer = 0;
    
    int growthTimer = 0;
    
    int counter = 0;
    boolean growThread = false;
    boolean growableAllowed = true;
    boolean particlesAllowed = false;
    
    static final short THREAD_BIT = 2;
    static final short HEAD_BIT = 4;
    static final short BLADE_BIT = 8;
    static final short SCISSOR_BIT = 16;
    static final short NEEDLE_BIT = 32;
    static final short NEEDLE_HOLE_BIT = 64;
    static final short NO_COLLIDE_BIT = 128;
    static final short PARTICLE_BIT = 256;
    static final short COLLECTIBLE_BIT = 512;
    
    float BACKGROUND_SPEED = 28.8f;
    static final int STARTING_LENGTH = 2;
    static final int MAX_THREAD_LENGTH = 5;
    int SPACER_AMOUNT = 0;
    static final int SPAWN_RATE = 7;
    static final int GROWTH_TIMER_OFFSET = 4;
    
    static final int LEISURELY_DIFFICULTY = 3;
    static final int BRISK_DIFFICULTY = 5;
    static final int BREAKNECK_DIFFICULTY = 7;
    
    int difficulty = 0;
    
    float SCROLLING_FOREGROUND_SPEED = tempo/60f*-3f;
    
    ArrayList<JointEdge> jointDeletionList = new ArrayList<JointEdge>();
    boolean jointDestroyable = true;
    
    ArrayList<Body> threadBodies = new ArrayList<Body>();

    ArrayList<Body> scissorBodies = new ArrayList<Body>();
    
    ArrayList<Body> needleBodies = new ArrayList<Body>();

    ArrayList<Body> collectibleBodies = new ArrayList();
    
    ArrayList<Vector2> queueToRemove = new ArrayList<Vector2>();
    
    ArrayList<Sprite> particleSprites = new ArrayList<Sprite>();
    ArrayList<Body> particleBodies = new ArrayList<Body>();
    
    Box2DDebugRenderer debugRenderer;
    OrthographicCamera camera;
    private Viewport viewport;
    Matrix4 debugMatrix;
    
    Vector2 mouseLoc;
    
    float torque = 0.0f;
    boolean drawSprite = true;
    boolean drawBoxes = false;
    boolean drawText = true;
    
    final float PIXELS_TO_METERS = 100f;

    boolean dansTryingToGetWorkDone = true;
    private Game tss;
    
    int needles_thread = 0;
    int thread_cut = 0;
    int beats = 0;
    int needles = 0;
    int scissors = 0;

    MusicDB db = new MusicDB();

    ThreadPieces threads;
    Scissors scissor;
    Needle needle;
    Collectible collectibles;
    LevelController levelController;

    public ThirtySecondSnippets(Game tss, int genre, int difficulty){

        this.tss = tss;
        genreId = genre;
        this.difficulty = difficulty;
        SPACER_AMOUNT = difficulty;

        backgroundType = prefs.getInteger("theme", 1);

        String song = "";

        String artist = "";

        titleDisplay = songTitle = song;
        songArtist = artist;
        tempo = difficulty * 30;

        timerpaceClosed = tempo/30 * 40;
        timerpaceOpen = tempo/30 * 20;

        beatOffset = (endtime / ((float)tempo/2));

        beepA = Gdx.audio.newSound(Gdx.files.internal("audio\\beepA.mp3"));
        beepC = Gdx.audio.newSound(Gdx.files.internal("audio\\beepC.mp3"));
        beepE = Gdx.audio.newSound(Gdx.files.internal("audio\\beepE.mp3"));
        beepG = Gdx.audio.newSound(Gdx.files.internal("audio\\beepG.mp3"));
        scissorSound = Gdx.audio.newSound(Gdx.files.internal("audio\\scissorSound.mp3"));

        Random r = new Random();
        int choice = r.nextInt(5);
        if (genreId <= 4){
            choice = genreId;
        }
        switch(choice){
            case 0:
                backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio\\Whispered.mp3"));
                break;
            case 1:
                backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio\\FindaWay.mp3"));
                break;
            case 2:
                backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio\\LostWoods2.mp3"));
                break;
            case 3:
                backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio\\Handlebars.mp3"));
                break;
            case 4:
                backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio\\8bit.mp3"));
                break;
            default:
                break;
        }

        backgroundMusic.setVolume(prefs.getFloat("musicvol", 1));
        backgroundMusic.play();
    }
    
    
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Buttons.LEFT) {
            posX = screenX - player_sprite.getWidth();
            posY = height - screenY - player_sprite.getHeight() / 2f / PIXELS_TO_METERS;
            mouseLoc = new Vector2(posX, posY);

            if (posY > (body.getPosition().y)){
                body.applyForceToCenter(new Vector2(0f,2f), true);                
            } else {
                body.applyForceToCenter(new Vector2(0f,-2f), true);
            }
            body.setTransform(body.getPosition().x, posY / PIXELS_TO_METERS, 0);
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (pointer == Buttons.LEFT) {
            posX = screenX - player_sprite.getWidth();
            posY = height - screenY - player_sprite.getHeight() / 2f/PIXELS_TO_METERS;
            mouseLoc = new Vector2(posX, posY);

            if (posY > (body.getPosition().y)){
                body.applyForceToCenter(new Vector2(0f,2f), true);
            } else {
                body.applyForceToCenter(new Vector2(0f,-2f), true);
            }
            body.setTransform(body.getPosition().x, posY / PIXELS_TO_METERS, 0);
        }
        return true;
    }

    @Override
    public void resize(int w, int h) {
        width = w;
        height = h;

        //scissor.width = width;
        //scissor.height = height;
        //needle.width = width;
        //needle.height = height;

        viewport.update((int) width, (int) height);
        //camera.setToOrtho(false, width, height);
        camera.update();
        viewport.apply();
        //System.out.println("Resize Width: " + width + ", Resize Height: " + height);
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
        if (keycode == Input.Keys.F4){
            drawText = !drawText;
        }
        if (keycode == Input.Keys.W){
            scissorBodies.add(scissor.createScissorsBody("down"));
        }
        if (keycode == Input.Keys.S){
            scissorBodies.add(scissor.createScissorsBody("up"));
        }
        if (keycode == Input.Keys.Q){
            needleBodies.add(needle.createNeedleBody("up"));
        }
        if (keycode == Input.Keys.A){
            needleBodies.add(needle.createNeedleBody("down"));
        }
        if (keycode == Keys.NUMPAD_9){
            collectibleBodies.add(collectibles.createCollectibleBody("up", 0));
        }
        if (keycode == Keys.NUMPAD_6){
            collectibleBodies.add(collectibles.createCollectibleBody("middle", 0));
        }
        if (keycode == Keys.NUMPAD_5){
            collectibleBodies.add(collectibles.createCollectibleBody("middle", 1));
        }
        if (keycode == Keys.NUMPAD_3){
            collectibleBodies.add(collectibles.createCollectibleBody("down", 0));
        }
        if (keycode == Input.Keys.R){
            
            body.setTransform(1.5f,2, 0);
            body.setLinearVelocity(new Vector2(0f,0f));
            body.setAngularVelocity(0);
        }
        if (keycode == Input.Keys.G){
            
            growThread = true;
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
    public boolean mouseMoved(int screenX, int screenY) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return true;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean checkPlayerX(int screenX) {
        float mouseX = screenX - player_sprite.getWidth();
        if (mouseX <= Gdx.graphics.getWidth() / 6) {
            return true;
        }
        return false;
    }
    
    public void createParticles(int num, float posX, float posY){
        
        for (int i = 0; i < num; i++){
            Sprite particle;
            Random rand = new Random();
            int tex;
            tex = rand.nextInt(5);
            
            if (tex == 0){
                particle = new Sprite(star1);
            } else if (tex == 1){
                particle = new Sprite(star2);                
            }else if (tex == 2){
                particle = new Sprite(star3);                
            }else if (tex == 3){
                particle = new Sprite(star4);                
            }else{
                particle = new Sprite(star5);                
            }
            particle.setPosition(posX + particle.getWidth()/2, posY + particle.getHeight()/2);
            particleSprites.add(particle);
            /*-------------------------------------------------------*/
            BodyDef particleDef = new BodyDef();
            particleDef.type = BodyType.DynamicBody;
            
            particleDef.position.set(particle.getX()/PIXELS_TO_METERS + particle.getWidth()/2 /
                             PIXELS_TO_METERS,
                particle.getY()/PIXELS_TO_METERS + particle.getHeight()/2 / PIXELS_TO_METERS);
        
            /*------------------------------------------------------  */
            
            Body particleBody = world.createBody(particleDef);
            
            CircleShape shape = new CircleShape();
            shape.setRadius(particle.getWidth()/2/PIXELS_TO_METERS);
            shape.setPosition(new Vector2(particle.getX()/PIXELS_TO_METERS + particle.getWidth()/2 / PIXELS_TO_METERS,
                    particle.getY()/PIXELS_TO_METERS + particle.getHeight()/2 / PIXELS_TO_METERS));
            
            FixtureDef threadDef = new FixtureDef();
            threadDef.shape = shape;
            threadDef.density = .1f;
            threadDef.filter.categoryBits = PARTICLE_BIT;
            threadDef.filter.maskBits = PARTICLE_BIT | THREAD_BIT | NEEDLE_BIT;
            
            particleBody.createFixture(threadDef);
            particleBodies.add(particleBody);
            
            switch(tex){
                case 0:
                   // particleBodies.get(particleBodies.size()-1).setTransform(particleBodies.get(particleBodies.size()-1).getPosition().x-.1f, particleBodies.get(particleBodies.size()-1).getPosition().y+.1f,bgx);
                    particleBodies.get(particleBodies.size()-1).applyForceToCenter(new Vector2(.1f, 0f), true);
                    break;
                case 1:
                   // particleBodies.get(particleBodies.size()-1).setTransform(particleBodies.get(particleBodies.size()-1).getPosition().x+.05f, particleBodies.get(particleBodies.size()-1).getPosition().y-.1f,bgx);
                    particleBodies.get(particleBodies.size()-1).applyForceToCenter(new Vector2(.1f, -.2f), true);
                    break;
                case 2:
                   // particleBodies.get(particleBodies.size()-1).setTransform(particleBodies.get(particleBodies.size()-1).getPosition().x+.2f, particleBodies.get(particleBodies.size()-1).getPosition().y-.15f,bgx);
                    particleBodies.get(particleBodies.size()-1).applyForceToCenter(new Vector2(.1f, -.1f), true);
                    break;
                case 3:
                   // particleBodies.get(particleBodies.size()-1).setTransform(particleBodies.get(particleBodies.size()-1).getPosition().x+.15f, particleBodies.get(particleBodies.size()-1).getPosition().y+.15f,bgx);
                    particleBodies.get(particleBodies.size()-1).applyForceToCenter(new Vector2(.1f, .2f), true);
                    break;
                case 4:
                   // particleBodies.get(particleBodies.size()-1).setTransform(particleBodies.get(particleBodies.size()-1).getPosition().x-.2f, particleBodies.get(particleBodies.size()-1).getPosition().y+.2f,bgx);
                    particleBodies.get(particleBodies.size()-1).applyForceToCenter(new Vector2(.1f, .1f), true);
                    break;
            }
            
            shape.dispose();
        }
    }
    
    public void spawn(int num){
        this.beats++;
        //Random rand = new Random();
        //int randNum = rand.nextInt(SPAWN_RATE);
        //System.out.println(randNum);
        topSpacer = 0;
        bottomSpacer = 0;
        switch (num){
            case 0:
                if (bottomSpacer <= 0){
                    needleBodies.add(needle.createNeedleBody("down"));
                    this.needles++;
                    lastRand = 0;
                    bottomSpacer = SPACER_AMOUNT;
                    //beepA.play(1,.5f,0);
                    //beepE.play(1,.5f,0);
                }
                topSpacer--;
                bottomSpacer--;
                break;
            case 1:
                if (topSpacer <= 0){
                    needleBodies.add(needle.createNeedleBody("up"));
                    this.needles++;
                    lastRand = 1;
                    topSpacer = SPACER_AMOUNT;
                    //beepG.play(1,.5f,0);
                    //beepE.play(1,.5f,0);
                }
                topSpacer--;
                bottomSpacer--;
                break;
            case 2:
                if (topSpacer <= 0){
                    scissorBodies.add(scissor.createScissorsBody("down"));
                    this.scissors++;
                    lastRand = 2;
                    //topSpacer = SPACER_AMOUNT;
                    //beepC.play(1,.5f,0);
                }
                topSpacer--;
                bottomSpacer--;
                break;
            case 3:
                if (bottomSpacer <= 0){
                    scissorBodies.add(scissor.createScissorsBody("up"));
                    this.scissors++;
                    lastRand = 3;
                    bottomSpacer = SPACER_AMOUNT;
                    //beepHiC.play(1,.5f,0);
                }
                topSpacer--;
                bottomSpacer--;
                break;
            case 4:
                if (bottomSpacer <= 0){
                    scissorBodies.add(scissor.createScissorsBody("up"));
                    this.scissors++;
                    lastRand = 3;
                    bottomSpacer = SPACER_AMOUNT;
                    //beepE.play(1,.5f,0);
                }
                topSpacer--;
                bottomSpacer--;
                break;
            case 5:
                collectibleBodies.add(collectibles.createCollectibleBody("middle", 0));
                break;
            default:
                break;
        }
        
    }

    public void activateEffect(int result){
        switch (result){
            case 0:
                //body.getFixtureList().get(0).getShape().setRadius(((Sprite)body.getUserData()).getWidth()*2);
                break;
            default:
                break;
        }
    }

    @Override
    public void show() {
        //Gets track from Spotify
        //gets height and width
        width = 1280;
        height = 720;
        //System.out.println("Width: " + width + ", Height: " + height);
        
        camera = new OrthographicCamera(width,height);

        //camera.viewportWidth = width/PIXELS_TO_METERS;
        //camera.viewportHeight = height/PIXELS_TO_METERS;
        camera.position.set(width/2f, height/2f, 0);
        viewport = new StretchViewport(width, height, camera);

        viewport.update((int)width,(int)height,true);
        viewport.apply();

        if (backgroundType == 1){
        bgx = 144;
        bgcolorx = 2016;
        } else if (backgroundType == 2 || backgroundType == 3){
            bgx = 0;
            bgcolorx = 0;
            bgcloudx = 0;
        }
        
        BACKGROUND_SPEED = tempo/60f*2f;
        SCROLLING_FOREGROUND_SPEED = tempo/60f*-3f;
        
        batch = new SpriteBatch();

        levelController = new LevelController(backgroundType);
        threadlet = levelController.threadlet;
        background = levelController.background;
        scissor1 = levelController.scissor1;
        scissor2 = levelController.scissor2;
        scissor3 = levelController.scissor3;
        scissor4 = levelController.scissor4;
        scissor5 = levelController.scissor5;
        scissor6 = levelController.scissor6;
        shortthreadlet = levelController.shortthreadlet;
        threadedBackground = levelController.threadedBackground;
        colorBackground = levelController.colorBackground;
        shadowBackground = levelController.shadowBackground;
        needleYellow = levelController.needleYellow;
        needleGreen = levelController.needleGreen;
        needleBlue = levelController.needleBlue;
        star1 = levelController.star1;
        star2 = levelController.star2;
        star3 = levelController.star3;
        star4 = levelController.star4;
        star5 = levelController.star5;
        woodsBack = levelController.woodsBack;
        woodsFront = levelController.woodsFront;
        woodsBackground = levelController.woodsBackground;
        woodsClouds = levelController.woodsClouds;
        emptyTree = levelController.emptyTree;
        tail = levelController.tail;

        font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"),Gdx.files.internal("fonts/font.png"),false);
        blackfont = new BitmapFont(Gdx.files.internal("fonts/font.fnt"),Gdx.files.internal("fonts/font.png"),false);
//        font.getData().setScale(0.8f,0.8f);
//        blackfont.getData().setScale(0.8f,0.8f);

        timerpaceClosed = (300 - tempo)/60 * 1;
        timerpaceOpen = (300 - tempo)/60 * 12;
        
        player_sprite = new Sprite(threadlet);

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
        fixtureDef.filter.maskBits = NEEDLE_HOLE_BIT | COLLECTIBLE_BIT;

        body.createFixture(fixtureDef);
        body.setLinearDamping(.5f);
        body.setAngularDamping(.5f);

        Sprite sprite= new Sprite(threadlet);

        body.setUserData(sprite);

        shape.dispose();
        /*----------------------------------------------------------------*/
        debugRenderer = new Box2DDebugRenderer();
        
        lastTimeBg = TimeUtils.nanoTime();
        lastTimeTempo = TimeUtils.nanoTime();

        Gdx.input.setInputProcessor(this);
        
        
        threads = new ThreadPieces(world, backgroundType, shortthreadlet, threadlet, tail);
        threadBodies = threads.createRope(2, 0, body, threadBodies);

        scissor = new Scissors(width, height, world, scissor1, scissor2, scissor3, scissor4, scissor5, scissor6);

        needle = new Needle(world, needleGreen, needleBlue, needleYellow, width, height);

        collectibles = new Collectible(world, width, height);
        
        world.setContactListener(new ContactListener() {

            //Called when two box2d objects come into contact with one another
            //Each object is returned as a fixture
            @Override
            public void beginContact(Contact contact) {

                //CUTTING LISTENERS
                if ((contact.getFixtureB().getFilterData().categoryBits == THREAD_BIT)
                        && contact.getFixtureA().getFilterData().categoryBits == BLADE_BIT && jointDestroyable) {
                    for (int i = 0; i < contact.getFixtureB().getBody().getJointList().size; i++) {
                        if (!jointDeletionList.contains(contact.getFixtureB().getBody().getJointList().get(i))) {
                            jointDeletionList.add(contact.getFixtureB().getBody().getJointList().get(i));
                            needle_combo = 0;
                            jointDestroyable = false;
                            scissorSound.play(prefs.getFloat("soundsfxvol", 1),.5f,0);
                        }
                    }
                }
                if (contact.getFixtureA().getFilterData().categoryBits == THREAD_BIT
                        && contact.getFixtureB().getFilterData().categoryBits == BLADE_BIT && jointDestroyable) {
                    for (int i = 0; i < contact.getFixtureA().getBody().getJointList().size; i++) {
                        if (!jointDeletionList.contains(contact.getFixtureA().getBody().getJointList().get(i))) {
                            jointDeletionList.add(contact.getFixtureA().getBody().getJointList().get(i));
                            needle_combo = 0;
                            jointDestroyable = false;
                            scissorSound.play(prefs.getFloat("soundsfxvol", 1),.5f,0);
                        }
                    }
                }

                //COLLECTIBLE LISTENERS
                if ((contact.getFixtureB().getFilterData().categoryBits == THREAD_BIT || contact.getFixtureB().getFilterData().categoryBits == HEAD_BIT)
                        && contact.getFixtureA().getFilterData().categoryBits == COLLECTIBLE_BIT) {
                    int result = collectibles.getCollectibleType(contact.getFixtureA().getBody());
                    activateEffect(result);
                    if (!queueToRemove.contains(new Vector2(collectibleBodies.indexOf(contact.getFixtureA().getBody()), 4))) {
                        queueToRemove.add(new Vector2(collectibleBodies.indexOf(contact.getFixtureA().getBody()), 4));
                    }
                }
                if ((contact.getFixtureA().getFilterData().categoryBits == THREAD_BIT || contact.getFixtureA().getFilterData().categoryBits == HEAD_BIT)
                        && contact.getFixtureB().getFilterData().categoryBits == COLLECTIBLE_BIT) {
                    int result = collectibles.getCollectibleType(contact.getFixtureB().getBody());
                    activateEffect(result);
                    if (!queueToRemove.contains(new Vector2(collectibleBodies.indexOf(contact.getFixtureB().getBody()), 4))) {
                        queueToRemove.add(new Vector2(collectibleBodies.indexOf(contact.getFixtureB().getBody()), 4));
                    }
                }

                //THREADED LISTENERS
                if ((contact.getFixtureA().getFilterData().categoryBits == HEAD_BIT)
                        && needleBodies.indexOf(contact.getFixtureB().getBody()) > -1
                        && needleBodies.get(needleBodies.indexOf(contact.getFixtureB().getBody())).getFixtureList().size > 2
                        && needleBodies.get(needleBodies.indexOf(contact.getFixtureB().getBody())).getFixtureList().get(2).getFilterData().categoryBits == NEEDLE_HOLE_BIT) {
                    //System.out.println("CONTACT HAPPENED");
                    for (int i = 0; i < needleBodies.get(needleBodies.indexOf(contact.getFixtureB().getBody())).getFixtureList().size; i++ ) {
                        Filter filt = needleBodies.get(needleBodies.indexOf(contact.getFixtureB().getBody())).getFixtureList().get(i).getFilterData();
                        filt.maskBits = NO_COLLIDE_BIT;
                        filt.categoryBits = NO_COLLIDE_BIT;
                        needleBodies.get(needleBodies.indexOf(contact.getFixtureB().getBody())).getFixtureList().get(i).setFilterData(filt);
                    }

                    if (growableAllowed ) {
                        if (backgroundType == 2 || backgroundType == 6 || backgroundType == 7) {
                            needleBodies.get(needleBodies.indexOf(contact.getFixtureB().getBody())).setUserData(emptyTree);
                        }
                        growThread = true;
                        needle_combo++;
                        growableAllowed = false;
                        particlesAllowed = true;
                        playNeedleSound();
                    }
                } else if ((contact.getFixtureB().getFilterData().categoryBits == HEAD_BIT)
                        && needleBodies.indexOf(contact.getFixtureA().getBody()) > -1
                        && needleBodies.get(needleBodies.indexOf(contact.getFixtureA().getBody())).getFixtureList().size > 2
                        && needleBodies.get(needleBodies.indexOf(contact.getFixtureA().getBody())).getFixtureList().get(2).getFilterData().categoryBits == NEEDLE_HOLE_BIT) {
                    //System.out.println("CONTACT HAPPENED");
                    for (int i = 0; i < needleBodies.get(needleBodies.indexOf(contact.getFixtureA().getBody())).getFixtureList().size; i++ ) {
                        Filter filt = needleBodies.get(needleBodies.indexOf(contact.getFixtureA().getBody())).getFixtureList().get(i).getFilterData();
                        filt.maskBits = NO_COLLIDE_BIT;
                        filt.categoryBits = NO_COLLIDE_BIT;
                        needleBodies.get(needleBodies.indexOf(contact.getFixtureA().getBody())).getFixtureList().get(i).setFilterData(filt);
                    }
                    if (growableAllowed) {
                        if (backgroundType == 2 || backgroundType == 6 || backgroundType == 7) {
                            needleBodies.get(needleBodies.indexOf(contact.getFixtureA().getBody())).setUserData(emptyTree);
                        }
                        growThread = true;
                        needle_combo++;
                        growableAllowed = false;
                        particlesAllowed = true;
                        playNeedleSound();
                    }
                }

            }

            //Called when two box2d objects move away from each other after contact
            //Each object is returned as a fixture
            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                //System.out.println("Presolve: #" + counter++);
                if (contact.getFixtureB().getFilterData().categoryBits == THREAD_BIT && contact.getFixtureA().getFilterData().categoryBits == BLADE_BIT && jointDestroyable) {
                    //System.out.println("CONTACT HAPPENED");
                    for (int i = 0; i < contact.getFixtureB().getBody().getJointList().size; i++) {
                        if (!jointDeletionList.contains(contact.getFixtureB().getBody().getJointList().get(i))) {
                            jointDeletionList.add(contact.getFixtureB().getBody().getJointList().get(i));
                            needle_combo = 0;
                            jointDestroyable = false;
                            scissorSound.play(prefs.getFloat("soundsfxvol", 1),.5f,0);
                        }
                    }

                }
                if (contact.getFixtureA().getFilterData().categoryBits == THREAD_BIT && contact.getFixtureB().getFilterData().categoryBits == BLADE_BIT && jointDestroyable) {
                    //System.out.println("CONTACT HAPPENED");
                    for (int i = 0; i < contact.getFixtureA().getBody().getJointList().size; i++) {
                        if (!jointDeletionList.contains(contact.getFixtureA().getBody().getJointList().get(i))) {
                            jointDeletionList.add(contact.getFixtureA().getBody().getJointList().get(i));
                            needle_combo = 0;
                            jointDestroyable = false;
                            scissorSound.play(prefs.getFloat("soundsfxvol", 1),.5f,0);
                        }
                    }
                }


            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
                //System.out.println("postsolve");
            }
        });

    }

    @Override
    public void render(float f) {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        collectibles.updateWidthHeight(width,height);

        scissor.width = width;
        scissor.height = height;
        scissor.world = world;

        needle.width = width;
        needle.height = height;
        needle.world = world;


        //System.out.println("Gdx Width: " + width + ", Gdx Height: " + height);
        viewport.update((int) width, (int) height);
        viewport.setWorldHeight(height);
        viewport.setWorldWidth(width);
        //camera.setToOrtho(false,width, height);
        viewport.apply(true);
        camera.update();

        world.step(1f / 60f, 6, 2);

        endtimer += f;

        body.applyTorque(torque, true);

        float ydir = posY - (body.getPosition().y * PIXELS_TO_METERS);

        if (ydir <= 5 && ydir >= -5) {
            body.setLinearVelocity(0f, 0f);
        }

        player_sprite.setPosition((body.getPosition().x * PIXELS_TO_METERS) - player_sprite.getWidth() / 2,
                (body.getPosition().y * PIXELS_TO_METERS) - player_sprite.getHeight() / 2);

        player_sprite.setRotation((float) Math.toDegrees(body.getAngle()));


        Gdx.gl.glUseProgram(0);

        if (backgroundType == 2){
            Gdx.gl.glClearColor(1, 1, 1, 1);
        } else if (backgroundType == 3){
            Gdx.gl.glClearColor(136/256f, 36/256f, 38/256f, 1);
        } else {
            Gdx.gl.glClearColor(1, 1, 1, 1);
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (TimeUtils.nanoTime() - lastTimeBg > 50000000) {

        }


        //TODO Fix this shite
        if(endtimer > 27){

        } else if (endtimer > currentBeat) {
            Random r = new Random();
            int rando = r.nextInt(5);
            if (rando == 0) {
                Random rand = new Random();
                int randNum = rand.nextInt(3) + 2;
                spawn(randNum);
            }
            if (rando == 1) {
                Random rand = new Random();
                int randNum = rand.nextInt(3) + 2;
                spawn(randNum);
            }
            if (rando == 2) {
                Random rand = new Random();
                int randNum = rand.nextInt(2);
                spawn(randNum);
            }
            if (rando == 3) {
                Random rand = new Random();
                int randNum = rand.nextInt(2);
                spawn(randNum);
            }
            if (rando == 4){
                spawn(5);
            }

            float amount = (float)r.nextInt(7);
            amount = (amount/2);
            if (amount == 0){
                amount = 1;
            }
            currentBeat += beatOffset * amount;
        }

            
        if (TimeUtils.nanoTime() - lastTimeTempo > (100000000 * 60)/tempo) {
            lastTimeTempo = TimeUtils.nanoTime();
            ArrayList<Integer> nums = scissor.animateScissor(iterator, timer, timerpaceOpen, timerpaceClosed, lastiterator, scissorBodies);
            iterator = nums.get(0);
            timer = nums.get(1);
            timerpaceOpen = nums.get(2);
            timerpaceClosed = nums.get(3);
            lastiterator = nums.get(4);

            changeMasks = true;
        }
        
        if (TimeUtils.nanoTime() - lastTimeBg > 100000000) {
            runtimeCounter++;
            growthTimer--;
            if (backgroundType == 1){
                bgx -= BACKGROUND_SPEED;
                if (HYPERTHREADING_MODE){
                    bgcolorx -= BACKGROUND_SPEED*2;
                } else {
                    bgcolorx -= BACKGROUND_SPEED;
                }
            } else if (backgroundType == 2 || backgroundType == 3 || backgroundType == 4 || backgroundType == 5){
                if (HYPERTHREADING_MODE){
                    bgcolorx -= BACKGROUND_SPEED*2f;
                    bgcloudx -= BACKGROUND_SPEED*2f;
                    bgx -= BACKGROUND_SPEED;
                } else {
                    bgcolorx -= BACKGROUND_SPEED;
                    bgcloudx -= BACKGROUND_SPEED;
                    bgx -= BACKGROUND_SPEED * .5f;
                }
            } else if (backgroundType == 6 || backgroundType == 7){
                if (HYPERTHREADING_MODE){
                    bgcolorx -= BACKGROUND_SPEED*2f;
                    bgx -= BACKGROUND_SPEED;
                } else {
                    bgcolorx -= BACKGROUND_SPEED;
                    bgx -= BACKGROUND_SPEED * .5f;
                }
            }

            if (particlesAllowed){
                createParticles(15,body.getPosition().x*PIXELS_TO_METERS,body.getPosition().y*PIXELS_TO_METERS);
                particlesAllowed = false;
            }
            
            if (growThread){
                if (threadBodies.size() < MAX_THREAD_LENGTH){
                    threadBodies.addAll(threads.createRope(1, threadBodies.size(), body, threadBodies));
                }
                needles_thread++;
                growThread = false;
                growableAllowed = true;
            }
            if (queueToRemove.size() > 0){
                System.out.println("Queue Size: " + queueToRemove.size());
                for (int i = queueToRemove.size()-1; i >= 0; i--){
                    int ref = (int) queueToRemove.get(i).x;
                    if (queueToRemove.get(i).y == 0){
                        if (ref < scissorBodies.size()) {
//                            System.out.println("Scissors: " + ref);
                            scissorBodies.remove(scissorBodies.get(ref));
                        }
                    } else if (queueToRemove.get(i).y == 1){
                        if (ref < threadBodies.size()){
//                            System.out.println("Thread: " + ref);
                            threadBodies.get(ref).getJointList().clear();
                            threadBodies.remove(threadBodies.get(ref));
                            this.thread_cut++;
                        }
                    } else if (queueToRemove.get(i).y == 2){
                        if (ref < needleBodies.size()){
//                            System.out.println("Needle: " + ref);
                            needleBodies.remove(needleBodies.get(ref));
                        }
                    } else if (queueToRemove.get(i).y == 3){
//                        System.out.println("Particle: " + ref);
                        if (ref < particleSprites.size()){
                            particleSprites.remove(particleSprites.get(ref));
                        }
                        if (ref < particleBodies.size()){
                            particleBodies.remove(particleBodies.get(ref));
                        }
                    } else if (queueToRemove.get(i).y == 4){
                        if (ref < collectibleBodies.size() && ref >= 0){
//                            System.out.println("Needle: " + ref);
                            collectibleBodies.remove(collectibleBodies.get(ref));
                        }
                    }
                }
                queueToRemove.clear();
            }
            lastTimeBg = TimeUtils.nanoTime();
        }

        if (bgx <= 0) {
            if (backgroundType == 1){
                bgx = 144;
            } else if (backgroundType == 2 || backgroundType == 3 || backgroundType == 4 || backgroundType == 5 || backgroundType == 6 || backgroundType == 7){
                bgx = 1035;
            }
        }
        if (bgcolorx <= 0){
            if (backgroundType == 1){
                bgcolorx = 2016;
            } else if (backgroundType == 2 || backgroundType == 3 || backgroundType == 4 || backgroundType == 5 || backgroundType == 6 || backgroundType == 7){
                bgcolorx = 1035;
            }
        }
        if (backgroundType == 2 || backgroundType == 3 || backgroundType == 4 || backgroundType == 5){
            if (bgcloudx <= 0){
                bgcloudx = 3000;
            }
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
        if (threadBodies.isEmpty() && body.getJointList().size > 0){
            world.destroyJoint(body.getJointList().peek().joint);
        }
        
        if (threadBodies.size() == MAX_THREAD_LENGTH){
            HYPERTHREADING_MODE = true;
            BACKGROUND_SPEED = 28f;
            SCROLLING_FOREGROUND_SPEED = tempo/60f*-2f * 2f;
            if (needle_combo < 5){
                needle_combo = 5;
            }
        } else {
            HYPERTHREADING_MODE = false;
            BACKGROUND_SPEED = 14.4f;
            SCROLLING_FOREGROUND_SPEED = tempo/60f*-2f;
        }

        if (endtimer > endtime){
                endLevel();
        } else {
            if (runtimeCounter >= 30){
                songTitle = songArtist;
            }
            if (runtimeCounter >= 60){
                songTitle = "";
            }
            if (HYPERTHREADING_MODE){
                score += SCORE_CONSTANT * threadBodies.size() * needle_combo;
                score_amount = lbl_score.toString() + score;
                multiplier = "";
                bonus = "BONUS: x" + needle_combo;
            } else {
                score += SCORE_CONSTANT * threadBodies.size();
                score_amount = lbl_score.toString() + score;
                multiplier = "x" + threadBodies.size();
                bonus = "";
            }
        }

        batch.begin();
        //batch.setColor(0,.4f,.4f,.2f);
        if(drawSprite){
            if (backgroundType == 1){
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
            } else if (backgroundType == 2){
                //batch.draw(woodsBackground, -width, -height, width*2, height*2);
                
                batch.draw(woodsBack, bgx, 0);
                batch.draw(woodsBack, bgx - 1035, 0);
                batch.draw(woodsBack, bgx + 1035, 0);
                
                batch.draw(woodsFront, bgcolorx, 0);
                batch.draw(woodsFront, bgcolorx - 1035, 0);
                batch.draw(woodsFront, bgcolorx + 1035, 0);
                
                batch.draw(woodsClouds, bgcloudx - 1000 , height - woodsClouds.getHeight());
            } else if (backgroundType == 3 || backgroundType == 6 || backgroundType == 7){
                //batch.draw(woodsBackground, -width, -height, width*2, height*2);
                
                batch.draw(woodsBack, bgx, 0);
                batch.draw(woodsBack, bgx - 1035f, 0);
                batch.draw(woodsBack, bgx + 1035f, 0);
                
                batch.draw(woodsFront, bgcolorx, 0);
                batch.draw(woodsFront, bgcolorx - 1035f, 0);
                batch.draw(woodsFront, bgcolorx + 1035f, 0);
            } else if (backgroundType == 4 || backgroundType == 5){
                batch.draw(woodsBackground, -width, -height, width*2, height*2);
                
                batch.draw(woodsBack, bgx, 0);
                batch.draw(woodsBack, bgx - 1035, 0);
                batch.draw(woodsBack, bgx + 1035, 0);
            }
            
            
            batch.draw(player_sprite, player_sprite.getX(), player_sprite.getY(),player_sprite.getOriginX(),
                       player_sprite.getOriginY(), player_sprite.getWidth(),player_sprite.getHeight(),
                    player_sprite.getScaleX(), player_sprite.getScaleY(), player_sprite.getRotation());

            for (Body scissors_body : scissorBodies){
                if (changeMasks) {
                    scissor.changeMaskBits(scissors_body, iterator);
                }
                if (scissors_body.getUserData() instanceof Sprite) {
                    Sprite sprite = (Sprite) scissors_body.getUserData();
                    sprite.setPosition(scissors_body.getPosition().x * PIXELS_TO_METERS - sprite.getWidth() / 2,
                            scissors_body.getPosition().y * PIXELS_TO_METERS - sprite.getHeight() / 2);
                    sprite.setRotation((float) Math.toDegrees(scissors_body.getAngle()));
                    sprite.draw(batch);

                    scissors_body.setLinearVelocity(new Vector2(SCROLLING_FOREGROUND_SPEED, 0f));
                }
                if (scissors_body.getPosition().x <= -1 && !queueToRemove.contains(new Vector2(scissorBodies.indexOf(scissors_body),0))) {
                    //System.out.println("SCISSOR ADDED TO REMOVE: " + scissorBodies.indexOf(scissors_body));
                    queueToRemove.add(new Vector2(scissorBodies.indexOf(scissors_body), 0));
                }
            }
            changeMasks = false;
            
            for (Body needle_body : needleBodies){
                needle_body.setLinearVelocity(new Vector2(SCROLLING_FOREGROUND_SPEED,0f));
                if (needle_body.getUserData() instanceof Sprite) {
                    Sprite sprite = (Sprite) needle_body.getUserData();
                    sprite.setPosition(needle_body.getPosition().x * PIXELS_TO_METERS - sprite.getWidth() / 2,
                            needle_body.getPosition().y * PIXELS_TO_METERS - sprite.getHeight() / 2);
                    sprite.setRotation((float) Math.toDegrees(needle_body.getAngle()));
                    sprite.draw(batch);
                }
                if (needle_body.getPosition().x <= -1 && !queueToRemove.contains(new Vector2(needleBodies.indexOf(needle_body), 2))) {
                    queueToRemove.add(new Vector2(needleBodies.indexOf(needle_body), 2));
                }
            }

            for (Body thread : threadBodies) {
                if (thread.getUserData() instanceof Sprite) {

                    Sprite sprite = (Sprite) thread.getUserData();

                    sprite.setPosition(thread.getPosition().x * PIXELS_TO_METERS - sprite.getWidth() / 2, thread.getPosition().y * PIXELS_TO_METERS - sprite.getHeight() / 2);
                    sprite.setRotation((float) Math.toDegrees(thread.getAngle()));
                    sprite.draw(batch);
                }
                if (thread.getPosition().x <= -1 && !queueToRemove.contains(new Vector2(threadBodies.indexOf(thread), 1))) {
                    queueToRemove.add(new Vector2(threadBodies.indexOf(thread), 1));
                }
            }

            for (Body collectible_body : collectibleBodies){
                collectible_body.setLinearVelocity(new Vector2(SCROLLING_FOREGROUND_SPEED,0f));
                if (collectible_body.getUserData() instanceof Sprite) {
                    Sprite sprite = (Sprite) collectible_body.getUserData();
                    sprite.setPosition(collectible_body.getPosition().x * PIXELS_TO_METERS - sprite.getWidth() / 2,
                            collectible_body.getPosition().y * PIXELS_TO_METERS - sprite.getHeight() / 2);
                    sprite.setRotation((float) Math.toDegrees(collectible_body.getAngle()));
                    sprite.draw(batch);
                }
                if (collectible_body.getPosition().x <= -1 && !queueToRemove.contains(new Vector2(collectibleBodies.indexOf(collectible_body), 4))) {
                    queueToRemove.add(new Vector2(collectibleBodies.indexOf(collectible_body), 4));
                }
            }
            
            for (Sprite particleSprite : particleSprites){
                batch.draw(particleSprite, particleSprite.getX(), particleSprite.getY(), particleSprite.getOriginX(), 
                        particleSprite.getOriginY(), particleSprite.getWidth(), particleSprite.getHeight(), 
                        particleSprite.getScaleX(), particleSprite.getScaleY(), particleSprite.getRotation());
                int index = particleSprites.indexOf(particleSprite);

                particleSprite.setPosition((particleBodies.get(index).getPosition().x * PIXELS_TO_METERS) - particleSprite.getWidth() / 2,
                        (particleBodies.get(index).getPosition().y * PIXELS_TO_METERS) - particleSprite.getHeight() / 2);

                particleSprite.setRotation((float) Math.toDegrees(particleBodies.get(index).getAngle()));

                if (particleSprite.getX() <= -50 && !queueToRemove.contains(new Vector2(index, 3))) {
                    queueToRemove.add(new Vector2(index, 3));
                }
            }
            if (backgroundType == 2 || backgroundType == 3){
                font = blackfont;
            }
            if (drawText){
                font.draw(batch, score_amount, 10, 65);
                font.draw(batch, multiplier, 510, 65);
                font.draw(batch, bonus, 510, 65);
                font.draw(batch, hyperthreading, 0, height-5);
                font.draw(batch, songTitle, 0, height-5);
            }
        }

        batch.end();
        if (drawBoxes){
            debugRenderer.render(world, debugMatrix);
        }
        
    }

    public void endLevel(){
        List<NameValuePair> parameters = new ArrayList<NameValuePair>(1);
        parameters.add(new BasicNameValuePair("uid", Installation.id()));
        parameters.add(new BasicNameValuePair("needles_thread", Integer.toString(this.needles_thread)));
        parameters.add(new BasicNameValuePair("thread_cut", Integer.toString(this.thread_cut)));
        parameters.add(new BasicNameValuePair("songs_played", "1"));
        parameters.add(new BasicNameValuePair("beats", Integer.toString(this.beats)));
        parameters.add(new BasicNameValuePair("total_needles", Integer.toString(this.needles)));
        
        System.out.println("Threaded: "+needles_thread);
        
        db.updateUser(parameters);
        
        Results results = new Results();
        ArrayList<String> res = new ArrayList<String>();
        res.add((int)((this.needles_thread/(double)this.needles)*100) + "% thread rate");
        results.setResults(res);
        
        System.out.println("-------------------------------");
        System.out.println(" Difficulty: " + difficulty);
        System.out.println("    Needles: " + needles);
        System.out.println("   Scissors: " + scissors);
        System.out.println("-------------------------------");

        //backgroundMusic.stop();

        FinishScreen finish = new FinishScreen(tss, lbl_score.toString() + score, genreId, results, (String)titleDisplay,"By: " + (String)songArtist, difficulty);
        tss.setScreen(finish);
    }

    public void playNeedleSound(){
        Random r = new Random();
        int sound = r.nextInt(5);
        switch (sound){
            case 0:
                beepC.play(prefs.getFloat("soundsfxvol", 1),2f,0);
                beepA.play(prefs.getFloat("soundsfxvol", 1),2f,0);
                break;
            case 1:
                beepC.play(prefs.getFloat("soundsfxvol", 1),2f,0);
                beepG.play(prefs.getFloat("soundsfxvol", 1),2f,0);
                break;
            case 2:
                beepE.play(prefs.getFloat("soundsfxvol", 1),2f,0);
                beepA.play(prefs.getFloat("soundsfxvol", 1),2f,0);
                break;
            case 3:
                beepC.play(prefs.getFloat("soundsfxvol", 1),2f,0);
                beepE.play(prefs.getFloat("soundsfxvol", 1),2f,0);
                break;
            case 4:
                beepA.play(prefs.getFloat("soundsfxvol", 1),2f,0);
                beepE.play(prefs.getFloat("soundsfxvol", 1),2f,0);
                break;
        }
    }
    
    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        backgroundMusic.stop();
        //batch.dispose();

        levelController.dispose();
        collectibles.dispose();
        backgroundMusic.dispose();
        beepA.dispose();
        beepC.dispose();
        beepE.dispose();
        beepG.dispose();
        scissorSound.dispose();

        threadBodies.clear();
        scissorBodies.clear();
        needleBodies.clear();
        collectibleBodies.clear();
        particleBodies.clear();
        particleSprites.clear();
        queueToRemove.clear();

        world.dispose();
        font.dispose();
        blackfont.dispose();

        System.out.println("Good day kind sir.");
    }
}