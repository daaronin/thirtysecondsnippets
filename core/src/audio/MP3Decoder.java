package audio;

import com.badlogic.gdx.audio.analysis.AudioTools;
import com.badlogic.gdx.audio.io.Mpg123Decoder;
import com.badlogic.gdx.files.FileHandle;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Another mp3 decoder. I got a suspicion that the other one sucks a bit :/
 * @author mzechner
 *
 */
public class MP3Decoder implements Decoder
{				
	Mpg123Decoder in;
	FloatBuffer buffer;
	byte[] bytes;
        int sampleWindowSize;
	
	public MP3Decoder( FileHandle file , int sampleWindowSize) throws Exception
	{
		Mpg123Decoder decoder = new Mpg123Decoder(file);
                System.out.println(decoder.getRate());
                System.out.println(decoder.getChannels());
		this.in = decoder;
                this.sampleWindowSize = sampleWindowSize;
	}

	@Override
	public int readSamples(float[] samples) 
	{
                short[] s_samples = new short[this.sampleWindowSize * this.in.getChannels()];
                int i = in.readSamples(s_samples, 0, sampleWindowSize * this.in.getChannels());
                //ShortBuffer new_short = AudioTools.allocateShortBuffer(1024, 1);
                //AudioTools.convertToMonoShort(shorts, new_short, 1024);
                //short[] s_samples_new = new_short.array();
                for(int j = 0;j<s_samples.length;j+=2){
                    if(this.in.getChannels() == 2){
                        samples[j/2] = ((float)s_samples[j] + (float)s_samples[j+1]) / 2;
                    }else{
                        samples[j] = (float)s_samples[j];
                    }
                    //samples[j] = (float)s_samples[j];
                    //System.out.println(samples[j/2]);
                }
               
                
                return i;
	}

 
	
}
