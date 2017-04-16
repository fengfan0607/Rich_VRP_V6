package DataIO;

public interface DataIO {
	public static String instance = "05";
	// public static String fileName =
	// "testInstance/ParticipantsSuite/ORTEC_TEST_" + instance + ".txt";
	// public static String title = "DATASET = ORTEC Test Set" + "\n" + "NAME =
	// Instance " + instance + "\n\n";

	public static String outputFileName = "testInstance/ParticipantsSuite/solution.txt";
	public static String solutionFileName = "testInstance/ParticipantsSuite/ORTEC_TEST_" + instance + ".sol.txt";

	// all time best competition
	public static String fileName = "testInstance/ParticipantsSuite/VeRoLog_r100d5_1.txt";
	public static String title = "DATASET = All time best instance set VeRoLog competition" + "\n"
			+ "NAME = tools over distance over vehicle days over vehicles with 100 requests over 5 days" + "\n\n";
	public static int DAYS = 0;
	public static int CAPACITY = 1;
	public static int MAX_TRIP_DISTANCE = 2;
	public static int DEPOT_COORDINATE = 3;
	public static int VEHICLE_COST = 4;
	public static int VEHICLE_DAY_COST = 5;
	public static int DISTANCE_COST = 6;
	public static int NUM_OF_TOOLS = 7;
	public static int NUM_OF_CUSTOMER = 8;
	public static int NUM_OF_REQUESTS = 9;

	public static int CONFIG_SIZE = 10;

}
