public class LowPassFilter implements FilterBlock {
    private double cutoffFrequency; // in Hz
    private double samplingFrequency; // in Hz
    private String name = "LowPassFilter";

    public String getName() {
        return name;
    }

    public LowPassFilter(double cutoffFrequency, double samplingFrequency) {
        this.cutoffFrequency = cutoffFrequency;
        this.samplingFrequency = samplingFrequency;
    }

    public Signal process(Signal inputSignal) {
        byte[] audioData = inputSignal.getAudioData();
        byte[] filteredData = applyLowPassFilter(audioData);
        return new Signal(filteredData, inputSignal.getFormat());
    }

    private byte[] applyLowPassFilter(byte[] audioData) {
        // Convert bytes to double values (assuming 16-bit PCM)
        double[] samples = byteArrayToDoubleArray(audioData);

        // Calculate filter coefficients (simple FIR low-pass filter)
        double rc = 1.0 / (2 * Math.PI * cutoffFrequency);
        double alpha = 1.0 / (1.0 + rc * samplingFrequency);
        double[] filteredSamples = new double[samples.length];

        // Apply the low-pass filter
        filteredSamples[0] = samples[0];
        for (int i = 1; i < samples.length; i++) {
            filteredSamples[i] = alpha * samples[i] + (1 - alpha) * filteredSamples[i - 1];
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
