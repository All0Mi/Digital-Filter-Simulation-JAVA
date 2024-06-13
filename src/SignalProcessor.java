import javax.sound.sampled.*;
import java.io.*;

public class SignalProcessor {
    private String inputFileName;
    private String outputFileName;

    private Signal inputSignal;
    private Signal outputSignal;

    protected FilterComposition filterComposition = new FilterComposition();

    public void readAudioFile() throws IOException, UnsupportedAudioFileException {
        inputSignal = new Signal(inputFileName);
    }

    public void writeAudioFile() throws IOException {
        // Ensure outputSignal is not null
        if (outputSignal == null) {
            throw new IllegalStateException("Output signal is not initialized. Process the signal first.");
        }

        // Write audio data to WAV file
        try (AudioInputStream audioInputStream = new AudioInputStream(
                new ByteArrayInputStream(outputSignal.getAudioData()),
                inputSignal.getFormat(), // Input format
                inputSignal.getAudioData().length / inputSignal.getFormat().getFrameSize() // Frame length
        )) {
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File(outputFileName));
        }
    }

    public void processSignal() {
        outputSignal = filterComposition.process(inputSignal);
    }

    public void setInputFileName(String filePath) {
        this.inputFileName = filePath;
    }

    public String getInputFileName() {
        return inputFileName;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String filePath) {
        this.outputFileName = filePath;
    }
}
