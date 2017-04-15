package LowerLevelPlan;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import DataIO.DataIO;
import dataModel.Request;
import dataModel.SolutionForEachDay;
import dataModel.SolutionsAll;
import dataModel.UpperPlan;
import dataModel.BlackBoard;

public class initialFeasiblePlanGen implements DataIO {
	public BlackBoard data;
	int[][] distance;
	int[] config;
	int maximumAllowedTravelDistance;
	// FeasiblityCheckForEachRoute checkForEachRoute;
	List<Request> requests;
	RouteValidator routeValidator;

	public initialFeasiblePlanGen(BlackBoard bb) {
		// TODO Auto-generated constructor stub

		data = bb;
		config = data.getConfig();
		distance = new int[config[NUM_OF_REQUESTS]][config[NUM_OF_REQUESTS]];
		distance = data.getDistance();
		maximumAllowedTravelDistance = config[MAX_TRIP_DISTANCE];
		// checkForEachRoute = new FeasiblityCheckForEachRoute(data);
		requests = data.getRequests();
		routeValidator = new RouteValidator(bb);
	}

	public SolutionsAll intialSolutionGen(UpperPlan up) {
		SolutionsAll solutionsAll = new SolutionsAll(up.getID());
		solutionsAll = generateFeasubleRoutes(up, solutionsAll);

		return solutionsAll;
	}

	public SolutionsAll generateFeasubleRoutes(UpperPlan up, SolutionsAll solutions) {

		int[][] totalPlans = up.getPlans();
		SolutionForEachDay[] solutionsAllDays = new SolutionForEachDay[data.getConfig()[DAYS]];
		for (int i = 0; i < totalPlans.length; i++) {
			List<List<Integer>> routesPerDay = generateFeasibleRoutePerDay(totalPlans[i]);
			SolutionForEachDay sed = new SolutionForEachDay();
			sed.setDayID(i + 1);
			sed.setVehicleRoutes(naiveMerge(routesPerDay));
			sed = removeZero(sed);
			solutionsAllDays[i] = sed;
		}
		solutions.setSolutions(solutionsAllDays);
		return solutions;
	}

	public List<List<Integer>> generateFeasibleRoutePerDay(int[] dayPlan) {
		List<List<Integer>> soltuionPerDay = new ArrayList<>();
		for (int i = 0; i < dayPlan.length; i++) {
			if (dayPlan[i] != 0) {
				List<Integer> route = new ArrayList<>();
				route.add(0);
				route.add(requests.get(i).getId() * dayPlan[i]);
				route.add(0);
				soltuionPerDay.add(route);
			} else {
				continue;
			}
		}

		return soltuionPerDay;
	}

	public SolutionForEachDay removeZero(SolutionForEachDay sed) {
		TreeMap<Integer, List<Integer>> vehicleRoutes = sed.getVehicleRoutes();
		for (int i = 0; i < vehicleRoutes.size(); i++) {
			List<Integer> route = vehicleRoutes.get(i + 1);
			for (int j = 1; j < route.size() - 1; j++) {
				if (route.get(j) == 0) {
					List<Integer> buf = new ArrayList<>(route);
					buf.remove(j);
					if (routeValidator.checkForEachRoute(buf)) {
						route.remove(j);
						vehicleRoutes.put(i + 1, route);
					}
				}
			}
		}
		sed.setVehicleRoutes(vehicleRoutes);
		return sed;
	}

	public TreeMap<Integer, List<Integer>> naiveMerge(List<List<Integer>> soltuionPerDay) {
		TreeMap<Integer, List<Integer>> vehicleRouteMapping = new TreeMap<>();
		List<Integer> distancePerRoute = new ArrayList<>();
		int distancePerRout = 0;
		for (int i = 0; i < soltuionPerDay.size(); i++) {
			List<Integer> singleRoute = soltuionPerDay.get(i);
			for (int j = 0; j < singleRoute.size() - 1; j++) {
				int pos1 = 0;
				int pos2 = 0;
				int req1 = Math.abs(singleRoute.get(j));
				int req2 = Math.abs(singleRoute.get(j + 1));
				if (req1 != 0) {
					pos1 = data.getRequests().get(req1 - 1).getCustomerID();
				}
				if (req2 != 0) {
					pos2 = data.getRequests().get(req2 - 1).getCustomerID();
				}
				distancePerRout += distance[pos1][pos2];
			}
			distancePerRoute.add(distancePerRout);
			distancePerRout = 0;
		}
		if (soltuionPerDay.size() == 1) {
			// vehicle.add(solutions.get(0));
			vehicleRouteMapping.put(1, soltuionPerDay.get(0));
		} else {
			int accumulateDistance = 0;
			List<Integer> route = new ArrayList<>();
			int index = 1;
			for (int i = 0; i < soltuionPerDay.size(); i++) {
				int curDistance = distancePerRoute.get(i);
				accumulateDistance += curDistance;
				if (accumulateDistance < maximumAllowedTravelDistance) {
					for (int j = 0; j < soltuionPerDay.get(i).size(); j++) {
						if (route.size() > 0 && route.get(route.size() - 1) == soltuionPerDay.get(i).get(j)) {
							continue;
						}
						route.add(soltuionPerDay.get(i).get(j));
					}
				} else if (accumulateDistance >= maximumAllowedTravelDistance) {
					vehicleRouteMapping.put(index++, route);
					route = new ArrayList<>();
					for (int j = 0; j < soltuionPerDay.get(i).size(); j++) {
						if (route.size() > 0 && route.get(route.size() - 1) == soltuionPerDay.get(i).get(j)) {
							continue;
						}
						route.add(soltuionPerDay.get(i).get(j));
					}
					accumulateDistance = curDistance;
				}
				if (i == soltuionPerDay.size() - 1) {
					vehicleRouteMapping.put(index++, route);
				}
			}
		}
		return vehicleRouteMapping;
	}

}
