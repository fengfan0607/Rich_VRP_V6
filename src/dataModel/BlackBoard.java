package dataModel;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import DataIO.DataIO;

public class BlackBoard implements DataIO {
	private int[] config;
	private List<Tool> toolList;
	private List<Request> requests;
	private int[][] distance;
	private int[] toolStock;
	private TreeMap<Integer, List<Integer>> requestToolMapping;
	private TreeMap<Integer, List<Integer>> fixedDayDelivery;

	public TreeMap<Integer, List<Integer>> getRequestToolMapping() {
		return requestToolMapping;
	}

	public void setRequestToolMapping(TreeMap<Integer, List<Integer>> requestToolMapping) {
		this.requestToolMapping = requestToolMapping;
	}

	public TreeMap<Integer, List<Integer>> getFixedDayDelivery() {
		return fixedDayDelivery;
	}

	public void setFixedDayDelivery(TreeMap<Integer, List<Integer>> fixedDayDelivery) {
		this.fixedDayDelivery = fixedDayDelivery;
	}

	public int[] getToolStock() {
		return toolStock;
	}

	public void setToolStock(int[] toolStock) {
		this.toolStock = toolStock;
	}

	public int[] getConfig() {
		return config;
	}

	public void setConfig(int[] config) {
		this.config = config;
	}

	public List<Tool> getToolList() {
		return toolList;
	}

	public void setToolList(List<Tool> toolList) {
		this.toolList = toolList;
	}

	public List<Request> getRequests() {
		return requests;
	}

	public void setRequests(List<Request> requests) {
		this.requests = requests;
	}

	public int[][] getDistance() {
		return distance;
	}

	@Override
	public String toString() {
		return "BlackBoard [config=" + Arrays.toString(config) + ", toolList=" + toolList + ", toolStock="
				+ Arrays.toString(toolStock) + ", requestToolMapping=" + requestToolMapping + ", fixedDayDelivery="
				+ fixedDayDelivery + "]";
	}

	public BlackBoard() {
		super();
	}

	public void setDistance(int[][] distance) {
		this.distance = distance;
	}
}
