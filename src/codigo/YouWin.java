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
