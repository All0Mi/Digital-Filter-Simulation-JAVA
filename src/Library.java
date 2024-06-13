import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Library {
    private List<Block> blocks;

    public Library() {
        blocks = new ArrayList<>();
        blocks.add(new Block(3, 2, Color.GRAY, "Low pass", 1000)); // Block type 1
        blocks.add(new Block(3, 2, Color.GRAY, "High pass", 1000)); // Block type 2
        // Add more block types if needed

    }

    public List<Block> getBlocks() {
        return blocks;
    }
}
