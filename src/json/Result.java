package json;

import helper.UniqueStringCollector;

public class Result {

    protected UniqueStringCollector section;
    protected UniqueStringCollector value;

    public Result(UniqueStringCollector section, UniqueStringCollector value) {
        this.section = section;
        this.value = value;
    }

    public UniqueStringCollector getSection() {
        return section;
    }

    public UniqueStringCollector getValue() {
        return value;
    }
}
