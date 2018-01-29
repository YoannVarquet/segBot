/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iprobot.helpers;

import java.awt.BorderLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author root
 */
public class CustomTimePlotter extends JPanel {

    /**
     * The time series data.
     */
    static public TimeSeries serie, serie1, serie2;
    static public JFrame f;
    static public CustomTimePlotter timePlot, timePlot2;
    static public boolean frameCreated = false;

    public CustomTimePlotter(final String title, String type) {

//        super(title);
        serie = new TimeSeries("", Millisecond.class);
        final TimeSeriesCollection dataset = new TimeSeriesCollection(serie);
        final JFreeChart chart;
        if (type.contains("2")) {
            serie2 = new TimeSeries("", Millisecond.class);
            serie1 = new TimeSeries("", Millisecond.class);
            final TimeSeriesCollection dataset0 = new TimeSeriesCollection(serie1);
            final TimeSeriesCollection dataset1 = new TimeSeriesCollection(serie2);
            chart = createChart2D(dataset0, dataset1);
        } else {
            chart = createChart(dataset);
        }

        final ChartPanel chartPanel = new ChartPanel(chart);
        //final JPanel content = new JPanel(new BorderLayout());
        this.add(chartPanel);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
//        setContentPane(content);
    }

    public void updatePlot(double value) {
        serie.addOrUpdate(new Millisecond(), value);
    }

    public void updatePlot(double value1, double value2) {
        serie1.addOrUpdate(new Millisecond(), value1);
        serie2.addOrUpdate(new Millisecond(), value2);
    }

    public CustomTimePlotter() {
        JLabel label1 = new JLabel("Tell me something");
        label1.setToolTipText("Click if you need help");

        add(label1);
        JButton button1 = new JButton("OK");
        button1.setBorderPainted(true);
        button1.setContentAreaFilled(true);
        button1.setToolTipText("This is my button");
        add(button1);
        JTextField txtField = new JTextField("Type here", 15);

        txtField.setToolTipText("It's a field");
        add(txtField);
    }

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
//        axis.setRange(-1.25, 1.250);
        axis.setAutoRange(true);
        return result;
    }

    private JFreeChart createChart2D(final XYDataset dataset1, final XYDataset dataset2) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
                "Dynamic Data Demo",
                "Time",
                "Value",
                null,
                true,
                true,
                false
        );
        final XYPlot plot = result.getXYPlot();
        AbstractXYItemRenderer timeSeriesRenderer1 = new XYLineAndShapeRenderer(true, false);
        AbstractXYItemRenderer timeSeriesRenderer2 = new XYLineAndShapeRenderer(true, false);
        plot.setDataset(0, dataset1);
        plot.setDataset(1, dataset2);
        plot.setRenderer(0, timeSeriesRenderer1);
        plot.setRenderer(1, timeSeriesRenderer2);
//        plot.getRenderer().setBaseShape(ShapeUtilities.createDiamond(0.5f));
        org.jfree.chart.axis.ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(60000.0);  // 60 seconds
        axis = plot.getRangeAxis();
//        axis.setRange(-1.25, 1.250);
        axis.setAutoRange(true);
        return result;
    }

    public static void createAndShowGUI(CustomTimePlotter p1, CustomTimePlotter p2) {
        timePlot2 = p1;
        timePlot = p2;
//         timePlot = new CustomTimePlotter();
//         timePlot2 = new CustomTimePlotter();
        f = new JFrame("Demo TimePlotter");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.add(timePlot, BorderLayout.NORTH);
        f.add(timePlot2, BorderLayout.SOUTH);
        f.setResizable(false);
        f.pack();
//        frame.setLocationRelativeTo(null);
        RefineryUtilities.centerFrameOnScreen(f);
        f.setVisible(true);
        frameCreated = true;
    }

    public static void main(final String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {

        CustomTimePlotter p1 = new CustomTimePlotter("Dynamic Live Data","2D");
        CustomTimePlotter p2 = new CustomTimePlotter("Dynamic Live Data","1D");
        createAndShowGUI(p2,p1);

//            }
//        });
        int cpt = 0;
        while (true) {
            if (frameCreated) {
                try {
                    timePlot2.updatePlot(Math.sin((cpt) / 50.0), -Math.sin((cpt) / 50.0));
                    timePlot.updatePlot(Math.sin((cpt) / 50.0));
                    Thread.sleep(30);
                    cpt++;
                } catch (InterruptedException ex) {
                    Logger.getLogger(CustomTimePlotter.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

    }

}
