/*
 * Barreras que protegen a la nave
 */
package codigo;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Javier
 */
public class Barrera {
    Image imagen = null;
    
    int posX=0;
    int posY=0;
    
    public Barrera(){
        try {
            imagen = ImageIO.read(getClass().getResource("/imagenes/barrera1.png"));

        } catch (IOException e) {
        }
    }
}
