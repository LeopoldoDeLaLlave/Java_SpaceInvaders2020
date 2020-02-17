/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    
    public Image imagen1 = null;
    public Image imagen2 = null;
    
    public int posX=0;
    public int posY=0;
    
    
    private int anchoPantalla;
    
    public int vida = 50;
    
    public void mueve(boolean direccion){
        if(direccion){
            posX--;
        }else{
            posX++;
        }

    }
    
    public Marciano(int _anchoPantalla){
        anchoPantalla = _anchoPantalla;
        
    }
}
