import java.util.LinkedList;
import java.util.Map.Entry;

public class Car implements Runnable {

	private CellSpace space;

	private double fuelTank;

	private double initialFuelTank;

	private double fuelFactor = 1d / 15d;

	private double distanceTraveled;

	private int carId;

	public static volatile Map map;

	public static volatile Report report = new Report();

	public Car(Cell startCell, Cell goalCell, double fuelTank, int carId) {

		this.carId = carId;

		this.fuelTank = fuelTank;

		this.initialFuelTank = fuelTank;

		space = new CellSpace();

		space.setGoalCell(goalCell.getX(), goalCell.getY(), goalCell.getZ());

		space.setStartCell(startCell.getX(), startCell.getY(), startCell.getZ());

	}

	@Override
	public void run() {

		report.addToFullReport(
				carId + ": Engine started start cell is: " + getStartCell() + " goal cell is: " + getGoalCell());

		Path path = findPath();

		if (path.getStatus() == 1) {

			System.out.println(report.incrementNumberOfArrivedCars());

			report.addToFullReport(
					carId + ": Done " + "Remaining cars: " + report.getNumberOfRemainingCars() + " Arrived cars: "
							+ report.getNumberOfArrivedCars() + " Failed cars: " + report.getNumberOfFailedCars()
							+ " Emergency Landed Cars: " + report.getnumberOfEmergencyLandedCars() + " Never ran Cars: "
							+ report.getnumberOfNeverStartedCars());

			report.addToMiniReport(carId + ": I started from " + space.getStartCell()
					+ " and reached my final destination " + space.getGoalCell() + " with average speed of 60KM/H in "
					+ Math.round((distanceTraveled / 60d) * 100d) / 100d + "H " + "I traveled a total distance of "
					+ distanceTraveled + "KM My fuel tank in the beginning was " + initialFuelTank
					+ " at the end it becomes " + Math.round((getFuelTank()) * 100d) / 100d);

		} else if (path.getStatus() == 2) {

			report.incrementNumberOfEmergencyLandedCars();

			report.addToFullReport(
					carId + ": Done " + "Remaining cars: " + report.getNumberOfRemainingCars() + " Arrived cars: "
							+ report.getNumberOfArrivedCars() + " Failed cars: " + report.getNumberOfFailedCars()
							+ " Emergency Landed Cars: " + report.getnumberOfEmergencyLandedCars() + " Never ran Cars: "
							+ report.getnumberOfNeverStartedCars());

			report.addToMiniReport(carId + ": I started from " + space.getStartCell()
					+ " and reached my final destination " + space.getGoalCell() + " with average speed of 60KM/H in "
					+ Math.round((distanceTraveled / 60d) * 100d) / 100d + "H " + "I traveled a total distance of "
					+ distanceTraveled + "KM My fuel tank in the beginning was " + initialFuelTank
					+ " at the end it becomes " + Math.round((getFuelTank()) * 100d) / 100d);

		} else if (path.getStatus() == -1) {

			report.incrementNumberOfFailedCars();

			report.addToFullReport(carId + ": You current fuel tank is not enough to continue your journy");

			report.addToFullReport(
					carId + ": Done " + "Remaining cars: " + report.getNumberOfRemainingCars() + " Arrived cars: "
							+ report.getNumberOfArrivedCars() + " Failed cars: " + report.getNumberOfFailedCars()
							+ " Emergency Landed Cars: " + report.getnumberOfEmergencyLandedCars() + " Never ran Cars: "
							+ report.getnumberOfNeverStartedCars());

			report.addToMiniReport(
					carId + ": I started from " + space.getStartCell() + " and didn't reach my final destination "
							+ space.getGoalCell() + " the remaining distance was " + (path.size() - 1)
							+ "KM which needed " + Math.round(((path.size() - 1) * fuelFactor) * 100d) / 100d
							+ " Liter in the fuel tank but the tank was having "
							+ Math.round((getFuelTank()) * 100d) / 100d + " Liter at this period of time");
		} else if (path.getStatus() == -2) {

			report.incrementnumberOfNeverStartedCars();

			report.addToFullReport(carId + ": You current fuel tank is not enough to start your journy");

			report.addToFullReport(
					carId + ": Done " + "Remaining cars: " + report.getNumberOfRemainingCars() + " Arrived cars: "
							+ report.getNumberOfArrivedCars() + " Failed cars: " + report.getNumberOfFailedCars()
							+ " Emergency Landed Cars: " + report.getnumberOfEmergencyLandedCars() + " Never ran Cars: "
							+ report.getnumberOfNeverStartedCars());

			report.addToMiniReport(
					carId + ": never started from " + space.getStartCell() + " and didn't reach my final destination "
							+ space.getGoalCell() + " the remaining distance was " + (path.size() - 1)
							+ "KM which needed " + Math.round(((path.size() - 1) * fuelFactor) * 100d) / 100d
							+ " Liter in the fuel tank but the tank was having "
							+ Math.round((getFuelTank()) * 100d) / 100d + " Liter at this period of time");
		}

		if (report.isLastCar()) {

			for (Entry<Cell, Boolean> entry : Car.map.map.entrySet()) {

				Cell key = entry.getKey();

				boolean value = entry.getValue();

				if (value)
					report.addToMapReport(key + " = " + value);
			}

			report.printReports();

		}

	}

	public Path findPath() {

		Path path = new Path();

		LinkedList<Cell> potentialNextCells = new LinkedList<Cell>();

		if (!isFuelEnough(space.getStartCell(), space.getGoalCell())) {
			path.setStatus(-2);
			return path;
		}

		Cell currentCell = space.getStartCell();

		map.blockCell(currentCell);

		path.add(currentCell);

		report.addToFullReport(carId + ": I am currently in " + currentCell);

		if (space.getG(space.getStartCell()) == Double.POSITIVE_INFINITY) {
			return path;
		}

		boolean isTrapped = false;

		while (!path.isComplete()) {

			potentialNextCells = space.getSuccessors(currentCell);

			if (potentialNextCells.isEmpty()) {
				return path;
			}

			double minimumCost = Double.POSITIVE_INFINITY;

			Cell minimumCell = new Cell();

			for (Cell potentialNextCell : potentialNextCells) {

				if (!map.contains(potentialNextCell) || map.isCellBlocked(potentialNextCell)) {
					continue;
				} else {
					isTrapped = true;
				}

				double costToMove = Geometry.euclideanDistance(currentCell, potentialNextCell);
				double euclideanDistance = Geometry.euclideanDistance(potentialNextCell, space.getGoalCell())
						+ Geometry.euclideanDistance(space.getStartCell(), potentialNextCell);
				costToMove += space.getG(potentialNextCell);

				// If the cost to move is essentially zero ...
				if (space.isClose(costToMove, minimumCost)) {
					if (0 > euclideanDistance) {

						minimumCost = costToMove;
						minimumCell = potentialNextCell;
					}
				} else if (costToMove < minimumCost) {

					minimumCost = costToMove;
					minimumCell = potentialNextCell;
				}

			}

			if (isTrapped) {

				potentialNextCells.clear();

				if (!map.contains(minimumCell) || map.isCellBlocked(minimumCell)) {
					continue;
				} else {
					map.blockCell(minimumCell);
				}

				report.addToFullReport(carId + ": " + "I am moving now to " + currentCell);

				map.unblockCell(currentCell);

				currentCell = new Cell(minimumCell);

				distanceTraveled++;

				decrementFuelTank(fuelFactor);

				path.add(currentCell);

				report.addToFullReport(carId + ": I am currently in " + currentCell);

				if (isTankEmpty() && !path.isComplete()) {
					map.unblockCell(currentCell);
					path.setStatus(-1);
					return path;
				}

				path.setComplete(space.getGoalCell().equals(path.getLast()));

			} else {

				decrementFuelTank(fuelFactor / 4);

				if (isTankEmpty() && !path.isComplete()) {
					map.unblockCell(currentCell);
					path.setStatus(-1);
					return path;
				}

				report.addToFullReport(carId + ": replanning!");

			}

			if (path.isComplete()) {
				map.unblockCell(currentCell);
				break;
			}

			if (emergencyLandingCheck(currentCell)) {

				Path landing = generateLandingPath(currentCell);

				report.addToFullReport(carId + ": emergency landing activated");

				if (landing == null) {
					map.unblockCell(currentCell);
					path.setStatus(2);
					return path;
				}

				while (!landing.isEmpty()) {

					Cell landingCell = landing.removeFirst();

					report.addToFullReport(carId + ": " + "I am moving now to " + landingCell);

					map.unblockCell(currentCell);

					distanceTraveled++;

					decrementFuelTank(fuelFactor);

					if (isTankEmpty()) {
						path.setStatus(-1);
						for (Cell c : landing) {
							map.unblockCell(c);
						}
						return path;
					}

					currentCell = landingCell;

					path.add(currentCell);

					report.addToFullReport(carId + ": I am currently in " + currentCell);

					if (landing.isEmpty()) {
						map.unblockCell(currentCell);
						path.setStatus(2);
						return path;
					}

				}

			}

		}

		path.setStatus(1);

		return path;
	}

	// needs refactoring
	private Path generateLandingPath(Cell currentCell) {

		Path path = new Path();

		int x = currentCell.getX();
		int y = currentCell.getY();
		int z = currentCell.getZ();

		int sign = -1;

		if (y == 0)
			return null;

		while (y > 0) {

			if (x == 0) {
				sign = 1;
			} else if (x == map.getMaxX()) {
				sign = -1;
			}

			x += sign;

			y--;

			Cell newCell = new Cell(x, y, z);

			if (map.isCellBlocked(newCell)) {
				y++;
				x -= sign;

				decrementFuelTank(fuelFactor / 8);

				if (isTankEmpty()) {
					map.unblockCell(currentCell);
					path.setStatus(-1);
					for (Cell c : path) {
						map.unblockCell(c);
					}
					return path;
				}

				continue;
			}
			map.blockCell(newCell);

			path.add(newCell);

		}

		return path;
	}

	private boolean emergencyLandingCheck(Cell cell) {

		double fuelGoalCellRatio = (Geometry.euclideanDistance(cell, getGoalCell()) * fuelFactor) / getFuelTank();

		return fuelGoalCellRatio > 0.7;
	}

	private boolean isFuelEnough(Cell startCell, Cell goalCell) {

		return Geometry.euclideanDistance(startCell, goalCell) * fuelFactor <= getFuelTank();
	}

	private boolean isTankEmpty() {
		return fuelTank == 0;
	}

	private void decrementFuelTank(double value) {

		fuelTank -= value;

		if (fuelTank < fuelFactor) {
			fuelTank = 0;
		}

	}

	public double getFuelTank() {
		return fuelTank;
	}

	public Cell getStartCell() {
		return space.getStartCell();
	}

	public Cell getGoalCell() {
		return space.getGoalCell();
	}

}
