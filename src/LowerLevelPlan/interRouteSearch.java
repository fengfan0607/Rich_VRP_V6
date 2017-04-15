package LowerLevelPlan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import DataIO.DataIO;
import dataModel.SolutionForEachDay;
import dataModel.BlackBoard;

/*
 * swap requests between two different route
 */
public class interRouteSearch implements DataIO {
	BlackBoard dataModel;
	int[] toolSize;
	int[][] distance;
	int[] config;
	int[] depotStock;
	int[] dynamicStock;
	TreeMap<Integer, List<Integer[]>> pickUpStock;
	RouteValidator checkForEachRoute;

	public interRouteSearch(BlackBoard bb) {
		// TODO Auto-generated constructor stub
		dataModel = bb;
		checkForEachRoute = new RouteValidator(dataModel);
		this.config = dataModel.getConfig();
		toolSize = new int[dataModel.getToolList().size()];
		for (int i = 0; i < dataModel.getToolList().size(); i++) {
			toolSize[i] = dataModel.getToolList().get(i).getSize();
		}
		distance = new int[config[NUM_OF_REQUESTS]][config[NUM_OF_REQUESTS]];
		distance = dataModel.getDistance();
		dynamicStock = new int[dataModel.getToolList().size()];
		depotStock = new int[dataModel.getToolList().size()];
		for (int i = 0; i < dataModel.getToolList().size(); i++) {
			depotStock[i] = dataModel.getToolList().get(i).getNumOfTools();
			dynamicStock[i] = dataModel.getToolList().get(i).getNumOfTools();
		}
		pickUpStock = new TreeMap<>();
	}

	public SolutionForEachDay localSearchSwapRequest(SolutionForEachDay sed) {
		// System.err.println("current day" + sed.getDayID());
		TreeMap<Integer, List<Integer>> vehicleRoutes = sed.getVehicleRoutes();

		while (true) {
			int numberOfVehicle = vehicleRoutes.size();
			int v1 = getVehicle(numberOfVehicle);
			int v2 = getVehicle(numberOfVehicle);
			while (v1 == v2) {
				v1 = getVehicle(numberOfVehicle);
				v2 = getVehicle(numberOfVehicle);
			}
			List<Integer> r1 = new ArrayList<>(vehicleRoutes.get(v1));
			List<Integer> r2 = new ArrayList<>(vehicleRoutes.get(v2));
			int p1 = getRequest(r1);
			int p2 = getRequest(r2);
			int req1 = r1.get(p1);
			int req2 = r2.get(p2);
			r1.set(p1, req2);
			r2.set(p2, req1);
			List<Integer> newR1 = null;
			List<Integer> newR2 = null;
			if (routeValidate(r1) != null) {
				newR1 = new ArrayList(routeValidate(r1));
			}
			if (routeValidate(r2) != null) {
				newR2 = new ArrayList(routeValidate(r2));
			}
			// List<Integer> newR2 = new ArrayList(routeValidate(r2));
			if (checkForEachRoute.checkForEachRoute(newR1) && checkForEachRoute.checkForEachRoute(newR2)) {
				if (newR1 != null && newR2 != null) {
					vehicleRoutes.put(v1, newR1);
					vehicleRoutes.put(v2, newR2);
				} else if (newR1 == null && newR2 != null) {
					vehicleRoutes.put(v2, newR2);
					vehicleRoutes = deleteRoute(vehicleRoutes, v1);
					// sed.setNumberOfVehicle(numberOfVehicle - 1);
				} else if (newR1 != null && newR2 == null) {
					vehicleRoutes.put(v1, newR1);
					vehicleRoutes = deleteRoute(vehicleRoutes, v2);
					// sed.setNumberOfVehicle(numberOfVehicle- 1);
				}
				break;
			}
		}
		sed.setVehicleRoutes(vehicleRoutes);
		return sed;
	}

	public int getVehicle(int limit) {
		int pos = ThreadLocalRandom.current().nextInt(1, limit + 1);
		return pos;
	}

	public TreeMap<Integer, List<Integer>> deleteRoute(TreeMap<Integer, List<Integer>> vehicleRoutes, int v) {
		TreeMap<Integer, List<Integer>> vehicleRoutesNew = new TreeMap<>();
		Set<Integer> keyss = vehicleRoutes.keySet();
		Iterator<Integer> itr = keyss.iterator();
		while (itr.hasNext()) {
			int key = itr.next();
			if (key < v) {
				vehicleRoutesNew.put(key, vehicleRoutes.get(key));
			} else if (key == v) {
				continue;
			} else {
				vehicleRoutesNew.put(key - 1, vehicleRoutes.get(key));
			}
		}
		return vehicleRoutesNew;
	}

	public int getRequest(List<Integer> route) {
		int pos = ThreadLocalRandom.current().nextInt(1, route.size() - 1);
		return pos;
	}

	public List<Integer> checkFeasibility(List<Integer> route) {
		List<Integer> nList = new ArrayList<>();
		nList.add(0);
		for (int i = 1; i < route.size() - 1; i++) {
			if (route.get(i) == 0 && route.get(i + 1) == 0) {
				i++;
				continue;
			}
			nList.add(route.get(i));
		}
		nList.add(0);
		if (nList.size() <= 2) {
			return null;
		}
		return nList;
	}

	public List<Integer> routeValidate(List<Integer> route) {
		List<Integer> list = new ArrayList<>();
		list.add(0);
		int index = 1;
		for (int i = 1; i < route.size() - 1; i++) {
			if (route.get(i) == route.get(i - 1) || (i + 1 < route.size() - 1) && route.get(i) == route.get(i + 1)
					|| (i == route.size() - 2 && route.get(i) == 0)) {
				continue;
			} else {
				list.add(index++, route.get(i));
			}
		}
		list.add(0);
		if (list.size() <= 2) {
			return null;
		}
		return list;
	}
}
