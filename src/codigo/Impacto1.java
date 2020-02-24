/*
 * Pone el sonido cuando impactan rayo y diparo o rayo y nave
 */
package codigo;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author Javier
 */
public class Impacto1 {
        Clip sonidoExplosion = null;
    
    public  void ruido(){
        try {
            sonidoExplosion = AudioSystem.getClip();
            sonidoExplosion.open(AudioSystem.getAudioInputStream(getClass().getResource("/sonido/golpe1.wav")));
        } catch (Exception ex) {
        }
        
        
    }
}
