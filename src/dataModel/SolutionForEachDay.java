package dataModel;

import java.util.List;
import java.util.TreeMap;

public class SolutionForEachDay {

	private int dayID;
	private TreeMap<Integer, List<Integer>> vehicleRoutes;

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
		return "SolutionForEachDay [dayID=" + dayID + ", vechileRoutes=" + vehicleRoutes + "]";
	}

}
