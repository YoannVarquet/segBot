package unitaryTests;

import iprobot.helpers.MPU6050;
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

    static MPU6050 mpu= new MPU6050();
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        while (true) {
            try {
                int[] acc = mpu.getAcceleration();
                System.out.println("ax: "+ acc[0]);
                System.out.println("ay: "+ acc[1]);
                System.out.println("az: "+ acc[2]);
                System.out.println();
                Thread.sleep(60);
                

            } catch (InterruptedException ex) {
                Logger.getLogger(MPUUnitaryTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
