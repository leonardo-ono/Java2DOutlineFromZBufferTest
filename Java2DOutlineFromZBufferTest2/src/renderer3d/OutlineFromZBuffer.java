package renderer3d;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * 2D Outline from Z-Buffer.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com);
 */
public class OutlineFromZBuffer extends JPanel {
    
    private final DepthBuffer depthBuffer;
    private final BufferedImage outline;
    private final Graphics2D og;
    private final int width;
    private final int height;
    
    public OutlineFromZBuffer(DepthBuffer depthBuffer) {
        this.depthBuffer = depthBuffer;
        width = depthBuffer.getWidth();
        height = depthBuffer.getHeight();
        outline = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        og = (Graphics2D) outline.getGraphics();
        og.setBackground(new Color(255, 255, 255, 0));
    }

    public BufferedImage getOutline() {
        return outline;
    }
    
    public void process() {
        og.clearRect(0, 0, outline.getWidth(), outline.getHeight());
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int current = depthBuffer.getNormalizedValue255(x, y);
                int right = depthBuffer.getNormalizedValue255(x + 1, y);
                int down = depthBuffer.getNormalizedValue255(x, y + 1);
                int dif1 = Math.abs(current - right);
                int dif2 = Math.abs(current - down);
                int difGreater = Math.max(dif1, dif2);
                int threshold = 5;
                if (difGreater > threshold) {
                    int radius = difGreater - threshold;
                    
                    og.setColor(Color.BLUE);
                    radius = Math.min(radius, 30) / 10;
                    
                    //og.setColor(Color.BLACK);
                    //radius = Math.min(radius, 10) / 7;
                    
                    og.fillOval(x - radius, y - radius, radius * 2, radius * 2);
                }
            }
        }
    }
    
}
