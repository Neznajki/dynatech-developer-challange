package helper;

import java.util.ArrayList;

public class UniqueStringCollector {
    protected static ArrayList<UniqueStringCollector> collection = new ArrayList<>();

    public static UniqueStringCollector getUnique(String value)
    {
        for (UniqueStringCollector existingValue: collection) {
            if (value == existingValue.getValue()) {
                return existingValue;
            }
        }

        UniqueStringCollector result = new UniqueStringCollector(value);
        collection.add(result);
        return result;
    }

    protected String value;
    public UniqueStringCollector(String value)
    {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
