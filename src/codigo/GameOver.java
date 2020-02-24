/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
