package com.gdp.thirtysecondsnippets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Dan on 10/25/2015.
 */
public class Needle extends AnimatedObject {
    World world;
    Sprite needleGreen, needleBlue, needleYellow;
    float width, height;
    public Needle(World WORLD, Sprite NEEDLEGREEN, Sprite NEEDLEBLUE, Sprite NEEDLEYELLOW, float WIDTH, float HEIGHT){
        world = WORLD;
        needleGreen = NEEDLEGREEN;
        needleBlue = NEEDLEBLUE;
        needleYellow = NEEDLEYELLOW;
        width = WIDTH;
        height = HEIGHT;
    }


    public Sprite createNeedleSprite(float posX, float posY, String orientation){

        Sprite newNeedleSprite;
        Random rand = new Random();
        switch (rand.nextInt(3)){
            case 0:
                newNeedleSprite =  new Sprite(needleGreen);
                break;
            case 1:
                newNeedleSprite =  new Sprite(needleBlue);
                break;
            case 2:
                newNeedleSprite =  new Sprite(needleYellow);
                break;
            default:
                newNeedleSprite =  new Sprite(needleGreen);
                break;
        }
        if ("up".equals(orientation)){
            newNeedleSprite.setPosition(posX - newNeedleSprite.getWidth() / 2, posY - newNeedleSprite.getHeight() / 2);
            newNeedleSprite.setRotation(180);
        } else if ("down".equals(orientation)){
            newNeedleSprite.setPosition(posX - newNeedleSprite.getWidth() / 2, 0 - newNeedleSprite.getHeight() / 2);
            newNeedleSprite.setRotation(0);
        } else {
            newNeedleSprite.setPosition(posX - newNeedleSprite.getWidth() / 2, posY - newNeedleSprite.getHeight() / 2);
            newNeedleSprite.setRotation(90);
        }
        return newNeedleSprite;
    }

    public Body createNeedleBody(String orientation){
        float x = width + 200;
        float y = height;
        Sprite needle_sprite;

        needle_sprite = createNeedleSprite(x,y, orientation);

        /**********************************************Main Body******************************************************/

        BodyDef needle_bodyDef = new BodyDef();
        needle_bodyDef.type = BodyDef.BodyType.DynamicBody;
        needle_bodyDef.fixedRotation = true;
        needle_bodyDef.gravityScale = 0f;

        needle_bodyDef.position.set((needle_sprite.getX() + needle_sprite.getWidth()/2) / PIXELS_TO_METERS,
                    (needle_sprite.getY() + needle_sprite.getHeight()/2) / PIXELS_TO_METERS);


        Body needle_body = world.createBody(needle_bodyDef);
        PolygonShape needle_shape = new PolygonShape();

        needle_shape.setAsBox(needle_sprite.getWidth()/2 / PIXELS_TO_METERS,
                needle_sprite.getHeight() / 2 / PIXELS_TO_METERS);

        FixtureDef needle_fixtureDef = new FixtureDef();
        needle_fixtureDef.shape = needle_shape;
        needle_fixtureDef.density = .1f;
        needle_fixtureDef.filter.categoryBits = NO_COLLIDE_BIT;
        needle_fixtureDef.filter.maskBits = NO_COLLIDE_BIT;

        if ("up".equals(orientation)){
            needle_body.setTransform(needle_body.getTransform().getPosition(), ((float)Math.toRadians(180)));
        } else if ("down".equals(orientation)){
            needle_body.setTransform(needle_body.getTransform().getPosition(), ((float)Math.toRadians(0)));
        } else {
            needle_body.setTransform(needle_body.getTransform().getPosition(), 90);
        }

        needle_body.createFixture(needle_fixtureDef);

        /******************************************Bottom Bar*************************************************/
        PolygonShape needle_bottom_shape = new PolygonShape();

        if ("down".equals(orientation)) {
            needle_bottom_shape.setAsBox(needle_sprite.getWidth() / 2 / PIXELS_TO_METERS,
                    needle_sprite.getHeight() / 64 / PIXELS_TO_METERS, new Vector2(0,
                            -needle_sprite.getY() / PIXELS_TO_METERS / 1.5f), (float)Math.toRadians(0));
        } else if ("up".equals(orientation)) {
            needle_bottom_shape.setAsBox(needle_sprite.getWidth() / 2 / PIXELS_TO_METERS,
                    needle_sprite.getHeight() / 64 / PIXELS_TO_METERS, new Vector2(0,
                            needle_sprite.getHeight() / PIXELS_TO_METERS / 3f), (float)Math.toRadians(180));
        }

        FixtureDef needle_bottom_fixtureDef = new FixtureDef();
        needle_bottom_fixtureDef.shape = needle_bottom_shape;
        needle_bottom_fixtureDef.density = .1f;
        needle_bottom_fixtureDef.filter.categoryBits = NEEDLE_BIT;
        needle_bottom_fixtureDef.filter.maskBits = THREAD_BIT;

        needle_body.createFixture(needle_bottom_fixtureDef);
        /**********************************************HOLE***********************************************************/
        PolygonShape needle_hole_shape = new PolygonShape();

        if ("down".equals(orientation)) {
            needle_hole_shape.setAsBox(needle_sprite.getWidth() / 2 / PIXELS_TO_METERS,
                    needle_sprite.getHeight() / 16 / PIXELS_TO_METERS, new Vector2(0,
                            -needle_sprite.getY() / PIXELS_TO_METERS/1.3f), (float)Math.toRadians(0));
        } else if ("up".equals(orientation)) {
            needle_hole_shape.setAsBox(needle_sprite.getWidth() / 2 / PIXELS_TO_METERS,
                    needle_sprite.getHeight() / 16 / PIXELS_TO_METERS, new Vector2(0,
                            needle_sprite.getHeight() / PIXELS_TO_METERS / 3f), (float)Math.toRadians(180));
        }

        FixtureDef needle_hole_fixtureDef = new FixtureDef();
        needle_hole_fixtureDef.shape = needle_hole_shape;
        needle_hole_fixtureDef.density = .1f;
        needle_hole_fixtureDef.filter.categoryBits = NEEDLE_HOLE_BIT;
        needle_hole_fixtureDef.filter.maskBits = HEAD_BIT;

            needle_body.createFixture(needle_hole_fixtureDef);
        /*******************************************Top Bar*****************************************************/
        PolygonShape needle_top_shape = new PolygonShape();

        if ("down".equals(orientation)) {
            needle_top_shape.setAsBox(needle_sprite.getWidth() / 2 / PIXELS_TO_METERS,
                    needle_sprite.getHeight() / 64 / PIXELS_TO_METERS, new Vector2(0,
                            -needle_sprite.getY() / PIXELS_TO_METERS / 1.1f), (float)Math.toRadians(0));
        } else if ("up".equals(orientation)) {
            needle_top_shape.setAsBox(needle_sprite.getWidth() / 2 / PIXELS_TO_METERS,
                    needle_sprite.getHeight() / 64 / PIXELS_TO_METERS, new Vector2(0,
                            needle_sprite.getHeight() / PIXELS_TO_METERS / 3.2f), (float)Math.toRadians(180));
        }

        FixtureDef needle_top_fixtureDef = new FixtureDef();
        needle_top_fixtureDef.shape = needle_top_shape;
        needle_top_fixtureDef.density = .1f;
        needle_top_fixtureDef.filter.categoryBits = NEEDLE_BIT;
        needle_top_fixtureDef.filter.maskBits = THREAD_BIT;

        needle_body.createFixture(needle_top_fixtureDef);
        needle_body.setUserData(needle_sprite);
        /**********************************************************************************************************/
        needle_shape.dispose();
        needle_top_shape.dispose();
        needle_hole_shape.dispose();

        return needle_body;
    }
}
