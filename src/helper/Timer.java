package helper;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Timer {

    protected Instant from;
    protected Instant to;
    protected List<Instant> intervals = new ArrayList<>();

    public Timer()
    {
        this.from = Instant.now();
    }

    public void showExecutionTime(String info)
    {
        this.intervals.add(this.from);
        this.to = Instant.now();
        System.out.println(String.format("%s, time took (%s)", info, Duration.between(this.from, this.to)));
        this.from = this.to;
    }
}
