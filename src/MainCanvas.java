import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class MainCanvas extends JPanel {
    private List<Block> placedBlocks;
    private Block currentBlock;
    public static final int GRID_SIZE = 40; // Slightly smaller grid size

    public MainCanvas() {
        placedBlocks = new ArrayList<>();
        currentBlock = null;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentBlock != null) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        int x = (e.getX() / GRID_SIZE) * GRID_SIZE;
                        int y = (e.getY() / GRID_SIZE) * GRID_SIZE;
                        currentBlock.setPosition(x, y);
                        if (!isOverlap(currentBlock)) {
                            placedBlocks.add(currentBlock);
                        }
                        currentBlock = null;
                        repaint();
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        currentBlock = null;
                        repaint();
                    }
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (currentBlock != null) {
                    int x = (e.getX() / GRID_SIZE) * GRID_SIZE;
                    int y = (e.getY() / GRID_SIZE) * GRID_SIZE;
                    currentBlock.setPosition(x, y);
                    repaint();
                }
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    currentBlock = null;
                    repaint();
                }
            }
        });

        setFocusable(true);
        requestFocusInWindow();
    }

    public void setCurrentBlock(Block block) {
        this.currentBlock = block;
        requestFocusInWindow();
    }

    private boolean isOverlap(Block newBlock) {
        for (Block block : placedBlocks) {
            if (blocksOverlap(block, newBlock)) {
                return true;
            }
        }
        return false;
    }

    private boolean blocksOverlap(Block block1, Block block2) {
        return block1.x < block2.x + block2.width * GRID_SIZE &&
                block1.x + block1.width * GRID_SIZE > block2.x &&
                block1.y < block2.y + block2.height * GRID_SIZE &&
                block1.y + block1.height * GRID_SIZE > block2.y;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        for (Block block : placedBlocks) {
            block.draw(g);
        }
        if (currentBlock != null) {
            currentBlock.draw(g);
        }
    }

    private void drawGrid(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.LIGHT_GRAY);
        float[] dash = { 1f, 4f };
        g2d.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dash, 0.0f));

        int width = getWidth();
        int height = getHeight();
        for (int i = 0; i < width; i += GRID_SIZE) {
            g2d.drawLine(i, 0, i, height);
        }
        for (int i = 0; i < height; i += GRID_SIZE) {
            g2d.drawLine(0, i, width, i);
        }
    }
}
