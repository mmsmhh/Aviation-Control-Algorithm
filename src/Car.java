public class Car implements Runnable {

	Cell start;
	Cell end;

	CellSpace space;

	CostBlockManager blockManager;

	String name;

	public Car(Cell start, Cell end, String name) {

		this.start = start;
		this.end = end;
		this.name = name;

		space = new CellSpace();
		blockManager = new CostBlockManager(space);

		space.setGoalCell(end.getX(), end.getY(), end.getZ());
		space.setStartCell(start.getX(), start.getY(), start.getZ());
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
		// TODO Auto-generated method stub
		Path path = findPath();
//		test.BlockedCells

		test.printWriter.println(name + ": " + "my path is " + path);
		test.printWriter.flush();

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

				space.clearSpace(IAMIN.getX(), IAMIN.getX(), IAMIN.getX());

				blockManager.blockCell(space.makeNewCell(cur.getX(), cur.getX(), cur.getX()));
				path = findPath();

				test.printWriter.println(name + ": " + "Done from replanning the new path is " + path);
				test.printWriter.flush();

			} else {

				synchronized (test.BlockedCells) {
					test.BlockedCells.add(cur);
				}

				test.printWriter.println(name + ": " + "I am moving now to " + cur);
				test.printWriter.flush();

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				synchronized (test.BlockedCells) {
					test.BlockedCells.remove(IAMIN);
				}

				IAMIN = cur;

				test.printWriter.println(name + ": " + "I am currently in " + IAMIN);
				test.printWriter.flush();

				if (path.isEmpty()) {
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

		test.printWriter.println(name + " Done remaining " + (240 - test.done));
		test.printWriter.flush();
	}

}
