/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iprobot.helpers;

import org.apache.commons.math3.filter.DefaultMeasurementModel;
import org.apache.commons.math3.filter.DefaultProcessModel;
import org.apache.commons.math3.filter.MeasurementModel;
import org.apache.commons.math3.filter.ProcessModel;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author root
 */
public class KalmanFilter {
    
    // discrete time interval
        double dt = 0.2d;
// position measurement noise (meter)
        double measurementNoise = 10d;
// acceleration noise (meter/sec^2)
        double accelNoise = 0.2d;

// A = [ 1 dt ]
//     [ 0  1 ]
        RealMatrix A = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
// B = [ dt^2/2 ]
//     [ dt     ]
        RealMatrix B = new Array2DRowRealMatrix(new double[][]{{0}, {0}});
// H = [ 1 0 ]
        RealMatrix H = new Array2DRowRealMatrix(new double[][]{{1d, 0d}});
// x = [ 0 0 ]
        RealVector x = new ArrayRealVector(new double[]{0, 0});

        RealMatrix tmp = new Array2DRowRealMatrix(new double[][]{
            {Math.pow(dt, 4d) / 4d, Math.pow(dt, 3d) / 2d},
            {Math.pow(dt, 3d) / 2d, Math.pow(dt, 2d)}});
// Q = [ dt^4/4 dt^3/2 ]
//     [ dt^3/2 dt^2   ]
        RealMatrix Q = new Array2DRowRealMatrix(new double[][]{{0, 0}, {0, 0}});
// P0 = [ 1 1 ]
//      [ 1 1 ]
        RealMatrix P0 = new Array2DRowRealMatrix(new double[][]{{1, 1}, {1, 1}});
// R = [ measurementNoise^2 ]
        RealMatrix R = new Array2DRowRealMatrix(new double[]{0.01d});

// constant control input, increase velocity by 0.1 m/s per cycle
        RealVector u = new ArrayRealVector(new double[]{1d});
        RealVector mNoise = new ArrayRealVector(1);
        ProcessModel pm = new DefaultProcessModel(A, B, Q, x, P0);
        MeasurementModel mm = new DefaultMeasurementModel(H, R);
        org.apache.commons.math3.filter.KalmanFilter filter = new org.apache.commons.math3.filter.KalmanFilter(pm, mm);

//        RandomGenerator rand = new JDKRandomGenerator();
//        RealVector tmpPNoise = new ArrayRealVector(new double[]{Math.pow(dt, 2d) / 2d, dt});
//        RealVector mNoise = new ArrayRealVector(1);
        public double update(double speedMSec){
                        filter.predict(u);
                // x = A * x + B * u + pNoise
                x = A.operate(x);
                // z = H * x + m_noise
                // simulate the measurement
                mNoise.setEntry(0, speedMSec);

                // z = H * x + m_noise
                RealVector z = H.operate(x).add(mNoise);
                filter.correct(z);

//            double position = filter.getStateEstimation()[0];
                double velocity = filter.getStateEstimation()[1];
                return velocity;
        }
    
    
}
