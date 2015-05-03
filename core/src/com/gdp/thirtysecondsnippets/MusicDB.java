/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gdp.thirtysecondsnippets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author George McDaid
 */
public class MusicDB {

    private String base_url = "http://30secondsnippets-gmcdaid.rhcloud.com/musicdb.php";
    private String key = "9JmgB8pQANJwsuH7";
    private final String USER_AGENT = "Mozilla/5.0";

    private static int SUCCESS = 7;
    private static int FAILURE = 6;
    
    public static final int USER_SETTING_NUMBER = 6;

    public MusicDB() {

    }

    public Track getTrackByGenre(String genre) {
        Track t = new Track();

        List<NameValuePair> parameters = new ArrayList<NameValuePair>(1);
        parameters.add(new BasicNameValuePair("getTrackByGenre", "true"));
        parameters.add(new BasicNameValuePair("key", key));
        parameters.add(new BasicNameValuePair("genre", genre));

        String response = sendPost(parameters);
        
        try{
            
            JsonValue root = new JsonReader().parse(response);
            int response_code = root.getInt("response_code", 0);
            String message = root.getString("message", "No Message Found");
        
        
            if (response_code == SUCCESS) {
                Json json = new Json();
                message = message.replace("\\\"", "\"");
                message = message.replace("\\", "");
                
                t = json.fromJson(Track.class, message);

            } else if (response_code == FAILURE) {
                throw new Exception("Failure: " + response_code);
            } else {
                throw new Exception("Unknown Response");
            }
        } catch (Exception ex) {
            Logger.getLogger(MusicDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return t;
    }
    
    public Track getTrackByGenreID(int id) {
        Track t = new Track();

        List<NameValuePair> parameters = new ArrayList<NameValuePair>(1);
        parameters.add(new BasicNameValuePair("getTrackByGenreID", "true"));
        parameters.add(new BasicNameValuePair("key", key));
        parameters.add(new BasicNameValuePair("id", String.valueOf(id)));

        String response = sendPost(parameters);
        
        try{
            
            JsonValue root = new JsonReader().parse(response);
            int response_code = root.getInt("response_code", 0);
            String message = root.getString("message", "No Message Found");
        
        
            if (response_code == SUCCESS) {
                Json json = new Json();
                message = message.replace("\\\"", "\"");
                message = message.replace("\\", "");
                
                t = json.fromJson(Track.class, message);

            } else if (response_code == FAILURE) {
                throw new Exception("Failure: " + response_code + " | " + message);
            } else {
                throw new Exception("Unknown Response");
            }
        } catch (Exception ex) {
            Logger.getLogger(MusicDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return t;
    }
    
    public ArrayList<Genre> getGenres(){
        ArrayList<Genre> genres = new ArrayList<Genre>();
        
        List<NameValuePair> parameters = new ArrayList<NameValuePair>(1);
        parameters.add(new BasicNameValuePair("getGenres", "true"));
        parameters.add(new BasicNameValuePair("key", key));

        String response = sendPost(parameters);
        
        try{
            
            JsonValue root = new JsonReader().parse(response);
            int response_code = root.getInt("response_code", 0);
            String message = root.getString("message", "No Message Found");
        
        
            if (response_code == SUCCESS) {
                Json json = new Json();
                message = message.replace("\\\"", "\"");
                message = message.replace("\\", "");
                
                Results res;
                res = json.fromJson(Results.class, message);
                
                genres = res.getResults();
                
            } else if (response_code == FAILURE) {
                throw new Exception("Failure: " + response_code);
            } else {
                throw new Exception("Unknown Response");
            }
        } catch (Exception ex) {
            Logger.getLogger(MusicDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return genres;
    }
    
    public boolean updateUser(List<NameValuePair> parameters){
        if(parameters.size() < USER_SETTING_NUMBER){
            System.out.println("ERROR: All user arguments must be supplied.");
            return false;
        }
        
        parameters.add(new BasicNameValuePair("updateUser", "true"));
        parameters.add(new BasicNameValuePair("key", key));
        
        String response = sendPost(parameters);
        
        try{
            
            JsonValue root = new JsonReader().parse(response);
            int response_code = root.getInt("response_code", 0);
            String message = root.getString("message", "No Message Found");
        
            if (response_code == SUCCESS) {
                 System.out.println("User Updated.");
                return true;
            } else if (response_code == FAILURE) {
                throw new Exception("Failure: " + response_code + " | " + message);
            } else {
                throw new Exception("Unknown Response");
            }
           
        } catch (Exception ex) {
            Logger.getLogger(MusicDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }

    private String sendPost(List<NameValuePair> parameters) {
        String res_body = "";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(base_url);
        try {
            post.setEntity(new UrlEncodedFormEntity(parameters));

            HttpResponse response = client.execute(post);
            int code = response.getStatusLine().getStatusCode();
            if (code < 200 && code >= 300) {
                throw new Exception("Problem with request: " + code);
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";

            while ((line = rd.readLine()) != null) {
                res_body += line.trim();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(MusicDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res_body;

    }

    public User getUserByID(String id) {
        User user = new User();

        List<NameValuePair> parameters = new ArrayList<NameValuePair>(1);
        parameters.add(new BasicNameValuePair("getUserByID", "true"));
        parameters.add(new BasicNameValuePair("key", key));
        parameters.add(new BasicNameValuePair("uid", id));

        String response = sendPost(parameters);
        
        try{
            
            JsonValue root = new JsonReader().parse(response);
            int response_code = root.getInt("response_code", 0);
            String message = root.getString("message", "No Message Found");
        
        
            if (response_code == SUCCESS) {
                Json json = new Json();
                message = message.replace("\\\"", "\"");
                message = message.replace("\\", "");
                
                user = json.fromJson(User.class, message);

            } else if (response_code == FAILURE) {
                throw new Exception("Failure: " + response_code + " | " + message);
            } else {
                throw new Exception("Unknown Response");
            }
        } catch (Exception ex) {
            Logger.getLogger(MusicDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return user;
    }
}