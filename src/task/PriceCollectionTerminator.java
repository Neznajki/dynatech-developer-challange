package task;

import java.io.BufferedReader;
import java.io.IOException;

public class PriceCollectionTerminator implements Runnable {
    public static int taskTimeoutSecond = 55;
    public static BufferedReader fileInputStream;

    @Override
    public void run() {
        try {
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
