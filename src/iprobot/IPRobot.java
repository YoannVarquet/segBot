/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iprobot;

import com.pi4j.io.gpio.BananaProPin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.util.CommandArgumentParser;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yoann
 */
public class IPRobot {

    I2CDevice mpu6050;
    GpioPinDigitalInput wheelCoder;
    boolean wheelCoderUpdated = false;
    long estimatedTime = 0;
    static final double ANGLE_INCREMENT = Math.PI / 10;
    int wheelCpt = 0;

    public IPRobot() {
//        try {
//            I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
//            mpu6050 = bus.getDevice(0x68);
////            byte data = (byte) mpu6050.read(0x0D);
////            System.out.println("byte read="+data);
//        } catch (I2CFactory.UnsupportedBusNumberException ex) {
//            Logger.getLogger(IPRobot.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(IPRobot.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        // create gpio controller
//        final GpioController gpio = GpioFactory.getInstance();
//
//        // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
//        wheelCoder = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_DOWN);        // set shutdown state for this input pin
//        wheelCoder.setShutdownOptions(true);
//        System.out.println("WheelEncoder setup");
//
//        // create and register gpio pin listener
//        wheelCoder.addListener(new GpioPinListenerDigital() {
//            @Override
//            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
//                estimatedTime = System.currentTimeMillis() - estimatedTime;
//                if (estimatedTime > 1000) {
//                    wheelCoderUpdated = true;
//                }
//
//                wheelCpt++;
//
//            }
//
//        });

    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        IPRobot segway = new IPRobot();
//                byte buffer[] = new byte[14];
//                int ax,ay,az,gx,gy,gz;
                
        // create GPIO controller instance
        GpioController gpio = GpioFactory.getInstance();
        Pin pin = CommandArgumentParser.getPin(
                RaspiPin.class,    // pin provider class to obtain pin instance from
                RaspiPin.GPIO_01,  // default pin if no pin argument found
                args);             // argument array to search in

        GpioPinPwmOutput pwm = gpio.provisionPwmOutputPin(pin);
        com.pi4j.wiringpi.Gpio.pwmSetMode(com.pi4j.wiringpi.Gpio.PWM_MODE_MS);
        com.pi4j.wiringpi.Gpio.pwmSetRange(1000);
        com.pi4j.wiringpi.Gpio.pwmSetClock(500);
        GpioPinDigitalOutput standbyMotors = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_26,   // PIN NUMBER
                                                           "stbyMotor",           // PIN FRIENDLY NAME (optional)
                                                           PinState.HIGH);      // PIN STARTUP STATE (optional)
        GpioPinDigitalOutput dirMotorTest = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05,   // PIN NUMBER
                                                           "DirMotorTest",           // PIN FRIENDLY NAME (optional)
                                                           PinState.HIGH);      // PIN STARTUP STATE (optional)
        int cpt =0;
        while (true) {
//
//            try {
//                //read registers for motion and fill the buffer
//                segway.mpu6050.read(0x3B, buffer, 0, 14);
//                    ax = (((int)buffer[0]) << 8) | buffer[1];
//    ay = (((int)buffer[2]) << 8) | buffer[3];
//    az = (((int)buffer[4]) << 8) | buffer[5];
//    gx = (((int)buffer[8]) << 8) | buffer[9];
//    gy = (((int)buffer[10]) << 8) | buffer[11];
//    gz = (((int)buffer[12]) << 8) | buffer[13];
//    System.out.println("ax:"+ax+"\tay:"+ay+"\taz:"+az+"\tgx:"+gx+"\tgy:"+gy+"\tgz:"+gz+"\t");
//    
//            } catch (IOException ex) {
//                Logger.getLogger(IPRobot.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            if (segway.wheelCoderUpdated) {
//                final long rpm = (60 * 1000 / 20 )/(segway.estimatedTime)* segway.wheelCpt;
////                final double elapsedSeconds = segway.estimatedTime / 1000000;
//                System.out.println("intant velocity:" + rpm +"rpm");
//                segway.wheelCpt=0;
//                segway.wheelCoderUpdated = false;
//            }


        // set the PWM rate to 500
        cpt++;
        cpt = cpt%999;
        pwm.setPwm(cpt);
        System.out.println("PWM rate is: " + pwm.getPwm());
        if(cpt == 0){
            dirMotorTest.toggle();
        }

        

        }

    }

}
