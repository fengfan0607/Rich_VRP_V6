package dataModel;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class UpperPlan {
	private int[][] plans;
	private int estimateToolCost;
	private int estimateAvgRequstPerDay;
	private List<List<Integer[]>> associationEachDay;
	private int[][] toolUsed;
	private Set<Integer> hasAssociationReq;

	public Set<Integer> getHasAssociationReq() {
		return hasAssociationReq;
	}

	public void setHasAssociationReq(Set<Integer> hasAssociationReq) {
		this.hasAssociationReq = hasAssociationReq;
	}

	public int[][] getToolUsed() {
		return toolUsed;
	}

	public void setToolUsed(int[][] toolUsed) {
		this.toolUsed = toolUsed;
	}

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

	@Override
	public String toString() {
		return "UpperPlan [plans=" + Arrays.toString(plans) + ", estimateToolCost=" + estimateToolCost
				+ ", estimateAvgRequstPerDay=" + estimateAvgRequstPerDay + ", associationEachDay=" + associationEachDay
				+ ", toolUsed=" + Arrays.toString(toolUsed) + ", ID=" + ID + "]";
	}
}
