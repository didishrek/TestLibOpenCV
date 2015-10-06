package fr.opencvtest.hello;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
//import org.opencv.imgcodecs.Imgcodecs;

public class Hello extends JPanel
{
	private static final long serialVersionUID = -1453555929208653193L;
	BufferedImage image;
	JFrame frame0 = new JFrame();
	private static boolean cont = true;
	
	public static void main( String[] args ){
	   System.loadLibrary("opencv_java300");
	   
	   Hello t = new Hello();
	   VideoCapture camera = new VideoCapture(0);
	   Mat frame = new Mat();
	   
	   camera.open(0);
	   if(!camera.isOpened()){
           System.out.println("Error");
       }
       else {
    	   int i = 0;
           while(cont){        
               if (camera.read(frame)){
                   BufferedImage image = t.MatToBufferedImage(frame);
                   if (i == 0){
                	   t.window(image, "Webcam", 0, 0);
                	   i = 1;
                   }
                   else
                	   t.updatewin(image);
               }
           }   
       }
       camera.release();
   }
	
	
	@Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

    public Hello() {
    }

    public Hello(BufferedImage img) {
        image = img;
    }   

    //Show image on window
    
    public void updatewin(BufferedImage img){
    	frame0.getContentPane().removeAll();
        frame0.getContentPane().add(new Hello(img));
        frame0.getContentPane().revalidate();
        frame0.getContentPane().repaint();
    }
    
    public void window(BufferedImage img, String text, int x, int y) {
    	updatewin(img);
    	frame0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame0.setTitle(text);
        frame0.setSize(img.getWidth(), img.getHeight() + 30);
        frame0.setLocation(x, y);
        frame0.setVisible(true);
    }

    //Load an image
    public BufferedImage loadImage(String file) {
        BufferedImage img;

        try {
            File input = new File(file);
            img = ImageIO.read(input);

            return img;
        } catch (Exception e) {
            System.err.println("error");
        }

        return null;
    }

    //Save an image
    public void saveImage(BufferedImage img) {        
        try {
            File outputfile = new File("Images/new.png");
            ImageIO.write(img, "png", outputfile);
        } catch (Exception e) {
            System.out.println("error");
        }
    }

    public BufferedImage MatToBufferedImage(Mat frame) {
        //Mat() to BufferedImage
        int type = 0;
        if (frame.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (frame.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        frame.get(0, 0, data);

        return image;
    }
}
