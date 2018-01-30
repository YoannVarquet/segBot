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
import iprobot.helpers.CustomTimePlotter;
import iprobot.helpers.KalmanFilter;
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
    static double smoothedValue = 0.75;
//PID 
    static double error = 1;
    static double error_prior = 0;
    static double integral = 0;
    static double derivative = 0;
    static double kP = 10;
    static double kI = 0.01;
    static double kD = 1, bias = 0.01;

    public static void main(final String[] args) {

        //create plot
        CustomTimePlotter p1 = new CustomTimePlotter("Dynamic Live Data", "1D");
        CustomTimePlotter p2 = new CustomTimePlotter("Dynamic Live Data", "2D");
        CustomTimePlotter.createAndShowGUI(p1, p2);

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

        motor.brake();
        double speedRpm, pulsePerSecond, speedMSec;
//        motor.drive(55);
//        int compterrr = 0;
//        KalmanFilter kf = new KalmanFilter();  
        int desiredSpeed = (int) (Math.random() * 512) - 256;

        while (true) {
            if (wheelCoderUpdated) {
                //compute RPM
                speedMSec = computeRpm();
                //low pass filter on the speed (filters the noise)
                smoothedValue += (estimatedTime / 200.0) * ((speedMSec - smoothedValue) / 1.5);

                //display values
                System.out.println("speedMSec= " + speedMSec + "\tsmoothedValue= " + smoothedValue);
                p1.updatePlot(desiredSpeed);
                p2.updatePlot(smoothedValue, speedMSec);
                
                //compute PID & correct motor drive
                double commande = computePID(desiredSpeed);
//                motor.drive(commande);

                //change speed when error null ou little
                if (error < 0.2d) {
                    desiredSpeed = (int) (Math.random() * 512) - 256;
                }
                previousTime = System.currentTimeMillis();
            }
        }

    }

    public static double computePID(double desiredSpeed) {
        //compute PID
        error = desiredSpeed - smoothedValue;
        integral = integral + (error * estimatedTime);
        derivative = (error - error_prior) / estimatedTime;
        error_prior = error;
        return kP * error + kI * integral + kD * derivative + bias;

    }

    public static double computeRpm() {
        double pulsePerSecond;
        double speedRpm;
        double speedMSec;
        //                compterrr++;

        //compute RPM
        pulsePerSecond = (wheelCpt * 1000.0) / (double) estimatedTime;
        speedRpm = (pulsePerSecond * 60.0) / 20.0;
        speedMSec = 0.052 * speedRpm * 0.10472;
        if (speedMSec > 1.25) {
            speedMSec = 1.25;
        }
        if (speedMSec < -1.25) {
            speedMSec = -1.25;
        }
        wheelCpt = 0;
        wheelCoderUpdated = false;
        return speedMSec;
    }

}
