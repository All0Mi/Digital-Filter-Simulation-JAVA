public class HighPassFilter implements FilterBlock {
    private double cutoffFrequency; // in Hz
    private double samplingFrequency; // in Hz
    private String name = "HighPassFilter";

    public String getName() {
        return name;
    }

    public HighPassFilter(double cutoffFrequency, double samplingFrequency) {
        this.cutoffFrequency = cutoffFrequency;
        this.samplingFrequency = samplingFrequency;
    }

    public Signal process(Signal inputSignal) {
        byte[] audioData = inputSignal.getAudioData();
        byte[] filteredData = applyHighPassFilter(audioData);
        return new Signal(filteredData, inputSignal.getFormat());
    }

    private byte[] applyHighPassFilter(byte[] audioData) {
        // Convert bytes to double values (assuming 16-bit PCM)
        double[] samples = byteArrayToDoubleArray(audioData);

        // Calculate filter coefficients (simple FIR high-pass filter)
        double rc = 1.0 / (2 * Math.PI * cutoffFrequency);
        double alpha = rc / (rc + (1.0 / samplingFrequency));
        double[] filteredSamples = new double[samples.length];

        // Apply the high-pass filter
        filteredSamples[0] = samples[0];
        for (int i = 1; i < samples.length; i++) {
            filteredSamples[i] = alpha * (filteredSamples[i - 1] + samples[i] - samples[i - 1]);
        }

        // Convert filtered double values back to bytes
        return doubleArrayToByteArray(filteredSamples);
    }

    private double[] byteArrayToDoubleArray(byte[] byteArray) {
        double[] doubleArray = new double[byteArray.length / 2];
        for (int i = 0; i < doubleArray.length; i++) {
            doubleArray[i] = ((short) ((byteArray[2 * i + 1] << 8) | (byteArray[2 * i] & 0xff))) / 32768.0;
        }
        return doubleArray;
    }

    private byte[] doubleArrayToByteArray(double[] doubleArray) {
        byte[] byteArray = new byte[doubleArray.length * 2];
        for (int i = 0; i < doubleArray.length; i++) {
            short shortValue = (short) (doubleArray[i] * 32768.0);
            byteArray[2 * i] = (byte) shortValue;
            byteArray[2 * i + 1] = (byte) (shortValue >> 8);
        }
        return byteArray;
    }

    public void setCutoffFrequency(double cutoffFrequency) {
        this.cutoffFrequency = cutoffFrequency;
    }
}
