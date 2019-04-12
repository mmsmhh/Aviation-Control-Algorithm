import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class test {

	public static volatile int numberOfArrivedCars = 0;

	public static volatile int numberOfFailedCars = 0;

	static int factor = 1;

	static int maxX = 80 * factor;
	static int maxY = 60 * factor;
	static int maxZ = 10;

	public static volatile BlockedCells BlockedCells = new BlockedCells();

	public static volatile FileWriter fileWriter;

	public static volatile PrintWriter printWriter;
	public static volatile Report report;

	public static void main(String[] args) throws IOException {

		fileWriter = new FileWriter("output.out");

		printWriter = new PrintWriter(fileWriter);

		report = new Report(1000 * factor);

		createCarsAndRunThem();

	}

	public static void createCarsAndRunThem() {

		ArrayList<Cell> usedCells = new ArrayList<Cell>();

		ArrayList<Car> myCars = new ArrayList<Car>();

		int carCounter = 1;

		int part = report.getNumberOfCars() / 3;

		ArrayList<Integer> fuelTanks = new ArrayList<Integer>();

		// part 1
		for (int i = 0; i < part; i++) {

			if (i % 10 == 0) {
				fuelTanks.clear();
				fuelTanks.add(30);
				fuelTanks.add(35);
				fuelTanks.add(40);
				fuelTanks.add(45);
				fuelTanks.add(48);
				fuelTanks.add(50);
				fuelTanks.add(55);
				fuelTanks.add(58);
				fuelTanks.add(60);
				fuelTanks.add(65);
			}

			Cell start = new Cell(getRandomDoubleBetweenRange(0, maxX / 4), getRandomDoubleBetweenRange(0, maxY),
					getRandomDoubleBetweenRange(0, maxZ / 4));

			while (usedCells.contains(start)) {
				start = new Cell(getRandomDoubleBetweenRange(0, maxX / 4), getRandomDoubleBetweenRange(0, maxY),
						getRandomDoubleBetweenRange(0, maxZ / 4));
			}

			usedCells.add(start);

			Car c = new Car(start,
					new Cell(getRandomDoubleBetweenRange(0, maxX / 4), getRandomDoubleBetweenRange(0, maxY),
							getRandomDoubleBetweenRange(0, maxZ / 4)),
					fuelTanks.remove(getRandomDoubleBetweenRange(1, fuelTanks.size()) - 1), "car" + carCounter++);

			myCars.add(c);

			System.out.println("Car " + (carCounter - 1) + " has been created with fuel tank " + c.getFuelTank());
		}

		// part2
		for (int i = 0; i < part; i++) {

			if (i % 10 == 0) {
				fuelTanks.clear();
				fuelTanks.add(30);
				fuelTanks.add(35);
				fuelTanks.add(40);
				fuelTanks.add(45);
				fuelTanks.add(48);
				fuelTanks.add(50);
				fuelTanks.add(55);
				fuelTanks.add(58);
				fuelTanks.add(60);
				fuelTanks.add(65);
			}

			Cell start = new Cell(getRandomDoubleBetweenRange(0, maxX / 3), getRandomDoubleBetweenRange(0, maxY),
					getRandomDoubleBetweenRange(0, maxZ / 3));

			while (usedCells.contains(start)) {
				start = new Cell(getRandomDoubleBetweenRange(0, maxX / 3), getRandomDoubleBetweenRange(0, maxY),
						getRandomDoubleBetweenRange(0, maxZ / 3));
			}

			usedCells.add(start);

			Car c = new Car(start,
					new Cell(getRandomDoubleBetweenRange(maxX / 2, maxX), getRandomDoubleBetweenRange(0, maxY),
							getRandomDoubleBetweenRange(maxZ / 2, maxZ)),
					fuelTanks.remove(getRandomDoubleBetweenRange(1, fuelTanks.size()) - 1), "car" + carCounter++);

			myCars.add(c);

			System.out.println("Car " + (carCounter - 1) + " has been created with fuel tank " + c.getFuelTank());
		}

		// part 3
		for (int i = 0; i < report.getNumberOfCars() - 2 * part; i++) {

			if (i % 10 == 0) {
				fuelTanks.clear();
				fuelTanks.add(30);
				fuelTanks.add(35);
				fuelTanks.add(40);
				fuelTanks.add(45);
				fuelTanks.add(48);
				fuelTanks.add(50);
				fuelTanks.add(55);
				fuelTanks.add(58);
				fuelTanks.add(60);
				fuelTanks.add(65);
			}

			Cell start = new Cell(getRandomDoubleBetweenRange(0, maxX / 6), getRandomDoubleBetweenRange(0, maxY),
					getRandomDoubleBetweenRange(0, maxZ / 6));

			while (usedCells.contains(start)) {
				start = new Cell(getRandomDoubleBetweenRange(0, maxX / 6), getRandomDoubleBetweenRange(0, maxY),
						getRandomDoubleBetweenRange(0, maxZ / 6));
			}

			usedCells.add(start);

			Car c = new Car(start,
					new Cell(getRandomDoubleBetweenRange(maxX + maxX / 6, maxX), getRandomDoubleBetweenRange(0, maxY),
							getRandomDoubleBetweenRange(maxZ + maxZ / 6, maxZ)),
					fuelTanks.remove(getRandomDoubleBetweenRange(1, fuelTanks.size()) - 1), "car" + carCounter++);

			myCars.add(c);

			System.out.println("Car " + (carCounter - 1) + " has been created with fuel tank " + c.getFuelTank());
		}

		System.out.println("Done Creating all Cars");

		for (Car c : myCars) {
			Thread t = new Thread(c);
			t.start();
		}

		System.out.println("Done Running all Cars");
	}

	public static int getRandomDoubleBetweenRange(int min, int max) {
		int x = (int) ((Math.random() * ((max - min) + 1)) + min);
		return x;
	}

}
