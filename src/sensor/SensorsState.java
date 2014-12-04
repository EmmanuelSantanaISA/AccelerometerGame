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
public class SensorsState {

    private Navigation Navigation;
    private Accelerometer Accelerometer;
    private Magnetometer Magnetometer;
    private Quaternion Quaternion;

    public Navigation getNavigation() {
        return Navigation;
    }

    public void setNavigation(Navigation Navigation) {
        this.Navigation = Navigation;
    }

    public Accelerometer getAccelerometer() {
        return Accelerometer;
    }

    public void setAccelerometer(Accelerometer Accelerometer) {
        this.Accelerometer = Accelerometer;
    }

    public Magnetometer getMagnetometer() {
        return Magnetometer;
    }

    public void setMagnetometer(Magnetometer Magnetometer) {
        this.Magnetometer = Magnetometer;
    }

    public Quaternion getQuaternion() {
        return Quaternion;
    }

    public void setQuaternion(Quaternion Quaternion) {
        this.Quaternion = Quaternion;
    }
}
