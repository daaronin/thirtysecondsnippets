/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gdp.thirtysecondsnippets;

import com.badlogic.gdx.Game;

/**
 *
 * @author George McDaid
 */
public class TSS extends Game{
    public static final String TITLE="Game Project"; 
    public static final int WIDTH=800,HEIGHT=480; // used later to set window size
    
    @Override
    public void create() {
            setScreen(new GenreMenu());
    }

}
