/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gdp.thirtysecondsnippets;

import analysis.SnippetAnalysis;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author George
 */
public class LoadTrackData  implements Screen{
    private final Game tss;
    private int genreId;
    private Table table = new Table();
    private Stage stage = new Stage();
    
    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("buttons.pack"));

    private Skin skin = new Skin(Gdx.files.internal("skin.json"), atlas);
    
    public LoadTrackData(Game tss, int genreId){
        this.tss = tss;
        this.genreId = genreId;
    }

    @Override
    public void show() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream is = null;
                try {
                    // do something important here, asynchronously to the rendering thread
                    MusicDB db = new MusicDB();
                    final Track t = db.getTrackByGenreID(genreId);
                    System.out.println(t.toString());
                    String filename = "music.mp3";
                    is = new URL(t.getPreview_url()).openStream();
                    
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

                    fos.flush();
                    fos.close();
                    
                     final Music m = Gdx.audio.newMusic(handle);
                    
                    final SnippetAnalysis analysis = new SnippetAnalysis(handle, 1024);
                    analysis.doAnalysis();
                    
                    // post a Runnable to the rendering thread that processes the result
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            ThirtySecondSnippets game = new ThirtySecondSnippets(tss, t, analysis, m);
                            tss.setScreen(game);
                        }
                    });
                } catch (MalformedURLException ex) {
                    Logger.getLogger(LoadTrackData.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(LoadTrackData.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        is.close();
                    } catch (IOException ex) {
                        Logger.getLogger(LoadTrackData.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
             }
      }).start();
        
        Label musicvol = new Label("Loading... ", skin.get("labelb", Label.LabelStyle.class));
        table.add(musicvol).center().expand();
        
        table.setBackground(skin.getDrawable("bg_blur"));
        table.setFillParent(true);
        //table.debug();
        
        stage.addActor(table);
        
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        
        
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        
    }

    @Override
    public void pause() {
        
    }

    @Override
    public void resume() {
        
    }

    @Override
    public void hide() {
        
    }

    @Override
    public void dispose() {
        
    }
    
}
