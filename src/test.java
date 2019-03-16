import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

public class test {

	public static volatile int done = 0;

	public static int getRandomDoubleBetweenRange(int min, int max) {
		int x = (int) ((Math.random() * ((max - min) + 1)) + min);
		return x;
	}

	public static volatile BlockedCells BlockedCells = new BlockedCells();

	public static volatile FileWriter fileWriter;
	public static volatile PrintWriter printWriter;

	public static void main(String[] args) throws IOException {

		fileWriter = new FileWriter("output.out");

		printWriter = new PrintWriter(fileWriter);

		ArrayList<Car> myCars = new ArrayList<Car>();

		ArrayList<Cell> usedCells = new ArrayList<Cell>();

		for (int i = 0; i < 300; i++) {

			Random r = new Random();

			Cell start = new Cell(getRandomDoubleBetweenRange(0, 80), getRandomDoubleBetweenRange(0, 10),
					getRandomDoubleBetweenRange(0, 60));

			while (usedCells.contains(start)) {
				start = new Cell(getRandomDoubleBetweenRange(0, 80), getRandomDoubleBetweenRange(0, 10),
						getRandomDoubleBetweenRange(0, 60));
			}

			usedCells.add(start);

			Car c = new Car(start, new Cell(getRandomDoubleBetweenRange(0, 80), getRandomDoubleBetweenRange(0, 10),
					getRandomDoubleBetweenRange(0, 60)), getRandomDoubleBetweenRange(60, 100), "car" + i);

			myCars.add(c);
		}

		System.out.println("Done Creating Cars");

		for (Car c : myCars) {
			Thread t = new Thread(c);
			t.start();
		}

	}

}
