package dataModel;

import java.util.Arrays;

public class SolutionsAll {

	private SolutionForEachDay[] solutions;
	private long totalCost;
	private long totalDistance;
	private long totalToolUsed;
	private int totalVehicle;
	private int maximumVehiclPerDay;
	private int ID;
	private int[] maximumToolInUse;

	public int[] getMaximumToolInUse() {
		return maximumToolInUse;
	}

	public void setMaximumToolInUse(int[] maximumToolInUse) {
		this.maximumToolInUse = maximumToolInUse;
	}

	private String output;

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public SolutionForEachDay[] getSolutions() {
		return solutions;
	}

	public void setSolutions(SolutionForEachDay[] solutions) {
		this.solutions = solutions;
	}

	public long getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(long totalCaose) {
		this.totalCost = totalCaose;
	}

	public long getTotalDistance() {
		return totalDistance;
	}

	public void setTotalDistance(long totalDistance) {
		this.totalDistance = totalDistance;
	}

	@Override
	public String toString() {
		return "SolutionsAll [totalCost=" + totalCost + ", totalDistance=" + totalDistance + ", totalToolUsed="
				+ totalToolUsed + ", totalVehicle=" + totalVehicle + ", maximumVehiclPerDay=" + maximumVehiclPerDay
				+ ", ID=" + ID + ", maximumToolInUse=" + Arrays.toString(maximumToolInUse) + "]";
	}

	public SolutionsAll(int ID) {
		super();
		this.ID = ID;
		// TODO Auto-generated constructor stub
	}

	public long getTotalToolUsed() {
		return totalToolUsed;
	}

	public void setTotalToolUsed(long totalToolUsed) {
		this.totalToolUsed = totalToolUsed;
	}

	public int getTotalVehicle() {
		return totalVehicle;
	}

	public void setTotalVehicle(int totalVehicle) {
		this.totalVehicle = totalVehicle;
	}

	public int getMaximumVehiclPerDay() {
		return maximumVehiclPerDay;
	}

	public void setMaximumVehiclPerDay(int maximumVehiclPerDay) {
		this.maximumVehiclPerDay = maximumVehiclPerDay;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

}
