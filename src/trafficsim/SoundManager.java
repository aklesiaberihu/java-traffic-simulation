package trafficsim;

import javax.sound.sampled.*;
import java.io.IOException; 
public final class SoundManager { 
    
    public void playSound(String resourcePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException { 
        playSound(resourcePath, 0.0f);  
    } 
 
    public void playSound(String resourcePath, float volume) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(resourcePath)); 
        Clip clip = AudioSystem.getClip(); 
        clip.open(audioIn); 

        
        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gain.setValue(volume);  
        }

        clip.start(); 
    }
}

