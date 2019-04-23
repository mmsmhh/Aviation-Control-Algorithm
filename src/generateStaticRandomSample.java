import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class generateStaticRandomSample {

	static int factor = 5;

	static int maxX = 80 * factor;
	static int maxZ = 60 * factor;
	static int maxY = 15;

	static int carsnum = (int) (1000 * factor);

	public static void main(String[] args) throws IOException {

		Car.map = new Map(maxX, maxY, maxZ);

		createCars();

	}

	public static int getRandomDoubleBetweenRange(int min, int max) {
		int x = (int) ((Math.random() * ((max - min) + 1)) + min);
		return x;
	}

	public static void createCars() throws IOException {

		FileWriter fileWriter = new FileWriter("sample1.in");

		PrintWriter printWriter = new PrintWriter(fileWriter);

		ArrayList<Cell> usedCells = new ArrayList<Cell>();

		int carCounter = 1;

		int part = carsnum / 3;

		ArrayList<Integer> fuelTanks = new ArrayList<Integer>();

		printWriter.println(carsnum + " " + maxX + " " + maxY + " " + maxZ);

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

			Cell start = new Cell(getRandomDoubleBetweenRange(0, maxX / 4), 0,
					getRandomDoubleBetweenRange(0, maxZ / 4));

			while (usedCells.contains(start) || (!Car.map.contains(start))) {
				start = new Cell(getRandomDoubleBetweenRange(0, maxX / 4), 0, getRandomDoubleBetweenRange(0, maxZ / 4));
			}

			usedCells.add(start);

			Car c = new Car(start,
					new Cell(getRandomDoubleBetweenRange(0, maxX / 4), 0, getRandomDoubleBetweenRange(0, maxZ / 4)),
					fuelTanks.remove(getRandomDoubleBetweenRange(1, fuelTanks.size()) - 1), carCounter++);

			while (!Car.map.contains(c.getStartCell()) || !Car.map.contains(c.getStartCell()))

			{

				c = new Car(start,
						new Cell(getRandomDoubleBetweenRange(0, maxX / 4), 0, getRandomDoubleBetweenRange(0, maxZ / 4)),
						fuelTanks.remove(getRandomDoubleBetweenRange(1, fuelTanks.size()) - 1), carCounter++);

			}

			printWriter.println(c.getStartCell().getX() + "," + c.getStartCell().getY() + "," + c.getStartCell().getZ()
					+ "," + c.getGoalCell().getX() + "," + c.getGoalCell().getY() + "," + c.getGoalCell().getZ() + ","
					+ c.getFuelTank());
			printWriter.flush();

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

			Cell start = new Cell(getRandomDoubleBetweenRange(0, maxX / 3), 0,
					getRandomDoubleBetweenRange(0, maxZ / 3));

			while (usedCells.contains(start) || (!Car.map.contains(start))) {
				start = new Cell(getRandomDoubleBetweenRange(0, maxX / 3), 0, getRandomDoubleBetweenRange(0, maxZ / 3));
			}

			usedCells.add(start);

			Car c = new Car(start,
					new Cell(getRandomDoubleBetweenRange(0, maxX / 4), 0, getRandomDoubleBetweenRange(0, maxZ / 4)),
					fuelTanks.remove(getRandomDoubleBetweenRange(1, fuelTanks.size()) - 1), carCounter++);

			while (!Car.map.contains(c.getStartCell()) || !Car.map.contains(c.getStartCell()))

			{

				c = new Car(start,
						new Cell(getRandomDoubleBetweenRange(maxX / 2, maxX), 0,
								getRandomDoubleBetweenRange(maxZ / 2, maxZ)),
						fuelTanks.remove(getRandomDoubleBetweenRange(1, fuelTanks.size()) - 1), carCounter++);

			}

			printWriter.println(c.getStartCell().getX() + "," + c.getStartCell().getY() + "," + c.getStartCell().getZ()
					+ "," + c.getGoalCell().getX() + "," + c.getGoalCell().getY() + "," + c.getGoalCell().getZ() + ","
					+ c.getFuelTank());
			printWriter.flush();

			System.out.println("Car " + (carCounter - 1) + " has been created with fuel tank " + c.getFuelTank());
		}

		// part 3
		for (int i = 0; i < carsnum - 2 * part; i++) {

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

			Cell start = new Cell(getRandomDoubleBetweenRange(0, maxX / 6), 0,
					getRandomDoubleBetweenRange(0, maxZ / 6));

			while (usedCells.contains(start) || (!Car.map.contains(start))) {
				start = new Cell(getRandomDoubleBetweenRange(0, maxX / 6), 0, getRandomDoubleBetweenRange(0, maxZ / 6));
			}

			usedCells.add(start);

			Car c = new Car(start,
					new Cell(getRandomDoubleBetweenRange(0, maxX / 4), 0, getRandomDoubleBetweenRange(0, maxZ / 4)),
					fuelTanks.remove(getRandomDoubleBetweenRange(1, fuelTanks.size()) - 1), carCounter++);

			while (!Car.map.contains(c.getStartCell()) || !Car.map.contains(c.getStartCell()))

			{
				c = new Car(start,
						new Cell(getRandomDoubleBetweenRange(maxX + maxX / 6, maxX), 0,
								getRandomDoubleBetweenRange(maxZ + maxZ / 6, maxZ)),
						fuelTanks.remove(getRandomDoubleBetweenRange(1, fuelTanks.size()) - 1), carCounter++);
			}

			printWriter.println(c.getStartCell().getX() + "," + c.getStartCell().getY() + "," + c.getStartCell().getZ()
					+ "," + c.getGoalCell().getX() + "," + c.getGoalCell().getY() + "," + c.getGoalCell().getZ() + ","
					+ c.getFuelTank());
			printWriter.flush();
			System.out.println("Car " + (carCounter - 1) + " has been created with fuel tank " + c.getFuelTank());
		}

		printWriter.close();
		System.out.println("Done Creating all Cars");
	}

}
