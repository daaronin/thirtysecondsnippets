package com.gdp.thirtysecondsnippets;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ThirtySecondSnippets extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
        Sprite sprite;
	float posX, posY;
        
	@Override
	public void create () {
                float w = Gdx.graphics.getWidth();
                float h = Gdx.graphics.getHeight();
		
                batch = new SpriteBatch();
		img = new Texture("yarn.png");
                sprite = new Sprite(img);
                
                posX = w/2 - sprite.getWidth()/2;
                posY = h/2 - sprite.getHeight()/2;
                sprite.setPosition(posX,posY);
                
                //Gdx.input.setInputProcessor(this);
                
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
                 
                if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                    if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
                        sprite.translateX(-1f);
                    else
                        sprite.translateX(-10.0f);
                }
                if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
                    if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
                        sprite.translateX(1f);
                    else
                        sprite.translateX(10.0f);
                }
                if(Gdx.input.isKeyPressed(Input.Keys.UP)){
                    if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
                        sprite.translateY(1f);
                    else
                        sprite.translateY(10.0f);
                }
                if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
                    if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
                        sprite.translateY(-1f);
                    else
                        sprite.translateY(-10.0f);
                }
                
                batch.begin();
		sprite.draw(batch);
		batch.end();
	}
        
       /* @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            if(button == Buttons.LEFT){
                posX = screenX - sprite.getWidth()/2;
                posY = Gdx.graphics.getHeight() - screenY - sprite.getHeight()/2;
            }
            if(button == Buttons.RIGHT){
                posX = Gdx.graphics.getWidth()/2 - sprite.getWidth()/2;
                posY = Gdx.graphics.getHeight()/2 - sprite.getHeight()/2;
            }
            return false;
        }
        */
}
