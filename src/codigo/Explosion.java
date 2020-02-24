/*
 * Animación y sonido cuando muere un marciano
 */
package codigo;

import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

/**
 *
 * @author Javier
 */
public class Explosion {
    Image imagen1 = null;

    //Posición de la explosión
    public int posX = 0;
    public int posY = 0;
    
    //Tiempo que está en pantalla la explosión
    public int tiempoDeVida = 15;
    
    Clip sonidoExplosion = null;
    
    //Al producirse la explosión hay un ruído
    public  Explosion(){
        try {
            sonidoExplosion = AudioSystem.getClip();
            sonidoExplosion.open(AudioSystem.getAudioInputStream(getClass().getResource("/sonido/explosion.wav")));
        } catch (Exception ex) {
        }
        
        
    }
    
}
