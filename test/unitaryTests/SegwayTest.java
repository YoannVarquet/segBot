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
      static  double prevP ;
     static   double prevI ;
     static   double prevD ;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        MotorController motorL = new MotorController(gpio, RaspiPin.GPIO_04, RaspiPin.GPIO_02, RaspiPin.GPIO_01, RaspiPin.GPIO_03);
        MotorController motorR = new MotorController(gpio, RaspiPin.GPIO_12, RaspiPin.GPIO_13, RaspiPin.GPIO_23, RaspiPin.GPIO_03);
//        CustomTimePlotterPanel p1 = new CustomTimePlotterPanel(2, -180, 180, "ax", "mot");
        CustomVariableTuner.SwingSlider s1 = new CustomVariableTuner.SwingSlider("p", 0, 100, 1, 5, 10, 1.0);
        CustomVariableTuner.SwingSlider s2 = new CustomVariableTuner.SwingSlider("i", 0, 100, 0, 1, 10, 1.0);
        CustomVariableTuner.SwingSlider s3 = new CustomVariableTuner.SwingSlider("d", 0, 100, 10, 5, 10, 1.0);
        CustomVariableTuner.SwingSlider s4 = new CustomVariableTuner.SwingSlider("center", -100, 100, 10, 5, 10,100.0);

        PanelHolder ph = new PanelHolder(/*p1,*/ s1, s2, s3, s4);
        MiniPID pid = new MiniPID(50, 10, 1);
        double output;
        double goal = 0.057;
//
//        double P = s1.value / 10.0;
//        double I = s2.value / 1000.0;
//        double D = s3.value / 10.0;
         prevP = s1.value ;
         prevI = s2.value ;
         prevD = s3.value;
        try {
            mpu = new Mpu6050_2();
            while (true) {
                pid = new MiniPID(prevP, prevI, prevD);
                boolean pidHasNOTChanged = true;
                while (pidHasNOTChanged) {
                    mpu.refresh();
                    output = pid.getOutput(mpu.filtered_x_angle, goal);
                    MotorController.drive(motorL, motorR, (int) (-1000*output));
//                    System.out.println((int) (output * 255));
//                System.out.println(mpu.filtered_x_angle);
//                    p1.updatePlot(mpu.filtered_x_angle * 180, (int) (output * -1000));

                    mpu.delay(1);
                    pidHasNOTChanged = PIDVariablesStates(s1, s2, s3, pid);
                }
            }
        } catch (I2CFactory.UnsupportedBusNumberException | IOException ex) {
            Logger.getLogger(SegwayTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static boolean PIDVariablesStates(CustomVariableTuner.SwingSlider s1, CustomVariableTuner.SwingSlider s2, CustomVariableTuner.SwingSlider s3, MiniPID pid) {
        double P;
        double I;
        double D;
        P = s1.value ;
        I = s2.value ;
        D = s3.value ;
        if (prevP != P) {
            pid.setP(prevP);
            pid.reset();
        }
        if (prevI != I) {
            pid.setI(prevI);
            pid.reset();
        }
        if (prevD != D) {
            pid.setD(prevD);
            pid.reset();
        }
        final boolean pidHasNOTChanged = (prevP == P && prevI == I && prevD == D);
        return pidHasNOTChanged;
    }

}
