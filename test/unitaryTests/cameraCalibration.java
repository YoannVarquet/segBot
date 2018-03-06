package unitaryTests;

import com.hopding.jrpicam.RPiCamera;
import com.hopding.jrpicam.enums.Exposure;
import com.hopding.jrpicam.exceptions.FailedToRunRaspistillException;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.RaspiPin;
import iprobot.helpers.CustomVariableTuner;
import iprobot.helpers.MiniPID;
import iprobot.helpers.MotorController;
import java.awt.image.BufferedImage;
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
public class cameraCalibration {

    static double prevP;
    static double prevI;
    static double prevD;
    static double goal;
    static MiniPID pid;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        RPiCamera piCamera = null;
        BufferedImage image = null;
        boolean refreshed = false;

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        MotorController motorL = new MotorController(gpio, RaspiPin.GPIO_04, RaspiPin.GPIO_02, RaspiPin.GPIO_01, RaspiPin.GPIO_03);
        MotorController motorR = new MotorController(gpio, RaspiPin.GPIO_12, RaspiPin.GPIO_13, RaspiPin.GPIO_23, RaspiPin.GPIO_03);
        try {
            // Create a Camera that saves images to the Pi's Pictures directory.
            piCamera = new RPiCamera("/home/pi/Pictures");
            piCamera.setWidth(500).setHeight(500) // Set Camera to produce 500x500 images.
                    .setBrightness(75) // Adjust Camera's brightness setting.
                    .setExposure(Exposure.AUTO) // Set Camera's exposure.
                    .setTimeout(2) // Set Camera's timeout.
                    .setAddRawBayer(true);            // Add Raw Bayer data to image files created by Camera.
            // Sets all Camera options to their default settings, overriding any changes previously made.
//            piCamera.setToDefaults();
        } catch (FailedToRunRaspistillException ex) {
            Logger.getLogger(cameraCalibration.class.getName()).log(Level.SEVERE, null, ex);
        }

        double x_des, y_des, theta_des;
        double x_curr = 0, y_curr = 0, theta_curr = 0;
        double r = 0.052, lr = 0.054;
        
        //main loop
        while (true && piCamera != null) {
            
            
            try {
                //get Image
                image = piCamera.takeBufferedStill();
                refreshed = true;
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(cameraCalibration.class.getName()).log(Level.SEVERE, null, ex);

                refreshed = false;
            }
            if (refreshed && image != null) {
                
                // get optical flow
                

                //get desired position from camera
                x_des = 0;
                y_des = 0;
                //compute errors
                double lx = x_des - x_curr;
                double ly = y_des - y_curr;
                double dist = Math.sqrt(lx * lx + ly * ly);
                theta_des = Math.atan2(ly, lx);
                double theta_err = Math.sin(theta_des - theta_curr);

                //compute speed
                double n1 = 0, n2 = 0;
                //if errors too little consider goal acheive
                if (theta_err < 0.05 && theta_err > -0.05) {
                    theta_err = 0;
                }
                if (dist < 0.1) {
                    dist = 0;
                }
                n2 = 2 * theta_err;
                n1 = 0.3 * dist;

                double V1 = (n1 + lr * n2) / r;
                double V2 = (n1 - lr * n2) / r;
            }
        }

    }

    static int map(int x, int rangeINmin, int rangeINmax, int rangeOUTmin, int rangeMAXout) {
        return (int) ((double) (x - rangeINmin) * (double) (rangeMAXout - rangeOUTmin) / (double) (rangeINmax - rangeINmin) + (double) rangeOUTmin);
    }

    private static boolean PIDVariablesStates(CustomVariableTuner.SwingSlider s1, CustomVariableTuner.SwingSlider s2, CustomVariableTuner.SwingSlider s3, CustomVariableTuner.SwingSlider s4) {
        double P;
        double I;
        double D;
        P = s1.value;
        I = s2.value;
        D = s3.value;
//        if (prevP != P) {
//            prevP=P;
//            pid.setP(prevP);
//            pid.reset();
//        }
//        if (prevI != I) {
//            prevI=I;
//            pid.setI(prevI);
//            pid.reset();
//        }
//        if (prevD != D) {
//            prevD=D;
//            pid.setD(prevD);
//            pid.reset();
//        }
        goal = s4.value;
        final boolean pidHasNOTChanged = (prevP == P && prevI == I && prevD == D);
        if (!pidHasNOTChanged) {
            prevP = P;
            prevI = I;
            prevD = D;
            setupPID(prevP, prevI, prevD, -255, 255);

        }
        return pidHasNOTChanged;
    }

    static void setupPID(double p, double i, double d, double min, double max) {
        pid = new MiniPID(p, i, d);
        pid.setMaxIOutput(40);
        pid.setOutputLimits(min, max);
        pid.setSetpointRange(3);
    }

}
