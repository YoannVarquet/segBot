/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iprobot.helpers;

import static demo.NormalDistributionDemo.createChart;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;

/**
 *
 * @author root
 */
public class TimePlotter extends ApplicationFrame {

    /**
     * The time series data.
     */
    static TimeSeries series;

    /**
     * The most recent value added.
     */
    //to plot the graph
    static double lastValue = 0.0;

    public TimePlotter(final String title) {

        super(title);
        this.series = new TimeSeries("speed", Millisecond.class);
        final TimeSeriesCollection dataset0 = new TimeSeriesCollection(this.series);
        final JFreeChart chart = createChart(dataset0);

        final ChartPanel chartPanel = new ChartPanel(chart);
        final JPanel content = new JPanel(new BorderLayout());
        content.add(chartPanel);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(content);

    }

    public void updatePlot(double value) {
        lastValue = value;
        series.add(new Millisecond(), lastValue);
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
        axis.setRange(-1.25, 1.250);
        return result;
    }

}
