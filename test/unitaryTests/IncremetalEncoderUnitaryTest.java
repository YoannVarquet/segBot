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
    static double smoothedValue=0.75;
    static double smoothedValue2=0.75;

    public static void main(final String[] args) {

        //create plot
        CustomTimePlotter p1 = new CustomTimePlotter("Dynamic Live Data","1D");
        CustomTimePlotter p2 = new CustomTimePlotter("Dynamic Live Data","2D");
        CustomTimePlotter.createAndShowGUI(p1,p2);

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
        motor.drive(55);
        int compterrr = 0;
        KalmanFilter kf = new KalmanFilter();
        while (true) {
            if (wheelCoderUpdated) {
                compterrr++;

                //compute RPM
                wheelCoderUpdated = false;
                //    motor.brake();
                pulsePerSecond = (wheelCpt * 1000.0) / (double) estimatedTime;
                speedRpm = (pulsePerSecond * 60.0) / 20.0;
                speedMSec = 0.052 * speedRpm * 0.10472;
                if(speedMSec>1.25)speedMSec=1.25;
                if(speedMSec<-1.25)speedMSec=-1.25;

// If you have a varying frame rate
                smoothedValue += (estimatedTime/200.0) * ((speedMSec - smoothedValue) / 1.5);
                smoothedValue2 += (estimatedTime/200.0) * ((smoothedValue - smoothedValue2) / 1.5); //System.out.println("speedRpm= " + speedRpm + "\twheelCpt= " + wheelCpt + "\testimatedTime= " + estimatedTime);
                        // motor.drive(255);
                wheelCpt = 0;

                final double kfValue = kf.update(speedMSec);
                System.out.println("speedMSec= " + speedMSec + "\tsmoothedValue= " + smoothedValue+ "\tkfValue= " + kfValue);
                p1.updatePlot(speedMSec);
                p2.updatePlot(smoothedValue, smoothedValue2);

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
