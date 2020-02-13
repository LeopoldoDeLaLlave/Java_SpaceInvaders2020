/*
 * Autor: Javier de la Llave
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
public class Disparo {
    
    Image imagen = null;
    public int posX = 0;
    public int posY = 0;
    
    Clip sonidoDisparo = null;
    
    
    public Disparo(){
        try {
            imagen = ImageIO.read(getClass().getResource("/imagenes/disparo.png"));

        } catch (IOException e) {
        }
    }
    
 
    
    public void mueve(){
        posY-=9;
    }
    
    public void posicionDisparo(Nave _nave){
        posX = _nave.posX+_nave.imagen.getWidth(null)/2 - imagen.getWidth(null)/2;
        
        posY = _nave.posY-_nave.imagen.getHeight(null)/2+6 ;
        
        try {
            sonidoDisparo = AudioSystem.getClip();
            sonidoDisparo.open(AudioSystem.getAudioInputStream(getClass().getResource("/sonido/laser.wav")));
        } catch (Exception ex) {
        }
    }


}
