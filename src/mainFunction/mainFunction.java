package mainFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import DataIO.DataIO;
import DataIO.DataOutPut;
import DataIO.DataRead;
import DataIO.readSolution;
import LowerLevelPlan.Evaluation;
import LowerLevelPlan.LocalSearch;
import LowerLevelPlan.RouteValidator;
import LowerLevelPlan.routeGeneration;
import UpperLevelPlan.UpperPlanGen;
import dataModel.BlackBoard;
import dataModel.SolutionsAll;
import dataModel.UpperPlan;

public class mainFunction implements DataIO {
	public static void main(String[] args) {
		DataOutPut.writeFile(outputFileName, title, true);
		DataRead dRead = new DataRead();
		System.err.println("read data .....");
		BlackBoard data = dRead.readData(new BlackBoard());
		UpperPlanGen planGen = new UpperPlanGen(data);
		UpperPlan plan = new UpperPlan();
		// plan.setPlans(readSolution.readPlan(data.getConfig()));
		// plan.setID(0);
		LocalSearch ls = new LocalSearch(data);
		List<SolutionsAll> solutionSet = new ArrayList<>();
		long bestCost = 439643056665l;
		SolutionsAll bestSolution = null;
//		plan = planGen.planGen();
//		routeGeneration rg = new routeGeneration(data);
//		rg.routeGen(plan.getPlans()[2]);
		// SolutionsAll sa = null;
		for (int i = 0; i < 5000; i++) {
			plan = planGen.planGen();
			SolutionsAll sa = ls.newLocalSearch(plan);
			solutionSet.add(sa);
			if (sa.getTotalCost() < bestCost) {
				bestSolution = sa;
				bestCost = bestSolution.getTotalCost();
				System.err.println("current best" + bestSolution.toString());
			}
			// System.err.println();
		}
		for (SolutionsAll sa : solutionSet) {
			System.err.println(sa.toString());
		}
		System.err.println("best cost" + bestCost);
		DataOutPut.writeFile(outputFileName, bestSolution.getOutput(), false);

	}
}
