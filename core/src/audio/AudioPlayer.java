package audio;

import javazoom.jl.decoder.BitstreamException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;

public class AudioPlayer implements Disposable {
   
   // Audio data.
   protected AudioDevice device;
   protected MpegDecoder decoder;
   
   // Player data.
   private PlayerThread thread;
   
   
   private AudioPlayer(){
   }
   
   /**
    * @return whether the audio has read through all the samples.
    */
   public boolean hasAudioEnded(){
      return thread.hasAudioEnded();
   }
   
   /**
    * Continue playing the audio.
    */
   public void play(){
      if(!thread.isAlive()){
         thread.start();
      }
      else{
         thread.setPlaying(true);
      }
   }
   
   /**
    * Pause playback of the audio.
    */
   public void pause(){
      thread.setPlaying(false);
   }
   
   /**
    * Create an audio player from a file.
    * @param file to create from.
    * @return
    */
   public static AudioPlayer createAudioPlayer(FileHandle file){
      AudioPlayer player = null;
      
      try {
         MpegDecoder decoder = new MpegDecoder(file);
         AudioDevice device = Gdx.audio.newAudioDevice(decoder.getSampleRate(), decoder.getChannelCount() == 1);
         
         if(device != null){
            player = new AudioPlayer();
            player.device = device;
            player.decoder = decoder;
            player.thread = new PlayerThread(player);
         }
      } catch (BitstreamException e) {
         e.printStackTrace();
      }
      
      return player;
   }

   @Override
   public void dispose() {
      if(thread != null){
         thread.dispose();
      }
      
      device.dispose();
      decoder.dispose();
   }

}