import javax.sound.sampled.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Signal {
    private byte[] audioData;
    private AudioFormat format;

    public Signal(String filePath) throws IOException, UnsupportedAudioFileException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file)) {
            format = audioInputStream.getFormat();
            int frameLength = (int) audioInputStream.getFrameLength();
            audioData = new byte[frameLength * format.getFrameSize()];
            int bytesRead = audioInputStream.read(audioData);

            System.out.println("Audio file " + filePath + " read successfully.");
            System.out.println("Bytes read: " + bytesRead);
        }
    }

    public Signal(Signal signal) {
        this.audioData = signal.audioData.clone();
        this.format = signal.format;
    }

    public Signal(byte[] audioData, AudioFormat format) {
        this.audioData = audioData;
        this.format = format;
    }

    public byte[] getAudioData() {
        return audioData;
    }

    public AudioFormat getFormat() {
        return format;
    }

}
