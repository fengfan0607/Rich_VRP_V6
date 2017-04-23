package UpperLevelGA;

import java.util.Arrays;
import java.util.function.IntPredicate;

import javax.xml.crypto.Data;

import DataIO.DataIO;
import dataModel.BlackBoard;
import dataModel.Request;

public class FitnessCal implements DataIO {

	static double getFitness(Individual individual) {
		BlackBoard data = individual.getData();
		int numberOfTools = data.getToolList().size();
		int[] dynamicStock = Arrays.copyOf(data.getToolStock(), numberOfTools);
		int[] depotStock = Arrays.copyOf(data.getToolStock(), numberOfTools);
		int[] maximumToolUsed = new int[numberOfTools];
		int[][] plan = individual.getChromsome();
		int[] toolUsedMaimumPerDay = new int[numberOfTools];
		int[] toolUsedMinimumPerDay = new int[numberOfTools];
		for (int i = 0; i < numberOfTools; i++) {
			toolUsedMaimumPerDay[i] = Integer.MIN_VALUE;
			toolUsedMinimumPerDay[i] = Integer.MAX_VALUE;
		}
		int maximumReq = Integer.MIN_VALUE;
		for (int i = 0; i < plan.length; i++) {
			int[] toolUsed = new int[numberOfTools];
			int reqNum = 0;
			for (int j = 0; j < data.getRequests().size(); j++) {
				if (plan[i][j] == 0) {
					continue;
				} else {
					reqNum++;
					Request request = data.getRequests().get(j);
					int toolKind = request.getRequestToolKind();
					int toolNumber = request.getRequestToolNumber();
					if (plan[i][j] == 1) {
						dynamicStock[toolKind - 1] -= toolNumber;
					} else {
						dynamicStock[toolKind - 1] += toolNumber;
					}
				}
			}
			maximumReq = Math.max(maximumReq, reqNum);
			for (int k = 0; k < numberOfTools; k++) {
				toolUsed[k] = depotStock[k] - dynamicStock[k];
				toolUsedMaimumPerDay[k] = Math.max(toolUsedMaimumPerDay[k], toolUsed[k]);
				toolUsedMinimumPerDay[k] = Math.min(toolUsedMinimumPerDay[k], toolUsed[k]);
				if (toolUsed[k] > maximumToolUsed[k]) {
					maximumToolUsed[k] = toolUsed[k];
				}
			}
		}
		double fitness = 0.0;
		for (int k = 0; k < numberOfTools; k++) {
			// if (maximumToolUsed[k] > depotStock[k]) {
			// fitness += 100;
			// }
			// // fitness += 50 * toolUsedMaimumPerDay[k];
			fitness += (double) maximumToolUsed[k] / depotStock[k];
		}
		fitness += 100 * maximumReq;
		return fitness;
	}

	static int[][] toolUsedCal(Individual individual) {

		BlackBoard data = individual.getData();
		int numberOfTools = data.getToolList().size();
		int[] dynamicStock = Arrays.copyOf(data.getToolStock(), numberOfTools);
		int[] depotStock = Arrays.copyOf(data.getToolStock(), numberOfTools);
		int[] maximumToolUsed = new int[numberOfTools];
		int[][] plan = individual.getChromsome();

		int[][] toolUsedAll = new int[data.getConfig()[DAYS]][numberOfTools];
		for (int i = 0; i < plan.length; i++) {
			int[] toolUsed = new int[numberOfTools];
			for (int j = 0; j < data.getRequests().size(); j++) {
				if (plan[i][j] == 0) {
					continue;
				} else {
					Request request = data.getRequests().get(j);
					int toolKind = request.getRequestToolKind();
					int toolNumber = request.getRequestToolNumber();
					if (plan[i][j] == 1) {
						dynamicStock[toolKind - 1] -= toolNumber;
					} else {
						dynamicStock[toolKind - 1] += toolNumber;
					}
				}
			}
			for (int k = 0; k < numberOfTools; k++) {
				toolUsed[k] = depotStock[k] - dynamicStock[k];
			}
			toolUsedAll[i] = toolUsed;
		}
		return toolUsedAll;
	}

}
