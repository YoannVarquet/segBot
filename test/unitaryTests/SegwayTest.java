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
public class SegwayTest {

    static Mpu6050_2 mpu;
    static double prevP;
    static double prevI;
    static double prevD;
    static double goal;
    static MiniPID pid;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        MotorController motorL = new MotorController(gpio, RaspiPin.GPIO_04, RaspiPin.GPIO_02, RaspiPin.GPIO_01, RaspiPin.GPIO_03);
        MotorController motorR = new MotorController(gpio, RaspiPin.GPIO_12, RaspiPin.GPIO_13, RaspiPin.GPIO_23, RaspiPin.GPIO_03);
//        CustomTimePlotterPanel p1 = new CustomTimePlotterPanel(2, -180, 180, "ax", "mot");
        CustomVariableTuner.SwingSlider s1 = new CustomVariableTuner.SwingSlider("p", 0, 5000, 4, 100, 100, 1.0);
        CustomVariableTuner.SwingSlider s2 = new CustomVariableTuner.SwingSlider("i", 0, 100, 0, 1, 10, 100.0);
        CustomVariableTuner.SwingSlider s3 = new CustomVariableTuner.SwingSlider("d", 0, 1000, 40, 100, 20, 1.0);
        CustomVariableTuner.SwingSlider s4 = new CustomVariableTuner.SwingSlider("center", -20, 20, 2, 5, 10, 100.0);

        PanelHolder ph = new PanelHolder(/*p1,*/s1, s2, s3, s4);
        setupPID(4.0, 0.1, 40.0, -255.0, 255.0);
        double output;
        goal = 0.11;
//
//        double P = s1.value / 10.0;
//        double I = s2.value / 1000.0;
//        double D = s3.value / 10.0;
        prevP = s1.value;
        prevI = s2.value;
        prevD = s3.value;
        try {
            mpu = new Mpu6050_2();
//            System.out.println("waiting for Igor to be upright up right");
//                    mpu.refresh();
//            while (!(Math.abs(mpu.filtered_x_angle - goal) < 0.02)) {
//                    mpu.refresh();
//                mpu.delay(20);
//                PIDVariablesStates(s1, s2, s3, s4);
//                System.out.println("....waiting for Igor to be upright up right" + Math.abs(mpu.filtered_x_angle - goal));
//            }

            System.out.println("Starting....");
            long prevT = System.currentTimeMillis();
            
            while (true) {
//                pid = new MiniPID(prevP, prevI, prevD);
//                    mpu.refresh();
                boolean pidHasNOTChanged = true;
                while (pidHasNOTChanged) {
                    mpu.refresh();
                    final double err = Math.abs(mpu.filtered_x_angle) - Math.abs(goal);
                    if (err < 0.1 && Math.signum(mpu.filtered_x_angle) == Math.signum(goal)) {
                    } else {
                        output = pid.getOutput(mpu.filtered_x_angle, goal);
////                    int vel = (int) (-500*output);
//                    int vel = map((int) (500 * output), -800, 800, 255, -255);
                        int vel = (int) (-1 * output);
                        MotorController.drive(motorL, motorR, vel);
//                    System.out.println((int) (output * 255));
//                System.out.println(mpu.filtered_x_angle);
                        System.out.println("err = "+err+ "\tx_angle=" + mpu.filtered_x_angle + "\tp=" + prevP + "\ti=" + prevI + "\td=" + prevD + "\tgoal=" + goal + "\tvel=" + vel);
//                    p1.updatePlot(((goal -mpu.filtered_x_angle)*255), vel);
                    }
                    mpu.delay(10);
                    pidHasNOTChanged = PIDVariablesStates(s1, s2, s3, s4);
                    System.out.println(prevT - System.currentTimeMillis());
                    prevT = System.currentTimeMillis();
                }
            }
        } catch (I2CFactory.UnsupportedBusNumberException | IOException ex) {
            Logger.getLogger(SegwayTest.class.getName()).log(Level.SEVERE, null, ex);
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
