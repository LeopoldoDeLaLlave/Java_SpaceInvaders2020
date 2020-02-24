/*
 * Es el enemigo del juego
 */
package codigo;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Javier de la Llave
 */
public class Marciano {
    
    //Dos imagenes para crear el efecto de animación
    public Image imagen1 = null;
    public Image imagen2 = null;
    
    //Posición en la que se encuentra el marciano
    public int posX=0;
    public int posY=0;
    
    //Volocidad a la que se desplaza lateralmente el marciano
    public int velocidad = 1;
    
    
    private int anchoPantalla;
    
    //Lo utilizamos para la animación
    public int vida = 50;
    
    //Desplaza al marciano lateralmente por la pantalla
    public void mueve(boolean direccion){
        if(direccion){
            posX-=velocidad;
        }else{
            posX+=velocidad;
        }

    }
    
    public Marciano(int _anchoPantalla){
        anchoPantalla = _anchoPantalla;
        
    }
}
