/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iprobot.helpers;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yoann
 */
public class I2CHelper {

    public I2CDevice device;

    public I2CHelper(int busNumber, byte devAddr) {
        try {
            I2CBus bus = I2CFactory.getInstance(busNumber);
            device = bus.getDevice(devAddr);
        } catch (I2CFactory.UnsupportedBusNumberException | IOException ex) {
            Logger.getLogger(I2CHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int readBit(int regAddr, int bitNum){
        try {
            return device.read(regAddr) & (1 << bitNum);
        } catch (IOException ex) {
            Logger.getLogger(I2CHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    
    public int readBits(int regAddr, int bitNum, int length){
        try {
            int b = device.read(regAddr);
            for(int i = 0 ; i<8 ; i++)
            {
                if(i<bitNum | i>bitNum+length)
                    b &= ~(1 << bitNum+i);
            }
            return b;
        } catch (IOException ex) {
            Logger.getLogger(I2CHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return -1;
    }

    public int readByte(int regAddr){
        try {
            return device.read(regAddr);
        } catch (IOException ex) {
            Logger.getLogger(I2CHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    int[] readBytes(int regAddr, int length){
            int[] data = new int[length];
            int[] data2 = new int[length];
            byte buffer[] = new byte[length];
        try {
            device.read(regAddr, buffer, 0, length);
            
            for (int i = 0; i < length; i++) {
                data[i] = readByte(regAddr + i);
                data2[i] = (int) buffer[i];
                System.out.println("data :" + data[i] + "data2 :" + data[i]);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(I2CHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

            return data;
    }
    
    byte[] readBytes_b(int regAddr, int length){
            byte[] data = new byte[length];
            byte[] data2 = new byte[length];
            byte buffer[] = new byte[length];
        try {
            device.read(regAddr, buffer, 0, length);
            
            for (int i = 0; i < length; i++) {
                data[i] = (byte) readByte(regAddr + i);
                data2[i] =  buffer[i];
                System.out.println("data :" + data[i] + "data2 :" + data[i]);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(I2CHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

            return data;
    }

    void writeByte(int regAddr, byte val)  {
        try {
            device.write(regAddr, val);
        } catch (IOException ex) {
            Logger.getLogger(I2CHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void writeBytes(int regAddr, byte[] val) {
        try {
            device.write(regAddr, val);
        } catch (IOException ex) {
            Logger.getLogger(I2CHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void writeBit(int regAddr, int bitNum, byte val) {
        try {
            byte b = (byte) readByte(regAddr);
            if (val == 0) {
                b &= ~(1 << bitNum);
            } else {
                b |= (1 << bitNum);
            }
            device.write(regAddr, b);
        } catch (IOException ex) {
            Logger.getLogger(I2CHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void writeBits(int regAddr, int bitNum, int length, byte val)  {
        try {
            byte b = (byte) readByte(regAddr);
            for (int i = 0; i < length; i++) {
                if ((val&(1 << bitNum+i)) == 0) {
                    b &= ~(1 << bitNum+i);
                } else {
                    b |= (1 << bitNum+i);
                }
            }
            device.write(regAddr, b);
        } catch (IOException ex) {
            Logger.getLogger(I2CHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    void writeBits(int regAddr, int bitNum, int length, int val)  {
        try {
            byte b = (byte) readByte(regAddr);
            for (int i = 0; i < length; i++) {
                if ((val&(1 << bitNum+i)) == 0) {
                    b &= ~(1 << bitNum+i);
                } else {
                    b |= (1 << bitNum+i);
                }
            }
            device.write(regAddr, b);
        } catch (IOException ex) {
            Logger.getLogger(I2CHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
