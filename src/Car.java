import java.util.LinkedList;
import java.util.Map.Entry;

public class Car implements Runnable {

	public static volatile Map map;

	public static volatile Report report = new Report();

	private int carId;

	private CellSpace space;

	private FuelTank fuelTank;

	private double distanceTraveled;

	public Car(Cell start, Cell goal, double fuelLiters, int carId) {

		this.carId = carId;

		fuelTank = new FuelTank(fuelLiters);

		space = new CellSpace();

		space.setGoalCell(goal.getX(), goal.getY(), goal.getZ());

		space.setStartCell(start.getX(), start.getY(), start.getZ());

	}

	@Override
	public void run() {

		report.addToFullReport(carId + ": Engine started start cell is: " + space.getStartCell() + " goal cell is: "
				+ space.getGoalCell());

		Path path = findPath();

		if (path.getStatus() == 1) {

			System.out.println(report.incrementNumberOfArrivedCars());

			report.addToFullReport(
					carId + ": Done " + "Remaining cars: " + report.getNumberOfRemainingCars() + " Arrived cars: "
							+ report.getNumberOfArrivedCars() + " Failed cars: " + report.getNumberOfFailedCars()
							+ " Emergency Landed Cars: " + report.getnumberOfEmergencyLandedCars() + " Never ran Cars: "
							+ report.getnumberOfNeverStartedCars());

//			report.addToMiniReport(carId + ": from" + space.getStartCell()
//					+ " to " + space.getGoalCell() + "speed 60KM/H in "
//					+ Math.round((distanceTraveled / 60d) * 100d) / 100d + "H");

			report.addToMiniReport(carId + ": start cell" + space.getStartCell() + " goal cell " + space.getGoalCell()
					+ " fuel tank: " + fuelTank.getInitialFuelTank() + "Liter total distance travelled " + distanceTraveled
					+ "KM remaning fuel " + Math.round((fuelTank.getFuelTank()) * 100d) / 100d   +"Liter");

//					"I traveled a total distance of "
//					+ distanceTraveled + "KM My fuel tank in the beginning was " + fuelTank.getInitialFuelTank()
//					+ " at the end it becomes " + Math.round((fuelTank.getFuelTank()) * 100d) / 100d);

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
					+ distanceTraveled + "KM My fuel tank in the beginning was " + fuelTank.getInitialFuelTank()
					+ " at the end it becomes " + Math.round((fuelTank.getFuelTank()) * 100d) / 100d);

		} else if (path.getStatus() == -1) {

			report.incrementNumberOfFailedCars();

			report.addToFullReport(carId + ": You current fuel tank is not enough to continue your journy");

			report.addToFullReport(
					carId + ": Done " + "Remaining cars: " + report.getNumberOfRemainingCars() + " Arrived cars: "
							+ report.getNumberOfArrivedCars() + " Failed cars: " + report.getNumberOfFailedCars()
							+ " Emergency Landed Cars: " + report.getnumberOfEmergencyLandedCars() + " Never ran Cars: "
							+ report.getnumberOfNeverStartedCars());

			report.addToMiniReport(carId + ": I started from " + space.getStartCell()
					+ " and didn't reach my final destination " + space.getGoalCell() + " the remaining distance was "
					+ (path.size() - 1) + "KM which needed "
					+ Math.round(((path.size() - 1) * fuelTank.getFuelFactor()) * 100d) / 100d
					+ " Liter in the fuel tank but the tank was having "
					+ Math.round((fuelTank.getFuelTank()) * 100d) / 100d + " Liter at this period of time");
		} else if (path.getStatus() == -2) {

			report.incrementnumberOfNeverStartedCars();

			report.addToFullReport(carId + ": You current fuel tank is not enough to start your journy");

			report.addToFullReport(
					carId + ": Done " + "Remaining cars: " + report.getNumberOfRemainingCars() + " Arrived cars: "
							+ report.getNumberOfArrivedCars() + " Failed cars: " + report.getNumberOfFailedCars()
							+ " Emergency Landed Cars: " + report.getnumberOfEmergencyLandedCars() + " Never ran Cars: "
							+ report.getnumberOfNeverStartedCars());

			report.addToMiniReport(carId + ": never started from " + space.getStartCell()
					+ " and didn't reach my final destination " + space.getGoalCell() + " the remaining distance was "
					+ (path.size() - 1) + "KM which needed "
					+ Math.round(((path.size() - 1) * fuelTank.getFuelFactor()) * 100d) / 100d
					+ " Liter in the fuel tank but the tank was having "
					+ Math.round((fuelTank.getFuelTank()) * 100d) / 100d + " Liter at this period of time");
		}

		if (report.isLastCar()) {

			for (Entry<Cell, MapPoint> entry : Car.map.map.entrySet()) {

				Cell key = entry.getKey();

				boolean value = entry.getValue().isBlocked();

				if (value)
					report.addToMapReport(key + " = " + value);
			}

			report.printReports();

		}

	}

	public Path findPath() {

		Path path = new Path();

		LinkedList<Cell> potentialNextCells = new LinkedList<Cell>();

		if (!fuelTank.isFuelEnough(space.getStartCell(), space.getGoalCell())) {
			path.setStatus(-2);
			return path;
		}

		Cell currentCell = space.getStartCell();

		map.blockCell(currentCell, carId);

		path.add(currentCell);

		report.addToFullReport(carId + ": I am currently in " + currentCell);

		if (space.getG(space.getStartCell()) == Double.POSITIVE_INFINITY) {
			return path;
		}

		boolean isTrapped = false;

		while (!path.isComplete()) {

			int blockCount = 0;

			LinkedList<Cell> blockedCells = new LinkedList<Cell>();

			potentialNextCells = space.getSuccessors(currentCell);

			if (potentialNextCells.isEmpty()) {
				return path;
			}

			double minimumCost = Double.POSITIVE_INFINITY;

			Cell minimumCell = new Cell();

			for (Cell potentialNextCell : potentialNextCells) {

				if (map.isCellBlocked(potentialNextCell)) {
					blockCount++;
					blockedCells.add(potentialNextCell);
					continue;
				} else {
					isTrapped = true;
				}

				double costToMove = Geometry.euclideanDistance(currentCell, potentialNextCell);
				double euclideanDistance = Geometry.euclideanDistance(potentialNextCell, space.getGoalCell())
						+ Geometry.euclideanDistance(space.getStartCell(), potentialNextCell);
				costToMove += space.getG(potentialNextCell);

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

				if (map.isCellBlocked(minimumCell)) {
					continue;
				} else {
					map.blockCell(minimumCell, carId);
				}

				report.addToFullReport(carId + ": " + "I am moving now to " + minimumCell + " there are " + blockCount
						+ " blocked cells out of " + potentialNextCells.size() + " cells");

				if (blockCount > 0) {

					for (Cell c : blockedCells)
						report.addToFullReport(carId + ": " + c.toString() + " by " + map.getBlockingID(c));

				}

				map.unblockCell(currentCell);

				currentCell = new Cell(minimumCell);

				distanceTraveled++;

				fuelTank.decrementFuelTank(fuelTank.getFuelFactor());

				path.add(currentCell);

				report.addToFullReport(carId + ": I am currently in " + currentCell);

				if (fuelTank.isTankEmpty() && !path.isComplete()) {
					map.unblockCell(currentCell);
					path.setStatus(-1);
					return path;
				}

				path.setComplete(space.getGoalCell().equals(path.getLast()));

			} else {

				fuelTank.decrementFuelTank(fuelTank.getFuelFactor() / 4);

				if (fuelTank.isTankEmpty() && !path.isComplete()) {
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

					fuelTank.decrementFuelTank(fuelTank.getFuelFactor());

					if (fuelTank.isTankEmpty()) {
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

				sign = sign * -1;

				fuelTank.decrementFuelTank(fuelTank.getFuelFactor() / 8);

				if (fuelTank.isTankEmpty()) {
					map.unblockCell(currentCell);
					path.setStatus(-1);
					for (Cell c : path) {
						map.unblockCell(c);
					}
					return path;
				}

				continue;
			}

			map.blockCell(newCell, carId);

			path.add(newCell);

		}

		return path;
	}

	private boolean emergencyLandingCheck(Cell cell) {

		double fuelGoalCellRatio = (Geometry.euclideanDistance(cell, space.getGoalCell()) * fuelTank.getFuelFactor())
				/ fuelTank.getFuelTank();

		return fuelGoalCellRatio > 0.5;
	}

	public Cell getStartCell() {
		return space.getStartCell();
	}

	public Cell getGoalCell() {
		return space.getGoalCell();
	}

	public double getFuelTank() {
		return fuelTank.getFuelTank();
	}
}
