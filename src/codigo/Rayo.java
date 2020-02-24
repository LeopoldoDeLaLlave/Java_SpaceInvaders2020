/*
 *Ataque del marciano que baja hacia la nave
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
    
    //Posición del rayo
    public int posX = 0;
    public int posY = 0;

    Clip sonidoRayo = null;

    //Cuando la nave lanza sl rayo hace ruido
    public void ruido() {
        try {
            sonidoRayo = AudioSystem.getClip();
            sonidoRayo.open(AudioSystem.getAudioInputStream(getClass().getResource("/sonido/rayo.wav")));
        } catch (Exception ex) {
        }
    }

    //Desplaza el rayo hacia abajo
    public void mueve() {
        posY += 3;

    }

}
