/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gdp.thirtysecondsnippets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.net.HttpParametersUtils;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author George McDaid
 */
public class MusicDB {
    
    private HttpRequest request = new HttpRequest();
    private String base_url = "http://3ss.app:8000/musicdb.php";
    private String key = "9JmgB8pQANJwsuH7";
    
    private static int SUCCESS = 7;
    private static int FAILURE = 6;
    
    public MusicDB(){
    
    }
    
    public Track getTrackByGenre(String genre){
        Track t = new Track();
        
        request.reset();
        request.setMethod(HttpMethods.POST);
        request.setUrl(base_url);
        
        Map parameters = new HashMap();
        parameters.put("getTrackByGenre", "true");
        parameters.put("key", key);
        parameters.put("genre", genre);
        
        request.setContent(HttpParametersUtils.convertHttpParameters(parameters));
        
        Gdx.net.sendHttpRequest (request, new HttpResponseListener() {
            @Override
            public void handleHttpResponse(HttpResponse httpResponse) {
                JsonValue root = new JsonReader().parse(httpResponse.getResultAsString());
                int response_code = root.getInt("response_code", 0);
                String response = root.getString("message", "No Message Found");
               
                HttpStatus status = httpResponse.getStatus();
                if (status.getStatusCode() >= 200 && status.getStatusCode() < 300) {
                    if(response_code == SUCCESS){
                        System.out.println(response_code + ": " + response);
                    }else if(response_code == FAILURE){
                        failed(new Throwable("Failure: " + response));
                    }else{
                        failed(new Throwable("Unknown Response"));
                    }
                }else{
                    failed(new Throwable("HTTP Status " + status.getStatusCode()));
                }
            }

            @Override
            public void failed(Throwable t) {
                    System.out.println(t.getMessage());
            }

            @Override
            public void cancelled() {
                System.out.println("R: Cancel");
            }
        });
        
        return t;
    }
}
