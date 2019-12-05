package helper;

public class Debug {
    public static boolean isDebug = false;

    public static void debugTrace() {
        if (isDebug) {
            System.out.println(Formatter.formatSize(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
            System.out.println(Formatter.formatSize(Runtime.getRuntime().totalMemory()));
            System.out.println(Runtime.getRuntime().totalMemory());
            System.out.println(Formatter.formatSize(Runtime.getRuntime().maxMemory()));
            System.out.println("here");
            System.out.println("test");
        }
    }
}
