package iprobot.helpers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yoann
 */
public class MotorController {

    GpioController gpio;
    GpioPinDigitalOutput In1pin, In2pin, STBYpin;
    GpioPinPwmOutput PWMpin;
    int PWMRange;

    public GpioController getGpio() {
        return gpio;
    }

    public void setGpio(GpioController gpio) {
        this.gpio = gpio;
    }

    public GpioPinDigitalOutput getIn1pin() {
        return In1pin;
    }

    public void setIn1pin(GpioPinDigitalOutput In1pin) {
        this.In1pin = In1pin;
    }

    public GpioPinDigitalOutput getIn2pin() {
        return In2pin;
    }

    public void setIn2pin(GpioPinDigitalOutput In2pin) {
        this.In2pin = In2pin;
    }

    public GpioPinDigitalOutput getSTBYpin() {
        return STBYpin;
    }

    public void setSTBYpin(GpioPinDigitalOutput STBYpin) {
        this.STBYpin = STBYpin;
    }

    public GpioPinPwmOutput getPWMpin() {
        return PWMpin;
    }

    public void setPWMpin(GpioPinPwmOutput PWMpin) {
        this.PWMpin = PWMpin;
    }

    public int getPWMRange() {
        return PWMRange;
    }

    public void setPWMRange(int PWMRange) {
        this.PWMRange = PWMRange;
    }

    public MotorController(GpioController gpioController, Pin in1pin, Pin in2pin, Pin pWMpin, Pin sTBYpin) {
        gpio = gpioController;
        In1pin = gpio.provisionDigitalOutputPin(in1pin, PinState.LOW);
        In2pin = gpio.provisionDigitalOutputPin(in2pin, PinState.LOW);
        PWMpin = gpio.provisionPwmOutputPin(pWMpin);
        boolean provisionStandByPin = true;
        for (GpioPin p : gpioController.getProvisionedPins()) {
            if (sTBYpin.getName().equals(p.getName())) {
                provisionStandByPin = false;
            }
        }
        if (provisionStandByPin) {
            STBYpin = gpio.provisionDigitalOutputPin(sTBYpin, PinState.LOW);
        }
        com.pi4j.wiringpi.Gpio.pwmSetMode(com.pi4j.wiringpi.Gpio.PWM_MODE_MS);
        com.pi4j.wiringpi.Gpio.pwmSetRange(255);
        com.pi4j.wiringpi.Gpio.pwmSetClock(500);
        PWMRange = 255;

    }

    public MotorController(GpioController gpioController, Pin in1pin, Pin in2pin, Pin pWMpin, Pin sTBYpin, int pWMRange) {
        gpio = gpioController;
        In1pin = gpio.provisionDigitalOutputPin(in1pin, PinState.LOW);
        In2pin = gpio.provisionDigitalOutputPin(in2pin, PinState.LOW);
        PWMpin = gpio.provisionPwmOutputPin(pWMpin);
        STBYpin = gpio.provisionDigitalOutputPin(sTBYpin, PinState.LOW);
        com.pi4j.wiringpi.Gpio.pwmSetMode(com.pi4j.wiringpi.Gpio.PWM_MODE_MS);
        com.pi4j.wiringpi.Gpio.pwmSetRange(pWMRange);
        com.pi4j.wiringpi.Gpio.pwmSetClock(500);
        PWMRange = pWMRange;

    }

    public void drive(int speed) {

        if (Math.abs(speed) > PWMRange) {
            speed = (int) (Math.signum(speed) * PWMRange);
        }
        if (STBYpin != null) // can be null as the standby pin is shared to several motor, only one of them can control the standby pin
        {
            STBYpin.setState(PinState.HIGH);
        }
        if (speed >= 0) {
            forward(speed);
        } else {
            reverse(-speed);
        }
    }

    public void drive(int speed, int durationMs) {
        drive(speed);
        try {
            Thread.sleep(durationMs);
        } catch (InterruptedException ex) {
            Logger.getLogger(MotorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void forward(int speed) {
        In1pin.setState(PinState.HIGH);
        In2pin.setState(PinState.LOW);
        PWMpin.setPwm(speed);

    }

    private void reverse(int speed) {
        In1pin.setState(PinState.LOW);
        In2pin.setState(PinState.HIGH);
        PWMpin.setPwm(speed);

    }

    public void brake() {
        In2pin.setState(PinState.HIGH);
        In1pin.setState(PinState.HIGH);
        PWMpin.setPwm(0);

    }

    public void changeDirection() {
        In2pin.setState(!(In2pin.getState().isHigh()));
        In1pin.setState(!(In1pin.getState().isHigh()));

    }

    public void standby() {
        STBYpin.setState(PinState.LOW);
    }

    static public void forward(MotorController motor1, MotorController motor2, int speed) {
        motor1.drive(speed);
        motor2.drive(speed);
    }

    static public void forward(MotorController motor1, MotorController motor2) {
        motor1.drive(motor1.PWMRange / 2);
        motor2.drive(motor2.PWMRange / 2);
    }

    static public void back(MotorController motor1, MotorController motor2, int speed) {
        int temp = Math.abs(speed);
        motor1.drive(-temp);
        motor2.drive(-temp);
    }

    static public void back(MotorController motor1, MotorController motor2) {
        motor1.drive(-motor1.PWMRange / 2);
        motor2.drive(-motor2.PWMRange / 2);
    }

    static public void left(MotorController left, MotorController right, int speed) {
        int temp = Math.abs(speed) / 2;
        left.drive(-temp);
        right.drive(temp);

    }

    static public void right(MotorController left, MotorController right, int speed) {
        int temp = Math.abs(speed) / 2;
        left.drive(temp);
        right.drive(-temp);

    }

    static public void brake(MotorController motor1, MotorController motor2) {
        motor1.brake();
        motor2.brake();
    }

}
