/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unitaryTests;

import boofcv.abst.tracker.TrackerObjectQuad;
import boofcv.factory.tracker.FactoryTrackerObjectQuad;
import boofcv.gui.image.ShowImages;
import boofcv.gui.tracker.TrackerObjectQuadPanel;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.webcamcapture.UtilWebcamCapture;
import boofcv.misc.BoofMiscOps;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.ImageBase;
import com.github.sarxos.webcam.Webcam;
import georegression.struct.shapes.Quadrilateral_F64;
import iprobot.helpers.TrackerObjectQuadPanel_Modified;
import java.awt.Color;
import java.awt.image.BufferedImage;


/**
 * Demonstration on how to use the high level {@link TrackerObjectQuad}
 * interface for tracking objects in a video sequence. This interface allows the
 * target to be specified using an arbitrary quadrilateral. Specific
 * implementations might not support that shape, so they instead will track an
 * approximation of it. The interface also allows information on target
 * visibility to be returned. As is usually the case, tracker specific
 * information is lost in the high level interface and you should consider using
 * the trackers directly if more control is needed.
 *
 * This is an active area of research and all of the trackers eventually diverge
 * given a long enough sequence.
 *
 * @author Peter Abeles
 */
public class ExampleTrackerObjectQuad {

       static boolean selected = false;
    public static void main(String[] args) {
//		DefaultMediaManager media = DefaultMediaManager.INSTANCE;
//		String fileName = UtilIO.pathExample("tracking/wildcat_robot.mjpeg");

        // Create the tracker.  Comment/Uncomment to change the tracker.
        TrackerObjectQuad tracker
                = FactoryTrackerObjectQuad.circulant(null, GrayU8.class);
//				FactoryTrackerObjectQuad.sparseFlow(null,GrayU8.class,null);
//				FactoryTrackerObjectQuad.tld(null,GrayU8.class);
//				FactoryTrackerObjectQuad.meanShiftComaniciu2003(new ConfigComaniciu2003(), ImageType.pl(3, GrayU8.class));
//				FactoryTrackerObjectQuad.meanShiftComaniciu2003(new ConfigComaniciu2003(true),ImageType.pl(3,GrayU8.class));

        // Mean-shift likelihood will fail in this video, but is excellent at tracking objects with
        // a single unique color.  See ExampleTrackerMeanShiftLikelihood
//				FactoryTrackerObjectQuad.meanShiftLikelihood(30,5,255, MeanShiftLikelihoodType.HISTOGRAM,ImageType.pl(3,GrayU8.class));
//		SimpleImageSequence video = media.openVideo(fileName, tracker.getImageType());
        // Open a webcam at a resolution close to 640x480
        Webcam webcam = UtilWebcamCapture.openDefault(640, 480);

        BufferedImage image = webcam.getImage();
        ImageBase frame = ConvertBufferedImage.convertFrom(image, (GrayU8) null);

        TrackerObjectQuadPanel_Modified gui = new TrackerObjectQuadPanel_Modified(new TrackerObjectQuadPanel_Modified.Listener() {
            @Override
            public void selectedTarget(Quadrilateral_F64 target) {
                System.out.println("estoy aqui");
                selected = true;
            }

            @Override
            public void pauseTracker() {
                System.out.println("nothing to pause");
            }
        });
        // For displaying the results
        gui.setPreferredSize(webcam.getViewSize());
        gui.setImageUI(image);
//        gui.setTarget(location, true);
        ShowImages.showWindow(gui, "Tracking Results", true);
        gui.enterSelectMode();
        while (!selected) {
            image = webcam.getImage();
            frame = ConvertBufferedImage.convertFrom(image, (GrayU8) null);
            gui.setImageUI(image);
            gui.repaint();
            BoofMiscOps.pause(50);
        }
        
        gui.enterIdleMode();
        // specify the target's initial location and initialize with the first frame
        Quadrilateral_F64 location = gui.quad.copy();
        tracker.initialize(frame, location);

        // Track the object across each video frame and display the results
        long previous = 0;
        while (true) {
            image = webcam.getImage();
            frame = ConvertBufferedImage.convertFrom(image, (GrayU8) null);
            boolean visible = tracker.process(frame, location);
            if(visible)  image.getGraphics().setColor(Color.green);
            else image.getGraphics().setColor(Color.red);
            image.getGraphics().drawOval(10, 10, 5, 5);
            if(visible)  image.getGraphics().drawString("FOUND", 20, 10);
            else image.getGraphics().drawString("NOT found", 20, 10);
            gui.setImageUI(image);
            gui.setTarget(location, visible);
            gui.repaint();

            // shoot for a specific frame rate
            long time = System.currentTimeMillis();
            BoofMiscOps.pause(Math.max(0, 50 - (time - previous)));
            previous = time;
        }
    }
}
