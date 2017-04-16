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
import test.test;

public class mainFunction implements DataIO {
	public static String title;
	public static String testFile;
	public static void main(String[] args) {
		if(runCase){
			title=titleTest;
			testFile = fileNameTest;
		}else{
			title = titleAllTimeBest;
			testFile = fileNameAllTimeBest;
		}
		DataOutPut.writeFile(outputFileName, title, true);
		DataRead dRead = new DataRead(testFile);
		System.err.println("read data .....");
		BlackBoard data = dRead.readData(new BlackBoard());
		UpperPlanGen planGen = new UpperPlanGen(data);
		UpperPlan plan = new UpperPlan();
		LocalSearch ls = new LocalSearch(data);
		List<SolutionsAll> solutionSet = new ArrayList<>();
		long bestCost = Long.MAX_VALUE;
		SolutionsAll bestSolution = null;
		for (int i = 0; i < 10; i++) {
			plan = planGen.planGen();
			plan.setID(i+1);
			SolutionsAll sa = ls.newLocalSearch(plan);
			solutionSet.add(sa);
			if (sa.getTotalCost() < bestCost) {
				bestSolution = sa;
				bestCost = bestSolution.getTotalCost();
				System.err.println("current best" + bestSolution.toString());
			}
		}
		for (SolutionsAll sa : solutionSet) {
			System.err.println(sa.toString());
		}
		System.err.println("best solotuion" + bestSolution.toString());
//		System.err.println(bestSolution.getOutput());
		DataOutPut.writeFile(outputFileName, bestSolution.getOutput(), false);

	}
}
