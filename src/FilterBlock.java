public interface FilterBlock {
    Signal process(Signal inputSignal);

    String getName();
}