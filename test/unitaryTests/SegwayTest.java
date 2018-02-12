package unitaryTests;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.i2c.I2CFactory;
import iprobot.helpers.CustomTimePlotterPanel;
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        MotorController motorL = new MotorController(gpio, RaspiPin.GPIO_04, RaspiPin.GPIO_02, RaspiPin.GPIO_01, RaspiPin.GPIO_03);
        MotorController motorR = new MotorController(gpio, RaspiPin.GPIO_12, RaspiPin.GPIO_13, RaspiPin.GPIO_23, RaspiPin.GPIO_03);
        CustomTimePlotterPanel p1 = new CustomTimePlotterPanel(2, -180, 180, "ax", "mot");
        PanelHolder ph = new PanelHolder(p1);
        MiniPID pid = new MiniPID(2,0.000,10);double output;
        try {
            mpu = new Mpu6050_2();
            while (true) {

                mpu.refresh();
                output = pid.getOutput(mpu.filtered_x_angle,0.057);
                MotorController.drive(motorL, motorR, (int) (output * -255));
//                System.out.println((int) (output * 255));
                System.out.println(mpu.filtered_x_angle);
                p1.updatePlot(mpu.filtered_x_angle * 180,(int) (mpu.filtered_x_angle * 255));

                mpu.delay(20);
            }
        } catch (I2CFactory.UnsupportedBusNumberException | IOException ex) {
            Logger.getLogger(SegwayTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
