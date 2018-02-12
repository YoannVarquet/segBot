/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iprobot.helpers;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.ViewportLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import org.jfree.ui.tabbedui.VerticalLayout;

/**
 *
 * @author root
 */
public class CustomVariableTuner extends JPanel {

    private final ArrayList<SwingSlider> sliders = new ArrayList<>();


    public CustomVariableTuner(int nbSlider, SwingSlider... sliders) {
        if (nbSlider <= 0 || sliders.length != nbSlider) {
            System.err.println("iprobot.helpers.CustomTimePlotterPanel.<init>() BAD ARGUMENTS");
        } else {

            for (int i = 0; i < nbSlider; i++) {
              this.add(sliders[i]);  
            }
            this.setPreferredSize(new java.awt.Dimension(640, 500));
        }
    }

     static public class SwingSlider extends JPanel {
        
        public double value;
        JLabel sliderValueLabel;

        public SwingSlider(String name, int min, int max, int init, int tickMin, int tickMax) {
            JLabel sliderLabel = new JLabel(name, JLabel.CENTER);
            sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            sliderValueLabel = new JLabel(Integer.toString(init), JLabel.CENTER);
            sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            this.setLayout(new VerticalLayout());
            JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, init);
            value = init;

            slider.setMinorTickSpacing(tickMin);
            slider.setMajorTickSpacing(tickMax);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);

            slider.addChangeListener(new javax.swing.event.ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    value = slider.getValue();
                    sliderValueLabel.setText(Double.toString(value));
                }
            });

            // We'll just use the standard numeric labels for now...
            slider.setLabelTable(slider.createStandardLabels(10));
            this.add(sliderLabel, BorderLayout.CENTER);
            this.add(slider, BorderLayout.CENTER);
            this.add(sliderValueLabel, BorderLayout.CENTER);
        }
        
        public SwingSlider(String name, int min, int max, int init, int tickMin, int tickMax, double divider) {
            JLabel sliderLabel = new JLabel(name, JLabel.CENTER);
            sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            sliderValueLabel = new JLabel(Integer.toString(init), JLabel.CENTER);
            sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            this.setLayout(new VerticalLayout());
            JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, init);
            value = init;

            slider.setMinorTickSpacing(tickMin);
            slider.setMajorTickSpacing(tickMax);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);

            slider.addChangeListener(new javax.swing.event.ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    value = slider.getValue()/divider;
                    sliderValueLabel.setText(Double.toString(value));
                }
            });

            // We'll just use the standard numeric labels for now...
            slider.setLabelTable(slider.createStandardLabels(10));
            this.add(sliderLabel, BorderLayout.CENTER);
            this.add(slider, BorderLayout.CENTER);
            this.add(sliderValueLabel, BorderLayout.CENTER);
        }
    }

     
    public static void main(final String[] args) {
        SwingSlider s1= new SwingSlider("p",-100,100,0,5,10,10.0);
        SwingSlider s2= new SwingSlider("i",-100,100,0,1,10,1000.0);
        SwingSlider s3= new SwingSlider("d",-100,100,0,5,10,10.0);

        PanelHolder ph = new PanelHolder(s1,s2,s3);
     while(true){
                    System.out.println("p = "+s1.value);
                    System.out.println("i = "+s2.value);
                    System.out.println("d = "+s3.value);
                    System.out.println();
     }

    }
}
