package LowerLevelPlan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import DataIO.DataIO;
import dataModel.SolutionForEachDay;
import dataModel.BlackBoard;

public class RouteValidator implements DataIO {
	BlackBoard dataModel;
	int[][] distance;
	int[] config;
	int maximumAllowedTravelDistance;
	int[] toolSize;

	public RouteValidator(BlackBoard bb) {
		// TODO Auto-generated constructor stub
		dataModel = bb;
		this.config = dataModel.getConfig();
		distance = new int[config[NUM_OF_REQUESTS]][config[NUM_OF_REQUESTS]];
		distance = dataModel.getDistance();
		maximumAllowedTravelDistance = config[MAX_TRIP_DISTANCE];
		toolSize = new int[dataModel.getToolList().size()];
		for (int i = 0; i < dataModel.getToolList().size(); i++) {
			toolSize[i] = dataModel.getToolList().get(i).getSize();
		}

	}

	public boolean checkForEachRoute(List<Integer> route) {
		if (route != null && (!distanceCheckPerRoute(route) || !capacityCheckForSingleRoute(route))) {
			return false;
		}
		return true;
	}

	public boolean checkEachDaySolution(SolutionForEachDay sed) {
		TreeMap<Integer, List<Integer>> vehicleRoutes = sed.getVehicleRoutes();
		Set<Integer> keyss = vehicleRoutes.keySet();
		Iterator<Integer> itr = keyss.iterator();
		while (itr.hasNext()) {
			int key = itr.next();
			List<Integer> route = vehicleRoutes.get(key);
			if (!distanceCheckPerRoute(route) || !capacityCheckForSingleRoute(route)) {
				return false;
			}
		}
		return true;
	}

	public boolean distanceCheckPerRoute(List<Integer> routes) {

		int totalDistance = 0;
		for (int j = 0; j < routes.size() - 1; j++) {
			int pos1 = 0;
			int pos2 = 0;
			int req1 = Math.abs(routes.get(j));
			int req2 = Math.abs(routes.get(j + 1));
			if (req1 != 0) {
				pos1 = dataModel.getRequests().get(req1 - 1).getCustomerID();
			}
			if (req2 != 0) {
				pos2 = dataModel.getRequests().get(req2 - 1).getCustomerID();
			}
			totalDistance += distance[pos1][pos2];
		}
		if (totalDistance > config[MAX_TRIP_DISTANCE]) {
			return false;
		}
		return true;
	}

	public boolean capacityCheckForSingleRoute(List<Integer> route) {
		int[] capacity = new int[route.size()];
		capacity[0] = 0;
		TreeMap<Integer, Integer> pickUpMap = new TreeMap<>();
		List<Integer> depotVisit = new ArrayList<>();
		int initialLoad = 0;
		for (int i = 1; i < route.size(); i++) {
			int node_r = route.get(i);
			int node = Math.abs(route.get(i));
			int loadAmount = 0;
			int loadType = 0;
			int curLoad = 0;
			if (node_r != 0) {
				loadAmount = dataModel.getRequests().get(node - 1).getRequestToolNumber();
				loadType = dataModel.getRequests().get(node - 1).getRequestToolKind();
				curLoad = loadAmount * toolSize[loadType - 1];
			}
			if (node_r < 0) {
				if (!pickUpMap.containsKey(loadType)) {
					pickUpMap.put(loadType, loadAmount);
				} else {
					pickUpMap.put(loadType, pickUpMap.get(loadType) + loadAmount);
				}
			} else if (node_r > 0) {
				if (!pickUpMap.containsKey(loadType)) {
					initialLoad += curLoad;
				} else {
					int intermediateDeliver = pickUpMap.get(loadType);
					if (intermediateDeliver < loadAmount) {
						initialLoad += (curLoad - intermediateDeliver * toolSize[loadType - 1]);
						pickUpMap.remove(loadType);
					} else {
						pickUpMap.put(loadType, pickUpMap.get(loadType) - intermediateDeliver);
					}
				}
			} else if (node == 0) {
				pickUpMap.clear();
				depotVisit.add(initialLoad);
				initialLoad = 0;
			}
		}
		// System.err.println(depotVisit);
		int index = 0;
		capacity[0] = depotVisit.get(index++);
		for (int i = 1; i < route.size(); i++) {
			int node_r = route.get(i);
			int node = Math.abs(route.get(i));
			int loadAmount = 0;
			int loadType = 0;
			int curLoad = 0;
			if (node_r != 0) {
				loadAmount = dataModel.getRequests().get(node - 1).getRequestToolNumber();
				loadType = dataModel.getRequests().get(node - 1).getRequestToolKind();
				curLoad = loadAmount * toolSize[loadType - 1];
			}
			if (node == 0 && i < route.size() - 1) {
				capacity[i] = depotVisit.get(index++);
			} else if (node_r > 0) {
				capacity[i] = capacity[i - 1] - curLoad;
			} else if (node_r < 0) {
				capacity[i] = capacity[i - 1] + curLoad;
			}
		}
		// System.err.println(Arrays.toString(capacity));
		for (Integer i : capacity) {
			if (i > config[CAPACITY]) {
				return false;
			}
		}
		return true;
	}
}
