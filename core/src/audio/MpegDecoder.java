package audio;

import java.io.IOException;
import java.io.InputStream;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.DecoderException;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.SampleBuffer;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class MpegDecoder implements Disposable{
   
   // Decoding
   private InputStream stream;
   private Bitstream bitStream;
   private Decoder decoder;
   
   // Sample info
   private int sampleRate;
   private int channelCount;
   
   /**
    * Create a decoder from a stream.
    * @param stream
    * @throws BitstreamException 
    */
   public MpegDecoder(InputStream stream) throws BitstreamException{
      this.stream = stream;
      this.bitStream = new Bitstream(stream);
      this.decoder = new Decoder();
      
      Header frameHeader = bitStream.readFrame();
      if(frameHeader == null){
         throw new IllegalArgumentException("Empty mpeg.");
      }
      
      this.sampleRate = frameHeader.frequency();
      this.channelCount = frameHeader.mode() == Header.SINGLE_CHANNEL ? 1 : 2;
      
      bitStream.closeFrame();
      bitStream.unreadFrame();
   }
   
   /**
    * Create a decoder from a file.
    * @param file
    * @throws BitstreamException 
    */
   public MpegDecoder(FileHandle file) throws BitstreamException{
      this(file.read());
   }

   /**
    * @return sample rate in hz (samples / second).
    */
   public int getSampleRate(){
      return sampleRate;
   }
   
   /**
    * @return the number of channels.
    */
   public int getChannelCount(){
      return channelCount;
   }
   
   /**
    * @param frames the amount of frames that will be read.
    * @return samples from the read frames.
    */
   public void readFrames(Array<Float> samples, int frames){
      try {
         samples.clear();
         
         for(int i = 0; i < frames; i++){
            Header frameHeader = bitStream.readFrame();
            if(frameHeader == null){
               break;
            }
            else{
               SampleBuffer buffer = (SampleBuffer) decoder.decodeFrame(frameHeader, bitStream);
               bitStream.closeFrame();
               
               short[] sampleBuffer = buffer.getBuffer();
               for(int n = 0; n < buffer.getBufferLength(); n += 1){
                  samples.add((float) sampleBuffer[n] / Short.MAX_VALUE);
               }
            }
         }
      } catch (BitstreamException e) {
         e.printStackTrace();
      } catch (DecoderException e) {
         e.printStackTrace();
      }
   }

   @Override
   public void dispose() {
      try {
         bitStream.close();
         stream.close();
      } catch (BitstreamException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
   
}