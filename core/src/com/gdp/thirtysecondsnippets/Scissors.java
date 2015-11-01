package com.gdp.thirtysecondsnippets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;

/**
 * Created by Dan on 10/24/2015.
 */
public class Scissors extends AnimatedObject {
    float width, height;
    World world;
    Sprite scissor1, scissor2, scissor3, scissor4, scissor5, scissor6;


    public Scissors(float WIDTH, float HEIGHT, World WORLD, Sprite SCISSOR1, Sprite SCISSOR2,
                    Sprite SCISSOR3, Sprite SCISSOR4, Sprite SCISSOR5, Sprite SCISSOR6){
        width = WIDTH;
        height = HEIGHT;
        world = WORLD;
        scissor1 = SCISSOR1;
        scissor2 = SCISSOR2;
        scissor3 = SCISSOR3;
        scissor4 = SCISSOR4;
        scissor5 = SCISSOR5;
        scissor6 = SCISSOR6;
    }


    public Sprite createScissorsSprite(float posX, float posY, String orientation){
        Sprite newScissorSprite =  new Sprite(scissor6);
        if ("down".equals(orientation)){
            newScissorSprite.setPosition(posX - newScissorSprite.getWidth() / 2, posY - newScissorSprite.getHeight() / 2);
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
        float x = width + 200;
        float y = height;
        Sprite scissors_sprite;

        scissors_sprite = createScissorsSprite(x,y, orientation);

        BodyDef scissors_bodyDef = new BodyDef();
        scissors_bodyDef.type = BodyDef.BodyType.KinematicBody;

        scissors_bodyDef.position.set((scissors_sprite.getX() + scissors_sprite.getWidth() / 2) / PIXELS_TO_METERS,
                (scissors_sprite.getY() + scissors_sprite.getHeight() / 2) / PIXELS_TO_METERS);

        Body scissors_body = world.createBody(scissors_bodyDef);
        PolygonShape scissors_shape = new PolygonShape();

        scissors_shape.setAsBox(scissors_sprite.getWidth()/2 / PIXELS_TO_METERS,
                scissors_sprite.getHeight() / 2 / PIXELS_TO_METERS);

        FixtureDef scissors_fixtureDef = new FixtureDef();
        scissors_fixtureDef.shape = scissors_shape;
        scissors_fixtureDef.density = .1f;
        scissors_fixtureDef.filter.categoryBits = NO_COLLIDE_BIT;
        scissors_fixtureDef.filter.maskBits = NO_COLLIDE_BIT;

        if ("down".equals(orientation)){
            scissors_body.setTransform(scissors_body.getTransform().getPosition(), (float)Math.toRadians(0));
        } else if ("up".equals(orientation)){
            scissors_body.setTransform(scissors_body.getTransform().getPosition(), (float)Math.toRadians(180));
        } else {
            scissors_body.setTransform(scissors_body.getTransform().getPosition(), 90);
        }

        scissors_body.createFixture(scissors_fixtureDef);
        
        /************************************Fixture*********************************************************/
        PolygonShape scissors_cutShape = new PolygonShape();

        if ("down".equals(orientation)) {
            scissors_cutShape.setAsBox(scissors_sprite.getWidth() / 32 / PIXELS_TO_METERS,
                    scissors_sprite.getHeight() / 4 / PIXELS_TO_METERS, new Vector2(0,
                            -scissors_sprite.getY() / PIXELS_TO_METERS / 3.5f), (float)Math.toRadians(0));
        } else if ("up".equals(orientation)){
            scissors_cutShape.setAsBox(scissors_sprite.getWidth() / 32 / PIXELS_TO_METERS,
                    scissors_sprite.getHeight() / 4 / PIXELS_TO_METERS, new Vector2(0,
                            scissors_sprite.getY() / PIXELS_TO_METERS / 2.5f), (float)Math.toRadians(180));
        } else {
            scissors_cutShape.setAsBox(scissors_sprite.getWidth() / 32 / PIXELS_TO_METERS,
                    scissors_sprite.getHeight() / 4 / PIXELS_TO_METERS, new Vector2(0,
                            -scissors_sprite.getY() / PIXELS_TO_METERS / 3.5f), 90);
        }

        FixtureDef scissors_cutDef = new FixtureDef();
        scissors_cutDef.shape = scissors_cutShape;
        scissors_cutDef.density = .1f;
        scissors_cutDef.filter.categoryBits = BLADE_BIT;
        scissors_cutDef.filter.maskBits = THREAD_BIT;

        scissors_body.createFixture(scissors_cutDef);
        
        scissors_body.setUserData(scissors_sprite);
        scissors_shape.dispose();

        return scissors_body;
    }

    public ArrayList<Integer> animateScissor(int iterator, int timer, int timerpaceOpen, int timerpaceClosed, int lastiterator, ArrayList<Body> scissorBodies){
        Sprite sprt;
        ArrayList<Integer> nums = new ArrayList<Integer>();
        if (iterator == 6){
            if (timer >= timerpaceOpen){
                lastiterator = 6;
                iterator = 5;
                timer = 0;
            } else {
                timer++;
            }
        } else if (iterator == 1){
            if (timer >= timerpaceClosed){
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
                sprt = scissor1;
                break;
            case 2:
                sprt = scissor2;
                break;
            case 3:
                sprt = scissor3;
                break;
            case 4:
                sprt = scissor4;
                break;
            case 5:
                sprt = scissor5;
                break;
            default:
                sprt = scissor6;
                break;
        }
        for (Body scissor_body : scissorBodies){
            Sprite scissor_sprite = ((Sprite)scissor_body.getUserData());

            scissor_sprite.set(sprt);
            scissor_body.setUserData(scissor_sprite);
        }
        nums.add(iterator);
        nums.add(timer);
        nums.add(timerpaceOpen);
        nums.add(timerpaceClosed);
        nums.add(lastiterator);
        return nums;
    }

    public void changeMaskBits(Body bod, int iterator){
        if (bod.getFixtureList().size >= 2){
            if (iterator == 1) {
                Filter fix = bod.getFixtureList().get(1).getFilterData();
                fix.maskBits = THREAD_BIT;
                fix.categoryBits = BLADE_BIT;
                bod.getFixtureList().get(1).setFilterData(fix);
            } else {
                Filter fix = bod.getFixtureList().get(1).getFilterData();
                fix.maskBits = NO_COLLIDE_BIT;
                fix.categoryBits = NO_COLLIDE_BIT;
                bod.getFixtureList().get(1).setFilterData(fix);
            }
        }
    }
}
