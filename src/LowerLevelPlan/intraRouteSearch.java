package LowerLevelPlan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import dataModel.BlackBoard;
import dataModel.SolutionForEachDay;

/*
 * swap request sequence in a single route
 */
public class intraRouteSearch {
	BlackBoard data;
	RouteValidator checkForEachRoute;

	public intraRouteSearch(BlackBoard bb) {
		// TODO Auto-generated constructor stub

		data = bb;
		checkForEachRoute = new RouteValidator(data);

	}

	public SolutionForEachDay intraSwap(SolutionForEachDay sed) {
		SolutionForEachDay newSed = new SolutionForEachDay();
		newSed.setDayID(sed.getDayID());
		TreeMap<Integer, List<Integer>> newVehicleRoutes = new TreeMap<>();
		newVehicleRoutes.putAll(sed.getVehicleRoutes());
		Set<Integer> keys = newVehicleRoutes.keySet();
		Iterator<Integer> iterator = keys.iterator();
		while (iterator.hasNext()) {
			int curVehicle = iterator.next();
			List<Integer> currentRoute = newVehicleRoutes.get(curVehicle);
			List<Integer> swappedRoute = new ArrayList<>(performSwapForEachRoute(currentRoute));
			int count = 0;
			while (!checkForEachRoute.checkForEachRoute(swappedRoute) && count < 2000) {
				count++;
				swappedRoute = performSwapForEachRoute(swappedRoute);
			}
			if (count == 200) {
				newVehicleRoutes.put(curVehicle, currentRoute);
			} else {
				newVehicleRoutes.put(curVehicle, swappedRoute);
			}

		}
		newSed.setVehicleRoutes(newVehicleRoutes);
		return newSed;
	}

	public static List<Integer> performSwapForEachRoute(List<Integer> curRoute) {
		if (curRoute.size() == 3) {
			return curRoute;
		}
		List<Integer> newRoute = new ArrayList<>(curRoute);
		int p1 = getPos(newRoute);
		int p2 = getPos(newRoute);
		while (p1 == p2) {
			p1 = getPos(newRoute);
			p2 = getPos(newRoute);
		}
		int req1 = newRoute.get(p1);
		int req2 = newRoute.get(p2);

		newRoute.set(p1, req2);
		newRoute.set(p2, req1);

		return checkFeasibility(newRoute);
	}

	public static List<Integer> checkFeasibility(List<Integer> route) {
		int index = 1;
		while (route.get(index) == 0) {
			index++;
		}
		List<Integer> nList = new ArrayList<>();
		nList.add(0);
		for (int i = index; i < route.size() - 1; i++) {
			if (route.get(i) == 0 && route.get(i + 1) == 0) {
				continue;
			}
			nList.add(route.get(i));
		}
		nList.add(0);
		return nList;
	}

	public static int getPos(List<Integer> route) {
		return ThreadLocalRandom.current().nextInt(1, route.size() - 1);
	}

}
