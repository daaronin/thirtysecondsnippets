package com.gdp.thirtysecondsnippets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Dan on 10/24/2015.
 */
public class ThreadPieces extends GenericObject {

    World world;
    float width = 0, height = 0;
    Sprite shortthreadlet, threadlet, tail;

    public ThreadPieces(World WORLD, int BACKGROUNDTYPE, Sprite SHORTTHREADLET, Sprite THREADLET, Sprite TAIL){
        world = WORLD;
        backgroundType = BACKGROUNDTYPE;
        shortthreadlet = SHORTTHREADLET;
        threadlet = THREADLET;
        tail = TAIL;
    }

    public Sprite createSprite(int i, int length, int startingLength, ArrayList<Body> threads){
        Sprite threadSprite = shortthreadlet;
            if (backgroundType == 1){
                threadSprite = new Sprite(shortthreadlet);
            } else if (backgroundType == 2){
                Random rand = new Random();
                if (rand.nextBoolean()){
                    threadSprite = new Sprite(shortthreadlet);
                } else {
                    threadSprite = new Sprite(threadlet);
                }
            } else if (backgroundType == 3){
                if (startingLength == 0){
                    if (length == 1){
                        threadSprite = new Sprite(tail);
                    } else if (length > 1){
                        if (i == length-1){
                            threadSprite = new Sprite(tail);
                        } else {
                            threadSprite = new Sprite(shortthreadlet);
                        }
                    }
                } else {
                    for (int j = 0; j < threads.size(); j++){
                        threads.get(j).setUserData(shortthreadlet);
                    }
                    threadSprite = new Sprite(tail);
                }
            } else if (backgroundType == 4 || backgroundType == 5 || backgroundType == 6 || backgroundType == 7){
                threadSprite = new Sprite(shortthreadlet);
            } else {
                threadSprite = new Sprite(shortthreadlet);
            }
        return threadSprite;
    }

    public ArrayList<Body> createRope(int length, int startingLength, Body head, ArrayList<Body> threadBodies){
        width = ((Sprite)head.getUserData()).getWidth();
        height = ((Sprite)head.getUserData()).getHeight();
        xlocation = ((Sprite)head.getUserData()).getX();
        ylocation = ((Sprite)head.getUserData()).getY();
        ArrayList<Body> segments = new ArrayList<Body>();

        ArrayList<RopeJoint> ropeJoints = new ArrayList<RopeJoint>();
        ArrayList<RevoluteJoint> joints = new ArrayList<RevoluteJoint>();

        BodyDef segmentDef = new BodyDef();
        segmentDef.type = BodyDef.BodyType.DynamicBody;
        segmentDef.gravityScale = 2;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth()/2 / PIXELS_TO_METERS, getHeight()
                /2 / PIXELS_TO_METERS);

        boolean guided = prefs.getBoolean("guide", false);

        for (int i = 0; i < length; i++){
            segments.add(world.createBody(segmentDef));
            segmentDef.position.set((xlocation + getWidth()/2) /
                            PIXELS_TO_METERS,
                    (ylocation + getHeight()/2) / PIXELS_TO_METERS);
            FixtureDef threadDef = new FixtureDef();
            threadDef.shape = shape;
            threadDef.density = .1f;
            threadDef.filter.categoryBits = THREAD_BIT;
            threadDef.filter.maskBits = SCISSOR_BIT | BLADE_BIT | NEEDLE_BIT;

            segments.get(i).createFixture(threadDef);
            if(guided){
                segments.get(i).setLinearDamping(.5f);
                segments.get(i).setAngularDamping(.5f);
            }
            sprite = createSprite(i, length,startingLength,threadBodies);
            //sprite.setSize(sprite.getWidth(),sprite.getHeight());
            sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight()/2);

            segments.get(segments.size()-1).setUserData(sprite);

        }

        if(guided){
            segments.get(segments.size()-1).setAngularDamping(2.5f);
            segments.get(segments.size()-1).setLinearDamping(2.5f);
        }


        shape.dispose();
        //DistanceJointDef the_joint = new DistanceJointDef();

        RevoluteJointDef jointDef = new RevoluteJointDef();
        RopeJointDef ropeJointDef = new RopeJointDef();
        //RopeJointDef ropeJointDef = new RopeJointDef();
        jointDef.collideConnected = false;
        jointDef.localAnchorA.x = -getWidth()/2/PIXELS_TO_METERS;
        jointDef.localAnchorB.x = getWidth()/2/PIXELS_TO_METERS;


        ropeJointDef.collideConnected = false;
        ropeJointDef.localAnchorA.x = -getWidth()/2/PIXELS_TO_METERS;
        ropeJointDef.localAnchorB.x = getWidth()/2/PIXELS_TO_METERS;
        ropeJointDef.maxLength = getWidth()/PIXELS_TO_METERS;
        //ropeJointDef.localAnchorA.x = -sprites.get(0).getWidth()/2/PIXELS_TO_METERS;
        //ropeJointDef.localAnchorB.x = sprites.get(0).getWidth()/2/PIXELS_TO_METERS;


        for (int i = 0; i < length-1; i++){
            jointDef.bodyA = segments.get(i);
            jointDef.bodyB = segments.get(i + 1);
            //jointDef.initialize(segments.get(i), segments.get(i+1), body.getPosition());
            joints.add((RevoluteJoint) world.createJoint(jointDef));

            ropeJointDef.bodyA = segments.get(i);
            ropeJointDef.bodyB = segments.get(i + 1);
            ropeJoints.add((RopeJoint) world.createJoint(ropeJointDef));

            //the_joint.localAnchorA.x = -sprites.get(0).getWidth()/2/PIXELS_TO_METERS;
            //the_joint.localAnchorB.x = sprites.get(0).getWidth()/2/PIXELS_TO_METERS;
            //the_joint.initialize(segments.get(i), segments.get(i +1), new Vector2(2,2), new Vector2(2,2));
            //the_joint.collideConnected = false;
            //world.createJoint(the_joint);
            //ropeJointDef.bodyA = segments.get(i);
            //ropeJointDef.bodyB = segments.get(i + 1);
            //ropeJoints.add((RopeJoint) world.createJoint(ropeJointDef));
        }
        if (startingLength == 0){
            jointDef.bodyA = head;
            jointDef.bodyB = segments.get(0);
            joints.add((RevoluteJoint) world.createJoint(jointDef));

            ropeJointDef.bodyA = head;
            ropeJointDef.bodyB = segments.get(0);
            ropeJoints.add((RopeJoint) world.createJoint(ropeJointDef));
        } else {
            jointDef.bodyA = threadBodies.get(threadBodies.size()-1);
            jointDef.bodyB = segments.get(0);
            joints.add((RevoluteJoint) world.createJoint(jointDef));

            ropeJointDef.bodyA = threadBodies.get(threadBodies.size() -1);
            ropeJointDef.bodyB = segments.get(0);
            ropeJoints.add((RopeJoint) world.createJoint(ropeJointDef));
        }
        return segments;
    }

    public float getWidth(){
        return width;
    }

    public float getHeight(){
        return height;
    }
}
