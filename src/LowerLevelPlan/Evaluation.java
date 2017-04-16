package LowerLevelPlan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.security.auth.login.Configuration;

import DataIO.DataIO;
import dataModel.BlackBoard;
import dataModel.Request;
import dataModel.SolutionForEachDay;
import dataModel.SolutionsAll;

public class Evaluation implements DataIO {
	int[] config;
	BlackBoard data;
	int[] dynamicStock;
	int[][] distance;
	int numberOfTools;

	public Evaluation(BlackBoard bb) {
		// TODO Auto-generated constructor stub
		data = bb;
		// esd = new EvaluationSolutionPerDay(data);
		this.config = data.getConfig();
		numberOfTools = config[NUM_OF_TOOLS];
	}

	public SolutionsAll costCal(SolutionsAll solutions) {
		String output = "";
		long totalDistance = 0;
		int totalNumberOfVehicle = 0;
		int maximumNumberOfVehicle = Integer.MIN_VALUE;
		long totalToolCost = 0;
		long totalCost = 0;
		int[] maximumNumberOfToolsInUse = new int[numberOfTools];
		for (int j = 0; j < numberOfTools; j++) {
			maximumNumberOfToolsInUse[j] = Integer.MIN_VALUE;
		}
		String output1 = "";
		SolutionForEachDay[] solutionForEachDays = solutions.getSolutions();
		for (int i = 0; i < solutionForEachDays.length; i++) {
			SolutionForEachDay sed = solutionForEachDays[i];
			output1 += sed.getOutput();
			totalNumberOfVehicle += sed.getTotalNumberOfVehicle();
			totalDistance += sed.getTotalDistance();
			int[] toolInUsePerDay = sed.getToolInUsePerDay();
			for (int j = 0; j < numberOfTools; j++) {
				maximumNumberOfToolsInUse[j] = Math.max(maximumNumberOfToolsInUse[j], toolInUsePerDay[j]);
			}
			maximumNumberOfVehicle = Math.max(maximumNumberOfVehicle, sed.getTotalNumberOfVehicle());
		}
		for (int k = 0; k < numberOfTools; k++) {
			totalToolCost += maximumNumberOfToolsInUse[k] * data.getToolList().get(k).getCostOfTools();
		}
		totalCost = totalDistance * config[DISTANCE_COST] + totalToolCost
				+ maximumNumberOfVehicle * config[VEHICLE_COST] + totalNumberOfVehicle * config[VEHICLE_DAY_COST];

		solutions.setTotalDistance(totalDistance);
		solutions.setTotalVehicle(totalNumberOfVehicle);
		solutions.setMaximumVehiclPerDay(maximumNumberOfVehicle);
		solutions.setMaximumToolInUse(maximumNumberOfToolsInUse);
		solutions.setTotalCost(totalCost);
		solutions.setOutput(output1);
		return solutions;
	}

}
