package com.gdp.thirtysecondsnippets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Dan on 10/25/2015.
 */
public class Collectible extends AnimatedObject {
    World world;
    ArrayList<Sprite> sprites = new ArrayList<Sprite>();
    float width, height;

    Texture pill,goodjob;


    public Collectible(World WORLD, float WIDTH, float HEIGHT){
        world = WORLD;
        width = WIDTH;
        height = HEIGHT;
        loadSprites();
    }

    public void loadSprites(){
        pill = new Texture(Gdx.files.internal("largerpill.png"));
        sprites.add(new Sprite(pill));
        goodjob = new Texture(Gdx.files.internal("goodjobbutton.png"));
        sprites.add(new Sprite(goodjob));
    }


    public Sprite createCollectibleSprite(float posX, float posY, String orientation, int type){

        Sprite newCollectibleSprite;
        newCollectibleSprite = sprites.get(type);
        newCollectibleSprite.setSize(100,100);
        if ("up".equals(orientation)){
            newCollectibleSprite.setPosition(posX - newCollectibleSprite.getWidth() / 2, posY - newCollectibleSprite.getHeight() * 2);
            newCollectibleSprite.setRotation(0);
        } else if ("down".equals(orientation)){
            newCollectibleSprite.setPosition(posX - newCollectibleSprite.getWidth() / 2, 0 + newCollectibleSprite.getHeight() / 2);
            newCollectibleSprite.setRotation(0);
        } else {
            Random rand = new Random();
            newCollectibleSprite.setPosition(posX - newCollectibleSprite.getWidth() / 2, posY-((float)rand.nextInt((int)posY)) - newCollectibleSprite.getHeight() / 2);
            newCollectibleSprite.setRotation(0);
        }
        return newCollectibleSprite;
    }

    public Body createCollectibleBody(String orientation, int type){
        float x = width + 200;
        float y = height;
        Sprite collectible_sprite;

        collectible_sprite = createCollectibleSprite(x,y, orientation, type);

        /**********************************************Main Body******************************************************/

        BodyDef collectible_bodyDef = new BodyDef();
        collectible_bodyDef.type = BodyDef.BodyType.DynamicBody;
        collectible_bodyDef.fixedRotation = true;
        collectible_bodyDef.gravityScale = 0f;

        collectible_bodyDef.position.set((collectible_sprite.getX() + collectible_sprite.getWidth()/2) / PIXELS_TO_METERS,
                    (collectible_sprite.getY() + collectible_sprite.getHeight()/2) / PIXELS_TO_METERS);


        Body collectible_body = world.createBody(collectible_bodyDef);
        PolygonShape collectible_shape = new PolygonShape();

        collectible_shape.setAsBox(collectible_sprite.getWidth()/2 / PIXELS_TO_METERS,
                collectible_sprite.getHeight() / 2 / PIXELS_TO_METERS);

        FixtureDef collectible_fixtureDef = new FixtureDef();
        collectible_fixtureDef.shape = collectible_shape;
        collectible_fixtureDef.density = .1f;
        collectible_fixtureDef.filter.categoryBits = COLLECTIBLE_BIT;
        collectible_fixtureDef.filter.maskBits = THREAD_BIT | HEAD_BIT;

        if ("up".equals(orientation)){
            collectible_body.setTransform(collectible_body.getTransform().getPosition(), ((float)Math.toRadians(0)));
        } else if ("down".equals(orientation)){
            collectible_body.setTransform(collectible_body.getTransform().getPosition(), ((float)Math.toRadians(0)));
        } else {
            collectible_body.setTransform(collectible_body.getTransform().getPosition(), ((float)Math.toRadians(0)));
        }

        collectible_body.createFixture(collectible_fixtureDef);

        collectible_body.setUserData(collectible_sprite);
        /**********************************************************************************************************/
        collectible_shape.dispose();

        return collectible_body;
    }

    public void updateWidthHeight(float WIDTH, float HEIGHT){
        width = WIDTH;
        height = HEIGHT;
    }

    public void dispose(){
        pill.dispose();
        goodjob.dispose();
    }

    public int getCollectibleType(Body body){
       return sprites.indexOf(body.getUserData());
    }
}
