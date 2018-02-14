/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iprobot.helpers;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinEdge;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 *
 * @author root
 */
public class WheelEncoder /*implements Runnable*/ {

    //variable used to compute the speed
    private final int PERIOD = 100;
    private int count;
    private int tickPerRevolution;
    private double speed;
    private double smoothedSpeed;
    private GpioPinDigitalInput wheelCoder;
    private long estimatedTime;
    private long previousTime;
    private boolean wheelCoderUpdated;

    //variables used to convert in RPM: m * pwm + b (obtained experimentally, different for all motors)
    double m;
    double b;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTickPerRevolution() {
        return tickPerRevolution;
    }

    public void setTickPerRevolution(int tickPerRevolution) {
        this.tickPerRevolution = tickPerRevolution;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSmoothedSpeed() {
        return smoothedSpeed;
    }

    public void setSmoothedSpeed(double smoothedSpeed) {
        this.smoothedSpeed = smoothedSpeed;
    }

    public GpioPinDigitalInput getWheelCoder() {
        return wheelCoder;
    }

    public void setWheelCoder(GpioPinDigitalInput wheelCoder) {
        this.wheelCoder = wheelCoder;
    }

    public double getM() {
        return m;
    }

    public void setM(double m) {
        this.m = m;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    @SuppressWarnings("")
    public WheelEncoder(Pin pin) {
        //Get the GPIO controller
        final GpioController gpio = GpioFactory.getInstance();
        // set wheel coder pin
        this.wheelCoder = gpio.provisionDigitalInputPin(pin, PinPullResistance.PULL_DOWN);

        // create and register gpio pin listener
        estimatedTime = System.currentTimeMillis();
        wheelCoder.addListener((GpioPinListenerDigital) (GpioPinDigitalStateChangeEvent event) -> {
            if (event.getEdge() == PinEdge.RISING) {
                count++;
//                System.out.println(count);
            }
            estimatedTime = System.currentTimeMillis() - previousTime;
            if (estimatedTime > PERIOD && !wheelCoderUpdated) {
                wheelCoderUpdated = true;
            }
        });
//        this.run();
        Thread t = new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (wheelCoderUpdated) {
                        //compute RPM
                        speed = computeRpm();
                        //low pass filter on the speed (filters the noise)
                        smoothedSpeed += (estimatedTime / PERIOD) * ((speed - smoothedSpeed) / 1.5);
                        previousTime = System.currentTimeMillis();
                    }
                }

            }
        };
        t.start();
    }

    @SuppressWarnings("")
    public WheelEncoder(Pin pin, int tickPerRevolution, double m, double b) {
        this.tickPerRevolution = tickPerRevolution;
        this.m = m;
        this.b = b;
        //Get the GPIO controller
        final GpioController gpio = GpioFactory.getInstance();
        // set wheel coder pin
        this.wheelCoder = gpio.provisionDigitalInputPin(pin, PinPullResistance.PULL_DOWN);

        // create and register gpio pin listener
        estimatedTime = System.currentTimeMillis();
        wheelCoder.addListener((GpioPinListenerDigital) (GpioPinDigitalStateChangeEvent event) -> {
            if (event.getEdge() == PinEdge.RISING) {
                count++;
//                System.out.println(count);
            }
            estimatedTime = System.currentTimeMillis() - previousTime;
            if (estimatedTime > 50 && !wheelCoderUpdated) {
                wheelCoderUpdated = true;
            }
        });
//        this.run();
        Thread t = new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (wheelCoderUpdated) {
                        //compute RPM
                        speed = computeRpm();
                        //low pass filter on the speed (filters the noise)
                        smoothedSpeed += (estimatedTime / 200.0) * ((speed - smoothedSpeed) / 1.5);
                        previousTime = System.currentTimeMillis();
                    }
                }

            }
        };
        t.start();
    }

//    @Override
//    public void run() {
//        while (true) {
//            if (wheelCoderUpdated) {
//                //compute RPM
//                speed = computeRpm();
//                //low pass filter on the speed (filters the noise)
//                smoothedSpeed += (estimatedTime / 200.0) * ((speed - smoothedSpeed) / 1.5);
//                previousTime = System.currentTimeMillis();
//            }
//        }
//
//    }
    public double computeRpm() {
        //compute RPM
        double pulsePerSecond = (this.count * 1000.0) / (double) estimatedTime;
        double speedRpm = (pulsePerSecond * 60.0) / this.tickPerRevolution;
//        speedMSec = 0.052 * speedRpm * 0.10472;
        this.count = 0;
        wheelCoderUpdated = false;
        return speedRpm;
    }

    public double PWMtoRPM(int pwm) {
        return (m * pwm + b);
    }

    public int RPMtoPWM(double rpm) {
        return (int) Math.round((rpm - b) / m);
    }

}
