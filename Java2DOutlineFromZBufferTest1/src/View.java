import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * 2D Outline using Z-Buffer Test.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com);
 */
public class View extends JPanel {
    
    private BufferedImage zbuffer;
    private BufferedImage image;
    private BufferedImage outline;
    
    public View() {
    }
    
    public void start() {
        try {
            zbuffer = ImageIO.read(getClass().getResourceAsStream("boruto_zbuffer.png"));
            //image = ImageIO.read(getClass().getResourceAsStream("image.png"));
            outline = new BufferedImage(zbuffer.getWidth(), zbuffer.getHeight(), BufferedImage.TYPE_INT_ARGB);
        } catch (IOException ex) {
            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw((Graphics2D) g);
    }
    
    private int getZBufferValue(int x, int y) {
        try {
            return new Color(zbuffer.getRGB(x, y)).getRed();
        }
        catch (Exception e) {
            return 0;
        }
    }
    
    private void draw(Graphics2D g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        //g.drawLine(0, 0, getWidth(), getHeight());
        
        Graphics2D og = (Graphics2D) outline.getGraphics();
        og.setBackground(new Color(255, 255, 255, 0));
        og.clearRect(0, 0, outline.getWidth(), outline.getHeight());
        og.setColor(Color.BLACK);
        
        for (int y = 0; y < zbuffer.getHeight(); y++) {
            for (int x = 0; x < zbuffer.getWidth(); x++) {
                int current = getZBufferValue(x, y);
                int right = getZBufferValue(x + 1, y);
                int down = getZBufferValue(x, y + 1);
                int dif1 = Math.abs(current - right);
                int dif2 = Math.abs(current - down);
                int difGreater = Math.max(dif1, dif2);
                int threshold = 1;
                if (difGreater > threshold) {
                    int radius = difGreater - threshold;
                    radius = Math.min(radius, 12) / 4;
                    og.setColor(Color.BLUE);
                    og.fillOval(x - radius, y - radius, radius * 2, radius * 2);
                }
            }
        }

        //g.drawImage(finalRender, 0, 0, getWidth(), getHeight(), null);
        //g.drawImage(zbuffer, 0, 0, getWidth(), getHeight(), null);
        g.drawImage(outline, 0, 0, getWidth(), getHeight(), null);
        
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            View view = new View();
            view.setPreferredSize(new Dimension(800, 600));
            JFrame frame = new JFrame();
            frame.setTitle("Outline using Z-Buffer Test");
            frame.getContentPane().add(view);
            frame.setResizable(false);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            view.requestFocus();
            view.start();
        });
    }
    
}
