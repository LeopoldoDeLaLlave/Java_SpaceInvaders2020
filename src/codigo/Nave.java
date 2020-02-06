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
public class Nave {
    
    Image imagen = null;
    public int posX = 0;
    public int posY = 0;
    
    private boolean pulsadoIzquierda = false;
    private boolean pulsadoDerecha = false;
    
    public Nave(){
        try {
            imagen = ImageIO.read(getClass().getResource("/imagenes/nave.png"));

        } catch (IOException e) {
        }
    }
    
    public void mueve(){
        if(pulsadoIzquierda){
            posX--;
        }
        if(pulsadoDerecha){
            posX++;
        }
    }

    public boolean isPulsadoIzquierda() {
        return pulsadoIzquierda;
    }

    public void setPulsadoIzquierda(boolean pulsadoIzquierda) {

        this.pulsadoIzquierda = pulsadoIzquierda;
        this.pulsadoDerecha = false;
    }

    public boolean isPulsadoDerecha() {
        return pulsadoDerecha;
    }

    public void setPulsadoDerecha(boolean pulsadoDerecha) {
        this.pulsadoDerecha = pulsadoDerecha;
        this.pulsadoIzquierda = false;
    }
}
