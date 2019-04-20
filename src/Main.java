import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static ArrayList<Car> allCars = new ArrayList<Car>();

	public static void main(String[] args) throws IOException {
		loadAndRunCars();
	}

	private static void loadAndRunCars() throws FileNotFoundException {

		Scanner sc = new Scanner(new File("sample1.in"));

		int n = sc.nextInt();

		int maxX = sc.nextInt();

		int maxY = sc.nextInt();

		int maxZ = sc.nextInt();

		sc.nextLine();

		Car.map = new Map(maxX, maxY, maxZ);

		Report.setNumberOfCars(n);

		System.out.println(n);
		System.out.println(maxX);
		System.out.println(maxY);
		System.out.println(maxZ);

		Scanner ss = new Scanner(System.in);

		for (int i = 0; i < n; i++) {

			String[] line = sc.nextLine().split(",");

			Cell startCell = new Cell(Integer.parseInt(line[0]), Integer.parseInt(line[1]), Integer.parseInt(line[2]));
			Cell goalCell = new Cell(Integer.parseInt(line[3]), Integer.parseInt(line[4]), Integer.parseInt(line[5]));
			double fuelTank = Double.parseDouble(line[6]);

			if (!Car.map.map.containsKey(startCell) || !Car.map.map.containsKey(goalCell)) {
				System.err.println(startCell);
				System.err.println(goalCell);
				System.err.println(i);
				System.exit(0);
			}

			Car car = new Car(startCell, goalCell, fuelTank, i + 1);

			Thread carThread = new Thread(car);

			carThread.start();

		}

		sc.close();

		System.out.println("Done loading and running all cars");

	}

}
