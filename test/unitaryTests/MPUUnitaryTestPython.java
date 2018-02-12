package unitaryTests;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.RaspiPin;
import iprobot.helpers.CustomTimePlotterPanel;
import iprobot.helpers.MPU6050;
import iprobot.helpers.MotorController;
import iprobot.helpers.PanelHolder;
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
public class MPUUnitaryTestPython {

    static MPU6050 mpu;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
//        try {
//            Process p = Runtime.getRuntime().exec("python ~/Desktop/mpu.py");
//            System.out.println(p.getInputStream());
//        } catch (IOException ex) {
//            Logger.getLogger(MPUUnitaryTestPython.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
                // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        MotorController motorL = new MotorController(gpio, RaspiPin.GPIO_04, RaspiPin.GPIO_02, RaspiPin.GPIO_01, RaspiPin.GPIO_03);
        MotorController motorR = new MotorController(gpio, RaspiPin.GPIO_12, RaspiPin.GPIO_13, RaspiPin.GPIO_23, RaspiPin.GPIO_03);

        mpu = new MPU6050();

        if (mpu.testConnection()) {
            System.out.println("MPU Connected");
        } else {
            System.out.println("MPU connection failed");
        }
        //create plot
//        CustomTimePlotterPanel p1 = new CustomTimePlotterPanel(6, -2, 2, "ax", "ay", "az", "gx", "gy", "gz");
        CustomTimePlotterPanel p2 = new CustomTimePlotterPanel(2, -50, 50, "pitch", "roll");
        PanelHolder ph = new PanelHolder(/*p1,*/p2);
                double[] smoothedValue = new double[6];
                double smoother = 0.5;
        while (true) {
            try {
                int[] acc = mpu.getAcceleration();
                int[] gyr = mpu.getRotation();
//                System.out.println("ax: " + acc[0]);
//                System.out.println("ay: " + acc[1]);
//                System.out.println("az: " + acc[2]);
//                System.out.println("gx: " + acc[0]);
//                System.out.println("gy: " + acc[1]);
//                System.out.println("gz: " + acc[2]);
//                System.out.println();
                Thread.sleep(100);
                smoothedValue[0] += 0.1 * ((acc[0] - smoothedValue[0]) / smoother);
                smoothedValue[1] += 0.1 * ((acc[1] - smoothedValue[1]) / smoother);
                smoothedValue[2] += 0.1 * ((acc[2] - smoothedValue[2]) / smoother);
                smoothedValue[3] += 0.1 * ((gyr[0] - smoothedValue[3]) / smoother);
                smoothedValue[4] += 0.1 * ((gyr[1] - smoothedValue[4]) / smoother);
                smoothedValue[5] += 0.1 * ((gyr[2] - smoothedValue[5]) / smoother);
                ComplementaryFilter(smoothedValue);
//                p1.updatePlot(smoothedValue[0]/8192.0/*, acc[1], acc[2],gyr[0], gyr[1], gyr[2]*/);
//                p1.updatePlot(smoothedValue[0]/8192.0, smoothedValue[1]/8192.0, smoothedValue[2]/8192.0
//                        ,smoothedValue[3]/65.536, smoothedValue[4]/65.536, smoothedValue[5]/65.536);
                p2.updatePlot(pitch, roll);
if(roll>1)
        MotorController.forward(motorL,motorR, (int) (6*(Math.abs(roll))));
else if(roll<-1)
        MotorController.back(motorL,motorR, (int) (6*(Math.abs(roll))));
else MotorController.brake(motorR, motorL);


            } catch (InterruptedException ex) {
                Logger.getLogger(MPUUnitaryTestPython.class.getName()).log(Level.SEVERE, null, ex);
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

    static void ComplementaryFilter(double[] smoothedValues) {
        float pitchAcc, rollAcc;

        // Integrate the gyroscope data -> int(angularSpeed) = angle
        pitch += ((float) smoothedValues[3] / GYROSCOPE_SENSITIVITY) * dt; // Angle around the X-axis
        roll -= ((float) smoothedValues[4] / GYROSCOPE_SENSITIVITY) * dt;    // Angle around the Y-axis

        // Compensate for drift with accelerometer data if !bullshit
        // Sensitivity = -2 to 2 G at 16Bit -> 2G = 32768 && 0.5G = 8192
//        double forceMagnitudeApprox = Math.abs(smoothedValues[0]) + Math.abs(smoothedValues[1]) + Math.abs(smoothedValues[2]);
//        if (forceMagnitudeApprox > 8192 && forceMagnitudeApprox < 32768) {
            // Turning around the X axis results in a vector on the Y-axis
            pitchAcc = (float) (Math.atan2((float) smoothedValues[1], (float) smoothedValues[2]) * 180 / M_PI);
            pitch = (float) (pitch * 0.98 + pitchAcc * 0.02);

            // Turning around the Y axis results in a vector on the X-axis
            rollAcc = (float) (Math.atan2((float) smoothedValues[0], (float) smoothedValues[2]) * 180 / M_PI);
            roll = (float) (roll * 0.98 + rollAcc * 0.02);
//        }
    }

}
