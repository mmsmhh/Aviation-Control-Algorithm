public class Car implements Runnable {

	private Cell start;
	private Cell end;

	private CellSpace space;

	private CostBlockManager blockManager;

	private int battery;

	String name;
	

	public Car(Cell start, Cell end, int battery, String name) {

		this.start = start;
		this.end = end;
		this.name = name;
		this.battery = battery;

		space = new CellSpace();
		blockManager = new CostBlockManager(space);

		space.setGoalCell(end.getX(), end.getY(), end.getZ());
		space.setStartCell(start.getX(), start.getY(), start.getZ());
	}

	public void decrementBattery() {
		battery--;
	}

	public int getBattery() {
		return battery;
	}

	public Path findPath() {

		Pathfinder pathfinder = new Pathfinder(blockManager);

		Path path = pathfinder.findPath();

		return path;
	}

	public Cell getStart() {
		return start;
	}

	public void setStart(Cell start) {
		this.start = start;
	}

	public Cell getEnd() {
		return end;
	}

	public void setEnd(Cell end) {
		this.end = end;
	}

	@Override
	public void run() {

		Path path = findPath();

		test.printWriter.println(name + ": " + "my path is " + path);
		test.printWriter.flush();

		if (getBattery() <= path.size()) {
			test.printWriter.println(name + ": You current battery is not enough to go to " + end
					+ " you need to go to the charging station");
			test.printWriter.flush();

			test.done += 1;
			System.out.println(test.done);

			return;
		}

		Cell IAMIN = path.removeFirst();

		test.printWriter.println(name + ": " + "I am currently in " + IAMIN);
		test.printWriter.flush();

		synchronized (test.BlockedCells) {

			test.BlockedCells.add(IAMIN);

		}

		while (!path.isEmpty()) {

			Cell cur = path.removeFirst();

			if (test.BlockedCells.checkIfBlockedCellsContains(cur)) {

				test.printWriter.println(name + ": " + "The " + cur + " is blocked rePlanning now");
				test.printWriter.flush();

				space.clearSpace(IAMIN.getX(), IAMIN.getY(), IAMIN.getZ());

				synchronized (test.BlockedCells) {

					for (Cell c : test.BlockedCells)
						blockManager.blockCell(space.makeNewCell(c.getX(), c.getY(), c.getZ()));

				}

				path = findPath();

				path.removeFirst();

				while (getBattery() <= path.size()) {

					test.printWriter.println(name
							+ ": You current battery is not enough to go to the new replanned path replanning again"
							+ " your current batter is: " + getBattery() + " the battery needed is: " + path.size());
					test.printWriter.flush();

					space.clearSpace(IAMIN.getX(), IAMIN.getY(), IAMIN.getZ());

					synchronized (test.BlockedCells) {

						for (Cell c : test.BlockedCells)
							blockManager.blockCell(space.makeNewCell(c.getX(), c.getY(), c.getZ()));

					}

					path = findPath();

					path.removeFirst();

				}

				test.printWriter.println(name + ": " + "Done from replanning the new path is " + path);
				test.printWriter.flush();

			} else {

				synchronized (test.BlockedCells) {
					test.BlockedCells.add(cur);
				}

				test.printWriter.println(name + ": " + "I am moving now to " + cur);
				test.printWriter.flush();

				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				synchronized (test.BlockedCells) {
					test.BlockedCells.remove(IAMIN);
				}

				IAMIN = cur;

				decrementBattery();

				test.printWriter.println(name + ": " + "I am currently in " + IAMIN);
				test.printWriter.flush();

				if (path.isEmpty()) {

					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					synchronized (test.BlockedCells) {
						test.BlockedCells.remove(IAMIN);
					}
				}

			}

		}

		test.printWriter.println(name + ": " + "I arrived");
		test.printWriter.flush();

		test.done += 1;
		System.out.println(test.done);

		test.printWriter.println(name + " Done remaining " + (test.getCount() - test.done));
		test.printWriter.flush();
	}

}
