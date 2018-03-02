package unitaryTests;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.i2c.I2CFactory;
import iprobot.helpers.CustomTimePlotterPanel;
import iprobot.helpers.CustomVariableTuner;
import iprobot.helpers.MiniPID;
import iprobot.helpers.MotorController;
import iprobot.helpers.Mpu6050_2;
import iprobot.helpers.PanelHolder;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author yoann
 */
public class SegwayTestTune {

    static Mpu6050_2 mpu;
    static double prevKU;
    static double prevTU;
    static double prevP;
    static double prevI;
    static double prevD;
    static double goal;
    static MiniPID pid;
    static int PWM_RANGE = 255;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        MotorController motorL = new MotorController(gpio, RaspiPin.GPIO_04, RaspiPin.GPIO_02, RaspiPin.GPIO_01, RaspiPin.GPIO_03,PWM_RANGE );
        MotorController motorR = new MotorController(gpio, RaspiPin.GPIO_12, RaspiPin.GPIO_13, RaspiPin.GPIO_23, RaspiPin.GPIO_03,PWM_RANGE);
//create tuning interface
        CustomTimePlotterPanel p1 = new CustomTimePlotterPanel(2, -300, 300, "ax", "mot");
        p1.setChartTimeRange(2000);
        CustomVariableTuner.SwingSlider s1 = new CustomVariableTuner.SwingSlider("kp", 0, 5000, 0, 50, 500, 1.0);
        CustomVariableTuner.SwingSlider s2 = new CustomVariableTuner.SwingSlider("ki", 0, 1000, 0, 10, 100, 100.0);
        CustomVariableTuner.SwingSlider s3 = new CustomVariableTuner.SwingSlider("kd", 0, 1000, 0, 10, 100, 100.0);
        
        PanelHolder ph = new PanelHolder(p1, s1, s2,s3);

        double output;
        goal = 0.11;

        double Ku = 0;
        double tu = 0.02;//(s)
        double KP = 0.6 * Ku;
        double KI = 2.0 * KP / tu;
        double KD = KP * tu / 8;

        setupPID(KP, KI, KD, -1.0*PWM_RANGE, 1.0*PWM_RANGE);

        try {
            //init MPU
            mpu = new Mpu6050_2();
            System.out.println("Starting....");
            long prevT = System.currentTimeMillis();

            while (true) {//loop forever

                boolean pidHasNOTChanged = true;

                while (pidHasNOTChanged) {//loop in this setting until the Slidders change
                    //get mpu values
                    mpu.refresh();

                    //compute motor output with the current PID settings
                    output = pid.getOutput(mpu.filtered_x_angle, goal);
                    int vel = (int) (-1 * output);

                    //send command to the motor
                    MotorController.drive(motorL, motorR, vel);

                    //slow down the computing unit
                    mpu.delay(10);

                    //display 
                    System.out.println("x_angle=" + mpu.filtered_x_angle + "\tp=" + prevP + "\ti=" + prevI + "\td=" + prevD + "\tgoal=" + goal + "\tvel=" + vel);
                    p1.updatePlot(((goal - mpu.filtered_x_angle) * PWM_RANGE), vel);

                    //check for change of settings (sliders)
                    pidHasNOTChanged = PIDVariablesStates(s1, s2);

                    //print loop time
                    System.out.println(prevT - System.currentTimeMillis());
                    prevT = System.currentTimeMillis();
                }
            }
        } catch (I2CFactory.UnsupportedBusNumberException | IOException ex) {
            MotorController.brake(motorL, motorR);
            Logger.getLogger(SegwayTestTune.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    static int map(int x, int rangeINmin, int rangeINmax, int rangeOUTmin, int rangeMAXout) {
        return (int) ((double) (x - rangeINmin) * (double) (rangeMAXout - rangeOUTmin) / (double) (rangeINmax - rangeINmin) + (double) rangeOUTmin);
    }

    private static boolean PIDVariablesStates(CustomVariableTuner.SwingSlider s1, CustomVariableTuner.SwingSlider s2) {
        double P;
        double I;
        double D;
        double KU = s1.value;
        double TU = s2.value;
        final boolean pidHasNOTChanged = (prevKU == KU && prevTU == TU );
        if (!pidHasNOTChanged) {
            prevKU = KU;
            prevTU = TU;
            setupPID(KU, TU,  -1.0*PWM_RANGE, 1.0*PWM_RANGE);

        }
        return pidHasNOTChanged;
    }
    
    private static boolean PIDVariablesStates(CustomVariableTuner.SwingSlider s1, CustomVariableTuner.SwingSlider s2, CustomVariableTuner.SwingSlider s3) {
        double P= s1.value;
        double I= s2.value;
        double D= s3.value;
        final boolean pidHasNOTChanged = (prevP == P && prevI == I && prevD == D );
        if (!pidHasNOTChanged) {
            prevP = P;
            prevI = I;
            prevD = D;
            setupPID(P, I, D,  -1.0*PWM_RANGE, 1.0*PWM_RANGE);

        }
        return pidHasNOTChanged;
    }

    static void setupPID(double p, double i, double d, double min, double max) {
        pid = new MiniPID(p, i, d);
        pid.setMaxIOutput(40);
        pid.setOutputLimits(min, max);
        pid.setSetpointRange(3);
        
    }    
    static void setupPID(double Ku, double Tu, double min, double max) {
        
        double KP = 0.6 * Ku;
        double KI = 2.0 * KP / Tu;
        double KD = KP * Tu / 8;
        pid = new MiniPID(KP, KI, KD);
        System.out.println("\nKP: "+KP +"\nKI: "+KI +"\nKD: "+KD );
        System.out.println();
        pid.setMaxIOutput(50);
        pid.setOutputLimits(min, max);
        pid.setSetpointRange(3);
    }

}
