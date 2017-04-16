package LowerLevelPlan;

import java.awt.Event;
import java.util.List;
import java.util.TreeMap;

import org.omg.CORBA.PUBLIC_MEMBER;

import DataIO.DataIO;
import DataIO.DataOutPut;
import dataModel.SolutionForEachDay;
import dataModel.BlackBoard;
import dataModel.SolutionsAll;
import dataModel.UpperPlan;

public class LocalSearch implements DataIO {
	BlackBoard data;
	Evaluation evaluation;
	interRouteSearch interSwap;
	intraRouteSearch intraSwap;
	routeGeneration irs;

	public LocalSearch(BlackBoard bb) {
		// TODO Auto-generated constructor stub
		data = bb;
		evaluation = new Evaluation(bb);
		interSwap = new interRouteSearch(bb);
		intraSwap = new intraRouteSearch(bb);
		irs = new routeGeneration(data);
	}

	public SolutionsAll newLocalSearch(UpperPlan plan) {

		SolutionsAll sa = irs.createSoltuions(plan);
		Evaluation evaluation = new Evaluation(data);
		evaluation.costCal(sa);

		return sa;
	}

	public void search(UpperPlan up) {
		DataOutPut.writeFile(outputFileName, title, true);
		initialFeasiblePlanGen init = new initialFeasiblePlanGen(data);
		SolutionsAll curBestSolution = init.intialSolutionGen(up);
		curBestSolution = evaluation.costCal(curBestSolution);
		long curBest = curBestSolution.getTotalCost();
		int conter = 0;
		System.err.println("Inter search  start best--" + " :" + curBestSolution.toString());
		while (conter < 500000) {
			SolutionsAll nextSolution = generateNewByInterSwap(curBestSolution);
			nextSolution = evaluation.costCal(nextSolution);
			if (nextSolution.getTotalCost() < curBest) {
				curBestSolution = nextSolution;
				curBest = nextSolution.getTotalCost();
				System.err.println("Inter search cur best--" + " :" + curBestSolution.toString());
			}
			conter++;
		}
		System.err.println("Inter search end best--" + " :" + curBestSolution.toString());
		DataOutPut.writeFile(outputFileName + "interbest", curBestSolution.getOutput(), false);
		System.err.println("***************************************************************");
		// System.err.println("intra swap test" + "--------" + "\n");
		//
		// SolutionsAll intraswapBest = generateNewByIntraSwap(curBestSolution);
		// intraswapBest = evaluation.costCal(intraswapBest);
		// System.err.println("Intra search start best--" + " :" +
		// intraswapBest.toString());
		// long curIntraBestCost = intraswapBest.getTotalCost();
		// int intraCounter = 0;
		// while (intraCounter < 500000) {
		// SolutionsAll nextIntraSolution =
		// generateNewByInterSwap(intraswapBest);
		// nextIntraSolution = evaluation.costCal(nextIntraSolution);
		// if (nextIntraSolution.getTotalCost() < curIntraBestCost) {
		// intraswapBest = nextIntraSolution;
		// curIntraBestCost = nextIntraSolution.getTotalCost();
		// System.err.println("Intra search cur best--" + " :" +
		// intraswapBest.toString());
		// }
		// intraCounter++;
		// }
		// System.err.println("Intra search end best--" + " :" +
		// intraswapBest.toString());
		DataOutPut.writeFile(outputFileName, curBestSolution.getOutput(), false);
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
		return nextSolution;
	}

	public SolutionsAll generateNewByIntraSwap(SolutionsAll oldSolutions) {
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
		nextSolution.setSolutions(intraSwap(oldResult));
		return nextSolution;
	}

	public SolutionForEachDay[] intraSwap(SolutionForEachDay[] oldSolutions) {
		SolutionForEachDay[] neighbourSolution = new SolutionForEachDay[oldSolutions.length];
		for (int i = 0; i < oldSolutions.length; i++) {
			if (oldSolutions[i] != null) {
				SolutionForEachDay ss = oldSolutions[i];
				SolutionForEachDay sedNew = intraSwap.intraSwap(ss);
				neighbourSolution[i] = sedNew;
			}
		}
		return neighbourSolution;
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
