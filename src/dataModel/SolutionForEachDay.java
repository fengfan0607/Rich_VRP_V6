package dataModel;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class SolutionForEachDay {

	private int dayID;
	private TreeMap<Integer, List<Integer>> vehicleRoutes;
	private long totalCostPerDay;
	private int[] toolInUsePerDay;
	private long totalDistance;
	private int totalNumberOfVehicle;
	private long toolCost;
	private String output;
	private int[] dynamicStock;

	public int[] getDynamicStock() {
		return dynamicStock;
	}

	public void setDynamicStock(int[] dynamicStock) {
		this.dynamicStock = dynamicStock;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public long getToolCost() {
		return toolCost;
	}

	public void setToolCost(long toolCost) {
		this.toolCost = toolCost;
	}

	public long getTotalCostPerDay() {
		return totalCostPerDay;
	}

	public void setTotalCostPerDay(long totalCostPerDay) {
		this.totalCostPerDay = totalCostPerDay;
	}

	public int[] getToolInUsePerDay() {
		return toolInUsePerDay;
	}

	public void setToolInUsePerDay(int[] toolInUsePerDay) {
		this.toolInUsePerDay = toolInUsePerDay;
	}

	public long getTotalDistance() {
		return totalDistance;
	}

	public void setTotalDistance(long totalDistance) {
		this.totalDistance = totalDistance;
	}

	public int getTotalNumberOfVehicle() {
		return totalNumberOfVehicle;
	}

	public void setTotalNumberOfVehicle(int totalNumberOfVehicle) {
		this.totalNumberOfVehicle = totalNumberOfVehicle;
	}

	public int getDayID() {
		return dayID;
	}

	public void setDayID(int dayID) {
		this.dayID = dayID;
	}

	public TreeMap<Integer, List<Integer>> getVehicleRoutes() {
		return vehicleRoutes;
	}

	public void setVehicleRoutes(TreeMap<Integer, List<Integer>> vechileRoutes) {
		this.vehicleRoutes = vechileRoutes;
	}

	public SolutionForEachDay() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "SolutionForEachDay [dayID=" + dayID + ", totalCostPerDay=" + totalCostPerDay + ", toolInUsePerDay="
				+ Arrays.toString(toolInUsePerDay) + ", totalDistance=" + totalDistance + ", totalNumberOfVehicle="
				+ totalNumberOfVehicle + ", toolCost=" + toolCost + "]";
	}

}
