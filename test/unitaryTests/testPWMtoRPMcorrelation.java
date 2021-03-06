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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author yoann
 */
public class testPWMtoRPMcorrelation {

    static GpioPinDigitalInput wheelCoder;
    static boolean wheelCoderUpdated = false;
    static long estimatedTime = 0;
    static long previousTime = 0;
    static int wheelCpt = 0;
    static double smoothedValue = 0.75;

    public static void main(final String[] args) {

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        MotorController motor = new MotorController(gpio, RaspiPin.GPIO_04, RaspiPin.GPIO_02, RaspiPin.GPIO_01, RaspiPin.GPIO_03);

        // set wheel coder pin
        wheelCoder = gpio.provisionDigitalInputPin(RaspiPin.GPIO_27, PinPullResistance.PULL_DOWN);

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

        double speedRPM;
        int speedPWM = 255;
        motor.drive(speedPWM);
        while (speedPWM > 0) {
            int nbValue = 0;

            double[] speedsRPM = new double[30];

            while (nbValue < 30) {
                if (wheelCoderUpdated) {
                    //compute RPM
                    speedRPM = computeRPM();
                    //low pass filter on the speed (filters the noise)
                    smoothedValue += (estimatedTime / 200.0) * ((speedRPM - smoothedValue) / 1.5);
                    speedsRPM[nbValue] = smoothedValue;
                    nbValue++;
                    previousTime = System.currentTimeMillis();
                }
            }
            double avg = 0.0;
            for (double i : speedsRPM) {
                avg += i;
            }
            avg /= 30.0;
            System.out.println("PWM = " + speedPWM + "\tRPM = " + avg);
            speedPWM--;

            motor.drive(speedPWM);
        }

    }

    public static double computeRPM() {
        double pulsePerSecond;
        double speedRpm;
        //compute RPM
        pulsePerSecond = (wheelCpt * 1000.0) / (double) estimatedTime;
        speedRpm = (pulsePerSecond * 60.0) / 143;

        if (speedRpm > 230) {
            speedRpm = 230;
        }
        if (speedRpm < -230) {
            speedRpm = -230;
        }
        wheelCpt = 0;
        wheelCoderUpdated = false;
        return speedRpm;
    }

}
