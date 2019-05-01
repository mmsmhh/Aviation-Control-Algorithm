import java.util.Hashtable;

public class Map {

	volatile Hashtable<Cell, MapPoint> map;

	private int maxX, maxY, maxZ;

	public Map(int x, int y, int z) {

		map = new Hashtable<Cell, MapPoint>();

		maxX = x;
		maxY = y;
		maxZ = z;

		generateMap(x, y, z);

	}

	private void generateMap(int x, int y, int z) {
		for (int i = 0; i <= x; i++) {
			for (int j = 0; j <= y; j++) {
				for (int k = 0; k <= z; k++) {

					Cell cell = new Cell(i, j, k);
					map.put(cell, new MapPoint(cell));

				}
			}
		}
	}

	public void clearMap() {
		this.map.clear();
	}

	public void blockCell(Cell cell, int ID) {

		synchronized (map) {
			this.map.get(cell).setBlocked(true);
			this.map.get(cell).setCarID(ID);
		}

	}

	public void unblockCell(Cell cell) {

		synchronized (map) {

			synchronized (map) {
				this.map.get(cell).setBlocked(false);
				this.map.get(cell).setCarID(0);
			}

		}

	}

	public int getBlockingID(Cell cell) {
		synchronized (map) {
			return map.get(cell).getCarID();
		}
	}

	public boolean isCellBlocked(Cell cell) {
		synchronized (map) {
			return map.get(cell).isBlocked();
		}
	}

	public boolean contains(Cell cell) {
		return map.containsKey(cell);
	}

	@Override
	public String toString() {
		return map.toString();
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public int getMaxZ() {
		return maxZ;
	}

	public boolean inRange(Cell cell) {

		boolean notMore = cell.getX() <= getMaxX() && cell.getY() <= getMaxY() && cell.getZ() <= getMaxZ();

		boolean notLess = cell.getX() >= 0 && cell.getY() >= 0 && cell.getZ() >= 0;

		boolean inRange = notMore && notLess;

		return inRange;

	}

}
