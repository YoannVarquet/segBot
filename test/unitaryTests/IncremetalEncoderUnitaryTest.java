package unitaryTests;


import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinEdge;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import iprobot.helpers.MotorController;
import iprobot.helpers.TimePlotter;
import org.jfree.ui.RefineryUtilities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author yoann
 */
public class IncremetalEncoderUnitaryTest {

   
    

    static GpioPinDigitalInput wheelCoder;
    static boolean wheelCoderUpdated = false;
    static long estimatedTime = 0;
    static long previousTime = 0;
    static final double ANGLE_INCREMENT = Math.PI / 10;
    static int wheelCpt = 0;
    static double smoothedValue;

    public static void main(final String[] args) {

        //create plot
        TimePlotter plot = new TimePlotter("Dynamic Live Data","2D");
        plot.pack();
        RefineryUtilities.centerFrameOnScreen(plot);
        plot.setVisible(true);

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        MotorController motor = new MotorController(gpio, RaspiPin.GPIO_04, RaspiPin.GPIO_02, RaspiPin.GPIO_01, RaspiPin.GPIO_03);

        // set wheel coder pin
        wheelCoder = gpio.provisionDigitalInputPin(RaspiPin.GPIO_25, PinPullResistance.PULL_DOWN);    

        // create and register gpio pin listener
        estimatedTime = System.currentTimeMillis();
        wheelCoder.addListener((GpioPinListenerDigital) (GpioPinDigitalStateChangeEvent event) -> {
            if (event.getEdge() == PinEdge.RISING) {
                wheelCpt++;
            }
            estimatedTime = System.currentTimeMillis() - previousTime;
            if (estimatedTime > 200 && !wheelCoderUpdated) {
                wheelCoderUpdated = true;
            }
        });

        //PID 
//        double error_prior = 0;
//        double integral = 0;
//        double kP = 10;
//        double kI = 0.01;
//        double kD = 1;
        motor.brake();
        double speedRpm, pulsePerSecond, speedMSec;
        motor.drive(255);
        int compterrr = 0;
        while (true) {
            if (wheelCoderUpdated) {
                compterrr++;

                //compute RPM
                wheelCoderUpdated = false;
                //    motor.brake();
                pulsePerSecond = (wheelCpt * 1000.0) / (double) estimatedTime;
                speedRpm = (pulsePerSecond * 60.0) / 20.0;
                speedMSec = 0.052 * speedRpm * 0.10472;

// If you have a varying frame rate
                smoothedValue += (estimatedTime/200.0) * ((speedMSec - smoothedValue) / 3.0); //System.out.println("speedRpm= " + speedRpm + "\twheelCpt= " + wheelCpt + "\testimatedTime= " + estimatedTime);
                        // motor.drive(255);
                wheelCpt = 0;

                System.out.println("speedMSec= " + speedMSec + "\tsmoothedValue= " + smoothedValue);
                plot.updatePlot(speedMSec, smoothedValue);

                if (compterrr > 100) {
                    compterrr = 0;
                    motor.drive((int) (Math.random() * 512) - 256);
                }
                previousTime = System.currentTimeMillis();
                //compute PID
//            motor.drive(100, 1070);
//            motor.brake();
//            System.out.println(wheelCpt);
//            wheelCpt = 0;
//                while (error < 10) {
//                    error = desired_value – actual_value;
//                    integral = integral + (error * iteration_time);
//                    derivative = (error – error_prior)/iteration_time;
//                    output = KP * error + KI * integral + KD * derivative + bias;
//                    error_prior = error;
////                sleep(iteration_time);
//                }
            }
        }

    }

}