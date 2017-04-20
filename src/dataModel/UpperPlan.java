package dataModel;

import java.util.Arrays;
import java.util.List;

public class UpperPlan {
	private int[][] plans;
	private int estimateToolCost;
	private int estimateAvgRequstPerDay;
	private List<List<Integer[]>> associationEachDay;

	public List<List<Integer[]>> getAssociationEachDay() {
		return associationEachDay;
	}

	public void setAssociationEachDay(List<List<Integer[]>> associationEachDay) {
		this.associationEachDay = associationEachDay;
	}

	public UpperPlan() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int[][] getPlans() {
		return plans;
	}

	public void setPlans(int[][] plans) {
		this.plans = plans;
	}

	public int getEstimateToolCost() {
		return estimateToolCost;
	}

	public void setEstimateToolCost(int estimateToolCost) {
		this.estimateToolCost = estimateToolCost;
	}

	public int getEstimateAvgRequstPerDay() {
		return estimateAvgRequstPerDay;
	}

	public void setEstimateAvgRequstPerDay(int estimateAvgRequstPerDay) {
		this.estimateAvgRequstPerDay = estimateAvgRequstPerDay;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	private int ID;

	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		for (int i = 0; i < plans.length; i++) {
			for (int j = 0; j < plans[0].length; j++) {
				sBuilder.append(plans[i][j] + "\t");
			}
			sBuilder.append("\n");
		}

		for (int i = 0; i < associationEachDay.size(); i++) {
			List<Integer[]> list = associationEachDay.get(i);
			if (list.size() > 0) {
				sBuilder.append("day" + (i + 1));
				for (int j = 0; j < list.size(); j++) {
					sBuilder.append(Arrays.toString(list.get(j)) + ",");
				}
				sBuilder.append("\n");
			}
		}
		sBuilder.append("plan horizion ID = " + ID + "\n");
		sBuilder.append("estimate tool cost = " + estimateToolCost + "\n");
		sBuilder.append("estimated maximum request per day = " + estimateAvgRequstPerDay + "\n");

		return sBuilder.toString();
	}
}
