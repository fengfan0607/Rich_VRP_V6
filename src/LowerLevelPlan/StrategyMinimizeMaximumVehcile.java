package LowerLevelPlan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.crypto.Data;

import DataIO.DataIO;
import dataModel.BlackBoard;
import dataModel.Request;

public class StrategyMinimizeMaximumVehcile implements DataIO {
	BlackBoard dataModel;
	int[][] distance;
	int[] config;
	int maximumAllowedTravelDistance;
	int[] toolSize;
	List<Request> requests;

	public StrategyMinimizeMaximumVehcile(BlackBoard bb) {
		// TODO Auto-generated constructor stub
		dataModel = bb;
		this.config = dataModel.getConfig();
		System.err.println("---" + config[MAX_TRIP_DISTANCE]);
		distance = new int[config[NUM_OF_REQUESTS]][config[NUM_OF_REQUESTS]];
		distance = dataModel.getDistance();
		maximumAllowedTravelDistance = config[MAX_TRIP_DISTANCE];
		toolSize = new int[dataModel.getToolList().size()];
		for (int i = 0; i < dataModel.getToolList().size(); i++) {
			toolSize[i] = dataModel.getToolList().get(i).getSize();
		}
		requests = dataModel.getRequests();
	}

	public TreeMap<Integer, List<Integer>> routeGen(int[] dayPlannedTasks, List<Integer[]> assciateReq) {
		TreeMap<Integer, List<Integer>> route = new TreeMap<>();
		Set<Integer> plannedTask = new HashSet<>();
		for (int i = 0; i < assciateReq.size(); i++) {
			List<Integer> list = new ArrayList<>();
			list.add(0);
			list.add(assciateReq.get(i)[0]);
			list.add(assciateReq.get(i)[1]);
			list.add(0);
			route.put(i, list);
			plannedTask.add(assciateReq.get(i)[0]);
			plannedTask.add(assciateReq.get(i)[1]);
		}

		List<Integer> remainingTaks = new ArrayList<>();
		for (int i = 0; i < dayPlannedTasks.length; i++) {
			if (!plannedTask.contains(dayPlannedTasks[i])) {
				remainingTaks.add(dayPlannedTasks[i]);
			}
		}
		System.err.println(route);
		return route;
	}

	public void insertRouteToMap(List<Integer> route, TreeMap<Integer, List<Integer>> routeMap) {
		
	}

	public List<Integer> insertTaskToRoute(int task, List<Integer> route) {
		if (route.size() >= 3) {
			for (int i = 1; i < route.size() - 1; i++) {
				List<Integer> newRoute = new ArrayList<>(route);
				newRoute.add(i, task);
				if (route.get(i - 1) == 0) {
					Request curReq = requests.get(Math.abs(task) - 1);
					if (curReq.getNearByRequests().contains(Math.abs(route.get(i))) && routeValid(newRoute)) {
						return newRoute;
					}
				}
			}
		}
		List<Integer> newRoute = new ArrayList<>();
		newRoute.add(0);
		newRoute.add(task);
		newRoute.add(0);
		return newRoute;
	}

	public boolean routeValid(List<Integer> route) {
		if (distanceCheckPerRoute(route) < config[MAX_TRIP_DISTANCE] && capacityCheckForSingleRoute(route)) {
			return true;
		}
		return false;
	}

	public boolean canCombine(List<Integer> r1, List<Integer> r2) {
		List<Integer> newRoute = new ArrayList<>(r1);
		newRoute.remove(newRoute.size() - 1);
		newRoute.addAll(newRoute.size(), r2);
		if (distanceCheckPerRoute(newRoute) < config[MAX_TRIP_DISTANCE] && capacityCheckForSingleRoute(newRoute)) {
			return true;
		}
		return false;
	}

	public List<Integer> combineRoute(List<Integer> r1, List<Integer> r2) {
		List<Integer> newRoute = new ArrayList<>(r1);
		newRoute.remove(newRoute.size() - 1);
		newRoute.addAll(newRoute.size(), r2);
		return newRoute;
	}

	public int distanceCheckPerRoute(List<Integer> routes) {
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
		return totalDistance;
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
