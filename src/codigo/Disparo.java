/*
 * Autor: Javier de la Llave
 */
package codigo;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Javier
 */
public class Disparo {
    
    Image imagen = null;
    public int posX = 0;
    public int posY = 0;
    
    
    public Disparo(){
        try {
            imagen = ImageIO.read(getClass().getResource("/imagenes/disparo.png"));

        } catch (IOException e) {
        }
    }
    
    public void mueve(){
        posY--;
    }


}
