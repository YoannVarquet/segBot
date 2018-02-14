package unitaryTests;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinEdge;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import iprobot.helpers.CustomTimePlotterPanel;
import iprobot.helpers.MotorController;
import iprobot.helpers.PanelHolder;
import iprobot.helpers.WheelEncoder;
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
public class IncremetalEncoderUnitaryTest2 {
    
    static GpioPinDigitalInput wheelCoder;
    static boolean wheelCoderUpdated = false;
    static long estimatedTime = 0, estimatedTimeDebug = 0;
    static long previousTime = 0;
    static final double ANGLE_INCREMENT = Math.PI / 10;
    static int wheelCpt = 0;
    static double smoothedValue = 0.75;
//PID 
    static double error = 1;
    static double error_prior = 0;
    static double integral = 0;
    static double derivative = 0;
    static double kP = 10;
    static double kI = 0.01;
    static double kD = 1, bias = 0.01;
    
    static double m = 0.7109164650680;
    static double b = -15.4586700193400;
    
    public static void main(final String[] args) {
        CustomTimePlotterPanel p1 = new CustomTimePlotterPanel(2, -5, 100, "Speed R", "Speed L");
        PanelHolder ph = new PanelHolder(p1);

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        MotorController motorL = new MotorController(gpio, RaspiPin.GPIO_04, RaspiPin.GPIO_02, RaspiPin.GPIO_01, RaspiPin.GPIO_03);
        MotorController motorR = new MotorController(gpio, RaspiPin.GPIO_12, RaspiPin.GPIO_13, RaspiPin.GPIO_23, RaspiPin.GPIO_03);
        WheelEncoder encoderL = new WheelEncoder(RaspiPin.GPIO_27, 143, m, b);
        WheelEncoder encoderR = new WheelEncoder(RaspiPin.GPIO_25, 143, m, b);
        motorL.attachEncoder(encoderL);
        motorR.attachEncoder(encoderR);

        MotorController.brake(motorR, motorL);
        motorL.drive(120);
        motorR.drive(50);
        while (true) {
            p1.updatePlot(motorR.encoder.getSmoothedSpeed(),motorL.encoder.getSmoothedSpeed());
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(IncremetalEncoderUnitaryTest2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}
