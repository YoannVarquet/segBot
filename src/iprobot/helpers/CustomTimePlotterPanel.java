/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iprobot.helpers;

import java.awt.Component;
import java.util.ArrayList;
import javafx.util.Pair;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

/**
 *
 * @author root
 */
public class CustomTimePlotterPanel extends JPanel {

    /**
     * The time series data.
     */
//    static private TimeSeries serie, serie1, serie2;
    //static public CustomTimePlotter timePlot, timePlot2;
//    private final ArrayList<CustomTimePlotterPanel> timePlotterList = new ArrayList<>();
    private final ArrayList<TimeSeries> seriePlotter = new ArrayList<>();
    private final ArrayList<TimeSeriesCollection> datasetPlotter = new ArrayList<>();
//    private final boolean frameCreated = false;
    private final ChartPanel chartPanel;

    public CustomTimePlotterPanel(int nbSeries, String... names) {
        if (nbSeries <= 0 || names.length != nbSeries) {
            chartPanel = null;
            System.err.println("iprobot.helpers.CustomTimePlotterPanel.<init>() BAD ARGUMENTS");
        } else {

            for (int i = 0; i < nbSeries; i++) {
                final TimeSeries timeSerieTmp = new TimeSeries(names[i], Millisecond.class);
                seriePlotter.add(timeSerieTmp);
                datasetPlotter.add(new TimeSeriesCollection(timeSerieTmp));
            }
            chartPanel = new ChartPanel(createChart(datasetPlotter));
            this.add(chartPanel);
            chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        }
    }

    public CustomTimePlotterPanel(int nbSeries, double rangeMin, double rangeMax, String... names) {
        if (nbSeries <= 0 || names.length != nbSeries) {
            chartPanel = null;
            System.err.println("iprobot.helpers.CustomTimePlotterPanel.<init>() BAD ARGUMENTS");
        } else {

            for (int i = 0; i < nbSeries; i++) {
                final TimeSeries timeSerieTmp = new TimeSeries(names[i], Millisecond.class);
                seriePlotter.add(timeSerieTmp);
                datasetPlotter.add(new TimeSeriesCollection(timeSerieTmp));
            }
            chartPanel = new ChartPanel(createChart(datasetPlotter));
            this.add(chartPanel);
            chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
            setRange(rangeMin, rangeMax);
        }
    }

    public void setRange(double rangeMin, double rangeMax) {
        if (chartPanel != null) {
            final XYPlot plot = chartPanel.getChart().getXYPlot();

            org.jfree.chart.axis.ValueAxis axis = plot.getDomainAxis();
            axis.setAutoRange(true);
            axis.setFixedAutoRange(60000.0);  // 60 seconds
            axis = plot.getRangeAxis();
            axis.setRange(rangeMin, rangeMax);
        }
    }

    public void updatePlot(double... value) {
        if (value.length != seriePlotter.size()) {
            System.err.println("iprobot.helpers.CustomTimePlotterPanel.updatePlot() BAD ARGUMENTS");
        } else {
            for (int i = 0; i < value.length; i++) {
                seriePlotter.get(i).addOrUpdate(new Millisecond(), value[i]);
            }
        }
    }

    private JFreeChart createChart(final ArrayList<TimeSeriesCollection> datasets) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart("Dynamic Data Demo", "Time", "Value", null, true, true, false);
        final XYPlot plot = result.getXYPlot();
        for (int i = 0; i < datasets.size(); i++) {
            plot.setDataset(i, datasets.get(i));
            plot.setRenderer(i, new XYLineAndShapeRenderer(true, false));

        }

        org.jfree.chart.axis.ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(60000.0);  // 60 seconds
        axis = plot.getRangeAxis();
        axis.setRange(-300, 300);
        return result;
    }

//    public static void main(final String[] args) {
//
//        int cpt = 0;
//        while (true) {
//            if (frameCreated) {
//                try {
////                    timePlot2.updatePlot(Math.sin((cpt) / 50.0), -Math.sin((cpt) / 50.0));
////                    timePlot.updatePlot(Math.sin((cpt) / 50.0));
//                    Thread.sleep(30);
//                    cpt++;
//
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(CustomTimePlotterPanel.class
//                            .getName()).log(Level.SEVERE, null, ex);
//                }
//
//            }
//        }
//
//    }
}
