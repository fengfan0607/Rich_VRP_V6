package UpperLevelGA;

import java.util.ArrayList;
import java.util.List;

import DataIO.DataIO;
import DataIO.DataOutPut;
import DataIO.DataRead;
import LowerLevelPlan.LocalSearch;
import dataModel.BlackBoard;
import dataModel.SolutionsAll;
import dataModel.UpperPlan;

public class GA implements DataIO {
	public static String title;
	public static String testFile;

	public static void main(String[] args) {
		if (runCase) {
			title = titleTest;
			testFile = fileNameTest;
		} else {
			title = titleAllTimeBest;
			testFile = fileNameAllTimeBest;
		}
		DataOutPut.writeFile(outputFileName, title, true);
		DataRead dRead = new DataRead(testFile);
		System.err.println("read data .....");
		BlackBoard data = dRead.readData(new BlackBoard());
		Population myPop = new Population(10, true, data);
		int generationCount = 0;
		double best = Double.MAX_VALUE;
		Individual bestInd = new Individual(data);
		while (generationCount < 1000) {
			generationCount++;

			if (myPop.getFittest().getFitness() < best) {
				best = myPop.getFittest().getFitness();
				bestInd = myPop.getFittest();
			}
			System.out.println("Generation: " + generationCount + " Fittest: " + best);
			// myPop = new Population(10, true, data);
			myPop = Algorithm.evolvePop(myPop);
		}
		System.out.println("Solution found!");
		System.out.println("Generation: " + generationCount);
		System.out.println("Genes:");
		System.out.println(myPop.getFittest());

		LocalSearch ls = new LocalSearch(data);
		List<SolutionsAll> solutionSet = new ArrayList<>();
		long bestCost = Long.MAX_VALUE;
		SolutionsAll bestSolution = null;
		UpperPlan plan = new UpperPlan();
		plan.setPlans(bestInd.getChromsome());
		for (int i = 0; i < 20; i++) {
			plan.setID(i + 1);
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
		// System.err.println(bestSolution.getOutput());
		DataOutPut.writeFile(outputFileName, bestSolution.getOutput(), false);

	}
}
