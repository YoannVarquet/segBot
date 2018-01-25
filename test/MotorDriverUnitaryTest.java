
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.util.CommandArgumentParser;
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
        MotorController motor = new MotorController(gpio, RaspiPin.GPIO_00, RaspiPin.GPIO_02, RaspiPin.GPIO_01, RaspiPin.GPIO_03);
        int cpt = 0;
        int inc = 1;
        while (true) {

            if (cpt >= 255) {
                inc = -1;
            }
            if (cpt <= -255) {
                inc = 1;
            }
            cpt += inc;
            motor.drive(cpt,5);
            System.out.println("PWM rate is: " + cpt);
        }
    
    
    
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
