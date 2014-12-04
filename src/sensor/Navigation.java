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
public class Navigation {

    private float Roll;
    private float Pitch;
    private float Yaw;

    public float getRoll() {
        return Roll;
    }

    public void setRoll(float Roll) {
        this.Roll = Roll;
    }

    public float getPitch() {
        return Pitch;
    }

    public void setPitch(float Pitch) {
        this.Pitch = Pitch;
    }

    public float getYaw() {
        return Yaw;
    }

    public void setYaw(float Yaw) {
        this.Yaw = Yaw;
    }
}
