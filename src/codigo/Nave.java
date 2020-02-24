/*
 * Esta nave la controla el usuario
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
    //Posición de la nave
    public int posX = 0;
    public int posY = 0;
    
    //Si recibe tres dispaos se acaba el juego
    int vidas = 3;
    
    //Nos indica la dirección en la que se desplaza la nave
    private boolean pulsadoIzquierda = false;
    private boolean pulsadoDerecha = false;
    
    /*public Nave(){

    }*/
    
    //Desplaza la nave lateralmente
    public void mueve(){
        if(pulsadoIzquierda && posX>0){
            posX-=3;
        }
        if(pulsadoDerecha && posX<VentanaJuego.ANCHOPANTALLA-(imagen.getWidth(null)+15)){
            posX+=3;
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
