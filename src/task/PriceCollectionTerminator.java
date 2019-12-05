package task;

import java.io.IOException;
import java.io.InputStreamReader;

public class PriceCollectionTerminator implements Runnable {
    public static int taskTimeoutSecond = 40;
    public static InputStreamReader fileInputStream;

    @Override
    public void run() {
        try {
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
