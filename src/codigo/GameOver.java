/*
 * Pone el sonido cuando se pierde
 */
package codigo;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author Javier
 */
public class GameOver {

    Clip sonidoGO = null;

    public void ruido() {
        try {
            sonidoGO = AudioSystem.getClip();
            sonidoGO.open(AudioSystem.getAudioInputStream(getClass().getResource("/sonido/go.wav")));
        } catch (Exception ex) {
        }

    }
}
