/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensor;

/**
 *
 * @author emmanuelsantana
 */
public class Quaternion {

    private float X;
    private float Y;
    private float Z;
    private float W;

    public float getW() {
        return W;
    }

    public void setW(float W) {
        this.W = W;
    }

    public float getX() {
        return X;
    }

    public void setX(float X) {
        this.X = X;
    }

    public float getY() {
        return Y;
    }

    public void setY(float Y) {
        this.Y = Y;
    }

    public float getZ() {
        return Z;
    }

    public void setZ(float Z) {
        this.Z = Z;
    }

    @Override
    public String toString() {
        return "Quaternion: X:" + X + " Y: " + Y + " Z: " + Z + " W: " + W;
    }
}
