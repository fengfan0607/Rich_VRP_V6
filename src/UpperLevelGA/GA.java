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

	public static Individual GA_plan(BlackBoard data) {
		Population myPop = new Population(1, true, data);
		int generationCount = 0;
		double best = Double.MAX_VALUE;
		Individual bestInd = new Individual(data);
		while (generationCount < 1) {
			generationCount++;

			if (myPop.getFittest().getFitness() < best) {
				best = myPop.getFittest().getFitness();
				bestInd = myPop.getFittest();
			}
			System.out.println("Generation: " + generationCount + " Fittest: " + best);
			// myPop = new Population(10, true, data);
			// myPop = Algorithm.evolvePop(myPop);
		}
		System.out.println("Solution found!");
		System.out.println("Genera	tion: " + generationCount);
		System.out.println("Genes:");
		System.out.println(myPop.getFittest());
		return bestInd;
	}
}
