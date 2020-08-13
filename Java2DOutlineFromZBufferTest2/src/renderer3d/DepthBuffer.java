package renderer3d;

import java.util.Arrays;

/**
 * DepthBuffer class.
 * 
 * @author Leonardo Ono (ono.leo80@gmail.com)
 */
public class DepthBuffer {
    
    private final double[][] depth;
    private final int width;
    private final int height;

    private double maxValue;
    private double minValue;
    
    public DepthBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        depth = new double[height][width];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    public double get(int x, int y) {
        if (x < 0 || x > width - 1 || y < 0 || y > height - 1) {
            return Double.NEGATIVE_INFINITY;
        } 
        return depth[y][x];
    }

    public void set(int x, int y, double z) {
        if (x < 0 || x > width - 1 || y < 0 || y > height - 1) {
            return;
        } 
        depth[y][x] = z;
    }

    public boolean update(int x, int y, double z) {
        if (x < 0 || x > width - 1 || y < 0 || y > height - 1) {
            return false;
        } 
        if (z > depth[y][x]) {
            depth[y][x] = z;
            
            if (z < minValue) {
                minValue = z;
            }
            if (z > maxValue) {
                maxValue = z;
            }
            
            return true;
        }
        else {
            return false;
        }
    }
    
    public void clear() {
        for (int y = 0; y < height; y++) {
            Arrays.fill(depth[y], Double.NEGATIVE_INFINITY);
        }
        minValue = Double.POSITIVE_INFINITY;
        maxValue = Double.NEGATIVE_INFINITY;
    }
    
    public int getNormalizedValue255(int x, int y) {
        double dif = maxValue - minValue;
        double z = get(x, y);
        double p = (z - minValue) / dif;
        return (int) (255 * p);
    }
    
}
