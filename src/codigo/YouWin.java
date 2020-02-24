/*
 * Pone el sonido cuando se gana
 */
package codigo;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author Javier
 */
public class YouWin {

    Clip sonidoWin = null;

    public void ruido() {
        try {
            sonidoWin = AudioSystem.getClip();
            sonidoWin.open(AudioSystem.getAudioInputStream(getClass().getResource("/sonido/win.wav")));
        } catch (Exception ex) {
        }

    }
}
