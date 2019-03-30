
public class Car implements Runnable {

	private CellSpace space;

	private CostBlockManager blockManager;

	private double fuelTank;

	private double startFuelTank;

	double kilosTraveled = 0;

	String name;

	private Cell start, end;

	double fuelFactor = 1d / 15d;

	public Car(Cell start, Cell end, int fuelTank, String name) {

		this.start = start;
		this.end = end;

		this.name = name;
		this.fuelTank = fuelTank;

		this.startFuelTank = fuelTank;

		space = new CellSpace();

		blockManager = new CostBlockManager(space);

		space.setGoalCell(end.getX(), end.getY(), end.getZ());

		space.setStartCell(start.getX(), start.getY(), start.getZ());
	}

	public void decrementFuelTank() {
		fuelTank = fuelTank - fuelFactor;

		if (fuelTank < 0)
			fuelTank = 0;
	}

	public double getFuelTank() {
		return fuelTank;
	}

	public Path findPath() {

		Pathfinder pathfinder = new Pathfinder(blockManager);

		Path path = pathfinder.findPath();

		return path;
	}

	@Override
	public void run() {

		Path path = findPath();

		test.printWriter.println(name + ": " + "my path is " + path);
		test.printWriter.flush();

		if (getFuelTank() < (path.size() - 1) * fuelFactor) {

			test.printWriter
					.println(name + ": Your current tank isn't enough to start you should visit the fuel station");
			test.printWriter.flush();

			test.report.incrementNumberOfNeverRunnedCars();

			test.printWriter.println(name + ": Done " + "Remaining cars: " + test.report.getNumberOfRemainingCars()
					+ " Arrived cars: " + test.report.getNumberOfArrivedCars() + " Failed cars: "
					+ test.report.getNumberOfFailedCars() + " Never run Cars: "
					+ test.report.getnumberOfNeverRunnedCars());
			test.printWriter.flush();

			test.report.addReport(name + ": I never started because the fuel was not enough to start"
					+ " the distance was " + (path.size() - 1) + "KM which needed " + ((path.size() - 1) * fuelFactor)
					+ " Liter in the fuel tank but the tank was having " + getFuelTank());

			return;
		}

		Cell currentCell = path.removeFirst();

		synchronized (test.BlockedCells) {
			test.BlockedCells.add(currentCell);
		}

		test.printWriter.println(name + ": " + "I am currently in " + currentCell);
		test.printWriter.flush();

		while (!path.isEmpty()) {

			Cell nextCell = path.removeFirst();

			if (test.BlockedCells.checkIfBlockedCellsContains(nextCell)) {

				test.printWriter.println(name + ": " + "The " + nextCell + " is blocked rePlanning now");
				test.printWriter.flush();

				space.clearSpace(currentCell.getX(), currentCell.getY(), currentCell.getZ());

				synchronized (test.BlockedCells) {
					blockManager.blockCell(space.makeNewCell(nextCell.getX(), nextCell.getY(), nextCell.getZ()));
				}

				path = findPath();

				while (getFuelTank() < (path.size() - 1) * fuelFactor) {

					if (decrementFuelAndCheckIfFailed()) {

						test.printWriter.println(name + ": You current battery is not enough to continue your journy");
						test.printWriter.flush();
						test.report.incrementNumberOfFailedCars();

						test.printWriter
								.println(name + ": Done " + "Remaining cars: " + test.report.getNumberOfRemainingCars()
										+ " Arrived cars: " + test.report.getNumberOfArrivedCars() + " Failed cars: "
										+ test.report.getNumberOfFailedCars() + " Never run Cars: "
										+ test.report.getnumberOfNeverRunnedCars());
						test.printWriter.flush();

						test.report.addReport(name + ": I started from " + start
								+ " and didn't reach my final destination " + end + " the remaining distance was "
								+ (path.size() - 1) + "KM which needed " + ((path.size() - 1) * fuelFactor)
								+ " Liter in the fuel tank but the tank was having " + getFuelTank()
								+ " Liter at this period of time");
						return;
					}

					test.printWriter.println(name
							+ ": You current fuel tank is not enough to go to the new replanned path replanning again"
							+ " your current fuel tank is: " + getFuelTank() + " liter the fuel tank needed is: "
							+ (path.size() - 1) * fuelFactor + " liter");
					test.printWriter.flush();

					space.clearSpace(currentCell.getX(), currentCell.getY(), currentCell.getZ());

					if (test.BlockedCells.checkIfBlockedCellsContains(nextCell)) {
						synchronized (test.BlockedCells) {
							blockManager
									.blockCell(space.makeNewCell(nextCell.getX(), nextCell.getY(), nextCell.getZ()));
						}
					}

					path = findPath();

				}

				currentCell = path.removeFirst();

				test.printWriter.println(name + ": " + "Done from replanning the new path is " + path);
				test.printWriter.flush();

			} else {

				synchronized (test.BlockedCells) {
					test.BlockedCells.add(nextCell);
				}

				test.printWriter.println(name + ": " + "I am moving now to " + nextCell);
				test.printWriter.flush();

				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				synchronized (test.BlockedCells) {
					test.BlockedCells.remove(currentCell);
				}

				currentCell = nextCell;

				kilosTraveled++;

				test.printWriter.println(name + ": " + "I am currently in " + currentCell);
				test.printWriter.flush();

			}

			decrementFuelTank();

		}

		synchronized (test.BlockedCells) {
			test.BlockedCells.remove(currentCell);
		}

		test.printWriter.println(name + ": " + "I arrived");
		test.printWriter.flush();

		System.out.println(test.report.incrementNumberOfArrivedCars());

		test.printWriter.println(
				name + ": Done " + "Remaining cars: " + test.report.getNumberOfRemainingCars() + " Arrived cars: "
						+ test.report.getNumberOfArrivedCars() + " Failed cars: " + test.report.getNumberOfFailedCars()
						+ " Never Runned Cars: " + test.report.getnumberOfNeverRunnedCars());
		test.printWriter.flush();

		test.report.addReport(name + ": I started from " + start + " and reached my final destination " + end
				+ " with average speed of 60KM/H in " + kilosTraveled / 60d + "H " + "I traveled a total distance of "
				+ kilosTraveled + "KM My fuel tank in the beginning was " + startFuelTank + " at the end it becomes "
				+ fuelTank);

		if (test.report.isLastCar()) {
			test.report.printReport();
		}

	}

	private boolean decrementFuelAndCheckIfFailed() {

		decrementFuelTank();

		return getFuelTank() <= 0;

	}
}
