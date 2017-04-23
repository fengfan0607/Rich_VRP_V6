package UpperLevelGA;

import java.util.concurrent.ThreadLocalRandom;

import dataModel.BlackBoard;
import dataModel.Request;

public class Algorithm {

	private static final double uniformRate = 0.1;
	private static final double mutationRate = 0.3;
	private static final int tournamentSize = 5;
	private static final boolean elitism = true;

	public static Population evolvePop(Population pop) {

		Population newPop = new Population(pop.size(), false, pop.getData());
		if (elitism) {
			newPop.saveIndividual(0, pop.getFittest());
		}

		int elitismOffset;
		if (elitism) {
			elitismOffset = 1;
		} else {
			elitismOffset = 0;
		}
		// Loop over the population size and create new individuals with
		// crossover
		for (int i = elitismOffset; i < pop.size(); i++) {
			Individual indiv1 = tournamentSelection(pop);
			Individual indiv2 = tournamentSelection(pop);
			Individual newIndiv = crossover(indiv1, indiv2, pop.getData());
			newPop.saveIndividual(i, newIndiv);
		}

		// Mutate population
		for (int i = elitismOffset; i < newPop.size(); i++) {
			mutate(newPop.getIndividual(i));
		}

		return newPop;
	}

	// Crossover individuals
	private static Individual crossover(Individual indiv1, Individual indiv2, BlackBoard data) {
		Individual newSol = new Individual(data);
		int day = indiv1.getChromsome().length;
		int numberOfreq = indiv1.getChromsome()[0].length;
		// Loop through genes
		for (int i = 0; i < numberOfreq; i++) {
			// Crossover
			if (Math.random() <= uniformRate) {
				for (int j = 0; j < day; j++) {
					if (indiv1.getPlanAtDay(j, i) > 0) {
						newSol.setPlan(i, j);
					}
				}
			} else {
				for (int j = 0; j < day; j++) {
					if (indiv2.getPlanAtDay(j, i) > 0) {
						newSol.setPlan(i, j);
					}
				}
			}
		}
		return newSol;
	}

	// Mutate an individual
	private static void mutate(Individual indiv) {
		// Loop through genes
		int[][] plan = indiv.getChromsome();

		for (int i = 0; i < plan.length; i++) {
			for (int j = 0; j < plan[0].length; j++) {
				if (Math.random() <= mutationRate && plan[i][j] > 0) {
					// Create random gene
					Request selectReq = indiv.getData().getRequests().get(j);
					if (!indiv.getHasAssciationReq().contains(selectReq.getId())) {
						int newDay = ThreadLocalRandom.current().nextInt(selectReq.getStart_Time(),
								selectReq.getEnd_Time() + 1);
						indiv.changePlan(i, j, newDay - 1);
					}

				}
			}
		}
	}

	private static Individual tournamentSelection(Population pop) {
		// Create a tournament population
		Population tournament = new Population(tournamentSize, false, pop.getData());
		// For each place in the tournament get a random individual
		for (int i = 0; i < tournamentSize; i++) {
			int randomId = (int) (Math.random() * pop.size());
			tournament.saveIndividual(i, pop.getIndividual(randomId));
		}
		// Get the fittest
		Individual fittest = tournament.getFittest();
		return fittest;
	}
}
