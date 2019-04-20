import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Report {

	ArrayList<String> carsFullReport = new ArrayList<String>();
	ArrayList<String> carsMiniReport = new ArrayList<String>();

	public static int numberOfCars = 0;

	int numberOfArrivedCars = 0;

	int numberOfFailedCars = 0;

	int numberOfNeverStartedCars = 0;

	FileWriter fileWriterFull;

	PrintWriter printWriterFull;

	FileWriter fileWriterMini;

	PrintWriter printWriterMini;

	FileWriter fileWriterMap;

	PrintWriter printWriterMap;

	public Report() {
		try {

			fileWriterFull = new FileWriter("fullReport.out");
			printWriterFull = new PrintWriter(fileWriterFull);

			fileWriterMini = new FileWriter("miniReport.out");
			printWriterMini = new PrintWriter(fileWriterMini);

			fileWriterMap = new FileWriter("mapReport.out");
			printWriterMap = new PrintWriter(fileWriterMap);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void addToFullReport(String report) {
		printWriterFull.println(report);
	}

	public synchronized void addToMiniReport(String report) {
		printWriterMini.println(report);
	}

	public void addToMapReport(String line) {
		printWriterMap.println(line);
	}

	public void printReports() {

		printWriterMini.println("Arrived cars: " + getNumberOfArrivedCars() + " Failed cars: " + getNumberOfFailedCars()
				+ " Never run Cars: " + getnumberOfNeverStartedCars());
		printWriterMini.flush();

		printWriterFull.close();

		printWriterMini.close();
		
		printWriterMap.close();


	}

	public static void setNumberOfCars(int numberOfCars) {
		Report.numberOfCars = numberOfCars;
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

	public int getnumberOfNeverStartedCars() {
		return numberOfNeverStartedCars;
	}

	public synchronized int incrementNumberOfArrivedCars() {
		numberOfArrivedCars++;
		return numberOfArrivedCars;
	}

	public synchronized int incrementNumberOfFailedCars() {
		numberOfFailedCars++;
		return numberOfFailedCars;
	}

	public synchronized int incrementnumberOfNeverStartedCars() {
		numberOfNeverStartedCars++;
		return numberOfNeverStartedCars;
	}

	public int getNumberOfRemainingCars() {
		return numberOfCars - (numberOfArrivedCars + numberOfFailedCars + numberOfNeverStartedCars);
	}

	public boolean isLastCar() {
		return getNumberOfRemainingCars() == 0;
	}

}
