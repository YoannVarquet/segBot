
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinEdge;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import iprobot.helpers.MotorController;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.apache.commons.math3.filter.DefaultMeasurementModel;
import org.apache.commons.math3.filter.DefaultProcessModel;
import org.apache.commons.math3.filter.KalmanFilter;
import org.apache.commons.math3.filter.MeasurementModel;
import org.apache.commons.math3.filter.ProcessModel;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
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
public class IncremetalEncoderUnitaryTest extends ApplicationFrame implements ActionListener {

    /**
     * The time series data.
     */
    private TimeSeries series;

    /**
     * The most recent value added.
     */
    static double lastValue = 0.0;

    /**
     * Constructs a new demonstration application.
     *
     * @param title the frame title.
     */
    public IncremetalEncoderUnitaryTest(final String title) {

        super(title);
        this.series = new TimeSeries("speed", Millisecond.class);
        final TimeSeriesCollection dataset = new TimeSeriesCollection(this.series);
        final JFreeChart chart = createChart(dataset);

        final ChartPanel chartPanel = new ChartPanel(chart);
        final JButton button = new JButton("Add New Data Item");
        button.setActionCommand("ADD_DATA");
        button.addActionListener(this);

        final JPanel content = new JPanel(new BorderLayout());
        content.add(chartPanel);
        content.add(button, BorderLayout.SOUTH);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(content);

    }

    /**
     * Creates a sample chart.
     *
     * @param dataset the dataset.
     *
     * @return A sample chart.
     */
    private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
                "Dynamic Data Demo",
                "Time",
                "Value",
                dataset,
                true,
                true,
                false
        );
        final XYPlot plot = result.getXYPlot();
        org.jfree.chart.axis.ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(60000.0);  // 60 seconds
        axis = plot.getRangeAxis();
        axis.setRange(0.0, 200.0);
        return result;
    }

    // ****************************************************************************
    // * JFREECHART DEVELOPER GUIDE                                               *
    // * The JFreeChart Developer Guide, written by David Gilbert, is available   *
    // * to purchase from Object Refinery Limited:                                *
    // *                                                                          *
    // * http://www.object-refinery.com/jfreechart/guide.html                     *
    // *                                                                          *
    // * Sales are used to provide funding for the JFreeChart project - please    * 
    // * support us so that we can continue developing free software.             *
    // ****************************************************************************
    /**
     * Starting point for the demonstration application.
     *
     * @param args ignored.
     */
    /**
     * @param args the command line arguments
     */
    static GpioPinDigitalInput wheelCoder;
    static boolean wheelCoderUpdated = false;
    static long estimatedTime = 0;
    static long previousTime = 0;
    static final double ANGLE_INCREMENT = Math.PI / 10;
    static int wheelCpt = 0;

    public static void main(final String[] args) {

        final IncremetalEncoderUnitaryTest demo = new IncremetalEncoderUnitaryTest("Dynamic Data Demo");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

//    public static void main(String[] args) {
        // discrete time interval
        double dt = 0.2d;
// position measurement noise (meter)
        double measurementNoise = 10d;
// acceleration noise (meter/sec^2)
        double accelNoise = 0.2d;

// A = [ 1 dt ]
//     [ 0  1 ]
        RealMatrix A = new Array2DRowRealMatrix(new double[][]{{1, dt}, {0, 1}});
// B = [ dt^2/2 ]
//     [ dt     ]
        RealMatrix B = new Array2DRowRealMatrix(new double[][]{{Math.pow(dt, 2d) / 2d}, {dt}});
// H = [ 1 0 ]
        RealMatrix H = new Array2DRowRealMatrix(new double[][]{{1d, 0d}});
// x = [ 0 0 ]
        RealVector x = new ArrayRealVector(new double[]{0, 0});

        RealMatrix tmp = new Array2DRowRealMatrix(new double[][]{
            {Math.pow(dt, 4d) / 4d, Math.pow(dt, 3d) / 2d},
            {Math.pow(dt, 3d) / 2d, Math.pow(dt, 2d)}});
// Q = [ dt^4/4 dt^3/2 ]
//     [ dt^3/2 dt^2   ]
        RealMatrix Q = tmp.scalarMultiply(Math.pow(accelNoise, 2));
// P0 = [ 1 1 ]
//      [ 1 1 ]
        RealMatrix P0 = new Array2DRowRealMatrix(new double[][]{{1, 1}, {1, 1}});
// R = [ measurementNoise^2 ]
        RealMatrix R = new Array2DRowRealMatrix(new double[]{Math.pow(measurementNoise, 2)});

// constant control input, increase velocity by 0.1 m/s per cycle
        RealVector u = new ArrayRealVector(new double[]{0.1d});

        ProcessModel pm = new DefaultProcessModel(A, B, Q, x, P0);
        MeasurementModel mm = new DefaultMeasurementModel(H, R);
        KalmanFilter filter = new KalmanFilter(pm, mm);

//        RandomGenerator rand = new JDKRandomGenerator();
//        RealVector tmpPNoise = new ArrayRealVector(new double[]{Math.pow(dt, 2d) / 2d, dt});
//        RealVector mNoise = new ArrayRealVector(1);
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

                //compute RPM
                wheelCoderUpdated = false;
                //    motor.brake();
                pulsePerSecond = (wheelCpt * 1000.0) / (double) estimatedTime;
                speedRpm = (pulsePerSecond * 60.0) / 20.0;
                System.out.println("speedRpm= " + speedRpm + "\twheelCpt= " + wheelCpt + "\testimatedTime= " + estimatedTime);
                // motor.drive(255);
                wheelCpt = 0;
                previousTime = System.currentTimeMillis();

                filter.predict(u);
                // x = A * x + B * u + pNoise
                x = A.operate(x).add(B.operate(u));
                // z = H * x + m_noise
                RealVector z = H.operate(x);
                filter.correct(z);

//            double position = filter.getStateEstimation()[0];
                double velocity = filter.getStateEstimation()[1];
                lastValue = velocity;
                System.out.println("speedRpm= " + speedRpm + "\tvelocity= " + velocity + "\testimatedTime= " + estimatedTime);

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

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {

        if (e.getActionCommand().equals("ADD_DATA")) {
            final Millisecond now = new Millisecond();
            System.out.println("Now = " + now.toString());
            this.series.add(new Millisecond(), lastValue);
        }
    }

}
