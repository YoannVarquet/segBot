package unitaryTests;

import iprobot.helpers.CustomTimePlotterPanel;
import iprobot.helpers.MPU6050;
import iprobot.helpers.PanelHolder;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author yoann
 */
public class MPUUnitaryTest {

    static MPU6050 mpu;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        mpu = new MPU6050();
        if (mpu.testConnection()) {
            System.out.println("MPU Connected");
        } else {
            System.out.println("MPU connection failed");
        }
        //create plot
        CustomTimePlotterPanel p1 = new CustomTimePlotterPanel(3, 0, 66000, "ax", "ay", "az");
        CustomTimePlotterPanel p2 = new CustomTimePlotterPanel(2, -360, 360, "pitch", "roll");
        PanelHolder ph = new PanelHolder(p1,p2);
        while (true) {
            try {
                int[] acc = mpu.getAcceleration();
                int[] gyr = mpu.getRotation();
                System.out.println("ax: " + acc[0]);
                System.out.println("ay: " + acc[1]);
                System.out.println("az: " + acc[2]);
                System.out.println("gx: " + acc[0]);
                System.out.println("gy: " + acc[1]);
                System.out.println("gz: " + acc[2]);
                System.out.println();
                ComplementaryFilter(acc,gyr);
                Thread.sleep(100);
                p1.updatePlot(acc[0], acc[1], acc[2]);
                p2.updatePlot(pitch, roll);

            } catch (InterruptedException ex) {
                Logger.getLogger(MPUUnitaryTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    static double ACCELEROMETER_SENSITIVITY = 8192.0;
    static double GYROSCOPE_SENSITIVITY = 65.536;

    static double M_PI = 3.14159265359;

   static  double dt = 0.1;							// 10 ms sample rate!    
   static  float pitch =0;
   static  float roll=0;

    static void ComplementaryFilter(int[] accData, int[] gyrData) {
        float pitchAcc, rollAcc;

        // Integrate the gyroscope data -> int(angularSpeed) = angle
        pitch += ((float) gyrData[0] / GYROSCOPE_SENSITIVITY) * dt; // Angle around the X-axis
        roll -= ((float) gyrData[1] / GYROSCOPE_SENSITIVITY) * dt;    // Angle around the Y-axis

        // Compensate for drift with accelerometer data if !bullshit
        // Sensitivity = -2 to 2 G at 16Bit -> 2G = 32768 && 0.5G = 8192
        int forceMagnitudeApprox = Math.abs(accData[0]) + Math.abs(accData[1]) + Math.abs(accData[2]);
//        if (forceMagnitudeApprox > 8192 && forceMagnitudeApprox < 32768) {
            // Turning around the X axis results in a vector on the Y-axis
            pitchAcc = (float) (Math.atan2((float) accData[1], (float) accData[2]) * 180 / M_PI);
            pitch = (float) (pitch * 0.98 + pitchAcc * 0.02);

            // Turning around the Y axis results in a vector on the X-axis
            rollAcc = (float) (Math.atan2((float) accData[0], (float) accData[2]) * 180 / M_PI);
            roll = (float) (roll * 0.98 + rollAcc * 0.02);
//        }
    }

}
