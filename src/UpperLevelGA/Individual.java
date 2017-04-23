package UpperLevelGA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import DataIO.DataIO;
import LowerLevelPlan.interRouteSearch;
import UpperLevelPlan.UpperLevelPlanUtil2;
import dataModel.BlackBoard;
import dataModel.Request;
import dataModel.UpperPlan;

public class Individual implements DataIO {
	private double fitness = 0.0;
	private int[][] chromsome;
	private BlackBoard data;
	public int[][] toolUsed;
	public Set<Integer> hasAssciationReq;
	List<List<Integer[]>> associateList;

	public int[][] getToolUsed() {
		return FitnessCal.toolUsedCal(this);
	}

	public List<List<Integer[]>> getAssociateList() {
		return associateList;
	}

	public void setAssociateList(List<List<Integer[]>> associateList) {
		this.associateList = associateList;
	}

	public void setToolUsed(int[][] toolUsed) {
		this.toolUsed = toolUsed;
	}

	public BlackBoard getData() {
		return data;
	}

	public Set<Integer> getHasAssciationReq() {
		return hasAssciationReq;
	}

	public void setHasAssciationReq(Set<Integer> hasAssciationReq) {
		this.hasAssciationReq = hasAssciationReq;
	}

	public void setData(BlackBoard data) {
		this.data = data;
	}

	public Individual(BlackBoard bb) {
		// TODO Auto-generated constructor stub
		data = bb;
		chromsome = new int[data.getConfig()[DAYS]][data.getRequests().size()];
		hasAssciationReq = new HashSet<>();
		associateList = new ArrayList<>();
	}

	public void generateIndividual() {
		UpperLevelPlanUtil2 util = new UpperLevelPlanUtil2(data);
		// UpperPlan plan = util.planCreate();
		chromsome = util.planCreate1();
		hasAssciationReq = util.getHasAssociationReq();
		associateList = util.getAssociateList();
	}

	public double getFitness() {
		if (fitness == 0.0) {
			fitness = FitnessCal.getFitness(this);
		}
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public int[][] getChromsome() {
		return chromsome;
	}

	public void setChromsome(int[][] chromsome) {
		this.chromsome = chromsome;
	}

	public void changePlan(int selectedDay, int selectedPlan, int newDay) {
		Request selectedReq = data.getRequests().get(selectedPlan);
		int duration = selectedReq.getNumOfDaysRequest();
		chromsome[selectedDay][selectedPlan] = 0;
		chromsome[selectedDay + duration][selectedPlan] = 0;
		chromsome[newDay][selectedPlan] = 1;
		chromsome[newDay + duration][selectedPlan] = -1;
	}

	public void setPlan(int plan, int day) {
		Request selectedReq = data.getRequests().get(plan);
		int duration = selectedReq.getNumOfDaysRequest();
		chromsome[day][plan] = 1;
		chromsome[day + duration][plan] = -1;
	}

	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		for (int j = 0; j < chromsome[0].length; j++) {
			sBuilder.append((j + 1) + "\t");
		}
		sBuilder.append("\n");
		for (int i = 0; i < chromsome.length; i++) {
			for (int j = 0; j < chromsome[0].length; j++) {
				sBuilder.append(chromsome[i][j] + "\t");
			}
			sBuilder.append("\n");
		}

		for (int i = 0; i < chromsome.length; i++) {
			sBuilder.append("day" + (i + 1) + "estimated tool uesed" + Arrays.toString(getToolUsed()[i]) + "\n");
		}
		return sBuilder.toString();
	}

	public int getPlanAtDay(int day, int plan) {
		return chromsome[day][plan];
	}

}
