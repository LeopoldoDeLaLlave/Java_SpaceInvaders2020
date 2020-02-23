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
