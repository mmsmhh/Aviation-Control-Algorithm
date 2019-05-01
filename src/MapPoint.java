
public class MapPoint {

	private Cell cell;
	private boolean isBlocked;
	private int carID;

	public MapPoint(Cell cell) {
		this.cell = cell;
		carID = 0;
		isBlocked = false;
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	public int getCarID() {
		return carID;
	}

	public void setCarID(int carID) {
		this.carID = carID;
	}

	public Cell getCell() {
		return cell;
	}

}
