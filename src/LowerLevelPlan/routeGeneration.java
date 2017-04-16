package LowerLevelPlan;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import com.google.common.collect.Collections2;

import DataIO.DataIO;
import DataIO.DataOutPut;
import DataIO.DataRead;
import DataIO.readSolution;
import UpperLevelPlan.UpperPlanGen;
import dataModel.BlackBoard;
import dataModel.Request;
import dataModel.SolutionForEachDay;
import dataModel.SolutionsAll;
import dataModel.UpperPlan;

public class routeGeneration implements DataIO {
	BlackBoard dataModel;
	int[][] distance;
	int[] config;
	int maximumAllowedTravelDistance;
	int[] toolSize;
	List<Request> requests;

	public routeGeneration(BlackBoard bb) {
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

	public SolutionsAll createSoltuions(UpperPlan up) {
		int[][] plans = up.getPlans();
		SolutionForEachDay[] solutions = new SolutionForEachDay[up.getPlans().length];
		for (int i = 0; i < plans.length; i++) {
			SolutionForEachDay sed = new SolutionForEachDay();
			TreeMap<Integer, List<Integer>> vechileRoutes = routeGen(plans[i]);
			sed.setVehicleRoutes(vechileRoutes);
			sed.setDayID(i + 1);
			solutions[i] = sed;
		}
		SolutionsAll sa = new SolutionsAll(1);
		sa.setSolutions(solutions);
		return sa;
	}

	public TreeMap<Integer, List<Integer>> routeGen(int[] dayPlannedTasks) {
		List<Integer> curPlan = new ArrayList<>();
		for (int i = 0; i < dayPlannedTasks.length; i++) {
			if (dayPlannedTasks[i] > 0) {
				int requestID = requests.get(i).getId();
				curPlan.add(requestID);
			} else if (dayPlannedTasks[i] < 0) {
				int requestID = requests.get(i).getId();
				curPlan.add(-1 * requestID);
			}
		}

		List<Integer> route = new ArrayList<>();
		route.add(0);
		int taskCounter = curPlan.size();
		while (taskCounter-- > 0) {
			int task = selectTask(curPlan);
			if (route.size() == 1) {
				route.add(task);
				route.add(0);
			} else {
				route = insertTask(route, task);
			}
		}
		// System.err.println(route);
		TreeMap<Integer, List<Integer>> vehicleMap = combined(splitRoute(route));
		return vehicleMap;
	}

	public List<routeSpec> splitRoute(List<Integer> route) {
		int point = 1;
		List<routeSpec> splitList = new ArrayList<>();
		for (int i = 1; i < route.size(); i++) {
			if (route.get(i) == 0) {
				List<Integer> list = new ArrayList<>(route.subList(point, i + 1));
				list.add(0, 0);
				if (distanceCheckPerRoute(list) > config[MAX_TRIP_DISTANCE])
					System.err.println(distanceCheckPerRoute(list) + ":" + list);
				routeSpec rs = new routeSpec(distanceCheckPerRoute(list), list);
				point = i + 1;
				splitList.add(rs);
			}
		}
		return splitList;
	}

	public TreeMap<Integer, List<Integer>> combined(List<routeSpec> splitList) {
		TreeMap<Integer, List<Integer>> vechileRoutes = new TreeMap<>();
		boolean[] flag = new boolean[splitList.size()];
		for (int i = 0; i < flag.length; i++) {
			flag[i] = false;
		}
		int confirmed = 0;
		int vehcileIndex = 1;
		for (int i = 0; i < splitList.size(); i++) {
			if (flag[i]) {
				continue;
			}
			int dis = splitList.get(i).getDistanceCost();
			flag[i] = true;
			confirmed++;
			// routeSpec rNew = new routeSpec();
			List<Integer> newRoute = new ArrayList<>(splitList.get(i).getRoute());
			while (confirmed < splitList.size() && dis < config[MAX_TRIP_DISTANCE]) {
				int posNew = getRandomNum(splitList.size(), i);
				routeSpec anotherRout = splitList.get(posNew);
				if (flag[posNew]) {
					continue;
				}
				if (!flag[posNew] && dis + anotherRout.getDistanceCost() < config[MAX_TRIP_DISTANCE]) {
					newRoute.remove(newRoute.size() - 1);
					newRoute.addAll(newRoute.size(), anotherRout.getRoute());
					dis = distanceCheckPerRoute(newRoute);
					flag[posNew] = true;
					confirmed++;
				} else if (dis + anotherRout.getDistanceCost() > config[MAX_TRIP_DISTANCE]) {
					break;
				}
			}

			vechileRoutes.put(vehcileIndex++, newRoute);
		}
		return vechileRoutes;
	}

	public int getRandomNum(int limit, int avoid) {
		int n = ThreadLocalRandom.current().nextInt(0, limit);
		while (n == avoid) {
			n = ThreadLocalRandom.current().nextInt(0, limit);
		}
		return n;
	}

	public List<Integer> insertTask(List<Integer> route, int task) {
		List<Integer> beforeInsert = new ArrayList<>(route);
		List<Integer> bestRoute = new ArrayList<>();
		beforeInsert = insertTaskToPos(task, route.size(), beforeInsert, 2);
		bestRoute = new ArrayList<>(beforeInsert);
		// System.err.println(bestRoute);
		int curDistance = distanceCheckPerRoute(bestRoute);
		List<Integer> interRoute = null;
		for (int i = 1; i < route.size(); i++) {
			if (i == 0) {
				List<Integer> r = new ArrayList<>(route);
				r = insertTaskToPos(task, 0, r, 3);
				interRoute = getInterRoute(0, r, 3);
				if (interRoute.get(0) != 0) {
					interRoute.add(0, 0);
				} else if (interRoute.get(interRoute.size() - 1) != 0) {
					interRoute.add(0);
				}
				if (distanceCheckPerRoute(interRoute) <= config[MAX_TRIP_DISTANCE]
						&& distanceCheckPerRoute(r) < curDistance && capacityCheckForSingleRoute(r)) {
					bestRoute = new ArrayList<>(r);
					curDistance = distanceCheckPerRoute(r);
				}
				continue;
			}
			for (int j = 1; j <= 3; j++) {
				List<Integer> r = new ArrayList<>(route);
				// System.err.println("beforeinsert" + route);
				r = insertTaskToPos(task, i, r, j);
				// System.err.println("inserted" + r);
				interRoute = getInterRoute(i, r, j);
				if (interRoute.get(0) != 0) {
					interRoute.add(0, 0);
				} else if (interRoute.get(interRoute.size() - 1) != 0) {
					interRoute.add(0);
				}
				// System.err.println("interRoute" + interRoute);
				if (distanceCheckPerRoute(interRoute) <= config[MAX_TRIP_DISTANCE]
						&& distanceCheckPerRoute(r) < curDistance && capacityCheckForSingleRoute(r)) {
					bestRoute = new ArrayList<>(r);
					curDistance = distanceCheckPerRoute(r);
				}
			}
		}

		return bestRoute;
	}

	public List<Integer> getInterRoute(int pos, List<Integer> route, int type) {
		int start = 0;
		int end = 0;
		List<Integer> interRoute = null;
		switch (type) {
		case 1:
			start = pos;
			end = pos;
			while (route.get(start) != 0) {
				start--;
			}
			while (route.get(end) != 0) {
				end++;
			}
			interRoute = new ArrayList<>(route.subList(start, end + 1));
			break;
		case 2:
			start = pos;
			end = pos + 1;
			while (route.get(start) != 0) {
				start--;
			}
			interRoute = new ArrayList<>(route.subList(start, end + 1));
			break;
		case 3:
			start = pos;
			end = pos + 1;
			while (route.get(end) != 0) {
				end++;
			}
			interRoute = new ArrayList<>(route.subList(start, end + 1));
			break;
		default:
			break;
		}
		// System.err.println("check interROute" + "pos:" + pos + route);
		// System.err.println("get interROute" + interRoute);
		return interRoute;
	}

	/*
	 * type 1: insert task type 2: insert task,0 type 3: insert 0,task
	 */
	public List<Integer> insertTaskToPos(int task, int pos, List<Integer> route, int type) {
		switch (type) {
		case 1:
			route.add(pos, task);
			break;
		case 2:
			if ((pos + 1 < route.size() && route.get(pos + 1) != 0) || pos == route.size()) {
				route.add(pos, task);
				route.add(pos + 1, 0);
			} else {
				route.add(pos, task);
			}

			break;
		case 3:
			if ((pos - 1 > 0 && route.get(pos - 1) != 0) || pos == 0) {
				route.add(pos, 0);
				route.add(pos + 1, task);
			} else {
				route.add(pos, task);
			}

			break;
		}
		return route;

	}

	public int selectTask(List<Integer> list) {
		int n = list.size();
		int pos = ThreadLocalRandom.current().nextInt(0, n);
		int task = list.get(pos);
		list.remove(pos);
		return task;
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
