/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iprobot.helpers;

import java.awt.BorderLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ShapeUtilities;

/**
 *
 * @author root
 */
public class TimePlotter extends ApplicationFrame {

    /**
     * The time series data.
     */
    static TimeSeries serie1, serie2;

    public TimePlotter(final String title, String type) {

        super(title);
        serie1 = new TimeSeries("", Millisecond.class);
        final TimeSeriesCollection dataset0 = new TimeSeriesCollection(serie1);
        final JFreeChart chart;
        if (type.contains("2")) {
            serie2 = new TimeSeries("", Millisecond.class);
            final TimeSeriesCollection dataset1 = new TimeSeriesCollection(serie2);
            chart = createChart2D(dataset0, dataset1);
        } else {
            chart = createChart(dataset0);
        }

        final ChartPanel chartPanel = new ChartPanel(chart);
        final JPanel content = new JPanel(new BorderLayout());
        content.add(chartPanel);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(content);
    }

    public void updatePlot(double value) {
        serie1.addOrUpdate(new Millisecond(), value);
    }

    public void updatePlot(double value1, double value2) {
        serie1.addOrUpdate(new Millisecond(), value1);
        serie2.addOrUpdate(new Millisecond(), value2);
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

    public static void main(final String[] args) {
        //create plot
        TimePlotter plot = new TimePlotter("Dynamic live Data", "2D");
        plot.pack();
        RefineryUtilities.centerFrameOnScreen(plot);
        plot.setVisible(true);
        int cpt=0;
        while (true) {

            try {
            plot.updatePlot(Math.sin((cpt)/50.0), -Math.sin((cpt)/50.0));
                Thread.sleep(30);
                cpt++;
            } catch (InterruptedException ex) {
                Logger.getLogger(TimePlotter.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
