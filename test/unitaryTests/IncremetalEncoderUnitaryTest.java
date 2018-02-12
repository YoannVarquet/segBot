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

        //create plot
        CustomTimePlotterPanel p1 = new CustomTimePlotterPanel(3, 0, +165, "desiredSpeed", "smoothedValue", "speedRpm");
//        CustomTimePlotterPanel p2 = new CustomTimePlotterPanel("Dynamic Live Data", "2D");
        PanelHolder ph = new PanelHolder(p1);
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        MotorController motorL = new MotorController(gpio, RaspiPin.GPIO_04, RaspiPin.GPIO_02, RaspiPin.GPIO_01, RaspiPin.GPIO_03);
        MotorController motorR = new MotorController(gpio, RaspiPin.GPIO_12, RaspiPin.GPIO_13, RaspiPin.GPIO_23, RaspiPin.GPIO_03);

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

        MotorController.brake(motorL,motorR);
        double speedRpm;
//        motor.drive(55);
//        int compterrr = 0;
//        KalmanFilter kf = new KalmanFilter();  
        int desiredSpeed = (int) (Math.random() * 200) + 50;
        MotorController.forward(motorL,motorR,desiredSpeed);
        System.out.println("desiredSpeed= " + desiredSpeed);
        int cpt = 0;

        while (true) {
            if (wheelCoderUpdated) {
                //compute RPM
                speedRpm = computeRpm();
                //low pass filter on the speed (filters the noise)
                smoothedValue += (estimatedTime / 200.0) * ((speedRpm - smoothedValue) / 1.5);

                //display values
                final double desiredSpeedRPM = PWMtoRPM(desiredSpeed);
                System.out.println(
                        "desiredSpeed= " + desiredSpeedRPM
                        + "\tspeedRpm= " + String.format("%1.3f", speedRpm)
                        + "\tsmoothedValue= " + String.format("%1.3f", smoothedValue));
//                p2.updatePlot(desiredSpeed);
                p1.updatePlot(desiredSpeedRPM, smoothedValue, speedRpm);

                //compute PID & correct motor drive
//                if (Math.abs(smoothedValue) - Math.abs(desiredSpeed) < 256) {
//                    estimatedTimeDebug = estimatedTime;
//                    double commande = computePID(desiredSpeedRPM);
//                    motor.drive(RPMtoPWM(commande));
//                }
MotorController.forward(motorL,motorR,RPMtoPWM(desiredSpeedRPM));
                //change speed when error null ou little
                cpt++;
                if (error < 5d && cpt>50) {
                    desiredSpeed = (int) (Math.random() * 255) ;
                    if (Math.abs(desiredSpeed) < 40) {
                        desiredSpeed = (int) (Math.random() * 200) + 50;
                    }
                    cpt=0;
                }
                previousTime = System.currentTimeMillis();
            }
        }

    }

    public static double computePID(double desiredSpeed) {
        //compute PID
        error = desiredSpeed - smoothedValue;
        integral = integral + (error * estimatedTimeDebug);
        derivative = (error - error_prior) / estimatedTimeDebug;
        error_prior = error;
        return checkLimits(kP * error + kI * integral + kD * derivative + bias,-163,163);

    }

    public static double computeRpm() {
        double pulsePerSecond;
        double speedRpm;
        double speedMSec;
        //                compterrr++;

        //compute RPM
        pulsePerSecond = (wheelCpt * 1000.0) / (double) estimatedTime;
        speedRpm = (pulsePerSecond * 60.0) / 20.0;
//        speedMSec = 0.052 * speedRpm * 0.10472;
//        if (speedMSec > 1.25) {
//            speedMSec = 1.25;
//        }
//        if (speedMSec < -1.25) {
//            speedMSec = -1.25;
//        }
        wheelCpt = 0;
        wheelCoderUpdated = false;
        return speedRpm;
    }

    public static double PWMtoRPM(int pwm) {
        return (m * pwm + b);
    }

    public static int RPMtoPWM(double rpm) {
        return (int) Math.round((rpm - b) / m);
    }

    private static double checkLimits(double commande, double i, double i0) {
        if(commande<i)commande=i;
        if(commande>i0)commande=i0;
        return commande;
    }

}
