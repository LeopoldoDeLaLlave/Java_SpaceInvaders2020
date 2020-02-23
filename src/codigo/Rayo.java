/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author Javier
 */
public class Rayo {

    Image imagen = null;
    public int posX = 0;
    public int posY = 0;

    Clip sonidoRayo = null;

    public void ruido() {
        try {
            sonidoRayo = AudioSystem.getClip();
            sonidoRayo.open(AudioSystem.getAudioInputStream(getClass().getResource("/sonido/rayo.wav")));
        } catch (Exception ex) {
            System.out.println("error");
        }
    }

    public void mueve() {
        posY += 1;

    }

}
