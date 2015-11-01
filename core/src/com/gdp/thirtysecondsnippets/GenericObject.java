package com.gdp.thirtysecondsnippets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Dan on 10/24/2015.
 */
public class GenericObject {

    Preferences prefs = Gdx.app.getPreferences("30SSSettings");

    static final short THREAD_BIT = 2;
    static final short HEAD_BIT = 4;
    static final short BLADE_BIT = 8;
    static final short SCISSOR_BIT = 16;
    static final short NEEDLE_BIT = 32;
    static final short NEEDLE_HOLE_BIT = 64;
    static final short NO_COLLIDE_BIT = 128;
    static final short PARTICLE_BIT = 256;

    final float PIXELS_TO_METERS = 100f;

    int backgroundType;

    float xlocation,ylocation;
    Sprite sprite;


}
