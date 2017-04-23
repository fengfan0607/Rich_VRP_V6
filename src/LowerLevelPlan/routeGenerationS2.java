package LowerLevelPlan;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import DataIO.DataIO;
import dataModel.BlackBoard;
import dataModel.Request;
import dataModel.SolutionForEachDay;
import dataModel.SolutionsAll;
import dataModel.UpperPlan;

public class routeGenerationS2 implements DataIO {
	BlackBoard dataModel;
	int[][] distance;
	int[] config;
	int maximumAllowedTravelDistance;
	int[] toolSize;
	List<Request> requests;
	EvaluationSolutionPerDay espd;
	int[] toolStock;
	interRouteSearch interSwap;
	Evaluation evaluation;

	public routeGenerationS2(BlackBoard bb) {
		// TODO Auto-generated constructor stub
		dataModel = bb;
		interSwap = new interRouteSearch(bb);
		evaluation = new Evaluation(bb);
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
		espd = new EvaluationSolutionPerDay(dataModel);
	}

	public SolutionsAll crateSolutionByInterSwapReq(SolutionsAll sa) {
		// initialFeasiblePlanGen init = new initialFeasiblePlanGen(dataModel);
		SolutionsAll curBestSolution = sa;
		curBestSolution = evaluation.costCal(curBestSolution);
		long curBest = curBestSolution.getTotalCost();
		int conter = 0;
		while (conter < 1000) {
			SolutionsAll nextSolution = generateNewByInterSwap(curBestSolution);
			nextSolution = evaluation.costCal(nextSolution);
			if (nextSolution.getTotalCost() < curBest) {
				curBestSolution = nextSolution;
				curBest = nextSolution.getTotalCost();
			}
			conter++;
		}
		return curBestSolution;
	}

	public SolutionsAll generateNewByInterSwap(SolutionsAll oldSolutions) {
		SolutionsAll nextSolution = new SolutionsAll(oldSolutions.getID() + 1);
		SolutionForEachDay[] oldResult = new SolutionForEachDay[oldSolutions.getSolutions().length];
		for (int i = 0; i < oldSolutions.getSolutions().length; i++) {
			SolutionForEachDay old = oldSolutions.getSolutions()[i];
			SolutionForEachDay neDay = new SolutionForEachDay();
			TreeMap<Integer, List<Integer>> newVehicleRouting = new TreeMap<>();
			newVehicleRouting.putAll(old.getVehicleRoutes());
			neDay.setVehicleRoutes(newVehicleRouting);
			neDay.setDayID(old.getDayID());
			oldResult[i] = neDay;
		}
		nextSolution.setSolutions(interSwap(oldResult));
		int[] preDynamicStock = new int[dataModel.getToolList().size()];
		preDynamicStock = Arrays.copyOf(dataModel.getToolStock(), dataModel.getToolStock().length);

		SolutionForEachDay[] ss = nextSolution.getSolutions();
		for (int i = 0; i < ss.length; i++) {
			SolutionForEachDay solutionForEachDay = nextSolution.getSolutions()[i];
			solutionForEachDay = getObjForDay(ss, i, solutionForEachDay, preDynamicStock);
			preDynamicStock = Arrays.copyOf(solutionForEachDay.getDynamicStock(), dataModel.getToolStock().length);
		}
		nextSolution.setSolutions(ss);
		return nextSolution;
	}

	public SolutionForEachDay getObjForDay(SolutionForEachDay[] solutions, int day, SolutionForEachDay sed,
			int[] dynamicStock) {
		espd.setDynamicStock(dynamicStock);
		sed = espd.costCalPerDay(sed);
		return sed;
	}

	public SolutionForEachDay[] interSwap(SolutionForEachDay[] oldSolutions) {
		SolutionForEachDay[] neighbourSolution = new SolutionForEachDay[oldSolutions.length];
		for (int i = 0; i < oldSolutions.length; i++) {
			if (oldSolutions[i] != null) {
				SolutionForEachDay ss = oldSolutions[i];
				SolutionForEachDay sedNew = interSwap.localSearchSwapRequest(ss);
				neighbourSolution[i] = sedNew;
			}
		}
		return neighbourSolution;
	}

}
