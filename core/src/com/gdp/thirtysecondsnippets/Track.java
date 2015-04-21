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
public class Track{

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }
    
    private String id = "";
    private String name = "";
    private String artist = "";
    private String preview_url = "";
    private String genre = "";
    private int popularity = 0;
    private double tempo = 0;
    private int time_sig = 0;
    private int in_key = 0;
    
    public Track(){
        
    }
    
    public Track(String id, String name, String artist, String preview_url, String genre, int popularity, double tempo, int time_sig, int in_key){
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.preview_url = preview_url;
        this.genre = genre;
        this.popularity = popularity;
        this.tempo = tempo;
        this.time_sig = time_sig;
        this.in_key = in_key;
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
    
    public double getTempo() {
        return tempo;
    }

    public void setTempo(double tempo) {
        this.tempo = tempo;
    }

    public int getTime_sig() {
        return time_sig;
    }

    public void setTime_sig(int time_sig) {
        this.time_sig = time_sig;
    }

    public int getIn_key() {
        return in_key;
    }

    public void setIn_key(int in_key) {
        this.in_key = in_key;
    }
    
    @Override
    public String toString(){
        return name+" by "+artist+ " | URL: "+this.preview_url+" | Tempo: "+this.tempo;
    }

    
}