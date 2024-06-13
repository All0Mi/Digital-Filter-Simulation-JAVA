import java.awt.Color;
import java.awt.Graphics;

public class Block {
    public int x, y, width, height;
    public Color color;
    public String title; // New attribute for block title
    public int cutoffFrequency; // New attribute for cutoff frequency

    public Block(int width, int height, Color color, String title, int cutoffFrequency) {
        this.width = width;
        this.height = height;
        this.color = color;
        this.title = title;
        this.cutoffFrequency = cutoffFrequency;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width * MainCanvas.GRID_SIZE, height * MainCanvas.GRID_SIZE);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width * MainCanvas.GRID_SIZE, height * MainCanvas.GRID_SIZE);
        // Draw title
        if (title != null && !title.isEmpty()) {
            g.drawString(title, x + width * MainCanvas.GRID_SIZE / 2 - title.length() * 3,
                    y + MainCanvas.GRID_SIZE / 2);
        }
        g.drawString(String.valueOf(cutoffFrequency) + " Hz",
                x + width * MainCanvas.GRID_SIZE / 2 - String.valueOf(cutoffFrequency).length() * 3,
                y + MainCanvas.GRID_SIZE / 2 + 20);

    }
}
