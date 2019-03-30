import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Report {

	ArrayList<String> carsReport = new ArrayList<String>();

	int numberOfCars = 0;

	int numberOfArrivedCars = 0;

	int numberOfFailedCars = 0;

	int numberOfNeverRunnedCars = 0;

	public Report(int numberOfCars) {
		this.numberOfCars = numberOfCars;
	}

	public void addReport(String report) {
		carsReport.add(report);
	}

	public void printReport() {

		try {

			FileWriter fileWriter = new FileWriter("report.out");

			PrintWriter printWriter = new PrintWriter(fileWriter);

			for (String report : carsReport) {
				printWriter.println(report);
				printWriter.flush();
			}

			fileWriter = new FileWriter("miniReport.out");

			printWriter = new PrintWriter(fileWriter);

			printWriter.println(" Arrived cars: " + getNumberOfArrivedCars() + " Failed cars: "
					+ getNumberOfFailedCars() + " Never run Cars: " + getnumberOfNeverRunnedCars());
			printWriter.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public int getNumberOfCars() {
		return numberOfCars;
	}

	public int getNumberOfArrivedCars() {
		return numberOfArrivedCars;
	}

	public int getNumberOfFailedCars() {
		return numberOfFailedCars;
	}

	public int getnumberOfNeverRunnedCars() {
		return numberOfNeverRunnedCars;
	}

	public synchronized int incrementNumberOfArrivedCars() {
		numberOfArrivedCars++;
		return numberOfArrivedCars;
	}

	public synchronized int incrementNumberOfFailedCars() {
		numberOfFailedCars++;
		return numberOfFailedCars;
	}

	public synchronized int incrementNumberOfNeverRunnedCars() {
		numberOfNeverRunnedCars++;
		return numberOfNeverRunnedCars;
	}

	public int getNumberOfRemainingCars() {
		return numberOfCars - (numberOfArrivedCars + numberOfFailedCars + numberOfNeverRunnedCars);
	}

	public boolean isLastCar() {
		return getNumberOfRemainingCars() == 0;
	}

}
