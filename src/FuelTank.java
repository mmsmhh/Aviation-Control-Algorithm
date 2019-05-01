
public class FuelTank {

	private double fuelTank;

	private double initialFuelTank;

	private double fuelFactor = 1d / 15d;

	public FuelTank(double fuelTank) {

		this.fuelTank = fuelTank;

		this.initialFuelTank = fuelTank;
	}

	public double getFuelTank() {
		return fuelTank;
	}

	public void setFuelTank(double fuelTank) {
		this.fuelTank = fuelTank;
	}

	public double getInitialFuelTank() {
		return initialFuelTank;
	}

	public void setInitialFuelTank(double initialFuelTank) {
		this.initialFuelTank = initialFuelTank;
	}

	public double getFuelFactor() {
		return fuelFactor;
	}

	public boolean isTankEmpty() {
		return fuelTank == 0;
	}

	public void decrementFuelTank(double value) {

		fuelTank -= value;

		if (fuelTank < fuelFactor) {
			fuelTank = 0;
		}

	}

	public boolean isFuelEnough(Cell startCell, Cell goalCell) {

		return Geometry.euclideanDistance(startCell, goalCell) * getFuelFactor() <= getFuelTank();
	}

}
