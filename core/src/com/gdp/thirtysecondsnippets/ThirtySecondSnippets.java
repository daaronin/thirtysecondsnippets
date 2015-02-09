package com.gdp.thirtysecondsnippets;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

public class ThirtySecondSnippets implements ApplicationListener, InputProcessor {
    SpriteBatch batch;
    Texture threadlet, background, scissors;
    Sprite player_sprite, scissors_sprite;
    float posX, posY;
    float scissorsX, scissorsY;
    float width, height;
    float bgx;
    long lastTimeBg;
    
    @Override
    public void create () {
            width = Gdx.graphics.getWidth();
            height = Gdx.graphics.getHeight();

            //width = 800;
            //height = 480;
            bgx = 800;
            
            batch = new SpriteBatch();
            threadlet = new Texture("smallcat.png");
            background = new Texture("backgroundstars.jpg");
            scissors = new Texture("scissors.png");
            player_sprite = new Sprite(threadlet);
            scissors_sprite = new Sprite(scissors);
            
            posX = width/8 - player_sprite.getWidth()/2;
            posY = height/2 - player_sprite.getHeight()/2;
            scissorsX = width * .65f - player_sprite.getWidth()/2;
            scissorsY = height * .75f - player_sprite.getHeight()/2;
            player_sprite.setPosition(posX,posY);
            scissors_sprite.setPosition(scissorsX,scissorsY);
            // set lastTimeBg to current time
            lastTimeBg = TimeUtils.nanoTime();

            Gdx.input.setInputProcessor(this);

    }

    @Override
    public void render () {
            Gdx.gl.glClearColor(1, 1, 1, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


            if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
                    player_sprite.translateX(-1f);
                else
                    player_sprite.translateX(-10.0f);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && checkPlayerX((int)posX)){
                if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
                    player_sprite.translateX(1f);
                else
                    player_sprite.translateX(10.0f);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.UP)){
                if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
                    player_sprite.translateY(1f);
                else
                    player_sprite.translateY(10.0f);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
                if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
                    player_sprite.translateY(-1f);
                else
                    player_sprite.translateY(-10.0f);
            }
            
            // move the separator each 1s
            if(TimeUtils.nanoTime() - lastTimeBg > 100000000){
                    // move the separator 50px
                    bgx -= 50;
                    scissors_sprite.translateX(-10f);
                    // set the current time to lastTimeBg
                    lastTimeBg = TimeUtils.nanoTime();
            }

            // if the seprator reaches the screen edge, move it back to the first position
            if(bgx == 0){
                    bgx = 800;
            }
            if(scissors_sprite.getX() <= -220){
                    scissors_sprite.setX(650);
            }

            batch.begin();

            batch.draw(background, bgx - 800, 0);
            batch.draw(background, bgx, 0);
            
            player_sprite.draw(batch);
            scissors_sprite.draw(batch);
            batch.end();
    }
        
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            if(button == Buttons.LEFT && checkPlayerX(screenX)){
                posX = screenX - player_sprite.getWidth();
                posY = Gdx.graphics.getHeight() - screenY - player_sprite.getHeight()/2;
                player_sprite.setPosition(posX,posY);
            }
            if(button == Buttons.RIGHT){
                posX = Gdx.graphics.getWidth()/2 - player_sprite.getWidth()/2;
                posY = Gdx.graphics.getHeight()/2 - player_sprite.getHeight()/2;
                player_sprite.setPosition(posX,posY);
            }
            return false;
        }
        

    @Override
    public void resize(int width, int height) {
        
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
        System.out.println("Good day kind sir.");
    }

    @Override
    public boolean keyDown(int keycode) {
        float moveAmount = 1.0f;
        if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
            moveAmount = 10.0f;
        if(keycode == Keys.LEFT)
            posX-=moveAmount;
        if(keycode == Keys.RIGHT && checkPlayerX((int)posX))
            posX+=moveAmount;
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
        if(pointer == Buttons.LEFT){
            if (checkPlayerX(screenX))
                posX = screenX - player_sprite.getWidth();
 
            posY = Gdx.graphics.getHeight() - screenY - player_sprite.getHeight()/2;
            player_sprite.setPosition(posX,posY);
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
    
    public boolean checkPlayerX(int screenX){
        float mouseX = screenX - player_sprite.getWidth();
        if (mouseX <= Gdx.graphics.getWidth()/6){
            return true;
        }
        return false;
    }
}
