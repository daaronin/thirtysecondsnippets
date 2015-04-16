package audio;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

class PlayerThread extends Thread implements Disposable{

   private static final int FRAMES_READ = 2;
   
   private AudioPlayer owner;
   private Array<Float> samples;
   
   private boolean playing = false;
   private boolean audioEnded = false;
   
   public PlayerThread(AudioPlayer owner){
      this.owner = owner;
      this.samples = new Array<Float>();
   }
   
   
   public boolean isPlaying() {
      return playing;
   }

   public void setPlaying(boolean playing) {
      this.playing = playing;
   }

   public boolean hasAudioEnded() {
      return audioEnded;
   }

   
   @Override
   public void start(){
      playing = true;
      super.start();
   }
   
   @Override
   public void run() {
      while(!audioEnded){
         if(playing){
            owner.decoder.readFrames(samples, PlayerThread.FRAMES_READ);
            
            if(samples.size == 0){
               audioEnded = true;
               playing = false;
            }
            else{

               float[] buffer = new float[samples.size];
               for(int n = 0; n < buffer.length; n++){
                  buffer[n] = samples.get(n);

                                                // TODO: Analyze Samples.
               }
               
                                        // TODO: Analyze Buffer.

               owner.device.writeSamples(buffer, 0, buffer.length);
            }
         }
      }
   }


   @Override
   public void dispose() {
      audioEnded = true;
      try {
         this.join();
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }

}