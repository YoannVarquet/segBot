/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iprobot.helpers;

import java.awt.LayoutManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
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

    public PanelHolder(LayoutManager layout, JPanel... panels) {
        createAndShowGUI(layout, panels);
    }

    /**
     * The time series data.
     */
    static public JFrame f;
    static public boolean frameCreated = false;

    public static void createAndShowGUI(JPanel... panels) {
        f = new JFrame("JFrame");
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        f.setLayout(new VerticalLayout());
        for (JPanel p : panels) {
            f.add(p);
        }
        f.setResizable(false);
        f.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {System.out.println(""+e.getKeyChar());
                if (e.getKeyCode()== KeyEvent.VK_S) {
                    final BufferedImage img = new BufferedImage(f.getWidth(), f.getHeight(), BufferedImage.TYPE_INT_RGB);
                    f.paint(img.getGraphics());
                     String name = Integer.toString((int)(Math.random()*10000))+".jpeg";
                    try {
                        ImageIO.write(img, "jpeg", new File("/home/pi/Desktop/"+name));
                        System.out.println("/home/pi/Desktop/"+name+ " saved");
                    } catch (IOException ex) {
                        Logger.getLogger(PanelHolder.class.getName()).log(Level.SEVERE, null, ex);
                        System.err.println("/home/pi/Desktop/"+name+ " NOT saved");
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
            final BufferedImage img = new BufferedImage(f.getWidth(), f.getHeight(), BufferedImage.TYPE_INT_RGB);
                    f.paint(img.getGraphics());
                     String name = Integer.toString((int)(Math.random()*10000))+".jpeg";
                    try {
                        ImageIO.write(img, "jpeg", new File("/home/pi/Desktop/"+name));
                        System.out.println("/home/pi/Desktop/"+name+ " saved");
                    } catch (IOException ex) {
                        Logger.getLogger(PanelHolder.class.getName()).log(Level.SEVERE, null, ex);
                        System.err.println("/home/pi/Desktop/"+name+ " NOT saved");
                    }
                    f.dispose();
                    System.exit(0);
            }
            
});
        f.pack();
//        frame.setLocationRelativeTo(null);
        RefineryUtilities.centerFrameOnScreen(f);
        f.setVisible(true);
        f.setResizable(true);
        frameCreated = true;
    }

    public static void createAndShowGUI(LayoutManager layout, JPanel... panels) {
        f = new JFrame("JFrame");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(layout);
        for (JPanel p : panels) {
            f.add(p);
        }
        f.setResizable(false);
        f.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {System.out.println(""+e.getKeyChar());
                if (e.getKeyCode()== KeyEvent.VK_S) {
                    final BufferedImage img = new BufferedImage(f.getWidth(), f.getHeight(), BufferedImage.TYPE_INT_RGB);
                    f.paint(img.getGraphics());
                     String name = Integer.toString((int)(Math.random()*10000))+".jpeg";
                    try {
                        ImageIO.write(img, "jpeg", new File("/home/pi/Desktop/"+name));
                        System.out.println("/home/pi/Desktop/"+name+ " saved");
                    } catch (IOException ex) {
                        Logger.getLogger(PanelHolder.class.getName()).log(Level.SEVERE, null, ex);
                        System.err.println("/home/pi/Desktop/"+name+ " NOT saved");
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
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
