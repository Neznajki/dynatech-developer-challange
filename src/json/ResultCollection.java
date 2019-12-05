package json;

import helper.UniqueStringCollector;

import java.util.List;

public class ResultCollection extends Result {
    private List<?> entries;

    public ResultCollection(UniqueStringCollector section, List<?> entries) {
        super(section, null);
        this.entries = entries;
    }

    public List<?> getEntries() {
        return entries;
    }
}
