import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class BlockedCells extends ArrayList<Cell> {

	private static final long serialVersionUID = 1L;

	public boolean checkIfBlockedCellsContains(Cell c) {

		synchronized (test.BlockedCells) {

			if (test.BlockedCells.size() < 1)
				return false;

			for (int i = 0; i < test.BlockedCells.size(); i++) {

				if (test.BlockedCells.get(i).getX() == c.getX() && test.BlockedCells.get(i).getY() == c.getY()
						&& test.BlockedCells.get(i).getZ() == c.getZ()) {
					return true;
				}
			}

		}

		return false;
	}

}
