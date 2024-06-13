import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        SignalProcessor signalProcessor = new SignalProcessor();
        JFrame frame = new JFrame("Block Placer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        Library library = new Library();
        MainCanvas canvas = new MainCanvas();

        JTextField textField = new JTextField("Cutoff Frequency");

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setPreferredSize(new Dimension(200, 600));
        sidePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2), // Outer border
                BorderFactory.createEmptyBorder(10, 10, 10, 10) // Margin
        ));

        for (Block block : library.getBlocks()) {
            JButton button = new JButton(block.title);
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getMinimumSize().height));
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Read the cutoff frequency value
                    String cutoffFrequencyText = textField.getText().trim(); // Remove leading and trailing whitespaces
                    if (cutoffFrequencyText.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please enter the cutoff frequency.", "Missing Cutoff Frequency", JOptionPane.ERROR_MESSAGE);
                        return; // Exit the method without adding the filter
                    }

                    try {
                        int cutoffFrequency = Integer.parseInt(cutoffFrequencyText);
                        System.out.println("Cutoff Frequency: " + cutoffFrequency);

                        // Use the cutoff frequency value in filter creation
                        if (block.title.equals("Low pass")) {
                            signalProcessor.filterComposition.addFilter(new LowPassFilter(cutoffFrequency, 44100));
                        } else if (block.title.equals("High pass")) {
                            signalProcessor.filterComposition.addFilter(new HighPassFilter(cutoffFrequency, 44100));
                        }

                        canvas.setCurrentBlock(
                                new Block(block.width, block.height, block.color, block.title, cutoffFrequency));
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Please enter a valid number for the cutoff frequency.", "Invalid Cutoff Frequency", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            sidePanel.add(button);
            sidePanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between buttons
        }

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(canvas, BorderLayout.CENTER);

        // Add buttons for input and output files
        JButton inputFileButton = new JButton("Select Input File");
        inputFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    signalProcessor.setInputFileName(selectedFile.getAbsolutePath());
                    signalProcessor.setOutputFileName(selectedFile.getParent() + "/output.wav");
                    System.out.println("Selected input file: " + signalProcessor.getInputFileName());
                    System.out.println("Selected output file: " + signalProcessor.getOutputFileName());
                }
            }
        });

        JButton processButton = new JButton("Process");
        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    signalProcessor.readAudioFile();
                    signalProcessor.processSignal();
                    signalProcessor.writeAudioFile();
                } catch (IOException | UnsupportedAudioFileException ee) {
                    ee.printStackTrace();
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(inputFileButton);
        buttonPanel.add(processButton);
        buttonPanel.add(textField);

        frame.add(sidePanel, BorderLayout.WEST);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH); // Add button panel to the bottom

        frame.setVisible(true);
    }
}
