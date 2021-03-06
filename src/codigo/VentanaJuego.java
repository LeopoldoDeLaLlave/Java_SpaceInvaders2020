/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Color;
import java.awt.Font;
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
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 *
 * @author Javier de la Llave
 */
public class VentanaJuego extends javax.swing.JFrame {

    static int ANCHOPANTALLA = 800;
    static int ALTOPANTALLA = 720;
    int filasMarcianos = 5;
    int columnasMarcianos = 14;
    int contador = 0;

    //Posición de la barreras
    int posYBarrera = 550;
    int posXBarrera = 50;

    boolean empezar = false; //Nos indica si el juego ha empezado
    boolean perdido = false; //Nos indica si los marcianos han llegado a la nave

    Random r = new Random();
    int num_random = 0;//Para sortear los disparos

    Impacto1 imp = new Impacto1();//Para el sonido de la colisiones
    Crash miCrash = new Crash();

    int posYMar = 0;//Guardamos la distancia que hay que sumarle a la posY de los marcianos
    BufferedImage buffer = null;
    //Buffer para guardar las imágenes de todos los marcianos
    BufferedImage plantilla = null;
    Image naveVida = null;//Representan las vidas
    Image go = null;//Imagen gameOver
    Image start = null;
    Image[] imagenes = new Image[30];

    int puntuacion = 0; //Guarda los puntos que lleva un jugador

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
    ArrayList<Barrera> listaBarreras = new ArrayList();//Guardo las barreras

    /**
     * Creates new form VentanaJuego
     */
    public VentanaJuego() {
        initComponents();
        empezarPartida();
    }

    //Prepara todos los elementos para empezar la partida
    public void empezarPartida() {
        //Cargamos la plantilla
        try {
            plantilla = ImageIO.read(getClass().getResource("/imagenes/invaders2.png"));
        } catch (IOException ex) {

        }

        //Guardo las imágenes que usaré
        imagenes[20] = plantilla.getSubimage(0, 320, 66, 32);//Sprite de la nave rival
        imagenes[21] = plantilla.getSubimage(66, 320, 64, 32);//Sprite de nuestra nave
        imagenes[23] = plantilla.getSubimage(194, 320, 64, 32);//Explosión parte A
        imagenes[24] = plantilla.getSubimage(256, 128, 32, 32);//Rayo del marciano

        //Cargo las 30 imágenes del spritesheet en el array de bufferedimages
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                imagenes[i * 4 + j] = plantilla.getSubimage(j * 64, i * 64, 64, 64).getScaledInstance(32, 32, Image.SCALE_SMOOTH);

            }
        }

        //Configuramos la pantalla
        setSize(ANCHOPANTALLA, ALTOPANTALLA);
        buffer = (BufferedImage) jPanel1.createImage(ANCHOPANTALLA, ALTOPANTALLA);
        buffer.createGraphics();

        //Dejo la nave lista
        miNave.imagen = imagenes[21];
        miNave.posX = ANCHOPANTALLA / 2 - miNave.imagen.getWidth(this) / 2;
        miNave.posY = ALTOPANTALLA - 100;

        //Dejamos los marcianos listos
        //Cramos un marciano con sus imagenes y posicion y lo añadimos a la lista
        for (int i = 0; i < filasMarcianos; i++) {
            for (int j = 0; j < columnasMarcianos; j++) {
                Marciano m = new Marciano(ANCHOPANTALLA);
                m.imagen1 = imagenes[2 * i];
                m.imagen2 = imagenes[2 * i + 1];
                m.posX = j * (10 + m.imagen1.getWidth(null)) + 5;
                m.posY = i * (10 + m.imagen1.getHeight(null)) + 120;
                listaMarcianos.add(m);
            }
        }

        llenarListaBarrera(posXBarrera, posYBarrera);
    }

    //Va dibujando los marcianos en la pantalla
    private void pintaMarcianos(Graphics2D _g2) {

        velocidadMarcianos();

        posicioMarcianos();

        if (cambiarDir) {
            direccionMarciano = !direccionMarciano;
            posYMar += 10;//Hago que los marcianos salten
        }

        cambiarDir = false;

        //Pintamos los marcianos
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

    //Establece posicion, velocidad y dirección de los marcianos
    private void posicioMarcianos() {
        for (int i = 0; i < listaMarcianos.size(); i++) {

            listaMarcianos.get(i).velocidad = velocidadMarcianos;
            listaMarcianos.get(i).mueve(direccionMarciano);
            if (listaMarcianos.get(i).posX >= ANCHOPANTALLA - (listaMarcianos.get(i).imagen1.getWidth(null) + 17)
                    || listaMarcianos.get(i).posX <= 0) {//Si un marciano llega al final de la pantalla cambia de sentido
                cambiarDir = true;
            }

            //Comprobamos si los marcianos han llegado a la nave, en caso de que lleguen se acaba el juego
            if ((listaMarcianos.get(i).posY + posYMar + listaMarcianos.get(i).imagen1.getHeight(null)) > miNave.posY) {
                perdido = true;
            }

            rayoMarcianos(i);
        }
    }

    //Los marcianos disparan aleatoriamente
    //varía la frecuencia en función de los marcianos que quedan
    private void rayoMarcianos(int i) {
        //Cuantos menos marcianos haya más dispararán, a no ser que solo quede uno
        if (listaMarcianos.size() == 1) {//Si queda un marciano
            velocidadMarcianos = 0;
        } else if (listaMarcianos.size() >= 40) {//Si quedan menos de 40 marcianos
            num_random = r.nextInt(7000);
        } else if (listaMarcianos.size() >= 13) {//Si quedan menos de 13 marcianos
            num_random = r.nextInt(3000);
        } else {//Si quedan más de dos tercios de marcianos
            num_random = r.nextInt(500);
        }

        if (num_random == 12) {//Si hay disparo
            Rayo rayo1 = new Rayo();
            //LE doy la posición del marciano que dispara
            rayo1.posX = listaMarcianos.get(i).posX + 32;
            rayo1.posY = listaMarcianos.get(i).posY + posYMar;
            rayo1.imagen = imagenes[24];
            listaRayos.add(rayo1);
            rayo1.ruido();
            rayo1.sonidoRayo.start();
        }
    }

    //Establece la velocidad de los marcianos
    private void velocidadMarcianos() {
        //Varíamos la velocidad de los marcianos en función de los que quedan
        if (listaMarcianos.size() == 1) {//Si queda un marciano
            velocidadMarcianos = 8;
        } else if (listaMarcianos.size() >= 40) {//Si quedan menos de 40 marcianos
            velocidadMarcianos = 1;
        } else if (listaMarcianos.size() >= 13) {//Si quedan menos de 13 marcianos
            velocidadMarcianos = 2;
        } else {//Si quedan más de dos tercios de marcianos
            velocidadMarcianos = 3;
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
            if (explosionAux.tiempoDeVida < 50) {//Se pinta solo durante un rato
                g2.drawImage(explosionAux.imagen1, explosionAux.posX, explosionAux.posY, null);
            }
            if (explosionAux.tiempoDeVida <= 0) {
                listaExplosiones.remove(i);
            }
        }
    }

    //Chequea si un disparo y un marciano colisionan
    private void chequeaColisionMarDis() {

        //Creo dos cuadros, uno para el disparo y otro para el marciano
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
                        //dibujo la explosión
                        Explosion e = new Explosion();
                        e.posX = listaMarcianos.get(i).posX;
                        e.posY = listaMarcianos.get(i).posY + posYMar;
                        e.imagen1 = imagenes[23];
                        listaExplosiones.add(e);
                        e.sonidoExplosion.start();//Suena el sonido
                        listaMarcianos.remove(i);
                        listaDisparos.remove(k);
                        puntuacion += 50;//El jugador gana 50 puntos
                    } catch (Exception e) {

                    }

                }
            }

        }

    }

    //Chequea si un disparo y un rayo colisionan
    private void chequeaColisionRayoDis() {
        //Creo dos cuadros, uno para el disparo y otro para el rayo
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
                        imp.ruido();
                        imp.sonidoExplosion.start();
                        listaRayos.remove(i);
                        listaDisparos.remove(k);
                        puntuacion += 10; //El jugador recibe 10 puntos
                    } catch (Exception e) {

                    }
                }
            }
        }
    }
    
    //Chequea si una barrera y un rayo colisionan
    private void chequeaColisionRayoBarrera() {
        //Creo dos cuadros, uno para el rayo y otro para la barrera
        Rectangle2D.Double rectanguloRayo = new Rectangle2D.Double();
        Rectangle2D.Double rectanguloBarrera = new Rectangle2D.Double();

        for (int k = 0; k < listaBarreras.size(); k++) {

            //Calculo el rectangulo del disparo
            rectanguloBarrera.setFrame(listaBarreras.get(k).posX, listaBarreras.get(k).posY, listaBarreras.get(k).imagen.getWidth(null), listaBarreras.get(k).imagen.getHeight(null));
            for (int i = 0; i < listaRayos.size(); i++) {

                //Calculo el rectángulo correspondiente al marciano que estoy comprobando
                rectanguloRayo.setFrame(listaRayos.get(i).posX, listaRayos.get(i).posY, listaRayos.get(i).imagen.getWidth(null), listaRayos.get(i).imagen.getHeight(null));

                if (rectanguloBarrera.intersects(rectanguloRayo)) {//Si entra aquí es porque han chocado
                    try {
                        miCrash.ruido();
                        miCrash.sonidoCrash.start();
                        listaRayos.remove(i);
                        listaBarreras.remove(k);
                    } catch (Exception e) {

                    }
                }
            }
        }
    }
    
    //Chequea si una barrera y un disparo colisionan
    private void chequeaColisionDisparoBarrera() {
        //Creo dos cuadros, uno para el disparo y otro para la barrera
        Rectangle2D.Double rectanguloDisparo = new Rectangle2D.Double();
        Rectangle2D.Double rectanguloBarrera = new Rectangle2D.Double();

        for (int k = 0; k < listaBarreras.size(); k++) {

            //Calculo el rectangulo del disparo
            rectanguloBarrera.setFrame(listaBarreras.get(k).posX, listaBarreras.get(k).posY, listaBarreras.get(k).imagen.getWidth(null), listaBarreras.get(k).imagen.getHeight(null));
            for (int i = 0; i < listaDisparos.size(); i++) {

                //Calculo el rectángulo correspondiente al marciano que estoy comprobando
                rectanguloDisparo.setFrame(listaDisparos.get(i).posX, listaDisparos.get(i).posY, listaDisparos.get(i).imagen.getWidth(null), listaDisparos.get(i).imagen.getHeight(null));

                if (rectanguloBarrera.intersects(rectanguloDisparo)) {//Si entra aquí es porque han chocado
                    try {
                        miCrash.ruido();
                        miCrash.sonidoCrash.start();
                        listaDisparos.remove(i);
                        listaBarreras.remove(k);
                    } catch (Exception e) {

                    }
                }
            }
        }
    }
    
    //Chequea si una barrera y un marciano colisionan
    private void chequeaColisionMarcianoBarrera() {
        //Creo dos cuadros, uno para el marciano y otro para la barrera
        Rectangle2D.Double rectanguloMarciano = new Rectangle2D.Double();
        Rectangle2D.Double rectanguloBarrera = new Rectangle2D.Double();

        for (int k = 0; k < listaBarreras.size(); k++) {

            //Calculo el rectangulo del disparo
            rectanguloBarrera.setFrame(listaBarreras.get(k).posX, listaBarreras.get(k).posY, listaBarreras.get(k).imagen.getWidth(null), listaBarreras.get(k).imagen.getHeight(null));
            for (int i = 0; i < listaMarcianos.size(); i++) {

                //Calculo el rectángulo correspondiente al marciano que estoy comprobando
                rectanguloMarciano.setFrame(listaMarcianos.get(i).posX, listaMarcianos.get(i).posY+posYMar, listaMarcianos.get(i).imagen1.getWidth(null), listaMarcianos.get(i).imagen1.getHeight(null));

                if (rectanguloBarrera.intersects(rectanguloMarciano)) {//Si entra aquí es porque han chocado

                    try {
                        miCrash.ruido();
                        miCrash.sonidoCrash.start();
                        listaBarreras.remove(k);
                    } catch (Exception e) {
                        
                    }
                }
            }
        }
    }

    //Chequea si la nave y el rayo colisionan
    private void chequeaColisionNaveRayo() {
        //Creo dos cuadros, uno para el nave y otro para el rayo
        Rectangle2D.Double rectanguloNave = new Rectangle2D.Double();
        Rectangle2D.Double rectanguloRayo = new Rectangle2D.Double();

        for (int k = 0; k < listaRayos.size(); k++) {
            //Calculo el rectangulo del rayo
            rectanguloRayo.setFrame(listaRayos.get(k).posX, listaRayos.get(k).posY, listaRayos.get(k).imagen.getWidth(null), listaRayos.get(k).imagen.getHeight(null));
            //Calculo el rectángulo de la nave
            rectanguloNave.setFrame(miNave.posX, miNave.posY, miNave.imagen.getWidth(null), miNave.imagen.getHeight(null));

            if (rectanguloNave.intersects(rectanguloRayo)) {//Si entra aquí es porque han chocado
                imp.ruido();
                imp.sonidoExplosion.start();
                listaRayos.remove(k);
                miNave.vidas -= 1;

            }

        }

    }

    private void bucleDelJuego() {
        //este método gobierna el redibujado de los objetos en el jpanel1

        //primero borro todo en el buffer
        Graphics2D g2 = (Graphics2D) buffer.getGraphics();

        //Ponemos el fondo negro
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, ANCHOPANTALLA, ALTOPANTALLA);

        pintarEtiquetas(g2);

        contador++;

        //Pintamos los elementos de juego y damos movimiento a la nave
        pintaMarcianos(g2);
        pintaRayos(g2);
        g2.drawImage(miNave.imagen, miNave.posX, miNave.posY, null);
        pintaDisparos(g2);
        pintaExplosiones(g2);

        miNave.mueve();
        //Comprobamos las colisiones
        chequeaColisionMarDis();
        chequeaColisionNaveRayo();
        chequeaColisionRayoDis();
        chequeaColisionRayoBarrera();
        chequeaColisionDisparoBarrera();
        chequeaColisionMarcianoBarrera();

        pintarBarrera(g2);
        if (miNave.vidas <= 0 || perdido) {//Si no quedan vidas o llegan los marcianos aparece pantalla game over
            pantallaGameOver(g2);
        }

        if (listaMarcianos.size() <= 0) {//Si no quedan marcianos se gana la partida
            pantallaWin(g2);
        }

        /////////////////////////////////
        //dibujo de golpe todo el buffer sobre el jPanel1
        g2 = (Graphics2D) jPanel1.getGraphics();
        g2.drawImage(buffer, 0, 0, this);

    }

    //Pinta las barreras que protegen a la nave
    private void pintarBarrera(Graphics2D g2) {

        for (int i = 0; i < listaBarreras.size(); i++) {
            g2.drawImage(listaBarreras.get(i).imagen, listaBarreras.get(i).posX, listaBarreras.get(i).posY, null);

        }

    }

    private void llenarListaBarrera(int _posXBarrera, int _posYBarrera) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                Barrera miBarrera = new Barrera();
                miBarrera.posX = _posXBarrera + (j * miBarrera.imagen.getWidth(null));
                miBarrera.posY = _posYBarrera + (i * miBarrera.imagen.getHeight(null));
                listaBarreras.add(miBarrera);
            }
        }
        _posYBarrera = 550;
        _posXBarrera = 330;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                Barrera miBarrera = new Barrera();
                miBarrera.posX = _posXBarrera + (j * miBarrera.imagen.getWidth(null));
                miBarrera.posY = _posYBarrera + (i * miBarrera.imagen.getHeight(null));
                listaBarreras.add(miBarrera);
            }
        }

        _posYBarrera = 550;
        _posXBarrera = 610;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                Barrera miBarrera = new Barrera();
                miBarrera.posX = _posXBarrera + (j * miBarrera.imagen.getWidth(null));
                miBarrera.posY = _posYBarrera + (i * miBarrera.imagen.getHeight(null));
                listaBarreras.add(miBarrera);
            }
        }
    }

    //Pinta la pantalla cuando el usuario gana
    private void pantallaWin(Graphics2D g2) {
        temporizador.stop();
        //Ruido de victoria
        YouWin miWin = new YouWin();
        miWin.ruido();
        miWin.sonidoWin.start();
        //Pongo las etiquetas
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, ANCHOPANTALLA, ALTOPANTALLA);
        g2.setColor(Color.GREEN);
        Font myFont = new Font("Agency FB", Font.BOLD, 150);
        g2.setFont(myFont);
        g2.drawString("YOU WIN!", 170, 320);
        myFont = new Font("Agency FB", Font.BOLD, 70);
        g2.drawString("SCORE:", 70, 500);
        g2.drawString(String.valueOf(puntuacion), 450, 500);
        //Lo dibujo
        g2 = (Graphics2D) jPanel1.getGraphics();
        g2.drawImage(buffer, 0, 0, this);

        try {//Paro el juego por 4 segungos
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Pinta la pantalla de game over
    private void pantallaGameOver(Graphics2D g2) {
        temporizador.stop();
        //Ruido de Game Over
        GameOver miGO = new GameOver();
        miGO.ruido();
        miGO.sonidoGO.start();

        //Cargo la imagen de Game Over
        try {
            go = ImageIO.read(getClass().getResource("/imagenes/go.jpg"));
        } catch (IOException ex) {

        }
        //Ponesmos la foto de fpondo y las etiquetas con los puntos
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, ANCHOPANTALLA, ALTOPANTALLA);
        g2.drawImage(go, 20, 0, null);
        g2.setColor(Color.GREEN);
        Font myFont = new Font("Agency FB", Font.BOLD, 70);
        g2.setFont(myFont);
        g2.drawString("SCORE:", 240, 450);
        g2.drawString(String.valueOf(puntuacion), 420, 450);
        g2 = (Graphics2D) jPanel1.getGraphics();
        g2.drawImage(buffer, 0, 0, this);

        try {//Paro el juego por 5 segungos
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Pone el contador y las vidas
    private void pintarEtiquetas(Graphics2D g2) {

        g2.setColor(Color.white);
        g2.drawString("SCORE:", 2, 30);
        g2.drawString("LIVES:", 570, 30);
        //Pintamos las vidas representadas con naves, una nave por vida
        try {
            naveVida = ImageIO.read(getClass().getResource("/imagenes/nave.png"));
        } catch (IOException ex) {

        }
        if (miNave.vidas == 3) {
            g2.drawImage(naveVida, 610, 0, null);
            g2.drawImage(naveVida, 660, 0, null);
            g2.drawImage(naveVida, 710, 0, null);
        } else if (miNave.vidas == 2) {
            g2.drawImage(naveVida, 610, 0, null);
            g2.drawImage(naveVida, 660, 0, null);
        } else if (miNave.vidas == 1) {
            g2.drawImage(naveVida, 610, 0, null);
        }
        //Ponemos la puntuación
        g2.setColor(Color.GREEN);
        g2.drawString(String.valueOf(puntuacion), 52, 30);
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/start2..jpg"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(76, 76, 76)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

                if (listaDisparos.size() < 2 && empezar && !perdido) {//Para que no se pueda disparar a lo loco
                    Disparo d = new Disparo();

                    d.posicionDisparo(miNave);

                    d.sonidoDisparo.start();//Suena el sonido

                    //agregamos el disparo a la lista de disparos
                    listaDisparos.add(d);
                } else {
                    //Arranco el temporizador para que empiece el juego
                    temporizador.start();
                    empezar = true;
                }

                if (miNave.vidas <= 0 || listaMarcianos.size() <= 0 || perdido) {//SI se ha terminado la partida
                    posYMar = 0;
                    puntuacion = 0;
                    miNave.vidas = 3;
                    cambiarDir = false;
                    direccionMarciano = false;
                    perdido = false;
                    //VAciamos todas las listas
                    listaMarcianos.clear();
                    listaDisparos.clear();
                    listaExplosiones.clear();
                    listaRayos.clear();
                    listaBarreras.clear();
                    empezarPartida();
                    temporizador.start();

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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
