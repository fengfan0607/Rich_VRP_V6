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
import dataModel.SolutionsAll;

public class Evaluation implements DataIO {
	int[] config;
	BlackBoard data;
	int[] depotStock;
	int[] dynamicStock;
	int[][] distance;
	TreeMap<Integer, List<Integer[]>> pickUpStock;

	public Evaluation(BlackBoard bb) {
		// TODO Auto-generated constructor stub
		data = bb;
		this.config = data.getConfig();
		distance = new int[config[NUM_OF_REQUESTS]][config[NUM_OF_REQUESTS]];
		distance = data.getDistance();
		dynamicStock = new int[data.getToolList().size()];
		depotStock = new int[data.getToolList().size()];
		pickUpStock = new TreeMap<>();
		for (int i = 0; i < data.getToolList().size(); i++) {
			depotStock[i] = data.getToolList().get(i).getNumOfTools();
			dynamicStock[i] = data.getToolList().get(i).getNumOfTools();
		}
	}

	public SolutionsAll costCal(SolutionsAll solutions) {
		String output = "";
		int[][] toolInUse = new int[config[DAYS]][config[NUM_OF_TOOLS]];
		SolutionForEachDay[] solutionForEachDay = solutions.getSolutions();
		int totalDistance = 0;
		int totalNumberOfVehicle = 0;
		int maximumNumberOfVehicle = Integer.MIN_VALUE;
		int[] maximumNumberOfToolsInUse = new int[config[NUM_OF_TOOLS]];
		long totalCost = 0l;
		for (int i = 0; i < config[NUM_OF_TOOLS]; i++) {
			maximumNumberOfToolsInUse[i] = Integer.MIN_VALUE;
		}
		for (int i = 0; i < solutionForEachDay.length; i++) {
			if (solutionForEachDay[i] != null) {
				totalDistance += distanceCal(solutionForEachDay[i]);
				int numberOfVehciles = solutionForEachDay[i].getVehicleRoutes().size();
				totalNumberOfVehicle += numberOfVehciles;
				// get number of tools used per day
				toolInUse[i] = getToolUsedPerDay(solutionForEachDay[i]);
				// cal the maximum number of tools used
				maximumNumberOfVehicle = Math.max(maximumNumberOfVehicle, numberOfVehciles);
				for (int j = 0; j < config[NUM_OF_TOOLS]; j++) {
					maximumNumberOfToolsInUse[j] = Math.max(maximumNumberOfToolsInUse[j], toolInUse[i][j]);
				}
				output += manufactorSed(solutionForEachDay[i]);
			}
		}
		for (int i = 0; i < data.getToolList().size(); i++) {
			dynamicStock[i] = data.getToolList().get(i).getNumOfTools();
		}
		for (int i = 0; i < maximumNumberOfToolsInUse.length; i++) {
			totalCost += maximumNumberOfToolsInUse[i] * data.getToolList().get(i).getCostOfTools();

			if (maximumNumberOfToolsInUse[i] > depotStock[i]) {
				totalCost = Integer.MAX_VALUE;
				break;
			}
		}
		totalCost = totalCost + (totalDistance * (long) config[DISTANCE_COST]);
		totalCost += (totalNumberOfVehicle * (long) config[VEHICLE_DAY_COST]);
		totalCost += (maximumNumberOfVehicle * (long) config[VEHICLE_COST]);

		solutions.setTotalDistance(totalDistance);
		solutions.setTotalVehicle(totalNumberOfVehicle);
		solutions.setMaximumVehiclPerDay(maximumNumberOfVehicle);
		solutions.setMaximumToolInUse(maximumNumberOfToolsInUse);
		solutions.setTotalCost(totalCost);
		solutions.setOutput(output);

		return solutions;
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

	public int distanceCal(SolutionForEachDay sed) {
		TreeMap<Integer, List<Integer>> vehicleRouteMapping = sed.getVehicleRoutes();
		// int[] distanceEachVehicle = new int[vehicleRouteMapping.size()];
		int totalDistance = 0;
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
		pickUpStock = new TreeMap<>();
		return toolUsed;
	}
}
