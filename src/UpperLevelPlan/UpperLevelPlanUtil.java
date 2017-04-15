package UpperLevelPlan;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import DataIO.DataIO;
import dataModel.BlackBoard;
import dataModel.Request;
import dataModel.UpperPlan;

public class UpperLevelPlanUtil implements DataIO {

	BlackBoard data;
	int numberOfDays;
	int numberOfRequest;
	List<Request> requests;
	Boolean[] requestsFlag;
	int counter;
	int[][] plan;
	int numberOfTools;
	int[] depotStock;
	int[] dynamicStock;
	int[] config;

	public UpperLevelPlanUtil(BlackBoard bb) {
		// TODO Auto-generated constructor stub
		this.data = bb;
		System.err.println(Arrays.toString(data.getConfig()));
		config = data.getConfig();
		numberOfDays = config[DAYS];
		numberOfRequest = config[NUM_OF_REQUESTS];
		requests = data.getRequests();
		requestsFlag = new Boolean[numberOfRequest];
		counter = 0;
		for (int i = 0; i < numberOfRequest; i++) {
			requestsFlag[i] = false;
		}
		plan = new int[numberOfDays][numberOfRequest];
		numberOfTools = config[NUM_OF_TOOLS];
		dynamicStock = new int[data.getToolList().size()];
		depotStock = new int[data.getToolList().size()];
		for (int i = 0; i < numberOfTools; i++) {
			depotStock[i] = data.getToolList().get(i).getNumOfTools();
			dynamicStock[i] = data.getToolList().get(i).getNumOfTools();
		}
	}

	public UpperPlan planCreate() {
		// TODO Auto-generated method stub
		UpperPlan up = new UpperPlan();

		// insert fixed day delivery requests
		TreeMap<Integer, List<Integer>> fixedDayPlan = data.getFixedDayDelivery();
		Set<Integer> keys = fixedDayPlan.keySet();
		Iterator<Integer> itr = keys.iterator();
		while (itr.hasNext()) {
			int day = itr.next();
			List<Integer> list = fixedDayPlan.get(day);
			for (int i = 0; i < list.size(); i++) {
				int rID = list.get(i);
				Request curReq = requests.get(rID - 1);
				confirmPlan(day - 1, curReq);
				findAssociationPickedUpDelivery(day - 1, curReq);
			}
		}
		// insert unfixed request
		for (int i = 0; i < requests.size(); i++) {
			if (requestsFlag[i]) {
				continue;
			} else {
				Request curReq = requests.get(i);
				// int day = curReq.getStart_Time() - 1;
				int day = ThreadLocalRandom.current().nextInt(curReq.getStart_Time(), curReq.getEnd_Time() + 1) - 1;
				confirmPlan(day, curReq);
			}
		}
		up.setPlans(plan);
		up.setEstimateAvgRequstPerDay(requestDistributionCal(up));
		up.setEstimateToolCost(toolUsedCal(up));
		// System.err.println(up);
		return up;
	}

	public void findAssociationPickedUpDelivery(int day, Request curRequest) {
		List<Integer> associateRequestForPickUpDelivery = curRequest.getAssociateRequestForPickUpDelivery();
		if (associateRequestForPickUpDelivery.size() > 0) {
			for (int i = 0; i < associateRequestForPickUpDelivery.size(); i++) {
				int requestID = associateRequestForPickUpDelivery.get(i);
				Request associateRequest = requests.get(requestID - 1);
				if (!requestsFlag[associateRequest.getId() - 1]) {
					confirmPlan(day + curRequest.getNumOfDaysRequest(), associateRequest);
				}
			}
		}
	}

	public void confirmPlan(int day, Request r) {
		plan[day][r.getId() - 1] = 1;
		plan[day + r.getNumOfDaysRequest()][r.getId() - 1] = -1;
		requestsFlag[r.getId() - 1] = true;
		updateCounter();
	}

	public void updateCounter() {
		this.counter++;
	}

	public void print(int[][] plans) {
		StringBuilder sBuilder = new StringBuilder();
		for (int i = 0; i < plans.length; i++) {
			for (int j = 0; j < plans[0].length; j++) {
				sBuilder.append(plans[i][j] + ",");
			}
			sBuilder.append("\n");
		}
		System.err.println(sBuilder.toString());
	}

	public int toolUsedCal(UpperPlan ph) {
		int[] maximumToolUsed = new int[numberOfTools];
		int[][] plan = ph.getPlans();
		for (int i = 0; i < plan.length; i++) {
			int[] toolUsed = new int[numberOfTools];
			for (int j = 0; j < data.getRequests().size(); j++) {
				if (plan[i][j] == 0) {
					continue;
				} else {
					Request request = data.getRequests().get(j);
					int toolKind = request.getRequestToolKind();
					int toolNumber = request.getRequestToolNumber();
					if (plan[i][j] == 1) {
						dynamicStock[toolKind - 1] -= toolNumber;
					} else {
						dynamicStock[toolKind - 1] += toolNumber;
					}
				}
			}
			for (int k = 0; k < numberOfTools; k++) {
				toolUsed[k] = depotStock[k] - dynamicStock[k];
				if (toolUsed[k] > maximumToolUsed[k]) {
					maximumToolUsed[k] = toolUsed[k];
				}
			}
		}

		int cost = 0;
		for (int k = 0; k < numberOfTools; k++) {
			System.err.println("tool cost" + data.getToolList().get(k).getCostOfTools());
			cost += maximumToolUsed[k] * data.getToolList().get(k).getCostOfTools();
		}
		return cost;
	}

	public int requestDistributionCal(UpperPlan up) {
		int maximumRequestPerDay = Integer.MIN_VALUE;
		int[][] plan = up.getPlans();
		for (int i = 0; i < plan.length; i++) {
			int taskExecutes = 0;
			for (int j = 0; j < plan[0].length; j++) {
				if (plan[i][j] != 0) {
					taskExecutes++;
				}
			}
			maximumRequestPerDay = Math.max(maximumRequestPerDay, taskExecutes);
		}
		return maximumRequestPerDay;
	}

}
