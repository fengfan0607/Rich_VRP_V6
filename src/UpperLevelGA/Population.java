package UpperLevelGA;

import dataModel.BlackBoard;

public class Population {

	Individual[] individuals;
	BlackBoard data;

	public Population(int popSize, boolean init, BlackBoard data) {
		// TODO Auto-generated constructor stub
		individuals = new Individual[popSize];
		this.data = data;
		if (init) {
			for (int i = 0; i < popSize; i++) {
				Individual newIndividual = new Individual(data);
				newIndividual.generateIndividual();
				saveIndividual(i, newIndividual);
			}
		}
	}

	public Individual[] getIndividuals() {
		return individuals;
	}

	public void setIndividuals(Individual[] individuals) {
		this.individuals = individuals;
	}

	public BlackBoard getData() {
		return data;
	}

	public void setData(BlackBoard data) {
		this.data = data;
	}

	public Individual getIndividual(int index) {
		return individuals[index];
	}

	public Individual getFittest() {
		Individual best = individuals[0];

		for (int i = 0; i < size(); i++) {
			if (getIndividual(i).getFitness() < best.getFitness()) {
				best = getIndividual(i);
			}
		}
		return best;
	}

	public int size() {
		return individuals.length;
	}

	public void saveIndividual(int index, Individual individual) {

		individuals[index] = individual;
	}

}
