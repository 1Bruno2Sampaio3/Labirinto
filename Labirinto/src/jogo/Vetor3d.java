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
public class Vetor3d {
    float X,Y,Z;

    
     public Vetor3d() {
        this.X = 0;
        this.Y = 0;
        this.Z = 0;
    }
     
    public Vetor3d(float x, float y, float z) {
        this.X = x;
        this.Y = y;
        this.Z = z;
    }
    
    
    /**
     * 
     * @return o vetor inverso de (x,y,z)
     */
    public Vetor3d getInverse()
    {
        return new Vetor3d(-X, -Y, -Z);
    }

    /**
     * 
     * @return tamanho do vetor
     */
    public float getLength()
    {
        return (float)Math.sqrt((X * X) + (Y * Y) + (Z * Z));
    }

    /**
     * 
     * @return vetor3d normal
     */
    public Vetor3d getNormal()
    {
        float l = getLength();

        if(l == 0.0f)
            return null;

        return new Vetor3d(X / l, Y / l, Z / l);
    }
    
    
    /**
     * calcula a soma de vetores
     * @param u
     * @param v
     * @return soma de u com v
     */
    public static Vetor3d vetor3Addition(Vetor3d u, Vetor3d v){
        Vetor3d w = new Vetor3d();
        w.X = u.X + v.X;
        w.Y = u.Y + v.Y;
        w.Z = u.Z + v.Z;
        return w;
    }
    
    
     public static Vetor3d vetor3Substraction(Vetor3d u, Vetor3d v)
    {
        Vetor3d w = new Vetor3d();

        w.X = v.X - u.X;
        w.Y = v.Y - u.Y;
        w.Z = v.Z - u.Z;

        return w;
    }
     
     
    /**
     * calcula a multiplicação de um vetor u com um escalar r
     * @param u vetor3d
     * @param r float
     * @return ru
     */
    public static Vetor3d vetor3Multiplication(Vetor3d u, float r){
        Vetor3d w = new Vetor3d();

        w.X = u.X * r;
        w.Y = u.Y * r;
        w.Z = u.Z * r;

        return w;
    }
    
    
    /**
     * calcula o produto cruz entre os vetores
     * @param u Vetor3d
     * @param v Vetor3d
     * @return uxv
     */
    public static Vetor3d vetor3CrossProduct(Vetor3d u, Vetor3d v){
        Vetor3d w = new Vetor3d();

        w.X = u.Y * v.Z - u.Z * v.Y;
        w.Y = u.Z * v.X - u.X * v.Z;
        w.Z = u.X * v.Y - u.Y * v.X;

        return w;
    }

    /**
     * produto ponto entre dois vetores
     * @param u Vetor3d
     * @param v Vetor3d
     * @return u.v
     */
    public static float vetor3DotProduct(Vetor3d u, Vetor3d v){
        return (v.X * u.X) + (v.Y * u.Y) + (v.Z * u.Z);
    }
}
