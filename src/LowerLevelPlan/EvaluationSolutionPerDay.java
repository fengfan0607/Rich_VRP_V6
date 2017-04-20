package LowerLevelPlan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import DataIO.DataIO;
import dataModel.BlackBoard;
import dataModel.Request;
import dataModel.SolutionForEachDay;

public class EvaluationSolutionPerDay implements DataIO {

	int[] config;
	BlackBoard data;
	int[] depotStock;
	int[] dynamicStock;
	int[][] distance;
	TreeMap<Integer, List<Integer[]>> pickUpStock;
	int numberOfToos;

	public EvaluationSolutionPerDay(BlackBoard bb) {
		// TODO Auto-generated constructor stub
		data = bb;
		this.config = data.getConfig();
		distance = new int[config[NUM_OF_REQUESTS]][config[NUM_OF_REQUESTS]];
		distance = data.getDistance();
		// dynamicStock = new int[data.getToolList().size()];
		depotStock = new int[data.getToolList().size()];
		pickUpStock = new TreeMap<>();
		for (int i = 0; i < data.getToolList().size(); i++) {
			depotStock[i] = data.getToolList().get(i).getNumOfTools();
		}
		numberOfToos = config[NUM_OF_TOOLS];
	}

	public int[] getDynamicStock() {
		return dynamicStock;
	}

	public void setDynamicStock(int[] dynamicStock) {
		this.dynamicStock = dynamicStock;
	}

	public SolutionForEachDay costCalPerDay(SolutionForEachDay sed) {
		// this.setDynamicStock(sed.getDynamicStock());
		long totalDistance = distanceCal(sed);
		int[] toolInUsePerDay = new int[numberOfToos];
		toolInUsePerDay = getToolUsedPerDay(sed);
		// System.err.println(Arrays.toString(toolInUsePerDay));
		int totalNumberOfVehicle = sed.getVehicleRoutes().size();
		long toolCost = 0;
		long totalCostPerDay = 0;
		for (int i = 0; i < numberOfToos; i++) {
			toolCost = toolInUsePerDay[i] * data.getToolList().get(i).getCostOfTools();
		}
		totalCostPerDay = totalDistance * config[DISTANCE_COST] + toolCost
				+ totalNumberOfVehicle * config[VEHICLE_DAY_COST];
		sed.setToolCost(toolCost);
		sed.setToolInUsePerDay(toolInUsePerDay);
		sed.setTotalCostPerDay(totalCostPerDay);
		sed.setTotalNumberOfVehicle(totalNumberOfVehicle);
		sed.setTotalDistance(totalDistance);
		sed.setOutput(manufactorSed(sed));
		sed.setDynamicStock(dynamicStock);
//		System.err.println("cal" + Arrays.toString(dynamicStock));
		return sed;
	}

	public String manufactorSed(SolutionForEachDay sed) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("DAY = " + sed.getDayID() + "\n");
		TreeMap<Integer, List<Integer>> vehicleRouteMapping = sed.getVehicleRoutes();
		Set<Integer> keys = vehicleRouteMapping.keySet();
		Iterator<Integer> itr = keys.iterator();
		while (itr.hasNext()) {
			int key = itr.next();
			List<Integer> route = vehicleRouteMapping.get(key);
			sBuilder.append(key + "\t" + "R" + "\t");
			int counter = 0;
			for (int i = 0; i < route.size(); i++) {
				if (i == route.size() - 1) {
					sBuilder.append(route.get(i));
				} else {
					sBuilder.append(route.get(i) + "\t");
				}
			}
			sBuilder.append("\n");
		}
		sBuilder.append("\n");
		// sed.setOutput(sBuilder.toString());
		return sBuilder.toString();
	}

	public long distanceCal(SolutionForEachDay sed) {
		TreeMap<Integer, List<Integer>> vehicleRouteMapping = sed.getVehicleRoutes();
		long totalDistance = 0;
		Set<Integer> keys = vehicleRouteMapping.keySet();
		for (int i = 0; i < keys.size(); i++) {
			List<Integer> routes = vehicleRouteMapping.get(i + 1);
			// int totalDistance = 0;
			for (int j = 0; j < routes.size() - 1; j++) {
				int pos1 = 0;
				int pos2 = 0;
				int req1 = Math.abs(routes.get(j));
				int req2 = Math.abs(routes.get(j + 1));
				if (req1 != 0) {
					pos1 = data.getRequests().get(req1 - 1).getCustomerID();
				}
				if (req2 != 0) {
					pos2 = data.getRequests().get(req2 - 1).getCustomerID();
				}
				totalDistance += distance[pos1][pos2];
			}
		}
		return totalDistance;
	}

	public int[] getToolUsedPerDay(SolutionForEachDay sed) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("DAY = " + sed.getDayID() + "\n");
		TreeMap<Integer, List<Integer>> vehicleRouteMapping = sed.getVehicleRoutes();
		Set<Integer> keys = vehicleRouteMapping.keySet();
		Iterator<Integer> itr = keys.iterator();
		while (itr.hasNext()) {
			int key = itr.next();
			List<Integer> route = vehicleRouteMapping.get(key);
			for (int i = 0; i < route.size(); i++) {
				if (route.get(i) != 0) {
					updateStock(route.get(i), key);
				}
			}
		}
		int[] currentToolUsed = getToolUsed().clone();
		return currentToolUsed;
	}

	// pickup stock: keep tracks of the tools been picked up per day
	public void updateStock(int request, int vehicleID) {
		int id = Math.abs(request);
		Request r = data.getRequests().get(id - 1);
		int toolID = r.getRequestToolKind();
		int toolNumber = r.getRequestToolNumber();
		if (request > 0) {
			// a deliver action
			if (pickUpStock.containsKey(toolID)) {
				List<Integer[]> toolSet = pickUpStock.get(toolID);
				for (int i = 0; i < toolSet.size(); i++) {
					Integer[] ts = toolSet.get(i).clone();
					if (vehicleID == ts[0]) {
						if (toolNumber >= ts[1]) {
							toolNumber -= ts[1];
							ts[1] = 0;
						} else {
							ts[1] -= toolNumber;
							toolNumber = 0;
						}
						toolSet.set(i, ts);
						pickUpStock.put(toolID, toolSet);
						break;
					}
				}
			}
			dynamicStock[toolID - 1] -= toolNumber;
		} else {
			// a pick up action
			if (!pickUpStock.containsKey(toolID)) {
				List<Integer[]> list = new ArrayList<>();
				Integer[] array = new Integer[] { vehicleID, toolNumber };
				list.add(array);
				pickUpStock.put(toolID, list);
			} else {
				List<Integer[]> toolSet = new ArrayList<>(pickUpStock.get(toolID));
				int index = 0;
				for (int i = 0; i < toolSet.size(); i++) {
					Integer[] ts = toolSet.get(i).clone();
					if (vehicleID == ts[0]) {
						ts[1] += toolNumber;
						toolSet.set(i, ts);
						pickUpStock.put(toolID, toolSet);
						break;
					} else {
						index++;
					}
				}
				if (index == toolSet.size()) {
					Integer[] array = new Integer[] { vehicleID, toolNumber };
					toolSet.add(array);
					pickUpStock.put(toolID, toolSet);
				}
			}

		}
	}

	public int[] getToolUsed() {
		int[] toolUsed = new int[config[NUM_OF_TOOLS]];
		for (int i = 0; i < toolUsed.length; i++) {
			toolUsed[i] = depotStock[i] - dynamicStock[i];
			if (pickUpStock.containsKey(i + 1)) {
				List<Integer[]> list = pickUpStock.get(i + 1);
				for (int k = 0; k < list.size(); k++) {
					Integer[] array = list.get(k).clone();
					// toolUsed[i] += array[1];
					dynamicStock[i] += array[1];
				}
			}
		}
		// setDynamicStock(dynamicStock);
		pickUpStock = new TreeMap<>();
		return toolUsed;
	}

}
