package helper;

public class MemoryHelper {
    public static Long getMemoryUsagePercent()
    {
        return Runtime.getRuntime().totalMemory() / Runtime.getRuntime().maxMemory() / 100;
    }
}
