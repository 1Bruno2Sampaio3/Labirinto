/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jogo;

import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import javax.media.opengl.GL2;
import java.io.File;
import java.io.IOException;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javafx.animation.Animation;
import javax.media.opengl.glu.GLUquadric;

/**
 *
 * @author Bruno Sampaio
 */
public class Labirinto implements GLEventListener, KeyListener{
    
    
    public static void main(String args[]) {
        new Labirinto();
    }
    
    static boolean visualizar = true, vencedor = false;
    static Camera camera = new Camera();
    public static float avancar = 3.5f, retroceder = 3.5f, girar = 0;
    int startList;
    boolean[] keybuffer = new boolean[256];
    
    GL2 gl;
    GLUT glut = new GLUT();
    GLU glu = new GLU();

    public Labirinto() {
        GLJPanel canvas = new GLJPanel();
        canvas.addGLEventListener(this);

        JFrame frame = new JFrame("Labirinto");
        frame.setSize(1024, 768);
        frame.getContentPane().add(canvas);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new Thread(new Runnable() {
                    public void run() {
                        System.exit(0);
                    }
                }).start();
            }
        });
        Animator a = new Animator(canvas);
        a.start();
        frame.addKeyListener(this);

    }

    public void init(GLAutoDrawable gLAutoDrawable) {
        
       
        
        
        gl = gLAutoDrawable.getGL().getGL2();
        System.err.println("INIT GL IS: " + gl.getClass().getName());

       
        // Enable VSync
        gl.setSwapInterval(1);

        // Setup the drawing area and shading mode
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel(GL2.GL_SMOOTH); // try setting this to GL_FLAT and see what happens.


    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        gl = drawable.getGL().getGL2();

        if (height <= 0) { // evita uma divisão por zero!!!

            height = 1;
        }
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, h, 0.1f, 20.0); //mudar para um valor muito longe e próximo
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glLoadIdentity();

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        gl = drawable.getGL().getGL2();
        float[] difuse = {0.7f, 0.8f, 0.9f, 1};
        float[] specular = {0.7f, 0.8f, 0.1f, 1};
        float[] position = {3.5f, 0, -2.5f, 1};
        float[] spotDir = {0, 1, 0};
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl.glLoadIdentity();
        
                Vetor3d camPosition = camera.getCameraPosition();
                Vetor3d camTarget = camera.getCameraTarget();
                Vetor3d upVector = camera.getUpVector();
                
                if (vencedor == false) {
                    if (visualizar == true) {
                        glu.gluLookAt(camPosition.X,
                                camPosition.Y,
                                camPosition.Z,
                                camTarget.X,
                                camTarget.Y,
                                camTarget.Z,
                                upVector.X,
                                upVector.Y,
                                upVector.Z);
                              
                        if (camPosition.X >= 3 && camPosition.X <= 4) {
                            if (camPosition.Z >= -3 && camPosition.Z <= -2) {
                                visualizar = true;
                            }
                        }
                    } else {
        
                        glu.gluLookAt(-10, 8, -10, 19, -15.0f, -10, 0, 1, 0);
                    }
                }
        
                if (camPosition.X >= 3 && camPosition.X <= 4) {
                    if (camPosition.Z >= -3 && camPosition.Z <= -2) {
        
                        glu.gluLookAt(0.0f, 0.5f, -6, 3.5f, 0.42f, -7, 0, 1, 0);
                        //System.out.println("GANHADOR");
                        gl.glColor3f(0, 1, 0);
                        gl.glBegin(GL2.GL_QUADS);
                        {
                            gl.glTexCoord2f(0, 1);
                            gl.glVertex3f(3, 0, -7);
                            gl.glTexCoord2f(1, 1);
                            gl.glVertex3f(4, 0, -7);
                            gl.glTexCoord2f(1, 0);
                            gl.glVertex3f(4, 0.8f, -7);
                            gl.glTexCoord2f(0, 0);
                            gl.glVertex3f(3, 0.8f, -7);
                        }
                        gl.glEnd();
                    }else{
                        vencedor = false;
                    }
                }
                if (visualizar == false) {
                    gl.glPushMatrix();
                    {
                        gl.glTranslatef(camPosition.X, camPosition.Y, camPosition.Z);
                        //System.out.println(camPosition.X + " -- " + camPosition.Y + " -- " + camPosition.Z);
                        gl.glScalef(1, 2, 1);
                        gl.glColor3f(0.5f, 0.5f, 1);
                        //desenha cubo que atuará como jogador
                        glut.glutSolidCube(0.2f);
                    }
                    gl.glPopMatrix();
                }
                
                gl.glPushMatrix();
                {
                    gl.glEnable(GL2.GL_LIGHTING);
                    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, difuse, 0);
                    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, difuse, 0);
                    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position, 0);
                    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specular, 0);
        
                    gl.glLightf(GL2.GL_LIGHT0, GL2.GL_SPOT_CUTOFF, 20);
                    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPOT_DIRECTION, spotDir, 0);
                    gl.glLightf(GL2.GL_LIGHT0, GL2.GL_SPOT_EXPONENT, 2);
                    gl.glEnable(GL2.GL_LIGHT0);
                    gl.glEnable(GL2.GL_COLOR_MATERIAL);
                }
                gl.glPopMatrix();
                glu.gluLookAt(4, 0, 9, 3, 0, -3, 0, 1, 0);
        
                gl.glPushMatrix();
                {
                    gl.glColor3f(1, 1, 1);
                    desenharPiso();
                    verParedes();
                    porParedes();
                }
                gl.glPopMatrix();
        
                gl.glPushMatrix();
                {
                    gl.glColor3f(1, 0, 0);
                    gl.glTranslatef(3.5f, 0.5f, -2.5f);
                    gl.glRotatef(girar, 0, 1, 0);
                    gl.glColor3f(1, 0.6f, 0);
                    glut.glutSolidTeapot(0.1f);
                    //gl.glTranslatef(0.25f, 0.1f, -2.5f);
                    //gl.glColor3f(1, 0, 0);
                     //glut.glutSolidCube(0.1f);
                }
                gl.glPopMatrix();
                gl.glPushMatrix();
                {
                    gl.glTranslatef(0.5f, 0.1f, -2.5f);
                    gl.glRotatef(90, 0, 1, 0);
                    gl.glColor3f(1, 0, 0);
                    glut.glutSolidCube(0.1f);
                }
                gl.glPopMatrix();
                girar += 0.2f;
                
                gl.glFlush();
    }

            public void desenharParedeHorizontal(int x, int z) {
               
                gl.glColor3f(0, 1, 0);
                gl.glBegin(GL2.GL_QUADS);
                {
                    //gl.glTexCoord2f(0, 0);
                    gl.glVertex3f(x, 0, z);
                    //gl.glTexCoord2f(1, 0);
                    gl.glVertex3f(x + 1, 0, z);
                    //gl.glTexCoord2f(1, 1);
                    gl.glVertex3f(x + 1, 1, z);
                    //gl.glTexCoord2f(0, 1);
                    gl.glVertex3f(x, 1, z);
                }
                gl.glEnd();    
            }
        
            public void desenharParedeVertical(int x, int z) {
                
                gl.glColor3f(0.64f, 0.55f, 0);
                gl.glBegin(GL2.GL_QUADS);
                {
                    //gl.glTexCoord2f(0, 0);
                    gl.glVertex3f(x, 0, z);
                    //gl.glTexCoord2f(1, 0);
                    gl.glVertex3f(x, 0, z - 1);
                    //gl.glTexCoord2f(1, 1);
                    gl.glVertex3f(x, 1, z - 1);
                    //gl.glTexCoord2f(0, 1);
                    gl.glVertex3f(x, 1, z);
                }
                gl.glEnd();
                
            }
        
            public void porParedes() {
                //primeira fila
                for (int i = 0; i < 7; i++) {
                    if (i != 3) {
                        desenharParedeHorizontal(i, 0);
                    }
                }
                //segunda fila
                desenharParedeHorizontal(2, -1);
                //terceira fila
                for (int i = 1; i < 5; i++) {
                    desenharParedeHorizontal(i, -2);
                }
                //quarta fila
                desenharParedeHorizontal(1, -3);
                desenharParedeHorizontal(6, -3);
                //quinta fila
                desenharParedeHorizontal(0, -4);
                desenharParedeHorizontal(3, -4);
                desenharParedeHorizontal(4, -4);
                //sexta fila
                desenharParedeHorizontal(1, -5);
                desenharParedeHorizontal(5, -5);
                desenharParedeHorizontal(6, -5);
                //sétima fila
                for (int i = 0; i < 7; i++) {
                    desenharParedeHorizontal(i, -6);
                }
        
            }
            
            public void desenharPiso() {
                
                gl.glBegin(GL2.GL_QUADS);
                {
                    //gl.glTexCoord2f(0, 0);
                    gl.glVertex3f(0, 0, 0);
                    //gl.glTexCoord2f(1, 0);
                    gl.glVertex3f(7, 0, 0);
                    //gl.glTexCoord2f(1, 1);
                    gl.glVertex3f(7, 0, -6);
                    //gl.glTexCoord2f(0, 1);
                    gl.glVertex3f(0, 0, -6);
                }
                gl.glEnd();
            }
            
            private void verParedes() {
                //primeira coluna da esquerda para a direita
                for (int i = 0; i > -6; i--) {
                    desenharParedeVertical(0, i);
                }
                //segunda coluna
                desenharParedeVertical(1, -1);
                desenharParedeVertical(1, -3);
                desenharParedeVertical(1, -4);
                //terceira coluna
                desenharParedeVertical(2, -4);
                //quarta coluna
                desenharParedeVertical(3, 0);
                desenharParedeVertical(3, -2);
                desenharParedeVertical(3, -5);
                //quinta coluna
                desenharParedeVertical(4, -1);
                desenharParedeVertical(4, -2);
                desenharParedeVertical(4, -3);
                desenharParedeVertical(4, -4);
                //sexta coluna
                desenharParedeVertical(5, -1);
                desenharParedeVertical(5, -3);
                //sétima coluna        
                desenharParedeVertical(6, 0);
                desenharParedeVertical(6, -1);
                desenharParedeVertical(6, -3);
                //última coluna
                for (int i = 0; i > -6; i--) {
                    desenharParedeVertical(7, i);
                }
        
            }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void dispose(GLAutoDrawable glad) {

    }
    
    @Override
    public void keyTyped(KeyEvent ke) {
      if (keybuffer['w']) {
            camera.moveForward(-0.25f);
           // avancar = avancar - 0.5f;
            //System.out.println(camera.getCameraPosition().Z);
        }

        if (keybuffer['s']) {
           camera.moveForward(0.25f);
           //avancar = avancar + 0.5f;
        }

        if (keybuffer['a']) {
            camera.rotateY(10);
            girar = girar + 5;
        }

        if (keybuffer['d']) {
            camera.rotateY(-10);
            girar = girar - 5;
        }
        if (ke.getKeyChar() == 'v') {
            if (visualizar == false) {
                visualizar = true;
            } else {
                visualizar = false;
            }
        }
    }
    
    @Override
    public void keyPressed(KeyEvent ke) {
        keybuffer[ke.getKeyChar()] = true;       
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        keybuffer[ke.getKeyChar()] = false;
    }
    
}