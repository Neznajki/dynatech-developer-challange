package task;

import helper.Debug;

public class PriceGatherTask implements Runnable {
	protected static Integer pendingTasks = 0;
	protected static Integer finishedTasks = 0;
	private ItineraryFlight itineraryFlight;

	public PriceGatherTask(ItineraryFlight itineraryFlight) {
		this.itineraryFlight = itineraryFlight;
		pendingTasks++;
	}

	@Override
	public void run() {
		try {
			if (ItineraryFlight.getMinPriced() != null && this.itineraryFlight.getCurrentTotalCost() >= ItineraryFlight.getMinPriced().getTotalCost()) {
				this.finishRun();
				return;
			}
			Integer itinerarySize = Itinerary.getSize();
			if (itinerarySize.equals(1)) {
				this.itineraryFlight.markPriceAsFinal();
				this.finishRun();
				return;
			}

			for (Fare fare: BestFareCollection.getInstance().getFaresByPosition(this.itineraryFlight.getPosition())) {
				ItineraryFlight nextFlightData = null;
				try {
					nextFlightData = ItineraryFlight.getDeepSearchInstance(this.itineraryFlight,fare);
				} catch (CloneNotSupportedException e) {
					if (Debug.isDebug) {
						e.printStackTrace();
						System.out.println(String.format("something got wrong in sub thread, %s", e.toString()));
					}
				}

				if (nextFlightData == null) {
					throw new RuntimeException("could not generate next flight data");
				}

				if (itinerarySize.equals(nextFlightData.getPosition())) {
					nextFlightData.markPriceAsFinal();
					ItineraryFlight.addFinalPrice(nextFlightData);

					continue;
				}

				if (ItineraryFlight.getMinPriced() != null && nextFlightData.getCurrentTotalCost() >= ItineraryFlight.getMinPriced().getTotalCost()) {
					continue;
				}

//				if (Debug.isDebug) { System.out.println(pendingTasks);}

				PriceGatherTask runnable = new PriceGatherTask(nextFlightData);
				TaskExecutor.addTask(runnable);
			}
		} catch (Exception e) {
			if (Debug.isDebug) { e.printStackTrace();}
		} finally {
			this.finishRun();
		}

	}

	private void finishRun() {
		finishedTasks++;

		if (pendingTasks.equals(finishedTasks) ) {
			TaskExecutor.shutdownConcurrent();
		}
	}

}
