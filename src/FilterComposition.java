import java.util.List;
import java.util.ArrayList; // Import the ArrayList class

public class FilterComposition {
    private List<FilterBlock> filters = new ArrayList<>();

    public FilterComposition(List<FilterBlock> filters) {
        this.filters = filters;
    }

    public FilterComposition() {
    }

    public void addFilter(FilterBlock filter) {
        filters.add(filter);
    }

    public void removeFilter(int index) {
        filters.remove(index);
    }

    public Signal process(Signal inputSignal) {
        Signal outputSignal = inputSignal;

        for (FilterBlock filter : filters) {
            outputSignal = filter.process(outputSignal);
            System.out.println("Applied filter: " + filter.getName());
        }

        return outputSignal;
    }
}
