
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
public class IncremetalEncoderUnitaryTest {

    /**
     * @param args the command line arguments
     */
    static GpioPinDigitalInput wheelCoder;
    static boolean wheelCoderUpdated = false;
    static long estimatedTime = 0;
    static long previousTime = 0;
    static final double ANGLE_INCREMENT = Math.PI / 10;
    static int wheelCpt = 0;

    public static void main(String[] args) {
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        MotorController motor = new MotorController(gpio, RaspiPin.GPIO_04, RaspiPin.GPIO_02, RaspiPin.GPIO_01, RaspiPin.GPIO_03);

        // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
        wheelCoder = gpio.provisionDigitalInputPin(RaspiPin.GPIO_25, PinPullResistance.PULL_DOWN);        // set shutdown state for this input pin
        wheelCoder.setShutdownOptions(true);
        System.out.println("WheelEncoder setup");

        // create and register gpio pin listener
        estimatedTime = System.currentTimeMillis();
        wheelCoder.addListener((GpioPinListenerDigital) (GpioPinDigitalStateChangeEvent event) -> {
            if (event.getEdge() == PinEdge.RISING) {
                wheelCpt++;
            }
//            motor.brake();
            estimatedTime = System.currentTimeMillis() - previousTime;
            if (estimatedTime > 200 && !wheelCoderUpdated) {
                wheelCoderUpdated = true;
            }

//            motor.drive(255);
        });

        //PID 
        double error_prior = 0;
        double integral = 0;
        double kP = 10;
        double kI = 0.01;
        double kD = 1;
        motor.brake();
        double speedRpm, pulsePerSecond;
        motor.drive(255);
        while (true) {
            if (wheelCoderUpdated) {
                wheelCoderUpdated = false;
                //    motor.brake();
                pulsePerSecond = (wheelCpt * 1000.0) / (double) estimatedTime;
                speedRpm = (pulsePerSecond * 60.0) / 20.0;
                System.out.println("speedRpm= " + speedRpm + "\twheelCpt= " + wheelCpt + "\testimatedTime= " + estimatedTime);
                // motor.drive(255);
                wheelCpt = 0;
                previousTime = System.currentTimeMillis();
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
