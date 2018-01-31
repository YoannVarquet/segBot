/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iprobot.helpers;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.tabbedui.VerticalLayout;

/**
 *
 * @author root
 */
public class PanelHolder {
    
    public PanelHolder(JPanel... panels) {
        createAndShowGUI(panels);
    }

    /**
     * The time series data.
     */
    static public JFrame f;
    static public boolean frameCreated = false;

    public static void createAndShowGUI(JPanel... panels) {
        f = new JFrame("JFrame");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new VerticalLayout());
        for (JPanel p : panels) {
            f.add(p);
        }
        f.setResizable(false);
        f.pack();
//        frame.setLocationRelativeTo(null);
        RefineryUtilities.centerFrameOnScreen(f);
        f.setVisible(true);
        f.setResizable(true);
        frameCreated = true;
    }
    
    public static void main(final String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {

        CustomTimePlotterPanel p1 = new CustomTimePlotterPanel(1, -1.5, 1.5, "sin");
        CustomTimePlotterPanel p2 = new CustomTimePlotterPanel(3, -1.5, 1.5, "sin", "-sin", "cos");
        
        PanelHolder ph = new PanelHolder(p1, p2);//createAndShowGUI(p1, p2);

//            }
//        });
        int cpt = 0;
        while (true) {
            if (frameCreated) {
                try {
                    p1.updatePlot(Math.sin((cpt) / 50.0));
                    p2.updatePlot(Math.sin((cpt) / 50.0), -Math.sin((cpt) / 50.0), Math.cos((cpt) / 50.0));
                    Thread.sleep(30);
                    cpt++;
                } catch (InterruptedException ex) {
                    Logger.getLogger(PanelHolder.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }
        
    }
    
}
