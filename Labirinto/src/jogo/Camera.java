/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jogo;

/**
 *
 * @author Bruno Sampaio
 */
public class Camera {
    //onde vou posicionar inicialmente a camera
    Vetor3d origViewDir = new Vetor3d(0, 0.0f, -3.0f);
    Vetor3d origRightVector = new Vetor3d(0.0f, 0.0f, 0.0f);
    Vetor3d origUpVector = new Vetor3d(0.0f, 0.5f, 0.0f);
    Vetor3d origPosition = new Vetor3d(0, 0.5f, 0.0f);

    //novas posições
    Vetor3d viewDir;   // direção da vista
    Vetor3d rightVector;
    Vetor3d upVector;//vetor da câmera
    Vetor3d position;

    //rotação da câmera
    float rotatedX;
    float rotatedY;
    float rotatedZ;

    float PiDiv180 = (float) Math.PI / 180.0f;   // para converter graus em radianos

    /**
     * construtor da classe
     */
    public Camera() {
        reset();//restaura a câmera com os valores padrões
    }

    /**
     * volta a câmera para seus valores padrões
     */
    public void reset() {
        viewDir = origViewDir;
        rightVector = origRightVector;
        upVector = origUpVector;
        position = origPosition;
        viewDir = viewDir.getNormal();

        rotatedX = 0.0f;
        rotatedY = 0.0f;
        rotatedZ = 0.0f;
    }

    /**
     * deslocamento da câmera, dependendo da rotação
     *
     * @param direction direção da câmera
     */
    public void move(Vetor3d direction) {
        //somamos a posição atual da câmera com a nova direção
        position = Vetor3d.vetor3Addition(position, direction);
    }

    /**
     * rotação da câmera no eixo x
     *
     * @param angle ângulo de rotação
     */
    public void rotateX(float angle) {

        rotatedX += angle;
        //passar os graus para radianos
        Vetor3d temp1 = Vetor3d.vetor3Multiplication(viewDir, (float) Math.cos(angle * PiDiv180));
        Vetor3d temp2 = Vetor3d.vetor3Multiplication(upVector, (float) Math.sin(angle * PiDiv180));

        viewDir = Vetor3d.vetor3Addition(temp1, temp2).getNormal();
        upVector = Vetor3d.vetor3Multiplication(Vetor3d.vetor3CrossProduct(viewDir, rightVector), -1.0f);
    }

    /**
     * rotação em y da câmera
     *
     * @param angle ângulo de rotação
     */
    public void rotateY(float angle) {
        rotatedY += angle;

        Vetor3d temp1 = Vetor3d.vetor3Multiplication(viewDir, (float) Math.cos(angle * PiDiv180));
        Vetor3d temp2 = Vetor3d.vetor3Multiplication(rightVector, (float) Math.sin(angle * PiDiv180));

        viewDir = Vetor3d.vetor3Substraction(temp2, temp1);
        viewDir = viewDir.getNormal();
        rightVector = Vetor3d.vetor3CrossProduct(viewDir, upVector);
    }

    public void rotateZ(float angle) {
        rotatedZ += angle;

        Vetor3d temp1 = Vetor3d.vetor3Multiplication(rightVector, (float) Math.cos(angle * PiDiv180));
        Vetor3d temp2 = Vetor3d.vetor3Multiplication(upVector, (float) Math.sin(angle * PiDiv180));

        rightVector = Vetor3d.vetor3Addition(temp1, temp2).getNormal();
        upVector = Vetor3d.vetor3Multiplication(Vetor3d.vetor3CrossProduct(viewDir, rightVector), -1.0f);
    }

    /**
     * avançar
     *
     * @param distance
     */
    public void moveForward(float distance) {
        //Uso menos porque a linha negativa no eixo z está em frente
        position = Vetor3d.vetor3Addition(position, Vetor3d.vetor3Multiplication(viewDir, -distance));
    }

    /**
     * para mover a câmera para a esquerda e para a direita
     *
     * @param distance
     */
    public void strafeRight(float distance) {
        position = Vetor3d.vetor3Addition(position, Vetor3d.vetor3Multiplication(rightVector, distance));
    }

    /**
     * move para cima ou para baixo a câmera
     *
     * @param distance
     */
    public void moveUpward(float distance) {
        position = Vetor3d.vetor3Addition(position, Vetor3d.vetor3Multiplication(upVector, distance));
    }

    /**
     * posição da câmera
     *
     * @return
     */
    public Vetor3d getCameraPosition() {
        return position;
    }

    /**
     * obter o objetivo da câmera (onde é o ponto de interesse)
     *
     * @return
     */
    public Vetor3d getCameraTarget() {
        return Vetor3d.vetor3Addition(position, viewDir);
    }

    /**
     * obter o vetor da câmera
     *
     * @return
     */
    public Vetor3d getUpVector() {
        return upVector;
    }
}


