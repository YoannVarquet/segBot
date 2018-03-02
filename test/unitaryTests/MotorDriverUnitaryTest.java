package unitaryTests;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.RaspiPin;
import iprobot.helpers.MotorController;
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
public class MotorDriverUnitaryTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GpioController gpio = GpioFactory.getInstance();
        MotorController motorL = new MotorController(gpio, RaspiPin.GPIO_04, RaspiPin.GPIO_02, RaspiPin.GPIO_01, RaspiPin.GPIO_03);
        MotorController motorR = new MotorController(gpio, RaspiPin.GPIO_12, RaspiPin.GPIO_13, RaspiPin.GPIO_23, RaspiPin.GPIO_03);

        int cpt = 0;
        while (cpt < 255) {
            motorL.drive(cpt);
            motorR.drive(cpt);
            System.out.println(cpt);
            cpt+=2;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MotorDriverUnitaryTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            MotorController.brake(motorL, motorR);
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(MotorDriverUnitaryTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
//        int cpt = 0;
//        int inc = 1;
//        int times = 2;
//        while (times > 0) {
//
//            if (cpt >= 255) {
//                inc = -1;
//                times--;
//            }
//            if (cpt <= -255) {
//                inc = 1;
//            }
//            cpt += inc;
//            motor.drive(cpt, 5);
//            System.out.println("PWM rate is: " + cpt);
//
//        }
        MotorController.brake(motorL, motorR);
    }
    /**
     * @param args the command line arguments
     */
//    public static void main(String[] args) {
//        // create GPIO controller instance
//        GpioController gpio = GpioFactory.getInstance();
//        Pin pin = CommandArgumentParser.getPin(RaspiPin.class, // pin provider class to obtain pin instance from
//                RaspiPin.GPIO_01, "");             // argument array to search in
//
//        //setup Pwm pin
//        GpioPinPwmOutput pwm = gpio.provisionPwmOutputPin(pin);
//        com.pi4j.wiringpi.Gpio.pwmSetMode(com.pi4j.wiringpi.Gpio.PWM_MODE_MS);
//        com.pi4j.wiringpi.Gpio.pwmSetRange(1000);
//        com.pi4j.wiringpi.Gpio.pwmSetClock(500);
//
//        //setup standBy pin
//        GpioPinDigitalOutput standbyMotors = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, // PIN NUMBER
//                "stbyMotor", // PIN FRIENDLY NAME (optional)
//                PinState.HIGH);
//
//        //setup direction pin
//        GpioPinDigitalOutput dirMotorTest = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, // PIN NUMBER
//                "DirMotorTest", // PIN FRIENDLY NAME (optional)
//                PinState.HIGH);
//
//        int cpt = 0;
//        int inc = 1;
//        while (true) {
//
//            if (cpt >= 1000) {
//                inc = -1;
//            }
//            if (cpt <= 0) {
//                inc = 1;
//            }
//            cpt += inc;
//            pwm.setPwm(cpt);
//            System.out.println("PWM rate is: " + pwm.getPwm());
//            if (cpt == 0) {
//                dirMotorTest.toggle();
//                System.out.println("direction changed");
//            }
//            try {
//                Thread.sleep(5);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(MotorDriverUnitaryTest.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        }
//
//    }

}
