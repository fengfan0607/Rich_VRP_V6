package DataIO;

public interface DataIO {

	public static boolean runCase = false;

	//
	// public static String fileNameTest =
	// "testInstance/ParticipantsSuite/myTest/testInstance.txt";
	// public static String titleTest = "DATASET = VeRoLog solver challenge
	// 2017" + "\n" + "NAME = testInstance" + "\n\n";

	public static String instance = "01";
	public static String fileNameTest = "testInstance/ParticipantsSuite/ORTEC_Test/ORTEC_TEST_" + instance + ".txt";
	public static String titleTest = "DATASET = ORTEC Test Set" + "\n" + "NAME = Instance " + instance + "\n\n";

	public static String outputFileName = "testInstance/ParticipantsSuite/solution.txt";
	public static String solutionFileName = "testInstance/ParticipantsSuite/ORTEC_Test/ORTEC_TEST_" + instance
			+ ".sol.txt";

	// all time best competition
	public static String alltimebes = "r100d5_3";
	public static String fileNameAllTimeBest = "testInstance/ParticipantsSuite/AllTimeBest/VeRoLog_" + alltimebes
			+ ".txt";
	public static String titleAllTimeBest = "DATASET = All time best instance set VeRoLog competition" + "\n"
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
