/*
 * Efecto sonido para cuando se rompe la barrera
*/
package codigo;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author Javier
 */
public class Crash {
    Clip sonidoCrash = null;
    
    public  void ruido(){
        try {
            sonidoCrash= AudioSystem.getClip();
            sonidoCrash.open(AudioSystem.getAudioInputStream(getClass().getResource("/sonido/crash.wav")));
        } catch (Exception ex) {
        }
        
        
    }
}
