import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Report {

	public static int numberOfCars = 0;

	private int numberOfArrivedCars = 0;

	private int numberOfFailedCars = 0;

	private int numberOfNeverStartedCars = 0;

	private int numberOfEmergencyLandedCars = 0;

	private FileWriter fileWriterFull;

	private PrintWriter printWriterFull;

	private FileWriter fileWriterMini;

	private PrintWriter printWriterMini;

	private FileWriter fileWriterMap;

	private PrintWriter printWriterMap;

	private boolean lastCarFlag = false;

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

	public synchronized void printReports() {

		printWriterMini.println("Arrived cars: " + getNumberOfArrivedCars() + " Failed cars: " + getNumberOfFailedCars()
				+ " Never run Cars: " + getnumberOfNeverStartedCars() + " Emergency Landed Cars: "
				+ getnumberOfEmergencyLandedCars());

		System.out.println("Arrived cars: " + getNumberOfArrivedCars() + " Failed cars: " + getNumberOfFailedCars()
				+ " Never run Cars: " + getnumberOfNeverStartedCars() + " Emergency Landed Cars: "
				+ getnumberOfEmergencyLandedCars());

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

	public int getnumberOfEmergencyLandedCars() {
		return numberOfEmergencyLandedCars;
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

	public synchronized int incrementNumberOfEmergencyLandedCars() {
		numberOfEmergencyLandedCars++;
		return numberOfEmergencyLandedCars;
	}

	public synchronized int getNumberOfRemainingCars() {
		int doneCars = numberOfEmergencyLandedCars + numberOfArrivedCars + numberOfFailedCars
				+ numberOfNeverStartedCars;

		return numberOfCars - doneCars;
	}

	public synchronized boolean isLastCar() {

		if (lastCarFlag) {
			return false;
		}

		if (getNumberOfRemainingCars() == 0) {
			lastCarFlag = true;
			return true;
		}

		return false;
	}

}
