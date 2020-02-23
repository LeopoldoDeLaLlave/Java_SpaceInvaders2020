/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.Timer;

/**
 *
 * @author Javier de la Llave
 */
public class VentanaJuego extends javax.swing.JFrame {

    static int ANCHOPANTALLA = 800;
    static int ALTOPANTALLA = 600;
    int filasMarcianos = 5;
    int columnasMarcianos = 14;
    int contador = 0;

    Random r = new Random();
    int num_random = 0;//Para sortear los disparos

    int posYMar = 0;//Guardamos la distancia que hay que sumarle a la posY de los marcianos
    BufferedImage buffer = null;
    //Buffer para guardar las imágenes de todos los marcianos
    BufferedImage plantilla = null;
    Image[] imagenes = new Image[30];

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

    /*Marciano miMarciano = new Marciano(ANCHOPANTALLA);
    Marciano[][] arrayMarcianos = new Marciano[filasMarcianos][columnasMarcianos];*/
    ArrayList<Marciano> listaMarcianos = new ArrayList();
    //int contadorMarcianos = 0;//Lo utilizamos como inice de la lista de marcianos

    int totalMarcianos = filasMarcianos * columnasMarcianos; //Indica cuantos marcianos hay en un inicio

    int velocidadMarcianos = 1; //Velocidad a la que se desplazan los marcianos

    boolean direccionMarciano = false; //Si es false ira a la derecha

    Nave miNave = new Nave();

    boolean cambiarDir = false;//En el momento que un marciano toque la pared se vuelve true y cambia la dirección

    ArrayList<Disparo> listaDisparos = new ArrayList();

    ArrayList<Explosion> listaExplosiones = new ArrayList();

    ArrayList<Rayo> listaRayos = new ArrayList();

    /**
     * Creates new form VentanaJuego
     */
    public VentanaJuego() {
        initComponents();

        try {
            plantilla = ImageIO.read(getClass().getResource("/imagenes/invaders2.png"));
        } catch (IOException ex) {

        }

        //Cargo las 30 imágenes del spritesheet en el array de bufferedimages
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                imagenes[i * 4 + j] = plantilla.getSubimage(j * 64, i * 64, 64, 64).getScaledInstance(32, 32, Image.SCALE_SMOOTH);

            }
        }

        imagenes[20] = plantilla.getSubimage(0, 320, 66, 32);//Sprite de la nave
        imagenes[21] = plantilla.getSubimage(66, 320, 64, 32);
        //imagenes[22] = plantilla.getSubimage(130, 320, 64, 32);//Explosión parte B
        imagenes[23] = plantilla.getSubimage(194, 320, 64, 32);//Explosión parte A
        imagenes[24] = plantilla.getSubimage(256, 128, 32, 32);//Rayo del marciano

        setSize(ANCHOPANTALLA, ALTOPANTALLA);
        buffer = (BufferedImage) jPanel1.createImage(ANCHOPANTALLA, ALTOPANTALLA);
        buffer.createGraphics();

        miNave.imagen = imagenes[21];

        miNave.posX = ANCHOPANTALLA / 2 - miNave.imagen.getWidth(this) / 2;
        miNave.posY = ALTOPANTALLA - 100;

        //Cramos un marciano con sus imagenes y posicion y lo añadimos a la lista
        for (int i = 0; i < filasMarcianos; i++) {
            for (int j = 0; j < columnasMarcianos; j++) {
                Marciano m = new Marciano(ANCHOPANTALLA);
                m.imagen1 = imagenes[2 * i];
                m.imagen2 = imagenes[2 * i + 1];
                m.posX = j * (10 + m.imagen1.getWidth(null));
                m.posY = i * (10 + m.imagen1.getHeight(null));
                listaMarcianos.add(m);
            }
        }

        //Arranco el temporizador para que empiece el juego
        temporizador.start();

    }

    private void pintaMarcianos(Graphics2D _g2) {

        //VAríamos la velocidad de los marcianos en función de los que quedan
        if (listaMarcianos.size() == 1) {//Si queda un marciano
            velocidadMarcianos = 8;
        } else if (listaMarcianos.size() >= 40) {//Si quedan menos de 40 marcianos
            velocidadMarcianos = 1;
        } else if (listaMarcianos.size() >= 13) {//Si quedan menos de 13 marcianos
            velocidadMarcianos = 2;
        } else {//Si quedan más de dos tercios de marcianos
            velocidadMarcianos = 3;
        }

        //Establecemos las posiciones de los marcianos
        for (int i = 0; i < listaMarcianos.size(); i++) {

            listaMarcianos.get(i).velocidad = velocidadMarcianos;
            listaMarcianos.get(i).mueve(direccionMarciano);
            if (listaMarcianos.get(i).posX >= ANCHOPANTALLA - listaMarcianos.get(i).imagen1.getWidth(null) + 5
                    || listaMarcianos.get(i).posX <= 0) {//Si un marciano llega al final de la pantalla
                cambiarDir = true;
            }

            //Sorteo si el mariano dispara o no
            num_random = r.nextInt(23000);
            if (num_random == 12) {
                Rayo rayo1 = new Rayo();
                rayo1.posX = listaMarcianos.get(i).posX + 32;
                rayo1.posY = listaMarcianos.get(i).posY + posYMar;
                rayo1.imagen = imagenes[24];
                listaRayos.add(rayo1);
                rayo1.ruido();
                rayo1.sonidoRayo.start();
            }
        }

        if (cambiarDir) {
            direccionMarciano = !direccionMarciano;
            posYMar += 10;//Hago que los marcianos salten

        }

        cambiarDir = false;

        //Cambiamos los marcianos
        for (int i = 0; i < listaMarcianos.size(); i++) {
            if (contador < 50) {
                _g2.drawImage(listaMarcianos.get(i).imagen1, listaMarcianos.get(i).posX, listaMarcianos.get(i).posY + posYMar, null);
            } else if (contador < 100) {
                _g2.drawImage(listaMarcianos.get(i).imagen2, listaMarcianos.get(i).posX, listaMarcianos.get(i).posY + posYMar, null);
            } else {
                contador = 0;
            }
        }

    }

    //Pinta todos los disparos
    private void pintaDisparos(Graphics2D g2) {

        Disparo disparoAux;

        for (int i = 0; i < listaDisparos.size(); i++) {
            disparoAux = listaDisparos.get(i);
            disparoAux.mueve();
            if (disparoAux.posY < 0) {
                listaDisparos.remove(i);
            } else {
                g2.drawImage(disparoAux.imagen, disparoAux.posX, disparoAux.posY, null);
            }
        }

    }

    //Pinta todos los rayos
    private void pintaRayos(Graphics2D g2) {

        Rayo rayoAux;

        for (int i = 0; i < listaRayos.size(); i++) {

            rayoAux = listaRayos.get(i);
            rayoAux.mueve();
            if (rayoAux.posY > ALTOPANTALLA) {

                listaRayos.remove(i);
            } else {
                g2.drawImage(rayoAux.imagen, rayoAux.posX, rayoAux.posY, null);
            }

        }

    }

    //Pinta las explosiones
    private void pintaExplosiones(Graphics2D g2) {

        Explosion explosionAux;

        for (int i = 0; i < listaExplosiones.size(); i++) {
            explosionAux = listaExplosiones.get(i);

            explosionAux.tiempoDeVida--;
            if (explosionAux.tiempoDeVida < 50) {
                g2.drawImage(explosionAux.imagen1, explosionAux.posX, explosionAux.posY, null);
            }
            if (explosionAux.tiempoDeVida <= 0) {
                listaExplosiones.remove(i);
            }
        }
    }

    //Chequea si un disparo y un marciano colisionan
    private void chequeaColisionMarDis() {
        Rectangle2D.Double rectanguloMarciano = new Rectangle2D.Double();
        Rectangle2D.Double rectanguloDisparo = new Rectangle2D.Double();

        for (int k = 0; k < listaDisparos.size(); k++) {

            //Calculo el rectangulo del disparo
            rectanguloDisparo.setFrame(listaDisparos.get(k).posX, listaDisparos.get(k).posY, listaDisparos.get(k).imagen.getWidth(null), listaDisparos.get(k).imagen.getHeight(null));
            for (int i = 0; i < listaMarcianos.size(); i++) {

                //Calculo el rectángulo correspondiente al marciano que estoy comprobando
                rectanguloMarciano.setFrame(listaMarcianos.get(i).posX, listaMarcianos.get(i).posY + posYMar, listaMarcianos.get(i).imagen1.getWidth(null), listaMarcianos.get(i).imagen1.getHeight(null));

                if (rectanguloDisparo.intersects(rectanguloMarciano)) {//Si entra aquí es porque han chocado
                    try {
                        Explosion e = new Explosion();
                        e.posX = listaMarcianos.get(i).posX;
                        e.posY = listaMarcianos.get(i).posY + posYMar;
                        e.imagen1 = imagenes[23];
                        e.imagen2 = imagenes[22];
                        e.imagen3 = imagenes[24];
                        listaExplosiones.add(e);
                        e.sonidoExplosion.start();//Suena el sonido
                        listaMarcianos.remove(i);
                        listaDisparos.remove(k);
                    } catch (Exception e) {
                        System.out.println("fallo");
                    }

                }
            }

        }

    }
    
        //Chequea si un disparo y un rayo colisionan
    private void chequeaColisionRayoDis() {
        Rectangle2D.Double rectanguloRayo = new Rectangle2D.Double();
        Rectangle2D.Double rectanguloDisparo = new Rectangle2D.Double();

        for (int k = 0; k < listaDisparos.size(); k++) {

            //Calculo el rectangulo del disparo
            rectanguloDisparo.setFrame(listaDisparos.get(k).posX, listaDisparos.get(k).posY, listaDisparos.get(k).imagen.getWidth(null), listaDisparos.get(k).imagen.getHeight(null));
            for (int i = 0; i < listaRayos.size(); i++) {

                //Calculo el rectángulo correspondiente al marciano que estoy comprobando
                rectanguloRayo.setFrame(listaRayos.get(i).posX, listaRayos.get(i).posY, listaRayos.get(i).imagen.getWidth(null), listaRayos.get(i).imagen.getHeight(null));

                if (rectanguloDisparo.intersects(rectanguloRayo)) {//Si entra aquí es porque han chocado
                    try {

                        listaRayos.remove(i);
                        listaDisparos.remove(k);
                    } catch (Exception e) {
                        System.out.println("fallo");
                    }

                }
            }

        }

    }

    //Chequea si la nave y el rayo colisionan
    private void chequeaColisionNaveRayo() {
        Rectangle2D.Double rectanguloNave = new Rectangle2D.Double();
        Rectangle2D.Double rectanguloRayo = new Rectangle2D.Double();

        for (int k = 0; k < listaRayos.size(); k++) { 
            System.out.println(listaRayos.size()+" "+k);
            //Calculo el rectangulo del rayo
            rectanguloRayo.setFrame(listaRayos.get(k).posX, listaRayos.get(k).posY, listaRayos.get(k).imagen.getWidth(null), listaRayos.get(k).imagen.getHeight(null));
            //Calculo el rectángulo de la nave
            rectanguloNave.setFrame(miNave.posX, miNave.posY, miNave.imagen.getWidth(null), miNave.imagen.getHeight(null));

            if (rectanguloNave.intersects(rectanguloRayo)) {//Si entra aquí es porque han chocado
                listaRayos.remove(k);

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
        pintaRayos(g2);
        //Dibujo la nave
        g2.drawImage(miNave.imagen, miNave.posX, miNave.posY, null);
        pintaDisparos(g2);
        pintaExplosiones(g2);
        miNave.mueve();
        chequeaColisionMarDis();
        chequeaColisionNaveRayo();
        chequeaColisionRayoDis();

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
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                miNave.setPulsadoDerecha(true);
                break;

            case KeyEvent.VK_LEFT:

                miNave.setPulsadoIzquierda(true);
                break;

            case KeyEvent.VK_SPACE:
                if (listaDisparos.size() < 2) {//Para que no se pueda disparar a lo loco
                    Disparo d = new Disparo();

                    d.posicionDisparo(miNave);

                    d.sonidoDisparo.start();//Suena el sonido

                    //agregamos el disparo a la lista de disparos
                    listaDisparos.add(d);
                }

                break;

        }
    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                miNave.setPulsadoIzquierda(false);
                break;

            case KeyEvent.VK_LEFT:
                miNave.setPulsadoDerecha(false);
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
