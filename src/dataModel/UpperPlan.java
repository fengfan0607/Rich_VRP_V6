package dataModel;

public class UpperPlan {
	private int[][] plans;
	private int estimateToolCost;
	private int estimateAvgRequstPerDay;

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
				sBuilder.append(plans[i][j] + ",");
			}
			sBuilder.append("\n");
		}
		sBuilder.append("plan horizion ID = " + ID + "\n");
		sBuilder.append("estimate tool cost = " + estimateToolCost + "\n");
		sBuilder.append("estimated maximum request per day = " + estimateAvgRequstPerDay + "\n");

		return sBuilder.toString();
	}
}
