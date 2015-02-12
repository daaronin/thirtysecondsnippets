/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gdp.thirtysecondsnippets;

/**
 *
 * @author George McDaid
 */
public class Track {

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }
    
    private String id = "";
    private String name = "";
    private String artist = "";
    private String preview_url = "";
    private String genre = "";
    private int popularity = 0;
    
    public Track(){
        
    }
    
    public Track(String id, String name, String artist, String preview_url, String genre, int popularity){
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.preview_url = preview_url;
        this.genre = genre;
        this.popularity = popularity;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPreview_url() {
        return preview_url;
    }

    public void setPreview_url(String preview_url) {
        this.preview_url = preview_url;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getPopularity() {
        return popularity;
    }
}
