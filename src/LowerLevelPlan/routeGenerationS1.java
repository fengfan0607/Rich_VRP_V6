package LowerLevelPlan;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import com.google.common.collect.Collections2;

import DataIO.DataIO;
import DataIO.DataOutPut;
import DataIO.DataRead;
import DataIO.readSolution;
import UpperLevelPlan.UpperPlanGen;
import dataModel.BlackBoard;
import dataModel.Request;
import dataModel.SolutionForEachDay;
import dataModel.SolutionsAll;
import dataModel.UpperPlan;

public class routeGenerationS1 implements DataIO {
	BlackBoard dataModel;
	int[][] distance;
	int[] config;
	int maximumAllowedTravelDistance;
	int[] toolSize;
	List<Request> requests;
	EvaluationSolutionPerDay espd;
	int[] toolStock;
	StrategyMinimizeDistance minimizeDistance;
	StrategyMinimizeToolUse minimizeToolUse;

	public routeGenerationS1(BlackBoard bb) {
		// TODO Auto-generated constructor stub

		dataModel = bb;
		this.config = dataModel.getConfig();
		System.err.println("---" + config[MAX_TRIP_DISTANCE]);
		distance = new int[config[NUM_OF_REQUESTS]][config[NUM_OF_REQUESTS]];
		distance = dataModel.getDistance();
		maximumAllowedTravelDistance = config[MAX_TRIP_DISTANCE];
		toolSize = new int[dataModel.getToolList().size()];
		for (int i = 0; i < dataModel.getToolList().size(); i++) {
			toolSize[i] = dataModel.getToolList().get(i).getSize();
		}
		requests = dataModel.getRequests();
		espd = new EvaluationSolutionPerDay(dataModel);
		toolStock = dataModel.getToolStock();
		minimizeDistance = new StrategyMinimizeDistance(dataModel);
		minimizeToolUse = new StrategyMinimizeToolUse(dataModel);
	}

	public SolutionsAll createSoltuionByMiminiDistance(UpperPlan up) {
		int[][] plans = up.getPlans();
		SolutionForEachDay[] solutions = new SolutionForEachDay[up.getPlans().length];
		int[] preDynamicStock = new int[dataModel.getToolList().size()];
		preDynamicStock = Arrays.copyOf(dataModel.getToolStock(), dataModel.getToolStock().length);
		for (int i = 0; i < plans.length; i++) {
			int counter = 50;
			long curBestCost = Long.MAX_VALUE;
			SolutionForEachDay curBest = new SolutionForEachDay();
			while (counter-- > 0) {
				SolutionForEachDay sed = new SolutionForEachDay();
				TreeMap<Integer, List<Integer>> vechileRoutes = minimizeDistance.routeGen(plans[i]);
				sed.setVehicleRoutes(vechileRoutes);
				sed.setDayID(i + 1);
				sed = getObjForDay(solutions, i, sed, preDynamicStock.clone());
				if (counter == 49) {
					curBest = sed;
				}
				if (checkToolUse(sed.getToolInUsePerDay()) && sed.getTotalCostPerDay() < curBestCost) {
					curBest = sed;
					curBestCost = sed.getTotalCostPerDay();
				}
			}
			solutions[i] = curBest;
			System.err.println(curBest.toString());
			preDynamicStock = Arrays.copyOf(curBest.getDynamicStock(), dataModel.getToolStock().length);
		}
		SolutionsAll sa = new SolutionsAll(1);
		sa.setSolutions(solutions);
		return sa;
	}
 
	public boolean checkToolUse(int[] curUse) {
		for (int i = 0; i < toolStock.length; i++) {
			if (curUse[i] > toolStock[i]) {
				return false;
			}
		}
		return true;

	}

	public SolutionForEachDay getObjForDay(SolutionForEachDay[] solutions, int day, SolutionForEachDay sed,
			int[] dynamicStock) {
		espd.setDynamicStock(dynamicStock);
		sed = espd.costCalPerDay(sed);
		return sed;
	}

}
