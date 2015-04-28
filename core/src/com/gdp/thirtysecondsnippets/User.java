/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gdp.thirtysecondsnippets;

/**
 *
 * @author George
 */
public class User {
    String uid = "";
    int needles_thread = 0;
    double thread_cut = 0;
    int songs_played = 0;
    int beats = 0;
    int total_needles = 0;
    
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getNeedles_thread() {
        return needles_thread;
    }

    public void setNeedles_thread(int needles_thread) {
        this.needles_thread = needles_thread;
    }

    public double getThread_cut() {
        return thread_cut;
    }

    public void setThread_cut(double thread_cut) {
        this.thread_cut = thread_cut;
    }

    public int getSong_played() {
        return songs_played;
    }

    public void setSong_played(int song_played) {
        this.songs_played = song_played;
    }

    public int getBeats() {
        return beats;
    }

    public void setBeats(int beats) {
        this.beats = beats;
    }

    public int getTotal_needles() {
        return total_needles;
    }

    public void setTotal_needles(int total_needles) {
        this.total_needles = total_needles;
    }
    
    public void insertTestData(){
        needles_thread = 12345;
        thread_cut = 56763;
        songs_played = 9088;
        beats = 270000;
        total_needles = 76987;        
    }
}
