/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.Timer;

/**
 *
 * @author Javier de la Llave
 */
public class VentanaJuego extends javax.swing.JFrame {

    static int ANCHOPANTALLA = 800;
    static int ALTOPANTALLA = 600;

    int filasMarcianos = 5;
    int columnasMarcianos = 10;
    int contador = 0;
    
    int posYMar = 0;
    BufferedImage buffer = null;

    //Bucle de animación del juego
    //en este caso, es un hilo de ejecución nuevo
    //que se encarga de resfrescar el contenido de la pantalla
    Timer temporizador = new Timer(10, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            //TODO: código de animación
            bucleDelJuego();
        }
    });
    
    
    Marciano miMarciano = new Marciano(ANCHOPANTALLA);
    Marciano[][] arrayMarcianos=  new Marciano[filasMarcianos][columnasMarcianos];
    boolean direccionMarciano = false; //Si es false ira a la derecha
    
    
    
    Nave miNave = new Nave();
    Disparo miDisparo = new Disparo();
    

    

    /**
     * Creates new form VentanaJuego
     */
    public VentanaJuego() {
        initComponents();
        setSize(ANCHOPANTALLA, ALTOPANTALLA);
        buffer = (BufferedImage) jPanel1.createImage(ANCHOPANTALLA, ALTOPANTALLA);
        buffer.createGraphics();
        
        
        miNave.posX = ANCHOPANTALLA/2 - miNave.imagen.getWidth(this)/2;
        miNave.posY = ALTOPANTALLA - 100;
        
        
        
        for (int i = 0; i < filasMarcianos; i++) {
            for (int j = 0; j < columnasMarcianos; j++) {
                arrayMarcianos[i][j]=new Marciano(ANCHOPANTALLA);
                arrayMarcianos[i][j].posX= j*(15+arrayMarcianos[i][j].imagen1.getWidth(null));
                arrayMarcianos[i][j].posY= i*(10+arrayMarcianos[i][j].imagen1.getHeight(null));
            }
        }
        miDisparo.posY = -2000;
        
        //Arranco el temporizador para que empiece el juego
        temporizador.start();
    }
    
    private void pintaMarcianos(Graphics2D _g2){
        for (int i = 0; i < filasMarcianos; i++) {
            for (int j = 0; j < columnasMarcianos; j++) {
                
                arrayMarcianos[i][j].mueve(direccionMarciano);
                if(arrayMarcianos[i][j].posX>= ANCHOPANTALLA-arrayMarcianos[i][j].imagen1.getWidth(null)+5 || 
                        arrayMarcianos[i][j].posX<= 0 ){
                    direccionMarciano = !direccionMarciano;
                    //posYMar +=10;//Hago que los marcianos salten, habria que sumarla al drawimage
                    //Hago que los marcianos salten
                    for (int k = 0; k < filasMarcianos; k++) {
                        for (int m = 0; m < columnasMarcianos; m++) {
                            arrayMarcianos[k][m].posY +=arrayMarcianos[k][m].imagen1.getHeight(null);
                        }
                    }
                    
                }
                
            }
        }
        
        for (int i = 0; i < filasMarcianos; i++) {
            for (int j = 0; j < columnasMarcianos; j++) {
                if(contador<50){
                    _g2.drawImage(arrayMarcianos[i][j].imagen1, arrayMarcianos[i][j].posX, arrayMarcianos[i][j].posY, null);
                }else if(contador<100){
                    _g2.drawImage(arrayMarcianos[i][j].imagen2, arrayMarcianos[i][j].posX, arrayMarcianos[i][j].posY, null);
                }else{
                    contador = 0;
                }
                
            }
            }
        
    }

    //Chequea si un disparo y un marciano colisionan
    private void chequeaColision(){
        Rectangle2D.Double rectanguloMarciano = new Rectangle2D.Double();
        Rectangle2D.Double rectanguloDisparo = new Rectangle2D.Double();
        
        //Calculo el rectangulo del disparo
        rectanguloDisparo.setFrame(miDisparo.posX, miDisparo.posY, miDisparo.imagen.getWidth(null), miDisparo.imagen.getHeight(null));
        
        
        for (int i = 0; i < filasMarcianos; i++) {
            for (int j = 0; j < columnasMarcianos; j++) {
                
                //Calculo el rectángulo correspondiente al marciano que estoy comprobando
                rectanguloMarciano.setFrame(arrayMarcianos[i][j].posX, arrayMarcianos[i][j].posY, arrayMarcianos[i][j].imagen1.getWidth(null), arrayMarcianos[i][j].imagen1.getHeight(null));
                
                if(rectanguloDisparo.intersects(rectanguloMarciano)){//Si entra aquí es porque han chocado
                arrayMarcianos[i][j].posY = 2000; 
                miDisparo.posY= -2000;
                }
            }
        }
    }
    
    private void bucleDelJuego() {
        //este método gobierna el redibujado de los objetos en el jpanel1
        
        //primero borro todo en el buffer
        Graphics2D g2 = (Graphics2D) buffer.getGraphics();
        
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, ANCHOPANTALLA, ALTOPANTALLA);
        
        
        contador++;
        
        /////////////////////////////////
        pintaMarcianos(g2);
        
        //Dibujo la nave
        g2.drawImage(miNave.imagen, miNave.posX, miNave.posY, null);
        g2.drawImage(miDisparo.imagen, miDisparo.posX, miDisparo.posY, null);
        miNave.mueve();
        miDisparo.mueve();
        chequeaColision();

        /////////////////////////////////
        
        //dibujo de golpe todo el buffer sobre el jPanel1
        g2 = (Graphics2D) jPanel1.getGraphics();
        g2.drawImage(buffer, 0, 0, this);
        
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 645, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 395, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        switch(evt.getKeyCode()){
            case KeyEvent.VK_RIGHT:
                miNave.setPulsadoDerecha(true);
                break;
                
                               
            case KeyEvent.VK_LEFT:
                miNave.setPulsadoIzquierda(true);
                break;
                
            case KeyEvent.VK_SPACE: 
                miDisparo.posicionDisparo(miNave);
                break;

        }
    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        switch(evt.getKeyCode()){
            case KeyEvent.VK_RIGHT:
                miNave.setPulsadoDerecha(false);
                break;
                
                               
            case KeyEvent.VK_LEFT:
                miNave.setPulsadoIzquierda(false);
                break;
        }
    }//GEN-LAST:event_formKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaJuego().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
