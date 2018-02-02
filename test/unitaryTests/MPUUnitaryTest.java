package unitaryTests;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import java.io.IOException;
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
public class MPUUnitaryTest {

    static I2CDevice mpu6050;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
            mpu6050 = bus.getDevice(0x68);
            byte data = (byte) mpu6050.read(0x0D);
            System.out.println("byte read=" + data);
        } catch (I2CFactory.UnsupportedBusNumberException | IOException ex) {
            Logger.getLogger(MPUUnitaryTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte buffer[] = new byte[14];
        int ax, ay, az, gx, gy, gz;
        while (true) {
            try {
                mpu6050.read(0x3B, buffer, 0, 14);
                ax = (((int) buffer[0]) << 8) | buffer[1];
//                ay = (((int) buffer[2]) << 8) | buffer[3];
//                az = (((int) buffer[4]) << 8) | buffer[5];
                gx = (((int) buffer[8]) << 8) | buffer[9];
//                gy = (((int) buffer[10]) << 8) | buffer[11];
//                gz = (((int) buffer[12]) << 8) | buffer[13];
                System.out.println("ax:" + ax +  "\tgx:" + gx + "\t");

            } catch (IOException ex) {
                Logger.getLogger(MPUUnitaryTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
