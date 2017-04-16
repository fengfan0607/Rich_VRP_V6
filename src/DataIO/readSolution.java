package DataIO;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import com.google.common.base.CharMatcher;

import dataModel.BlackBoard;

public class readSolution implements DataIO {

	public static int[][] readPlan(int[] config) {
		int nRequests = config[NUM_OF_REQUESTS];
		int day = config[DAYS];
		int[][] plan = new int[day][nRequests];
		try {
			FileInputStream io = new FileInputStream(solutionFileName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(io));
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.contains("DAY")) {
					int curDay = Integer.valueOf(CharMatcher.DIGIT.retainFrom(line)) - 1;
					line = reader.readLine();
					while (!line.contains("DAY") && line.contains("0")) {
						String[] buf = line.split("\t");
						String[] data = Arrays.copyOfRange(buf, 2, buf.length);
						for (int i = 0; i < data.length; i++) {
							int value = Integer.valueOf(data[i]);
							if (value > 0) {
								plan[curDay][value - 1] = 1;
							} else if (value < 0) {
								value = Math.abs(value);
								plan[curDay][value - 1] = -1;
							}
						}
//						System.err.println(Arrays.toString(data));
						line = reader.readLine();
					}
				}
			}
		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		StringBuilder sBuilder = new StringBuilder();
		for (int i = 0; i < day; i++) {
			for (int j = 0; j < nRequests; j++) {
				sBuilder.append(plan[i][j] + ",");
			}
			sBuilder.append("\n");
		}
		// System.err.println(sBuilder.toString());
		return plan;
	}

	// public static void main(String[] args) {
	// DataRead read = new DataRead();
	// BlackBoard board = new BlackBoard();
	// board = read.readData(board);
	// readPlan(board.getConfig());
	// }
}
