package helper;


import task.Fare;

public class AverageCounter {

    protected Long totalSum = Long.valueOf(0);
    protected Integer flightTotalSize = 0;
    protected Integer average = null;

    public void addFareToCalculation(Fare fare)
    {
        if (flightTotalSize >= 50000) {
            return;
        }

        totalSum += fare.getPrice();
        flightTotalSize += fare.getFlight().size();
    }

    public boolean isAboveAverage(Fare fare)
    {
        return (fare.getPrice() / fare.getFlight().size()) <= getAverage();
    }

    private long getAverage() {
        if (this.average == null) {
            this.average = Math.round(this.totalSum / flightTotalSize);
            System.out.println(this.average);
        }
        return this.average;
    }
}
